package implementation;

import interpreter.ErrorHandler;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import impl.PrintScript;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.function.Consumer;

public class CustomImplementationFactory implements InterpreterFactory {

    @Override
    public PrintScriptInterpreter interpreter() {
        return (src, version, emitter, handler) -> {
            String source = null;
            try {
                source = Files.readString(src.toPath(), StandardCharsets.US_ASCII);
            } catch (IOException e) {
                e.printStackTrace();
            }
            new PrintScript(new PrintEmitterAdapter(emitter), new ErrorHandlerAdapter(handler), version, false).interpret(source);
        };
    }
}

class PrintEmitterAdapter implements Consumer<String> {

    PrintEmitter emitter;

    public PrintEmitterAdapter(PrintEmitter emitter) {
        this.emitter = emitter;
    }

    @Override
    public void accept(String s) {
        emitter.print(s);
    }

}

class ErrorHandlerAdapter implements Consumer<String> {

    ErrorHandler handler;

    public ErrorHandlerAdapter(ErrorHandler handler) {
        this.handler = handler;
    }


    @Override
    public void accept(String s) {
        handler.reportError(s);
    }
}