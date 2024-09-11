package adapters;

import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import org.example.ASTNode;
import org.example.Interpreter;
import org.example.Runner;
import org.example.parser.Parser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

public class PrintScriptInterpreterImplementation implements PrintScriptInterpreter {
  @Override
  public void execute(
          InputStream src,
          String version,
          PrintEmitter emitter,
          ErrorHandler handler,
          InputProvider provider
  ) {

    try {
      Runner runner = new Runner();
      Iterator<Interpreter> interpreterIterator = runner.run(src.toString()).iterator();

      while (interpreterIterator.hasNext()) {
        Interpreter interpreter = interpreterIterator.next();
        if (interpreter.hasException()) {
          handler.reportError(interpreter.getException().getMessage());
        }
        if (interpreter) {
          emitter.print(interpreter.getOutput());
        }
      }
    } catch (OutOfMemoryError | Exception e){
      handler.reportError(e.getMessage());
    }

//    try {
//      if (!version.equals("1.0") && !version.equals("1.1")) {
//        handler.reportError("Unsupported version: " + version);
//        return;
//      }
//
//      BufferedReader reader = new BufferedReader(new InputStreamReader(src));
//      StringBuilder codeBuilder = new StringBuilder();
//      String line;
//      while ((line = reader.readLine()) != null) {
//        codeBuilder.append(line).append("\n");
//      }
//      String code = codeBuilder.toString();
//
//      Storage storage = new Storage();
//
//      Lexer lexer = new Lexer;
//
//      Parser parser = new Parser();
//      ASTNode parsers = parser.parse(code, storage);
//      Interpreter interpreter = new Interpreter();
//
//    } catch (Exception e) {
//      handler.reportError(e.getMessage() != null ? e.getMessage() : "Unknown error occurred");
//    }

  }
}
