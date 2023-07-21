package implementation;

import cli.Config;
import consumer.*;
import interpreter.*;

import java.io.*;

public class CustomInterpreter implements PrintScriptInterpreter {

    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider){
        try {
            ASTNodeConsumerInterpreter interpreter = initializeInterpreter(version, src);
            new InterpreterRunner().runConsumerInterpreter(interpreter, emitter, handler, provider);
        }catch (Throwable e) {
            handler.reportError(e.getMessage());
        }

    }
    private ASTNodeConsumerInterpreter initializeInterpreter(String version, InputStream file){
        return new Interpret(new Config().generateASTNproviderInputStream(version, file));
    }
}