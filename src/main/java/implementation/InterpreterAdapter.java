package implementation;

import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import java.io.InputStream;
import org.example.lexer.*;
import org.example.interpreter.*;
import org.example.Parser;

public class InterpreterAdapter implements PrintScriptInterpreter {

    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        String string = src.toString();
    }
}
