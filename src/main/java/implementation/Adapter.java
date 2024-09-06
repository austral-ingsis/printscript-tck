package implementation;

import com.printscript.interpreter.IInterpreter;
import com.printscript.interpreter.TracingInterpreter;
import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import node.ASTNode;
import printScreen.lexer.Lexer;
import printScreen.parser.Parser;
import token.Token;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;

public class Adapter implements PrintScriptInterpreter {
    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        final Lexer lexer = new Lexer();
        final Parser parser = new Parser();
        final TracerAdapter tracer = new TracerAdapter(emitter);
        final IInterpreter interpreter = new TracingInterpreter(tracer, false);
        try {
            Reader reader = new InputStreamReader(src);
            Iterator<List<Token>> tokens = lexer.lex(reader);
            Iterator<ASTNode> ast = parser.parse(tokens);
            interpreter.interpret(ast);
        } catch (Throwable e) {
            handler.reportError(e.getMessage().split(":")[0]);
        }
    }
}
