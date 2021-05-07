package implementation;

import edu.austral.ingsis.CLI;
import edu.austral.ingsis.CLIPrinter;
import edu.austral.ingsis.exception.CompilationTimeException;
import interpreter.ErrorHandler;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;

import java.io.File;
import java.io.FileNotFoundException;

public class PrintScriptInterpreterImpl implements PrintScriptInterpreter {
  @Override
  public void execute(File src, String version, PrintEmitter emitter, ErrorHandler handler) {
    final String path = src.getPath();
    String command = "execute " + path + " " + version + " output-enabled=false";
    PrintEmitterAdaptor printEmitterAdaptor = new PrintEmitterAdaptor(emitter);
    printEmitterAdaptor.setIntegerMode();
    final CLI cli = new CLI(printEmitterAdaptor);
    
    try {
      cli.run(command);
    } catch (FileNotFoundException | CompilationTimeException e) {
      handler.reportError(e.getMessage());
    }
  
  
  }
}
