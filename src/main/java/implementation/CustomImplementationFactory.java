package implementation;

import common.providers.token.TokenProvider;
import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import lexer.provider.FileTokenProvider;

import java.io.File;

public class CustomImplementationFactory implements InterpreterFactory {

    @Override
    public PrintScriptInterpreter interpreter() {


        // make sure to ADAPT your implementation to PrintScriptInterpreter interface.
        return new CustomInterpreter();

        // Dummy impl: return (src, version, emitter, handler) -> { };
    }
}

