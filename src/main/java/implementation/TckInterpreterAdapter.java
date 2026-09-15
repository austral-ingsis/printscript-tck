package implementation;

import edu.austral.ingsis.printscript.common.OperatorDefinition;
import edu.austral.ingsis.printscript.common.PrintScriptException;
import edu.austral.ingsis.printscript.common.Version;
import edu.austral.ingsis.printscript.common.ast.Statement;
import edu.austral.ingsis.printscript.interpreter.EnvironmentReader;
import edu.austral.ingsis.printscript.interpreter.ExecutionContext;
import edu.austral.ingsis.printscript.lexer.FilePositionalSource;
import edu.austral.ingsis.printscript.lexer.PrintScriptLexer;
import edu.austral.ingsis.printscript.parser.PrintScriptParser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;

/**
 * Adapts our real interpreter to the TCK's {@link PrintScriptInterpreter} contract. Our own
 * pipeline throws a {@link PrintScriptException} (lexical, syntax or semantic) at whichever stage
 * hits a problem; the TCK instead expects every error reported through {@link ErrorHandler} and
 * {@code execute} to return normally — this is the one place that catches it and reports it
 * instead of letting it escape.
 */
public final class TckInterpreterAdapter implements PrintScriptInterpreter {

    // Some TCK fixtures deliberately run the JVM out of heap while collecting output, so by the
    // time OutOfMemoryError is thrown, every collected message is still live (reachable) - GC
    // can't reclaim any of it. Reporting the error back through ErrorHandler needs a bit of fresh
    // heap of its own (the message String, the ArrayList node...), which can fail with a second,
    // uncatchable OutOfMemoryError right when we're trying to recover from the first. Reserving
    // this buffer upfront and dropping it the moment we catch the error frees just enough headroom
    // for that recovery path - the classic "emergency pool" trick for handling OOM gracefully.
    private static final int OOM_RECOVERY_RESERVE_BYTES = 512 * 1024;

    @Override
    public void execute(
            InputStream src,
            String version,
            PrintEmitter emitter,
            ErrorHandler handler,
            InputProvider provider) {
        byte[] oomRecoveryReserve = new byte[OOM_RECOVERY_RESERVE_BYTES];
        Path tempFile = Sources.spoolToTempFile(src);
        try {
            Version parsedVersion = Version.fromLabel(version);
            Set<OperatorDefinition> noPlugins = Set.of();

            try (FilePositionalSource source = new FilePositionalSource(tempFile)) {
                var lexer = new PrintScriptLexer(noPlugins);
                var parser = new PrintScriptParser(noPlugins, parsedVersion);
                Iterator<Statement> statements = parser.parse(lexer.tokenize(source));

                // The TCK has no equivalent of readEnv's provider
                EnvironmentReader environmentReader =
                        name -> Optional.ofNullable(System.getenv(name));

                // The TCK's InputProvider is purely a value source - unlike our own
                // StdInInputProvider (which prints the prompt itself as part of reading), it never
                // displays anything. The TCK's fixtures expect the prompt to show up as a print
                // message regardless, so this adapter does that half explicitly before asking the
                // provider for the value.
                edu.austral.ingsis.printscript.interpreter.InputProvider inputProvider =
                        prompt -> {
                            emitter.print(prompt);
                            return provider.input(prompt);
                        };

                var context = new ExecutionContext(emitter::print, inputProvider, environmentReader);

                new edu.austral.ingsis.printscript.interpreter.PrintScriptInterpreter()
                        .interpret(statements, context);
            }
        } catch (PrintScriptException e) {
            handler.reportError(describe(e));
        } catch (OutOfMemoryError e) {
            oomRecoveryReserve = null; // free the reserve before allocating anything to recover
            handler.reportError(e.getMessage());
        } catch (RuntimeException | IOException e) {
            handler.reportError(e.getMessage() != null ? e.getMessage() : e.toString());
        } finally {
            deleteQuietly(tempFile);
        }
    }

    static void deleteQuietly(Path tempFile) {
        try {
            Files.deleteIfExists(tempFile);
        } catch (IOException ignored) {
            // best-effort cleanup; deleteOnExit (set when the file was created) is the fallback
        }
    }

    static String describe(PrintScriptException e) {
        return e.getClass().getSimpleName()
                + ": "
                + e.getMessage()
                + " (line "
                + e.start().line()
                + ", column "
                + e.start().column()
                + ")";
    }
}
