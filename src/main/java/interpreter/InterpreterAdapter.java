package interpreter;

import common.argument.ErrorHandler;
import common.argument.InputProvider;
import common.argument.PrintEmitter;
import common.convertors.StringConvertor;
import runner.Runner;

import java.io.InputStream;
import java.util.ArrayList;

public class InterpreterAdapter implements PrintScriptInterpreter {
    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        Runner runner = new Runner();
        String code = StringConvertor.convert(src);
        OutputEmitter printLog = new OutputEmitter(new ArrayList<>());
        ErrorReport errorLog = new ErrorReport("");
        runner.execute(code, version, printLog, errorLog);

        for (String print : printLog.getPrints()) {
            emitter.print(print);
        }

        handler.reportError(errorLog.getResult());
    }
}
