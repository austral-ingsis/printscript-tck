package implementation;

import com.printscript.linter.Linter;
import com.printscript.linter.violation.Violation;
import com.printscript.models.node.ASTNode;
import com.printscript.models.token.Token;
import interpreter.ErrorHandler;
import interpreter.PrintScriptLinter;
import printScreen.lexer.com.printscript.lexer.Lexer;
import printScreen.parser.com.printscript.parser.IParser;
import printScreen.parser.com.printscript.parser.Parser;

import java.io.*;
import java.util.Iterator;
import java.util.List;

public class LinterAdapter implements PrintScriptLinter {
    private final Lexer lexer = new Lexer();
    private final IParser parser = new Parser();
    private final Linter linter = Linter.INSTANCE;
    private File file;
    private ErrorHandler handler;

    @Override
    public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
        final Reader reader = new InputStreamReader(src);
        final Iterator<List<Token>> tokens = lexer.lex(reader);
        final Iterator<ASTNode> ast = parser.parse(tokens);
        file = new Loader().loadFile(config);
        this.handler = handler;
        ast.forEachRemaining(this::lint);
    }

    private void lint(ASTNode node) {
        List<Violation> violations = linter.lint(node, file);
        List<String> result = violations.stream().map(Violation::toString).toList();
        result.forEach(handler::reportError);
    }
}
