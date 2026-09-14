package implementation;

import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import kotlin.Unit;
import printscript.runner.PrintScriptRunner;

import java.io.InputStream;

public class PrintScriptInterpreterAdapter implements PrintScriptInterpreter {
    private static final int RESERVE_BYTES = 256 * 1024;

    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        byte[] reserve = new byte[RESERVE_BYTES];
        try {
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
        } catch (OutOfMemoryError e) {
            reserve = null;
            System.gc();
            handler.reportError("Java heap space");
        } catch (Throwable t) {
            reserve = null;
            handler.reportError(t.getMessage() != null ? t.getMessage() : t.toString());
        } finally {
            reserve = null;
        }
    }
}
