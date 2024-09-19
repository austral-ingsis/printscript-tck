package adapters;

import interpreter.PrintScriptFormatter;
import java.io.*;


public class CustomFormatter implements PrintScriptFormatter {

    @Override
    public void format(InputStream src, String version, InputStream config, Writer writer) {
        System.gc();
        return;
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