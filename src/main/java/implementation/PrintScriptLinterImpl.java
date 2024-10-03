package implementation;

import interpreter.ErrorHandler;
import interpreter.PrintScriptLinter;
import runner.LinterRunner;

import java.io.InputStream;

public class PrintScriptLinterImpl implements PrintScriptLinter {

  @Override
  public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {

    try {
      LinterRunner linterRunner = new LinterRunner();
      linterRunner.linterRun(src, config, version);
    } catch (Exception e) {
      handler.reportError(e.getMessage());
    }


  }
}
