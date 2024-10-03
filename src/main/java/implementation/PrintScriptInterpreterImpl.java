package implementation;

import adapter.InputProviderAdapter;
import adapter.PrintEmitterAdapter;
import interpreter.*;
import runner.Runner;

import java.io.InputStream;

public class PrintScriptInterpreterImpl implements PrintScriptInterpreter {

  @Override
  public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider inputProvider) {
    try {
      PrintEmitterAdapter printEmitterAdapter = new PrintEmitterAdapter(emitter);
      InputProviderAdapter inputProviderAdapter = new InputProviderAdapter(inputProvider, printEmitterAdapter);
      Runner runner = new Runner();
      runner.run(src, version, printEmitterAdapter, inputProviderAdapter);
    }
    catch (Error | Exception e) {
      handler.reportError(e.getMessage());
    }
  }
}