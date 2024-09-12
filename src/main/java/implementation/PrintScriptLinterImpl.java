package implementation;

import configurations.ConfigLoader;
import configurations.Configuration;
import interpreter.ErrorHandler;
import interpreter.PrintScriptLinter;
import linters.StaticCodeAnalyzer;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class PrintScriptLinterImpl implements PrintScriptLinter {
  //private final Lexer lexer = new Lexer();

  @Override
  public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
  /*  if (version.equals("1.1")) {
      throw new UnsupportedOperationException("Version 1.1 is not supported");
    }

    //es que es un object
    Configuration configuration = ConfigLoader.INSTANCE.loadConfiguration(String.valueOf(new BufferedReader(new InputStreamReader(config))));

    StringBuilder code = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(src))) {
      String line;
      while ((line = reader.readLine()) != null) {
        code.append(line).append("\n");
      }
    } catch (Exception e) {
      handler.reportError(e.getMessage());
      return;
    }

    // tokenization pero ups no se puede
    List<Token> tokens = lexer.tokenize(code.toString());

    StaticCodeAnalyzer analyzer = new StaticCodeAnalyzer(configuration);
    List<String> errors = analyzer.analyze(tokens);

    for (String error : errors) {
      handler.reportError(error);
    }
  */}
}
