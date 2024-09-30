package implementation;

import com.printscript.interpreter.Interpreter;
import com.printscript.interpreter.builder.InterpreterBuilder;
import com.printscript.interpreter.strategy.PreConfiguredProviders;
import com.printscript.lexer.Lexer;
import com.printscript.lexer.TokenProvider;
import com.printscript.lexer.util.PreConfiguredTokens;
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
import java.util.Objects;

public class InterpreterAdapter implements PrintScriptInterpreter {
    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        PreConfiguredTokens tokProv = PreConfiguredTokens.INSTANCE;
        TokenProvider prov = Objects.equals(version, "1.0") ? tokProv.getTOKENS_1_0() : tokProv.getTOKENS_1_1();
        final Lexer lexer = new Lexer(prov);
        final Parser parser = new PrintParser();
        final InputAdapter input = new InputAdapter(provider);
        final OutputAdapter output = new OutputAdapter(emitter);
        InterpreterBuilder builder = new InterpreterBuilder();
        builder.setInput(input);
        builder.setOutput(output);
        PreConfiguredProviders providers = PreConfiguredProviders.INSTANCE;
        builder.setProvider(Objects.equals(version, "1.0") ? providers.getVERSION_1_0() : providers.getVERSION_1_1());
        try {
            Interpreter interpreter = builder.build();
            Reader reader = new InputStreamReader(src);
            Iterator<List<Token>> tokens = lexer.lex(reader);
            Iterator<ASTNode> ast = parser.parse(tokens);
            interpreter.interpret(ast);
        } catch (Throwable e) {
            handler.reportError(e.getMessage().split(":")[0]);
        }
    }
}
