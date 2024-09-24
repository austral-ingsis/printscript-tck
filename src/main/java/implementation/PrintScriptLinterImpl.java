package implementation;

import interpreter.ErrorHandler;
import interpreter.PrintScriptLinter;
import linters.StaticCodeAnalyzer;
import org.example.commands.Analyze;

import java.io.InputStream;

public class PrintScriptLinterImpl implements PrintScriptLinter {
  @Override
  public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
    Analyze analyzer = new Analyze();
    StaticCodeAnalyzer staticCodeAnalyzer = new StaticCodeAnalyzer(config, version);
    //habría que pasar la config a la clase Configuration
    try {
      if (!version.equals("1.0") && !version.equals("1.1")) {
        handler.reportError("Unsupported version: " + version);
      } else {
        try {
          Object result = analyzer.run();
        } catch (Exception e) {
          handler.reportError("Error: " + e.getMessage());
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
