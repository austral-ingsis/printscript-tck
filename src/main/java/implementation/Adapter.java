package implementation;

import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import node.ASTNode;
import printScreen.lexer.Lexer;
import interpreter.Interpreter;
import printScreen.parser.Parser;
import token.Token;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;

public class Adapter implements PrintScriptInterpreter {
    private final Lexer lexer = new Lexer();
    private final Parser parser = new Parser();
    private final Interpreter interpreter = new Interpreter();

    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        try {
            Reader reader = new InputStreamReader(src);
            Iterator<List<Token>> tokens = lexer.lex(reader);
            Iterator<ASTNode> ast = parser.parse(tokens);
            interpreter.interpret(ast);
            interpreter.getLog().forEach(emitter::print);
        } catch (Exception e) {
            handler.reportError(e.getMessage());
        }
    }
}
