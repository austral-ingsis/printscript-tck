package implementation;

import app.PrintScriptApp;
import interpreter.*;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;

public class PrintScriptInterpreterImpl implements PrintScriptInterpreter {

    private record InterpreterInputProvider(InputProvider provider) implements InputProvider, interpreter.inputs.InputProvider {
        @Override
        public String input(String name) {
            return provider.input(name);
        }
    }

    @Override
    public void execute(File src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        try{
            String fileContent = Files.readString(src.toPath(), StandardCharsets.US_ASCII);
            PrintScriptApp app = new PrintScriptApp(version);
            ArrayList<String> logs = app.testInterpret(fileContent, new InterpreterInputProvider(provider));
            for (String log : logs) {
                emitter.print(log);
            }
        } catch (Exception e) {
            handler.reportError(e.getMessage());
        }
    }
}
