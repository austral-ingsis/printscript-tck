package interpreter;

import interpreter.factory.InterpreterFactory;
import output.OutputResult;

import java.io.InputStream;
import java.util.List;

public class MyRunner {
    public void execute(InputStream code, String version, OutputResult printLog, OutputResult errorLog) {
        IterableInterpreter interpreter = new IterableInterpreter(version, code);
        List<String> prints;
        try {
            while(interpreter.hasNext()) {
                prints = interpreter.next();
                for (String print: prints) {
                    printLog.saveResult(print);
                }
            }
        } catch (Throwable e) {
            interpreter = null;
            System.gc();
            System.out.println(e.getMessage());
            errorLog.saveResult(e.getMessage());
        }
    }
}
