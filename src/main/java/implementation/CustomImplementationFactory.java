package implementation;

import adapter.Adapter;
import interpreter.InputAdapter;
import interpreter.OutputAdapter;
import interpreter.PrintScriptInterpreter;

import java.util.Objects;

public class CustomImplementationFactory implements InterpreterFactory {

    @Override
    public PrintScriptInterpreter interpreter() {
        //TODO your PrintScript implementation should be returned here.
        // make sure to ADAPT your implementation to PrintScriptInterpreter interface.
        return (src, version, emitter, handler, provider) -> {
            try{
                Adapter adapter = new Adapter();
                adapter.execute(src, parseVersion(version), new OutputAdapter(emitter), new InputAdapter(provider));
            } catch (Exception | Error e){
                handler.reportError(e.getMessage());
            }
        };
    }

    private String parseVersion(String version) {
        return Objects.equals(version, "1.0") ? "v1" : "v2";
    }
}