package implementation;

import com.printscript.interpreter.interfaces.InterpreterResult;
import com.printscript.interpreter.providers.DefaultEnvProvider;
import com.printscript.interpreter.results.InterpreterFailure;
import com.printscript.runner.Runner;
import interpreter.*;


import java.io.InputStream;

public class PrintScriptInterpreterImpl implements PrintScriptInterpreter {
    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
      final InputProvAdapter inputProvider = new InputProvAdapter(provider);
      final OutPutAdapter outPutProvider = new OutPutAdapter(emitter);
      final DefaultEnvProvider envProvider = new DefaultEnvProvider();
      final ErrorHandlerObs errorHandlerObs = new ErrorHandlerObs(handler);

      EnvProvider envProv = new EnvProvider();
      String envVAR = envProv.getEnv("BEST_FOOTBALL_CLUB");


      PrintEmitterObs emitterObs = new PrintEmitterObs(emitter);


      final Runner runner = new Runner(inputProvider, outPutProvider, envProvider);

      try {
        if (!version.equals("1.0") && !version.equals("1.1")) {
          handler.reportError("Unsupported version: " + version);
        } else {
          try {
            InterpreterResult result =  runner.run(src, version);
            if (result instanceof InterpreterFailure) {
              String errorMessage = ((InterpreterFailure) result).getErrorMessage();
              errorHandlerObs.notifyChange(errorMessage);
            }
          } catch (Exception | Error  e) {
            errorHandlerObs.notifyChange(e.getMessage());
          }
        }
      } catch (Exception | Error e) {
        errorHandlerObs.notifyChange(e.getMessage());
      }
    }
}