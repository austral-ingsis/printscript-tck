package interpreter;

import interpreter.factory.InterpreterFactory;
import runner.OutputResult;

import java.io.InputStream;
import java.util.List;

public class MyRunner {
    public void execute(InputStream code, String version, OutputResult printLog, OutputResult errorLog) {
        Interpreter interpreter = InterpreterFactory.getInterpreter(version);
        try {
            List<String> prints = interpreter.interpretInputStream(code);
            interpreter = null;
            for (String print : prints) {
                printLog.saveResult(print);
            }
        } catch (Throwable e) {
            interpreter = null;
            System.gc();
            System.out.println(e.getMessage());
            errorLog.saveResult(e.getMessage());
        }
    }
}
