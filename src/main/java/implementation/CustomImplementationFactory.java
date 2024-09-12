package implementation;

import interpreter.*;

public class CustomImplementationFactory implements PrintScriptFactory {

    @Override
    public PrintScriptInterpreter interpreter() {
        // your PrintScript implementation should be returned here.
        // make sure to ADAPT your implementation to PrintScriptInterpreter interface.
        return new PullInterpreter();

        // Dummy impl: return (src, version, emitter, handler) -> { };
    }

    @Override
    public PrintScriptFormatter formatter() {
        // your PrintScript formatter should be returned here.
        // make sure to ADAPT your formatter to PrintScriptFormatter interface.
          return new Formatter();
        // Dummy impl: return (src, version, config, writer) -> { };
    }

    @Override
    public PrintScriptLinter linter() {
        // your PrintScript linter should be returned here.
        // make sure to ADAPT your linter to PrintScriptLinter interface.
        return new Linter();
    }

}