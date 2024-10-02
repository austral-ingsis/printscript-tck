package implementation;

import com.printscript.interpreter.interfaces.InterpreterResult;
import com.printscript.interpreter.providers.DefaultEnvProvider;
import com.printscript.interpreter.providers.DefaultInputProvider;
import com.printscript.interpreter.providers.DefaultOutPutProvider;
import com.printscript.interpreter.results.InterpreterFailure;
import com.printscript.interpreter.results.InterpreterSuccess;
import com.printscript.runner.Runner;
import interpreter.*;


import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class PrintScriptInterpreterImpl implements PrintScriptInterpreter {
    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
      try {
        if (!version.equals("1.0") && !version.equals("1.1")) {
          handler.reportError("Unsupported version: " + version);
        } else {
          try {
            DefaultInputProvider inputProvider = new DefaultInputProvider();
            DefaultOutPutProvider outPutProvider = new DefaultOutPutProvider();
            DefaultEnvProvider envProvider = new DefaultEnvProvider();
            Runner runner = new Runner(inputProvider, outPutProvider, envProvider);

            ErrorHandlerObs errorHandlerObs = new ErrorHandlerObs(handler);
            PrintEmitterObs emitterObs = new PrintEmitterObs(emitter);
            InterpreterResult results = runner.run(src, version);
            EnvProvider envProv = new EnvProvider();
            String envVAR = envProv.getEnv("BEST_FOOTBALL_CLUB");
            System.out.println(envVAR);
            if (results instanceof InterpreterFailure) {
                errorHandlerObs.notifyChange(results);
            } else if (results instanceof InterpreterSuccess) {
                emitterObs.notifyChange(results);
            }
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
}