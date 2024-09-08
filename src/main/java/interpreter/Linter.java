package interpreter;

import org.apache.commons.io.IOUtils;
import org.example.*;
import org.example.lexerresult.LexerSuccess;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class Linter implements PrintScriptLinter{
  @Override
  public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
    try {
      if (!version.equals("1.0")) return;
      InputStream adaptedConfig = adaptConfig(config);
      StaticCodeAnalyzer linter = new PrintScriptSca(adaptedConfig);
      Scanner scanner = new Scanner(src).useDelimiter("\n");
      Lexer lexer = new PrintScriptLexer();
      while (scanner.hasNext()) {
        Result lexerResult = lexer.lex(scanner);
        if (!lexerResult.isSuccessful()) {
          handler.reportError(lexerResult.errorMessage());
        }
          LexerSuccess success = (LexerSuccess) lexerResult;
          List<Result> results = linter.analyze(success.getTokens());
          for (Result result : results) {
              if (result.isSuccessful()) continue;
              handler.reportError(result.errorMessage());
          }
      }

    } catch (IOException e) {
        handler.reportError("Error reading file");
    }
  }

  private String streamToString(InputStream stream) throws IOException {
    StringBuilder build = new StringBuilder();
    byte[] buf = new byte[1024];
    int length;
    try (InputStream is = stream) {
      while ((length = is.read(buf)) != -1) {
        build.append(new String(buf, 0, length));
      }
	}
    return build.toString();
  }

  private InputStream adaptConfig(InputStream config) throws IOException {
    String stringConfig = streamToString(config);
    String adaptedStringConfig = stringConfig
            .replace("identifier_format", "identifierFormat")
            .replace("camel case", "camelCase")
            .replace("snake case", "snakeCase")
            .replace("\"mandatory-variable-or-literal-in-println\": true", "\"printlnExpressions\": false")
            .replace("\"mandatory-variable-or-literal-in-println\": false", "\"printlnExpressions\": true");
    return IOUtils.toInputStream(adaptedStringConfig);
  }
}
