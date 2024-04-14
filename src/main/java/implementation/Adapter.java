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
import java.util.List;

class Adapter implements PrintScriptInterpreter {
    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        try {
            //Todo : agregar version
            List<AbstractSyntaxTree> ast = getAsts(src, provider, handler);
            Interpreter interpreter = new Interpreter(getInputReaderType(provider));
            emitter.print(interpreter.execute(ast).getString());
        }
        catch (Exception e) {
            handler.reportError(e.getMessage());
        }
    }
    private InputReaderType getInputReaderType(InputProvider provider){
        AdaptInput adaptInput = new AdaptInput(provider);
        return adaptInput;
    }

    private String getString(InputStream src) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(src));
        String line;

        while ((line = reader.readLine()) != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
        }

        reader.close();
        return sb.toString();
    }
    private List <AbstractSyntaxTree> getAsts(InputStream src, InputProvider provider, ErrorHandler handler){
        try {
            String script = getString(src);
            Lexer lexer = new Lexer(new ValueMapper(), new StrategyMapper());
            List<Token> tokens = lexer.execute(script);
            Parser parser = new Parser();
            List<AbstractSyntaxTree> ast = parser.execute(tokens);
            return ast;
        }
        catch (Exception e){
            handler.reportError(e.getMessage());
        }
        return null;
    }
}