package implementation;

import implementation.handlers.ErrorHandlerStream;
import implementation.handlers.PrintEmitterStream;
import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import edu.Runner;

import java.io.*;


public class MyPrintScriptInterpreter implements PrintScriptInterpreter {
  @Override
  public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider){
    openCollectors(emitter, handler, provider);
    try {
      Runner runner = new Runner(version);
      runner.execute(src);
    } catch (RuntimeException e) {
      System.err.println("Runtime error: " + e.getMessage());
      e.printStackTrace(System.err);
    } finally {
      closeCollectors(emitter, handler, provider);
    }

  }

  private void closeCollectors(PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
    System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    System.setErr(new PrintStream(new FileOutputStream(FileDescriptor.err)));
  }

  private void openCollectors(PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
    PrintStream printStream = new PrintEmitterStream(System.out, emitter);
    System.setOut(printStream);
    PrintStream errorStream = new ErrorHandlerStream(System.err, handler);
    System.setErr(errorStream);
  }
}
