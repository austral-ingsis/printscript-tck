package implementation;

import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import lexer.director.LexerDirector;
import org.example.ast.nodes.ProgramNode;
import org.example.interpreter.Interpreter;
import org.example.interpreter.InterpreterImpl;
import org.example.lexer.Lexer;
import org.example.parser.ParserImpl;
import org.example.token.Token;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Adapter implements PrintScriptInterpreter {

    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        try {
            Lexer lexer = new LexerDirector().createLexer(version);
            Interpreter interpreter = new InterpreterImpl();
            ParserImpl parser = new ParserImpl();
            String fileInString = this.getString(src);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            List<String> separatedByConditionals = new ArrayList<>(Arrays.stream(fileInString.split("if\\(")).toList());
            for (int i = 1; i < separatedByConditionals.size(); i++) {
                separatedByConditionals.set(i, "if(" + separatedByConditionals.get(i));
            }
            if (separatedByConditionals.size() <= 1) {
                executeByLine(fileInString, version, emitter, handler, provider);
            } else {
                for (String branch : separatedByConditionals) {
                    List<Token> tokens = lexer.tokenize(branch);
                    ProgramNode ast = parser.parse(tokens);
                    checkInput(emitter, provider, interpreter, ast, baos);
                }
            }
        } catch (Exception e) {
            handler.reportError(e.getMessage());
        }
    }

    private void executeByLine(String src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        try {
            Interpreter interpreter = new InterpreterImpl();
            Lexer lexer = new LexerDirector().createLexer(version);
            ParserImpl parser = new ParserImpl();
            ProgramNode ast = parser.parse(lexer.tokenize(src));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            checkInput(emitter, provider, interpreter, ast, baos);
        } catch (Exception | Error e) {
            handler.reportError(e.getMessage());
        }
    }

    private void checkInput(PrintEmitter emitter, InputProvider provider, Interpreter interpreter, ProgramNode ast, ByteArrayOutputStream baos) {
        PrintStream ps = new PrintStream(baos);
        PrintStream oldOut = System.out;
        System.setOut(ps);
        var input = provider.input("asd");
        if (input != null) {
            interpreter.interpret(ast, true, input);
            System.setOut(oldOut);
            String response = baos.toString().replace("\r","");
            String[] lines = response.split("\n");
            for (String line : lines) {
                emitter.print(line);
            }
        }
        else {
            interpreter.interpret(ast, false, "");
            System.setOut(oldOut);
            String response = baos.toString().replace("\r","").trim();
            if (!response.isBlank()) {
                Arrays.stream(response.split("\n")).forEach(emitter::print);
            }
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


}
