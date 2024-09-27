package implementation;

import com.google.gson.Gson;
import configurations.Configuration;
import interpreter.ErrorHandler;
import interpreter.PrintScriptLinter;
import linters.StaticCodeAnalyzer;
import org.example.ReaderIterator;
import org.example.token.TokenType;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PrintScriptLinterImpl implements PrintScriptLinter {
  @Override
  public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
    BufferedReader reader = new BufferedReader(new InputStreamReader(config, StandardCharsets.UTF_8));
    Gson gson = new Gson();
    Configuration configuration = gson.fromJson(reader, Configuration.class);
    StaticCodeAnalyzer staticCodeAnalyzer = new StaticCodeAnalyzer(configuration, version);
    List<Token> tokens = new ArrayList<Token>();
    Lexer lexer = new Lexer(version);
    try {
      if (!version.equals("1.0") && !version.equals("1.1")) {
        handler.reportError("Unsupported version: " + version);
      } else {
        try {
          Iterator<String> readerIterator = new ReaderIterator().getLineIterator(src);
          Object lexerTokenize = lexer.tokenize(readerIterator);
          while (lexerTokenize.hasNext()) {
            Token token = lexer.next();
            tokens.add(token);
          }
          List<String> result = staticCodeAnalyzer.analyze(tokens);
        } catch (Exception e) {
          handler.reportError("Error: " + e.getMessage());
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
