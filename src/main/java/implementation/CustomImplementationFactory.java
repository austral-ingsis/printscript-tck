package implementation;

import interpreter.PrintScriptFormatter;
import interpreter.PrintScriptInterpreter;
import interpreter.PrintScriptLinter;

import cnc.common.ContentManager;
import cnc.common.Failure;
import cnc.common.Result;
import cnc.language.InterpreterIO;
import cnc.language.LanguageFacade;
import cnc.printscript.PrintScriptEngine;
import kotlin.Unit;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class CustomImplementationFactory implements PrintScriptFactory {

    @Override
    public PrintScriptInterpreter interpreter() {
        return (src, version, emitter, handler, provider) -> {
            try {
                LanguageFacade printScript = PrintScriptEngine.INSTANCE.from(version);
                InterpreterIO io = new InterpreterIO(
                        prompt -> {
                            if (prompt != null && !prompt.isEmpty()) {
                                emitter.print(prompt);
                            }
                            return provider.input(prompt);
                        },
                        System::getenv,
                        msg -> {
                            emitter.print(msg);
                            return Unit.INSTANCE;
                        }
                );
                Result<Unit> result = printScript.execute(ContentManager.Companion.of(src), io);
                if (result instanceof Failure) {
                    handler.reportError(((Failure<Unit>) result).getMsg());
                }
            } catch (Throwable t) {
                if (t instanceof OutOfMemoryError) {
                    System.gc();
                }
                handler.reportError(t.getMessage() != null ? t.getMessage() : t.toString());
            }
        };
    }

    @Override
    public PrintScriptFormatter formatter() {
        return (src, version, config, writer) -> {
            try {
                LanguageFacade printScript = PrintScriptEngine.INSTANCE.from(version);
                String configJson = new String(config.readAllBytes(), StandardCharsets.UTF_8);
                String formatted = printScript.format(ContentManager.Companion.of(src), configJson);
                writer.write(formatted);
            } catch (Throwable t) {
                // handle error
            }
        };
    }

    @Override
    public PrintScriptLinter linter() {
        return (src, version, config, handler) -> {
            try {
                LanguageFacade printScript = PrintScriptEngine.INSTANCE.from(version);
                String configJson = new String(config.readAllBytes(), StandardCharsets.UTF_8);
                List<String> warnings = printScript.lint(ContentManager.Companion.of(src), configJson);
                for (String warning : warnings) {
                    handler.reportError(warning);
                }
            } catch (Throwable t) {
                handler.reportError(t.getMessage() != null ? t.getMessage() : t.toString());
            }
        };
    }
}
