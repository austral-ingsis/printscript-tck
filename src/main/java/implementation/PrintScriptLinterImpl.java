package implementation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.printscript.lexer.Lexer;
import com.printscript.linter.configurations.CaseConfigurationDeserializer;
import com.printscript.linter.configurations.Configuration;
import com.printscript.linter.linters.StaticCodeAnalyzer;
import com.printscript.runner.ReaderIterator;
import com.printscript.token.Token;
import configurations.IdentifierFormat;
import interpreter.ErrorHandler;
import interpreter.PrintScriptLinter;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class PrintScriptLinterImpl implements PrintScriptLinter {
  @Override
  public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
    BufferedReader reader = new BufferedReader(new InputStreamReader(config, StandardCharsets.UTF_8));
    String configContent = reader.lines().collect(Collectors.joining("\n"));
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(IdentifierFormat.class, new CaseConfigurationDeserializer())
            .create();
    Configuration configuration = gson.fromJson(configContent, Configuration.class);
    System.out.println(configuration);
    StaticCodeAnalyzer staticCodeAnalyzer = new StaticCodeAnalyzer(configuration, version);
    List<Token> tokens = new ArrayList<>();
    Lexer lexer = new Lexer(version);
    try {
      if (!version.equals("1.0") && !version.equals("1.1")) {
        handler.reportError("Unsupported version: " + version);
      } else {
        try {
          Iterator<String> readerIterator = new ReaderIterator().getLineIterator(src);
          Iterator<Token> lexerTokenize = lexer.tokenize(readerIterator);
          while (lexerTokenize.hasNext()) {
            Token token = lexerTokenize.next();
            tokens.add(token);
          }
          staticCodeAnalyzer.analyze(tokens);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
