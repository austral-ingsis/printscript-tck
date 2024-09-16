package interpreter;

import common.argument.ErrorHandler;
import common.argument.InputProvider;
import common.argument.PrintEmitter;
import interpreter.visitor.staticprovider.EnvLoader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class InterpreterAdapter implements PrintScriptInterpreter {
    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        String bestFootballClub = System.getenv("BEST_FOOTBALL_CLUB");
        EnvLoader.addNewConstants("BEST_FOOTBALL_CLUB", bestFootballClub);

        MyRunner runner = new MyRunner();
        OutputEmitter printLog = new OutputEmitter(emitter);
        ErrorReport errorLog = new ErrorReport("");
        setInput(provider);
        runner.execute(src, version, printLog, errorLog);

        if (!errorLog.getResult().isEmpty()){
            handler.reportError(errorLog.getResult());
        }
    }

    private static void setInput(InputProvider provider) {
        String input = provider.input("input");
        if (input != null){
            System.setIn(new ByteArrayInputStream(input.getBytes()));
        }
    }
}
