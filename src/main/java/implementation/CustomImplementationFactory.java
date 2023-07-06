package implementation;

import input.InputStreamInput;
import input.LexerInput;
import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import interpreterUtils.Printer;
import interpreterUtils.ReadInput;
import kotlin.Unit;
import org.jetbrains.annotations.NotNull;
import printscript.CommonPrintScriptRunner;
import printscript.PrintscriptRunner;
import version.Version;
import version.VersionClassesKt;

import java.io.InputStream;

public class CustomImplementationFactory implements InterpreterFactory {


    @Override
    public PrintScriptInterpreter interpreter() {
        return new PrintScriptInterpreter() {
            @Override
            public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
                String stringV = version.equals("1.0") ? "v1" : "v2";
                Version v = VersionClassesKt.getVersionFromString(stringV);
                PrintscriptRunner printscriptRunner = new CommonPrintScriptRunner(printerAdapter(emitter),v,readInputAdapter(provider));
                LexerInput input = new InputStreamInput(src);

                try {
                    printscriptRunner.runExecution(input.getFlow(), errorHandlerAdapter(handler));
                } catch (OutOfMemoryError error) {
                    handler.reportError(error.getMessage());
                }
            }
        };
    }

    private Printer printerAdapter(PrintEmitter emitter) {
        return new Printer() {
            @Override
            public void print(@NotNull String message) {
                emitter.print(message);
            }
        };
    }

    private ReadInput readInputAdapter(InputProvider inputProvider) {
        return new ReadInput() {
            @NotNull
            @Override
            public String read(@NotNull String s) {
                return inputProvider.input(s);
            }
        };
    }


    private errorHandler.ErrorHandler errorHandlerAdapter(ErrorHandler handler) {
        return new errorHandler.ErrorHandler() {
            @Override
            public void reportError(String message) {
                handler.reportError(message);
            }
        };
    }

    private Unit print(PrintEmitter emitter, String newValue) {
        return emitter.print(newValue);
    }

    private String provide(InputProvider provider, String newValue) {
        return provider.input(newValue);
    }


}
