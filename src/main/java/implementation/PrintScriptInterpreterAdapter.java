package implementation;

import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import kotlin.Unit;
import printscript.runner.PrintScriptRunner;

import java.io.InputStream;

public class PrintScriptInterpreterAdapter implements PrintScriptInterpreter {
    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        PrintScriptRunner.INSTANCE.execute(
            src,
            version,
            msg -> {
                emitter.print(msg);
                return Unit.INSTANCE;
            },
            provider::input,
            err -> {
                handler.reportError(err);
                return Unit.INSTANCE;
            }
        );
    }
}
