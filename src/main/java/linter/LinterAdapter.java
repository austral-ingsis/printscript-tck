package linter;

import common.argument.ErrorHandler;
import runner.Runner;

import java.io.InputStream;

public class LinterAdapter implements PrintScriptLinter {
  @Override
  public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
    Runner runner = new Runner();

  }
}
