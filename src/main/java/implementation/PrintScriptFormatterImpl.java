package implementation;

import adapter.WriterAdapter;
import fileWriter.OutputProvider;
import formatter.FormatterRunner;
import interpreter.PrintScriptFormatter;

import java.io.InputStream;
import java.io.Writer;

public class PrintScriptFormatterImpl implements PrintScriptFormatter {
    @Override
    public void format(InputStream src, String version, InputStream config, Writer writer) {
      try {
        OutputProvider outputProvider = new WriterAdapter(writer);
        FormatterRunner formatterRunner = new FormatterRunner();
        formatterRunner.format(src, config, outputProvider, version);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
}
