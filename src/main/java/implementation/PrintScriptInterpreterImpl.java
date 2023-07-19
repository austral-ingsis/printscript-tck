package implementation;
import app.errorHandler.MyErrorHandler;
import app.interpreter.Interpret;
import app.interpreter.PrintScriptInterpetI;
import app.interpreter.PrintScriptInterpretStates;
import app.literalInputter.LiteralInputter;
import app.printer.interpret.PrintScriptInterpretStatesPrinter;
import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreterr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;

public class PrintScriptInterpreterImpl implements PrintScriptInterpreterr {
    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        if (version.equals("1.0")) {
            PrintScriptInterpetI printScriptInterpetI = new PrintScriptInterpetI("", new CustomErrorHandler(handler), new Inputter(provider));
            PrintScriptInterpretStatesPrinter interpretStatesPrinter = new PrintScriptInterpretStatesPrinter(s -> {
                if (s.endsWith(".0")) {
                    emitter.print(s.substring(0, s.length()-2));
                } else {
                    emitter.print(s);
                }
                return null;
            });
            Interpret interpret = new Interpret(interpretStatesPrinter, printScriptInterpetI);
            interpret.interpret(src);
        } else {
            PrintScriptInterpetI printScriptInterpetI = new PrintScriptInterpetI("1.1", new CustomErrorHandler(handler), new Inputter(provider));
            PrintScriptInterpretStatesPrinter interpretStatesPrinter = new PrintScriptInterpretStatesPrinter(s -> {
                if (s.endsWith(".0")) {
                    emitter.print(s.substring(0, s.length()-2));
                } else {
                    emitter.print(s);
                }
                return null;
            });
            Interpret interpret = new Interpret(interpretStatesPrinter, printScriptInterpetI);
            interpret.interpret(src);
        }
    }

    class Inputter implements LiteralInputter {

        InputProvider provider;

        public Inputter(InputProvider provider) {
            this.provider = provider;
        }

        @Nullable
        @Override
        public String input() {
            return provider.input("");
        }
    }

    class CustomErrorHandler implements app.errorHandler.ErrorHandler<PrintScriptInterpretStates> {

        ErrorHandler handler;

        public CustomErrorHandler(ErrorHandler handler) {
            this.handler = handler;
        }

        @Override
        public PrintScriptInterpretStates handle(@NotNull String s, PrintScriptInterpretStates printScriptInterpretStates) {
            handler.reportError(s);
            return new MyErrorHandler().handle(s, printScriptInterpretStates);
        }
    }
}
