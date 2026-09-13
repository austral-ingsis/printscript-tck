package implementation.adapter;

import ast.ASTNode;
import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.Interpreter;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;

import java.io.InputStream;
import java.util.Iterator;

/**
 * Adapts PrintScript's Interpreter to the TCK's PrintScriptInterpreter.
 *
 * The TCK reports errors instead of propagating them, so every failure -- lexing, parsing and
 * evaluation alike -- is caught here and handed to the ErrorHandler.
 */
public class InterpreterAdapter implements PrintScriptInterpreter {

    /**
     * Heap held aside for the duration of a run and dropped on the way into the handler.
     *
     * Reporting allocates -- the TCK's ErrorCollector grows its ArrayList on the first add --
     * and a heap exhausted by many small allocations has nothing left to satisfy even that:
     * the handler throws a second OutOfMemoryError in place of the first being reported, and
     * that one escapes execute(). Dropping the reference first is enough to prevent it. The
     * JVM collects before it gives up on an allocation, so the collection that reporting
     * itself triggers finds this block unreachable instead of a heap that is entirely live.
     * What survives reporting is the headroom the caller needs to inspect the errors
     * afterwards, which on an exhausted heap is otherwise just as scarce.
     *
     * 256 KB of the TCK's 7 MB: enough to be worth collecting, small enough not to move where
     * exhaustion falls. A 1 MB reserve was no more reliable and about 45% slower per run.
     */
    private static final int RESERVE_BYTES = 256 * 1024;

    /** A field, not a local, so the reserve cannot be optimised away as unused. */
    private byte[] reserve;

    @Override
    public void execute(
            InputStream src,
            String version,
            PrintEmitter emitter,
            ErrorHandler handler,
            InputProvider provider
    ) {
        reserve = new byte[RESERVE_BYTES];
        try {
            // Printer and Reader are single-method Kotlin interfaces, so the TCK's collaborators
            // adapt to them directly.
            final Interpreter interpreter =
                    Interpreter.Companion.forVersion(version, emitter::print, provider::input);

            final Iterator<ASTNode> statements = Pipeline.statements(src, version);
            while (statements.hasNext()) {
                interpreter.execute(statements.next());
            }
        } catch (Throwable throwable) {
            // Throwable, not Exception: the large-file test expects OutOfMemoryError to be
            // reported as an error rather than to escape. Released before describing or
            // reporting, both of which allocate.
            reserve = null;
            handler.reportError(Pipeline.describe(throwable));
        } finally {
            reserve = null;
        }
    }
}
