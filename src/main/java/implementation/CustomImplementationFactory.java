package implementation;

import interpreter.PrintScriptInterpreter;
import impl.PrintScript;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

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

