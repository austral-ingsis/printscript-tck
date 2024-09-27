package implementation;

import interfaces.InterpreterResult;
import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import org.example.Runner;
import org.example.parser.Parser;
import providers.DefaultEnvProvider;
import providers.DefaultInputProvider;
import providers.DefaultOutPutProvider;
import results.InterpreterFailure;
import results.InterpreterResultInformation;
import results.InterpreterSuccess;


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
