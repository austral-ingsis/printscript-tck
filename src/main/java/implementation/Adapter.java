package implementation;

import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import lexer.director.LexerDirector;
import org.example.ast.nodes.Node;
import org.example.ast.nodes.ProgramNode;
import org.example.interpreter.Interpreter;
import org.example.interpreter.InterpreterImpl;
import org.example.lexer.Lexer;
import org.example.parser.ParserImpl;
import org.example.token.Token;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class Adapter implements PrintScriptInterpreter {

    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        exec(src, version, emitter, handler, provider);
    }

    private void exec(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        try {
            executeByLine(src, version, emitter, handler, provider);
        } catch (Exception e) {
            handler.reportError(e.getMessage());
        }
    }

    private void executeByLine(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        try {
            Lexer lexer = new LexerDirector().createLexer(version);
            Interpreter interpreter = new InterpreterImpl();
            BufferedReader reader = new BufferedReader(new InputStreamReader(src));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("if")) {
                    line = handleIf(line, reader);
                }
                List<Token> tokens = lexer.tokenize(line);
                ProgramNode ast = new ParserImpl().parse(tokens);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                checkInput(emitter, provider, interpreter, ast, baos);
            }
            reader.close();
        } catch (Error | Exception e) {
            if (e instanceof OutOfMemoryError) {
                handler.reportError("Java heap space");
            } else {
                handler.reportError(e.getMessage());
            }
        }
    }

    private String handleIf(String line, BufferedReader reader) throws IOException {
        while (!line.contains("}")) {
            line += reader.readLine();
        }
        String newLine = reader.readLine();

        if (newLine.contains("else")) {
            while (!newLine.contains("}")) {
                newLine += reader.readLine();
            }
        }
        line += " " + newLine + " ";
        return line;
    }

    private void checkInput(PrintEmitter emitter, InputProvider provider, Interpreter interpreter, ProgramNode ast, ByteArrayOutputStream baos) {
        PrintStream ps = new PrintStream(baos);
        PrintStream oldOut = System.out;
        System.setOut(ps);
        var input = provider.input("asd");
        if (input != null) {
            interpreter.interpret(ast, true, input);
            System.setOut(oldOut);
            String response = baos.toString().replace("\r", "");
            String[] lines = response.split("\n");
            for (String line : lines) {
                emitter.print(line);
            }
        } else {
            interpreter.interpret(ast, false, "");
            System.setOut(oldOut);
            String response = baos.toString().replace("\r", "").trim();
            if (!response.isBlank()) {
                Arrays.stream(response.split("\n")).forEach(emitter::print);
            }
        }
    }
}
