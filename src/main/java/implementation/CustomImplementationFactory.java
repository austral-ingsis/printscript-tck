package implementation;

import adapter.Adapter;
import interpreter.*;

import java.io.BufferedInputStream;
import java.util.Arrays;

public class CustomImplementationFactory implements PrintScriptFactory {
    Adapter adapter = new Adapter();
    OutputAdapterJava outputAdapter;
    InputAdapter inputAdapter;

    @Override
    public PrintScriptInterpreter interpreter() {
        return (src, version, emitter, handler, provider) -> {
            try {
                this.outputAdapter = new OutputAdapterJava(emitter);
                this.inputAdapter = new InputAdapter(provider);
                adapter.execute(src, version, outputAdapter, handler, inputAdapter);
            } catch (Exception | Error e) {
                handler.reportError(e.getMessage());
            }
        };
    }

    @Override
    public PrintScriptFormatter formatter() {
        // your PrintScript formatter should be returned here.
        // make sure to ADAPT your formatter to PrintScriptFormatter interface.
        throw new NotImplementedException("Needs implementation"); // TODO: implement

        // Dummy impl: return (src, version, config, writer) -> { };
    }

    @Override
    public PrintScriptLinter linter() {
        // your PrintScript linter should be returned here.
        // make sure to ADAPT your linter to PrintScriptLinter interface.
        throw new NotImplementedException("Needs implementation"); // TODO: implement
    }
}