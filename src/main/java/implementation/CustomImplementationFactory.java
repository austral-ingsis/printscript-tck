package implementation;

import interpreter.*;

public class CustomImplementationFactory implements PrintScriptFactory {

    @Override
    public PrintScriptInterpreter interpreter() {
        return new PrintScriptInterpreterAdapter();
    }

    @Override
    public PrintScriptFormatter formatter() {
        return new PrintScriptFormatterAdapter();
    }

    @Override
    public PrintScriptLinter linter() {
        return new PrintScriptLinterAdapter();
    }
}