package implementation;

import com.printscript.interpreter.interfaces.InterpreterResult;
import com.printscript.interpreter.providers.DefaultEnvProvider;
import com.printscript.interpreter.providers.DefaultInputProvider;
import com.printscript.interpreter.providers.DefaultOutPutProvider;
import com.printscript.interpreter.results.InterpreterFailure;
import com.printscript.interpreter.results.InterpreterResultInformation;
import com.printscript.interpreter.results.InterpreterSuccess;
import com.printscript.runner.Runner;
import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;


import java.io.InputStream;
import java.util.Iterator;

public class PrintScriptInterpreterImpl implements PrintScriptInterpreter {
    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        try {
            if (!version.equals("1.0") && !version.equals("1.1")) {
                handler.reportError("Unsupported version: " + version);
            }
            DefaultInputProvider inputProvider = new DefaultInputProvider();
            DefaultOutPutProvider outPutProvider = new DefaultOutPutProvider();
            DefaultEnvProvider envProvider = new DefaultEnvProvider();
            Runner runner = new Runner(inputProvider, outPutProvider, envProvider);
            InterpreterResult results = runner.run(src, version);
          ((InterpreterResultInformation) results).getResult().getStorage();
          ((InterpreterFailure) results).getError();
          ((InterpreterSuccess) results).getOriginalValue();
            System.out.println(((InterpreterSuccess) results).getOriginalValue());
            System.out.println(((InterpreterFailure) results).getError());
//            if (results instanceof InterpreterSuccess) {
//              InterpreterSuccess success = (InterpreterSuccess) results;
//              System.out.println(success.getOriginalValue());
//              success.getOriginalValue();
//            } else if (results instanceof InterpreterFailure) {
//              InterpreterFailure failure = (InterpreterFailure) results;
//              System.out.println(failure.getError());
//            } else {
//              handler.reportError("");
//            }

        } catch (Exception | OutOfMemoryError e) {
            handler.reportError("Error: " + e.getMessage());
        }
    }
}
