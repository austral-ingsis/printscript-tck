package formatter;

import interpreter.PrintScriptFormatter;
import runner.Operations;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class AdaptedFormatter implements PrintScriptFormatter {

    @Override
    public void format(InputStream src, String version, InputStream config, Writer writer) {
            Operations runner = new Operations(src, version, null);
            try {
                String output = runner.format();
                for ( char letter : output.toCharArray()){
                    writer.append(letter);
                }
            }
            catch (Exception e) {
                return;
            }
        }
    }
