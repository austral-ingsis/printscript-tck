package implementation;

import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.example.Program;
import org.example.Runner;
import org.example.lexer.*;
import org.example.lexer.token.Token;

import static org.example.Runner.*;
import static org.example.lexer.LexerProvider.provideV10;
import static org.example.lexer.LexerProvider.provideV11;

public class InterpreterAdapter implements PrintScriptInterpreter {

    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(src))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line.replace("\r", " ")).append(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(content);
        String string = content.toString();
        Boolean v = false;
        if (version.equals("1.0")) {
            v = true;
        }
        List<Token> tokens = new ArrayList<>();
        try {
            if (v) tokens = lex(string);
            else tokens = lexV11(string);
        } catch (Exception e) {
            handler.reportError(e.toString());
        }
        Program ast = null;
        try {
            if (v) ast = Runner.parse(tokens);
            else ast = Runner.parseV11(tokens);
        }
        catch (Exception e){
            handler.reportError(e.toString());
        }
        try {
            interpret(ast);
        }
        catch (Exception e){
            handler.reportError(e.toString());
        }
    }


}
