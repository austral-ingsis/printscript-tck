package implementation;

import app.MyPrintScriptApp;
import app.PrintScriptApp;
import app.interpreter.PrintScriptInterpretStates;
import app.printer.PrintScriptInterpretStatesPrinter;
import app.printer.Printer;
import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreterr;

import java.io.InputStream;

public class PrintScriptInterpreterImpl implements PrintScriptInterpreterr {
    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        if (version.equals("1.0")) {
            PrintScriptInterpretStatesPrinter value = new PrintScriptInterpretStatesPrinter(s -> {emitter.print(s);return null;});
            PrintScriptApp app = new MyPrintScriptApp(value);
            app.interpret(src);
        } else {
            System.out.println("Not implemented yet. Come back soon!");
        }
    }
}
