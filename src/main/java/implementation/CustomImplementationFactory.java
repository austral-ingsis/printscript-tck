package implementation;

import PrintScript.JavaApp;
import interpreter.*;
import sources.FileProgramSource;

import java.io.File;
import java.util.Scanner;

public class CustomImplementationFactory implements InterpreterFactory {

    static class PrintScriptInterpreterImpl implements PrintScriptInterpreter {

        class DisplayMethodImpl implements DisplayMethod {
            private PrintEmitter emitter;
            DisplayMethodImpl(PrintEmitter emitter){
                this.emitter = emitter;
            }
            @Override
            public void display(String text) {
                emitter.print(text);
            }
        }

        class InputMethodImpl implements InputMethod {
            private InputProvider provider;
            InputMethodImpl(InputProvider provider) {
                this.provider = provider;
            }
            @Override
            public String readInput(String name, DisplayMethod displayMethod) {
                displayMethod.display(name);
                return provider.input(name);
            }
        }

        @Override
        public void execute(File src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
            String path = src.getPath();
            FileProgramSource source = new FileProgramSource(path);
            JavaApp cli = new JavaApp();
            DisplayMethodImpl display = new DisplayMethodImpl(emitter);
            InputMethodImpl input = new InputMethodImpl(provider);
            try{
                cli.interpret(source, version, display, input);
            } catch (Exception e) {
                handler.reportError(e.getMessage());
            }
        }
    }

    @Override
    public PrintScriptInterpreter interpreter() {
        return new PrintScriptInterpreterImpl();
    }
}