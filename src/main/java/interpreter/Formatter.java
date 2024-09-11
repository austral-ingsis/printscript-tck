package interpreter;

import org.example.CodeFormatter;

import java.io.InputStream;
import java.io.Writer;


public class Formatter implements PrintScriptFormatter {
    @Override
    public void format(InputStream src, String version, InputStream config, Writer writer) {
        try {
            if (src == null || version == null || config == null || writer == null) {
                throw new IllegalArgumentException();
            }
            if (!version.equals("1.0")) {
                throw new IllegalArgumentException();
            }
            CodeFormatter formatter = new CodeFormatter();

        } catch (Exception e) {
            throw  new RuntimeException();
        }
    }
}
