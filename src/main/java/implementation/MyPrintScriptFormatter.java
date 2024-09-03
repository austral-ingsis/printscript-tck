package implementation;

import interpreter.PrintScriptFormatter;

import java.io.InputStream;
import java.io.Writer;

public class MyPrintScriptFormatter implements PrintScriptFormatter {
  @Override
  public void format(InputStream src, String version, InputStream config, Writer writer) {
  }
}
