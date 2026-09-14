package implementation;

import interpreter.PrintScriptFormatter;
import interpreter.PrintScriptInterpreter;
import interpreter.PrintScriptLinter;

import java.io.BufferedInputStream;
import java.util.Arrays;

public class CustomImplementationFactory implements PrintScriptFactory {

    @Override
    public PrintScriptInterpreter interpreter() {
       InterpreterImplementation impl = new InterpreterImplementation();
       return impl;
    }

    @Override
    public PrintScriptFormatter formatter() {
        return new FormatterImplementation();
    }

    @Override
    public PrintScriptLinter linter() {
        return new LinterImplementation();
    }
}