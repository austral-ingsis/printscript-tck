package implementation;

import adapter.Adapter;
import interpreter.InputAdapter;
import interpreter.OutputAdapter;
import interpreter.OutputAdapterJava;
import interpreter.PrintScriptInterpreter;


import java.util.Objects;

public class CustomImplementationFactory implements InterpreterFactory {
    Adapter adapter = new Adapter();
    OutputAdapterJava outputAdapter;
    InputAdapter inputAdapter;
    @Override
    public PrintScriptInterpreter interpreter() {

        return (src, version, emitter, handler, provider) -> {
            try{
                this.outputAdapter = new OutputAdapterJava(emitter);
                this.inputAdapter = new InputAdapter(provider);
                adapter.execute(src, version, outputAdapter, handler, inputAdapter);
            } catch (Exception | Error e){
                handler.reportError(e.getMessage());
            }
        };
    }

}