package adapters;

import interpreter.PrintScriptFormatter;
import kotlin.sequences.Sequence;
import main.*;
import org.example.lexer.Lexer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.Writer;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;

public class CustomFormatter implements PrintScriptFormatter {

    private Path getPathFromInputStream(InputStream src) throws IOException {
        Path tempFile = Files.createTempFile("temp", ".tmp");
        Files.copy(src, tempFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        tempFile.toFile().deleteOnExit();
        return tempFile;
    }

    @Override
    public void format(InputStream src, String version, InputStream config, Writer writer) {

    }
}