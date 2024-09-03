package implementation;

import edu.Runner;
import implementation.handlers.ErrorHandlerStream;
import implementation.handlers.PrintEmitterStream;
import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class MyPrintScriptInterpreter implements PrintScriptInterpreter {
  @Override
  public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider){
    Iterator<String> input = createIterator(src);
    Runner runner = new Runner(version);
    openCollectors(emitter, handler, provider);
    runner.execute(input);
    closeCollectors(emitter, handler, provider);
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

  private Iterator<String> createIterator(InputStream src) {
    BufferedReader reader = new BufferedReader(new InputStreamReader(src));
    List<String> lines = reader.lines().collect(Collectors.toList());
    Iterator<String> iterator = lines.iterator();
    return iterator;
  }
}
