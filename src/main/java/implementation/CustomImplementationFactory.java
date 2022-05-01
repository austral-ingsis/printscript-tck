package implementation;

import PrintScript.JavaApp;
import interpreter.ErrorHandler;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import sources.FileProgramSource;

import java.io.File;

public class CustomImplementationFactory implements InterpreterFactory {

    static class PrintScriptInterpreterImpl implements PrintScriptInterpreter {
        @Override
        public void execute(File src, String version, PrintEmitter emitter, ErrorHandler handler) {
            String path = src.getPath();
            FileProgramSource source = new FileProgramSource(path);
            JavaApp cli = new JavaApp();
            try{
                emitter.print(cli.interpret(source));
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