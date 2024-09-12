package implementation;

import interpreter.PrintScriptFormatter;
import org.example.CodeFormatter;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class PrintScriptFormatterImpl implements PrintScriptFormatter {
    @Override
    public void format(InputStream src, String version, InputStream config, Writer writer) {
        try {
            if (src == null || version == null || config == null || writer == null) {
                throw new IllegalArgumentException("One or more arguments are null.");
            }
            if (!"1.0".equals(version)) {
                throw new IllegalArgumentException("Unsupported version: " + version);
            }
            File tempCodeFile = File.createTempFile("tempCode", ".txt");
            File tempConfigFile = File.createTempFile("tempConfig", ".json");

            try (FileOutputStream codeOut = new FileOutputStream(tempCodeFile);
                 FileOutputStream configOut = new FileOutputStream(tempConfigFile)) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = src.read(buffer)) != -1) {
                    codeOut.write(buffer, 0, bytesRead);
                }
                while ((bytesRead = config.read(buffer)) != -1) {
                    configOut.write(buffer, 0, bytesRead);
                }
            }

            CodeFormatter formatter = new CodeFormatter();
            formatter.format(tempCodeFile.getPath(), tempConfigFile.getPath());

            try (FileReader fileReader = new FileReader(tempCodeFile)) {
                char[] buffer = new char[1024];
                int charsRead;
                while ((charsRead = fileReader.read(buffer)) != -1) {
                    writer.write(buffer, 0, charsRead);
                }
            }

            tempCodeFile.delete();
            tempConfigFile.delete();

        } catch (IOException e) {
            throw new RuntimeException("error occurred during formatting", e);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred during formatting", e);
        }
    }
}