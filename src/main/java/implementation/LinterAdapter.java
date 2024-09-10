package implementation;

import com.google.gson.Gson;
import com.printscript.linter.Linter;
import com.printscript.linter.LinterConfig;
import com.printscript.linter.violation.Violation;
import com.printscript.models.node.ASTNode;
import com.printscript.models.token.Token;
import com.printscript.parser.PrintParser;
import interpreter.ErrorHandler;
import interpreter.PrintScriptLinter;
import com.printscript.lexer.Lexer;
import com.printscript.parser.Parser;

import java.io.*;
import java.util.Iterator;
import java.util.List;

public class LinterAdapter implements PrintScriptLinter {
    private final Lexer lexer = new Lexer();
    private final Parser parser = new PrintParser();
    private final Linter linter = Linter.INSTANCE;
    private LinterConfig config;
    private ErrorHandler handler;

    @Override
    public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
        final Reader reader = new InputStreamReader(src);
        final Iterator<List<Token>> tokens = lexer.lex(reader);
        final Iterator<ASTNode> ast = parser.parse(tokens);
        Loader loader = new Loader();
        File file = loader.loadFile(config);
        Gson gson = new Gson();
        ConfigAdapter adapter = gson.fromJson(loader.getReader(file), ConfigAdapter.class);
        this.config = adapter.adapt();
        this.handler = handler;
        ast.forEachRemaining(this::lint);
    }

    private void lint(ASTNode node) {
        List<Violation> violations = linter.lint(node, config);
        List<String> result = violations.stream().map(Violation::toString).toList();
        result.forEach(handler::reportError);
    }
}
