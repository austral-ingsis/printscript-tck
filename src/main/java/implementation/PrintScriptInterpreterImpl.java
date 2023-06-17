package implementation;

import app.MyPrintScriptApp;
import app.PrintScriptApp;
import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreterr;

import java.io.InputStream;

public class PrintScriptInterpreterImpl implements PrintScriptInterpreterr {
    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        if (version.equals("1.0")) {
            PrintScriptApp app = new MyPrintScriptApp();
            app.interpret(src);
        } else {
            System.out.println("Not implemented yet. Come back soon!");
        }
    }
}
