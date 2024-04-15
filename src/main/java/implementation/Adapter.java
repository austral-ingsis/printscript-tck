package implementation;
import interpreter.PrintScriptInterpreter;
import interpreter.*;
import org.example.*;
import org.example.inputReader.InputReaderType;
import org.example.splittingStrategy.StrategyMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

class Adapter implements PrintScriptInterpreter {
    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        exec(src, version, emitter, handler, provider);
    }

    private void exec(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(src));
            String line;
            Lexer lexer = new Lexer(new ValueMapper(), new StrategyMapper());
            Interpreter interpreter = new Interpreter(getInputReaderType(provider));

            while ((line = reader.readLine()) != null) {
                List<Token> tokens = lexer.execute(line);
                List<AbstractSyntaxTree> ast = new Parser().execute(tokens);
                String response = interpreter.execute(ast).getString();
                if (!response.isBlank()) emitter.print(response);
            }

            reader.close();
        }
        catch (Exception e) {
            handler.reportError(e.getMessage());
        }
    }

    private InputReaderType getInputReaderType(InputProvider provider){
        return new AdaptInput(provider);
    }

}