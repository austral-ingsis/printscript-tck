package implementation;

import common.providers.token.TokenProvider;
import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import interpreter.implementation.FunctionInterpreter;
import interpreter.implementation.Interpreter;
import interpreter.output.ConsolePrintOutputter;
import interpreter.output.Outputter;
import lexer.provider.FileTokenProvider;
import org.jetbrains.annotations.NotNull;
import printscript.v1.app.StreamedExecution;

import java.io.File;

public class CustomInterpreter implements PrintScriptInterpreter {


    @Override
    public void execute(File src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        Outputter out = new PrintCollectorOutputter(emitter);

        try{
            new StreamedExecution(src, version, out).execute();
        } catch (Exception e){
            handler.reportError(e.getMessage());
        }


    }
}

class PrintCollectorOutputter implements Outputter {
    private final PrintEmitter printEmitter;
    public PrintCollectorOutputter(PrintEmitter printEmitter){
        this.printEmitter = printEmitter;
    }

    @Override
    public void output(@NotNull String s) {
        this.printEmitter.print(s);
    }
}
