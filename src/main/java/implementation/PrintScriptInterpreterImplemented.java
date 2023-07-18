package implementation;

import ast.AbstractSyntaxTree;
import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import org.austral.edu.*;
import parser.Parser;
import parser.ParserV1;
import parser.ParserV2;

import java.io.InputStream;
import java.util.List;

public class PrintScriptInterpreterImplemented implements PrintScriptInterpreter {

    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        Lexer lexer = new LexerV1();
        Lexer lexerV2 = new LexerV2();
        Parser parser = new ParserV1();
        Parser parserV2 = new ParserV2();
        InterpreterV1 interpreterV1 = new InterpreterV1(new PrintEmitterAdapter(emitter));
        InterpreterV2 interpreterV2 = new InterpreterV2(new PrintEmitterAdapter(emitter), new InputProviderAdapter(provider));

        try {
            String code = "";
            int actual = src.read();
            while (actual != -1) {
                code += String.valueOf((char) actual);
                actual = src.read();
            }
            if (version.equals("1.0")) {
                List<Token> tokens = lexer.lex(new StringInput(code));
                AbstractSyntaxTree ast = parser.parse(tokens);
                interpreterV1.interpret(ast);
            } else {
                List<Token> tokens = lexerV2.lex(new StringInput(code));
                AbstractSyntaxTree ast = parserV2.parse(tokens);
                interpreterV2.interpret(ast);
            }

        } catch (Throwable throwable) {
            handler.reportError(throwable.getMessage());
        }

    }
}
