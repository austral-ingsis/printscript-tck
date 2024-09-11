package adapters;

import interpreter.PrintScriptFormatter;
import kotlin.sequences.Sequence;
import main.*;
import org.example.lexer.Lexer;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Files;

public class CustomFormatter implements PrintScriptFormatter {

    @Override
    public void format(InputStream src, String version, InputStream config, Writer writer) {
        FormatterFactory factory = new FormatterFactory();
        factory.format(src, config, version, writer);
    }
}

//class TCKConfigReader {
//    private final Path path;
//
//    public TCKConfigReader(Path path) {
//        this.path = path;
//    }
//
//    public FormatterConfig readConfig() {
//        try {
//            return new FormatterConfig(Files.readAllLines(path));
//        } catch (IOException e) {
//            throw new RuntimeException("Error reading config file", e);
//        }
//    }
//}