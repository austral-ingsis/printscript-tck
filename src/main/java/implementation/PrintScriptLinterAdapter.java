package implementation;

import interpreter.ErrorHandler;
import interpreter.PrintScriptLinter;
import kotlin.Unit;
import printscript.runner.PrintScriptRunner;

import java.io.InputStream;

public class PrintScriptLinterAdapter implements PrintScriptLinter {
    @Override
    public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
        PrintScriptRunner.INSTANCE.lint(
            src,
            version,
            config,
            err -> {
                handler.reportError(err);
                return Unit.INSTANCE;
            }
        );
    }
}
