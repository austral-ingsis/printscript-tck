package implementation;

import interpreter.OutputAdapter;
import interpreter.PrintScriptInterpreter;

public class CustomImplementationFactory implements InterpreterFactory {

    @Override
    public PrintScriptInterpreter interpreter() {
        // your PrintScript implementation should be returned here.
        // make sure to ADAPT your implementation to PrintScriptInterpreter interface.
        return (src, version, emitter, handler, provider) -> {
            try{
                OutputAdapter outputAdapter = new OutputAdapter(emitter);
                Adapter exe = new Adapter();
                exe.execute(src, parseVersion(version), outputAdapter);
            } catch (Exception | Error e){
                handler.reportError(e.getMessage());
            }
        };
    }

    private String parseVersion(String version) {
        return "v1";
    }
}