package implementation;

import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import printScreen.lexer.Lexer;
import printScreen.parser.CatchableParser;
import interpreter.Interpreter;
import token.Token;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Adapter implements PrintScriptInterpreter {
    private final Lexer lexer = new Lexer();
    private final CatchableParser parser = new CatchableParser();
    private final Interpreter interpreter = new Interpreter();

    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        try {
            Reader reader = new InputStreamReader(src);
            Iterator<List<Token>> tokens = lexer.lex(reader);
            CatchableParser.CatchableParserIterator ast = parser.parse(tokens);
            if (ast.hasException()) {
                Exception exception = ast.getException();
                String message = Objects.requireNonNull(exception).getMessage();
                handler.reportError(message);
                return;
            }
            interpreter.interpret(ast);
            if (interpreter.hasException()) {
                Exception exception = interpreter.getException();
                String message = Objects.requireNonNull(exception).getMessage();
                handler.reportError(message);
            } else {
                interpreter.getLog().forEach(emitter::print);
            }
        } catch (Exception e) {
            handler.reportError(e.getMessage());
        }
    }
}
