package implementation;

import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptFormatter;
import interpreter.PrintScriptInterpreter;
import interpreter.PrintScriptLinter;
import result.IncorrectResult;
import result.Result;
import service.ExecuteService;
import service.FormatService;
import service.LintService;
import version.Version;

import java.io.InputStream;
import java.io.Writer;

public class CustomImplementationFactory implements PrintScriptFactory {

    @Override
    public PrintScriptInterpreter interpreter() {
        return (InputStream src, String versionStr, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) -> {
            try {
                Version version = Version.fromString(versionStr);
                ExecuteService executeService = new ExecuteService();
                engine.OutputEmitter outputEmitter = emitter != null ? emitter::print : msg -> {};
                engine.InputSupplier inputSupplier = provider != null ? provider::input : prompt -> "";

                Result<String> result = executeService.execute(version, outputEmitter, inputSupplier, src);
                if (!result.isCorrect()) {
                    if (handler != null) {
                        handler.reportError(((IncorrectResult<String>) result).error());
                    }
                }
            } catch (OutOfMemoryError e) {
                if (handler != null) {
                    handler.reportError(e.getMessage() != null ? e.getMessage() : "Java heap space");
                }
            } catch (Throwable t) {
                if (handler != null) {
                    handler.reportError(t.getMessage() != null ? t.getMessage() : t.toString());
                }
            }
        };
    }

    @Override
    public PrintScriptFormatter formatter() {
        return (InputStream src, String versionStr, InputStream config, Writer writer) -> {
            try {
                Version version = Version.fromString(versionStr);
                FormatService formatService = new FormatService();
                formatService.format(version, src, config, writer);
            } catch (Exception e) {
                throw new RuntimeException("Formatting failed: " + e.getMessage(), e);
            }
        };
    }

    @Override
    public PrintScriptLinter linter() {
        return (InputStream src, String versionStr, InputStream config, ErrorHandler handler) -> {
            try {
                Version version = Version.fromString(versionStr);
                LintService lintService = new LintService();
                Result<String> result = lintService.analyze(version, src, config);
                if (!result.isCorrect()) {
                    if (handler != null) {
                        handler.reportError(((IncorrectResult<String>) result).error());
                    }
                }
            } catch (Exception e) {
                if (handler != null) {
                    handler.reportError("Linter error: " + e.getMessage());
                }
            }
        };
    }
}