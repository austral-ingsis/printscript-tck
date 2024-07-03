package implementation;

import adapter.Adapter;
import interpreter.InputAdapter;
import interpreter.OutputAdapter;
import interpreter.OutputAdapterJava;
import interpreter.PrintScriptInterpreter;


import java.util.Objects;

public class CustomImplementationFactory implements InterpreterFactory {
    @Override
    public PrintScriptInterpreter interpreter() {

        return (src, version, emitter, handler, provider) -> {
            try{
                Adapter adapter = new Adapter();
                OutputAdapterJava outputAdapter = new OutputAdapterJava(emitter);
                InputAdapter inputAdapter = new InputAdapter(provider);
                adapter.execute(src, version, outputAdapter, handler, inputAdapter);
            } catch (Exception | Error e){
                handler.reportError(e.getMessage());
            }
        };
    }

}