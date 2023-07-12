package implementation;
import app.interpreter.Interpret;
import app.interpreter.PrintScriptInterpetI;
import app.printer.interpret.PrintScriptInterpretStatesPrinter;
import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreterr;

import java.io.InputStream;

public class PrintScriptInterpreterImpl implements PrintScriptInterpreterr {
    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        if (version.equals("1.0")) {
            PrintScriptInterpetI printScriptInterpetI = new PrintScriptInterpetI("");
            PrintScriptInterpretStatesPrinter interpretStatesPrinter = new PrintScriptInterpretStatesPrinter(s -> {emitter.print(s);return null;});
            Interpret interpret = new Interpret(interpretStatesPrinter, printScriptInterpetI);
            interpret.interpret(src);
        } else {
            System.out.println("Not implemented yet. Come back soon!");
        }
    }
}
