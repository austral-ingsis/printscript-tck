package implementation;

import interpreter.PrintScriptFormatter;
import org.example.Runner;

import java.io.*;
import java.util.stream.Collectors;

public class FormatterAdapter implements PrintScriptFormatter {
    @Override
    public void format(InputStream src, String version, InputStream config, Writer writer) {
//        StringBuilder configuration = new StringBuilder();
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(config))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                configuration.append(line.replace("\r", " ")).append(System.lineSeparator());
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println(version);
//        System.out.println(configuration);
//
//        try {
//            writer.write(Runner.format(src, version, configuration.toString()));;
//        }
//        catch (Exception e){
//            throw new RuntimeException(e);
//        }

    }
}
