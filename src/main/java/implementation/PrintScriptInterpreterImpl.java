package implementation;

import ast.AstNode;
import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import interpreter.builder.InterpreterBuilder;
import interpreter.result.InterpreterResult;
import interpreter.result.PrintResult;
import interpreter.variable.Variable;
import lexer.Lexer;
import lexer.factory.LexerBuilder;
import parser.parser.Parser;
import parser.parserBuilder.PrintScriptOnePointZeroParserBuilder;
import token.Token;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrintScriptInterpreterImpl implements PrintScriptInterpreter {

    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        Lexer lexer = new LexerBuilder().build(version);
        Parser parser = new PrintScriptOnePointZeroParserBuilder().build();
        interpreter.interpreter.PrintScriptInterpreter interpreter = new InterpreterBuilder().build(version);
        Map<Variable, Object> variables = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(src))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = preprocessLine(reader, line);
                try {
                    List<Token> tokens = lexer.lex(line);
                    AstNode ast = parser.createAST(tokens);
                    InterpreterResult result = (InterpreterResult) interpreter.interpret(ast, variables);
                    if (result instanceof PrintResult) {
                        String toPrint = ((PrintResult) result).getToPrint();
                        emitter.print(toPrint);
                        System.out.println("Printed: " + toPrint);
                    }
                } catch (Exception e) {
                    handler.reportError(e.getMessage());
                }
            }
        } catch (IOException e) {
            handler.reportError(e.getMessage());
        }
    }

    private String readIfBlock(BufferedReader reader, String line) throws IOException {
        String result = line;
        String ifLine = line;
        String ifBlock = reader.readLine();
        String line3 = reader.readLine();

        if (line3 != null && line3.trim().equals("} else {")) {
            String elseBlock = reader.readLine();
            String elseEnd = reader.readLine();
            result = ifLine + ifBlock + line3 + elseBlock + elseEnd;
        } else {
            result = ifLine + ifBlock + line3;
        }
        return result;
    }

    private boolean isIfStatement(String line) {
        return line.startsWith("if(") || line.startsWith("if (");
    }

    private String preprocessLine(BufferedReader reader, String line) throws IOException {
        String processedLine = line;
        if (isIfStatement(line)) {
            processedLine = readIfBlock(reader, line);
        }
        System.out.println("Preprocessed line: " + processedLine);
        return processedLine;
    }

}
