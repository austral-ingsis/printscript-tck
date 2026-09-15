package implementation;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Spools an {@link InputStream} to a temp file — every TCK entry point hands us one, but our real
 * pipeline lexes through {@link edu.austral.ingsis.printscript.lexer.FilePositionalSource}, which
 * needs random-access I/O over a real file rather than a one-shot stream. Buffering the whole
 * stream into a {@code String} instead (the obvious alternative) would defeat the point of the
 * large-file tests: a big enough source blows the tiny heap those tests run under, so it has to
 * land on disk, not in a String.
 */
final class Sources {

    private Sources() {}

    static Path spoolToTempFile(InputStream in) {
        try (in) {
            Path tempFile = Files.createTempFile("printscript-tck-", ".ps");
            tempFile.toFile().deleteOnExit();
            Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
            return tempFile;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
