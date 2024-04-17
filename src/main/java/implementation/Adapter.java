package implementation;
import interpreter.PrintScriptInterpreter;
import interpreter.*;
import org.example.*;
import org.example.inputReader.InputReaderType;
import org.example.parser.Parser;
import org.example.splittingStrategy.StrategyMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Adapter implements PrintScriptInterpreter {
    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        exec(src, version, emitter, handler, provider);
    }

    private void exec(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        try {
            Lexer lexer = new Lexer(version, new StrategyMapper());
            Interpreter interpreter = new Interpreter(getInputReaderType(provider));
            String fileInString = getString(src);

            List<String> separatedByConditionals = new ArrayList<>(Arrays.stream(fileInString.split("if\\(")).toList());
            for (int i = 1; i < separatedByConditionals.size(); i++) {
                separatedByConditionals.set(i, "if(" + separatedByConditionals.get(i));
            }
            if (separatedByConditionals.size() <= 1) {
                InputStream inputStream = new ByteArrayInputStream(fileInString.getBytes(StandardCharsets.UTF_8));
                executeByLine(inputStream,version, emitter, handler, provider);
            }
            else {
                for (String branch : separatedByConditionals) {
                    List<Token> tokens = lexer.execute(branch);
                    List<AbstractSyntaxTree> ast = new Parser().execute(tokens);
                    String response = interpreter.execute(ast).getString();
                    if (!response.isBlank()) {
                        Arrays.stream(response.split("\n")).forEach(emitter::print);
                    }
                }

            }
        }
        catch (Exception e) {
            handler.reportError(e.getMessage());
        }
    }

    private void executeByLine(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        try{
            Lexer lexer = new Lexer(version, new StrategyMapper());
            Interpreter interpreter = new Interpreter(getInputReaderType(provider));
            BufferedReader reader = new BufferedReader(new InputStreamReader(src));
            String line;
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
        catch (Error e) {
            handler.reportError(e.getMessage());
        }

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
    private InputReaderType getInputReaderType(InputProvider provider){
        return new AdaptInput(provider);
    }

}