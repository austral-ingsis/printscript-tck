package implementation;

import edu.austral.ingsis.CLI;
import edu.austral.ingsis.CLIPrinter;
import interpreter.PrintScriptInterpreter;

public class CustomImplementationFactory implements InterpreterFactory {
  
  @Override
  public PrintScriptInterpreter interpreter() {
    return new PrintScriptInterpreterImpl();
  }
}