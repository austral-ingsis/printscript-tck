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
            runConsumerInterpreter(interpreter, emitter, handler, provider);
        }catch (Throwable e) {
            handler.reportError(e.getMessage());
        }

    }
    public void runConsumerInterpreter(ASTNodeConsumerInterpreter consumer, PrintEmitter emitter, ErrorHandler handler, InputProvider provider){
        ConsumerResponse result = consumer.consume();
        while (!(result instanceof ConsumerResponseEnd)) {
            if (result instanceof ConsumerResponseSuccess successResult) {
                if (successResult.getMsg() != null) {
                    emitter.print(successResult.getMsg());
                }
            } else if (result instanceof ConsumerResponseError errorResult) {
                handler.reportError(errorResult.getError());
                return;
            } else if (result instanceof ConsumerResponseImput inputResult) {
                emitter.print(inputResult.getMsg());
                String input = provider.input(inputResult.getMsg()); // Assuming the method name is `Input`
                consumer.getValue(input);
            }
            result = consumer.consume();
        }
    }
    private ASTNodeConsumerInterpreter initializeInterpreter(String version, InputStream file){
        return new Interpret(new Config().generateASTNproviderInputStream(version, file));
    }
}