package implementation;

import ast.AstNode;
import cli.FileReader;
import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import interpreter.builder.InterpreterBuilder;
import interpreter.result.InterpreterResult;
import interpreter.result.MultipleResults;
import interpreter.result.PrintResult;
import interpreter.variable.Variable;
import lexer.Lexer;
import lexer.factory.LexerBuilder;
import parser.parser.Parser;
import parser.parserBuilder.printScript10.PrintScript10ParserBuilder;
import parser.parserBuilder.printScript11.PrintScript11ParserBuilder;
import token.Token;
import token.TokenType;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrintScriptInterpreterImpl implements PrintScriptInterpreter {

    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        Lexer lexer = new LexerBuilder().build(version);
        Parser parser;
        if (version.equals("1.0")) {
            parser = new PrintScript10ParserBuilder().build();
        } else if (version.equals("1.1")) {
            parser = new PrintScript11ParserBuilder().build();
        } else {
            throw new IllegalArgumentException("Invalid version");
        }
        interpreter.interpreter.PrintScriptInterpreter interpreter = new InterpreterBuilder().build(version);
        Map<Variable, Object> variables = addInputToSymbolTable(provider);

        try {


            FileReader reader = new FileReader(src, version);
            while (reader.hasNextLine()) {
                List<List<Token>> statementsInLine = reader.getNextLine();
                InterpreterResult result;
                for (List<Token> statement : statementsInLine) {
                    AstNode ast = null;
                    try {
                        ast = parser.createAST(statement);
                    } catch (Error | Exception e) {
                        if(e instanceof OutOfMemoryError) {
                            handler.reportError("Java heap space");
                        } else {
                            handler.reportError(e.getMessage());
                        }
                        break;
                    }

                    try {
                        result = (InterpreterResult) interpreter.interpret(ast, variables);
                        printResults(result, emitter);
                    } catch (Error | Exception e) {
                        if(e instanceof OutOfMemoryError) {
                            handler.reportError("Java heap space");
                        } else {
                            handler.reportError(e.getMessage());
                        }
                        break;
                    }
                }
            }
        } catch (Error | Exception e) {
            if(e instanceof OutOfMemoryError) {
                handler.reportError("Java heap space");
            } else {
                handler.reportError(e.getMessage());
            }
        }
    }

    private void printResults(InterpreterResult result, PrintEmitter emmiter) {
        if (result instanceof PrintResult) {
            //System.out.println(((PrintResult) result).getToPrint());
            emmiter.print(((PrintResult) result).getToPrint());
        } else if (result instanceof MultipleResults) {
            for (InterpreterResult subResult : ((MultipleResults) result).getValues()) {
                printResults(subResult, emmiter);
            }
        } else {
            // Do nothing
        }
    }


    private Map<Variable, Object> addInputToSymbolTable(InputProvider provider){
        Map<Variable, Object> variables = new HashMap<>();
        String input = provider.input("");
        variables.put(new Variable("input", TokenType.STRINGTYPE, TokenType.CONST), input);
        return variables;
    }

}
