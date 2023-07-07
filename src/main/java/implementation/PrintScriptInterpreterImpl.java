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
            PrintScriptApp app = new MyPrintScriptApp();
            app.interpret(src);
        } else {
            System.out.println("Not implemented yet. Come back soon!");
        }
    }

    class PrinterAdapter implements Printer<PrintScriptInterpretStates> {

        PrintEmitter emitter;

        public PrinterAdapter(PrintEmitter emitter) {
            this.emitter = emitter;
        }

        @Override
        public PrintScriptInterpretStates print(PrintScriptInterpretStates printScriptInterpretStates) {
            PrintScriptInterpretStatesPrinter value = new PrintScriptInterpretStatesPrinter();
            String toPrint = printScriptInterpretStates.component3().print().component1();
            if (toPrint != null) {
                emitter.print(toPrint);
            }
            return value.print(printScriptInterpretStates);
        }
    }
}
