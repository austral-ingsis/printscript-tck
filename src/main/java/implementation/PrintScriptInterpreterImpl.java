package implementation;

import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import org.example.Runner;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PrintScriptInterpreterImpl implements PrintScriptInterpreter {

  @Override
  public void execute(
          InputStream src,
          String version,
          PrintEmitter emitter,
          ErrorHandler handler,
          InputProvider provider
  ) {
    try {
      if (!version.equals("1.0") && !version.equals("1.1")) {
        handler.reportError("Unsupported version: " + version);
        return;
      }

      BufferedReader reader = new BufferedReader(new InputStreamReader(src));
      StringBuilder codeBuilder = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        codeBuilder.append(line).append("\n");
      }
      String code = codeBuilder.toString();

      //lexing, parsing, interpreting :(
      Runner runner = new Runner();
      Object result = runner.run(code);

      if (result != null) {
        emitter.print(result.toString());
      }

    } catch (OutOfMemoryError e) {
      handler.reportError(e.getMessage());
    } catch (Exception e) {
      handler.reportError(e.getMessage() != null ? e.getMessage() : "Unknown error");
    }
  }
}
