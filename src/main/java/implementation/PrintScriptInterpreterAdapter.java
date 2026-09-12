package implementation;

import com.printscript.app.Dialect;
import com.printscript.ast.Statement;
import com.printscript.interpreter.Interpreter;
import com.printscript.interpreter.SystemEnvironment;
import com.printscript.interpreter.Value;
import com.printscript.interpreter.ValueOps;
import com.printscript.language.Environment;
import com.printscript.pipeline.StatementStream;
import com.printscript.report.ErrorRenderer;
import com.printscript.report.Failure;
import com.printscript.report.Result;
import com.printscript.report.Success;

import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;

import java.io.InputStream;

public class PrintScriptInterpreterAdapter implements PrintScriptInterpreter {

    private static final int RESERVE_BYTES = 256 * 1024;

    private final ErrorRenderer renderer = new ErrorRenderer();

    private byte[] reserve;

    @Override
    public void execute(
            InputStream src,
            String version,
            PrintEmitter emitter,
            ErrorHandler handler,
            InputProvider provider) {

        final var dialect = Pipeline.dialect(version);
        if (dialect == null) {
            handler.reportError("Unsupported version: " + version);
            return;
        }

        final var functions = dialect.getFunctions().invoke(
                () -> provider.input(""),
                SystemEnvironment.INSTANCE);

        final var interpreter = new Interpreter(
                new Environment<Value>(),
                emitter::print,
                new ValueOps(),
                dialect.getExecutors(),
                functions);

        reserve = new byte[RESERVE_BYTES];

        try {
            run(Pipeline.statements(src, dialect), interpreter, handler);
        } catch (OutOfMemoryError | StackOverflowError error) {
            reserve = null;
            handler.reportError(error.getMessage());
        } finally {
            reserve = null;
        }
    }

    private void run(StatementStream statements, Interpreter interpreter, ErrorHandler handler) {
        while (statements.hasNext()) {
            final Result<Statement> parsed = statements.next();
            if (parsed instanceof Failure failure) {
                handler.reportError(renderer.render(failure.getError()));
                return;
            }

            final var executed = interpreter.execute(((Success<Statement>) parsed).getValue());
            if (executed instanceof Failure failure) {
                handler.reportError(renderer.render(failure.getError()));
                return;
            }
        }
    }
}
