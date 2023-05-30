package implementation;

import input.InputStreamInput;
import input.LexerInput;
import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import kotlin.Unit;
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
                String stringV = version.equals("1.0") ? "v1" : version.equals("1.1") ? "v2" : null;
                Version v = VersionClassesKt.getVersionFromString(stringV);
                PrintscriptRunner printscriptRunner = new CommonPrintScriptRunner(v);
                LexerInput input = new InputStreamInput(src);
                printscriptRunner.runExecution(input.getFlow(), (String newValue) -> print(emitter, newValue), (String newValue) -> provide(provider, newValue),null);
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
