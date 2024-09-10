package implementation;

import com.printscript.interpreter.GoatedInterpreter;
import com.printscript.interpreter.Interpreter;
import com.printscript.lexer.Lexer;
import com.printscript.models.node.ASTNode;
import com.printscript.models.token.Token;
import com.printscript.parser.Parser;
import com.printscript.parser.PrintParser;
import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;

public class InterpreterAdapter implements PrintScriptInterpreter {
    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        final Lexer lexer = new Lexer();
        final Parser parser = new PrintParser();
        final InputAdapter input = new InputAdapter(provider);
        final OutputAdapter output = new OutputAdapter(emitter);
        final Interpreter interpreter = new GoatedInterpreter(input, output);
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
