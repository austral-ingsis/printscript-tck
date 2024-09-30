package implementation;

import com.google.gson.Gson;
import com.printscript.lexer.util.PreConfiguredTokens;
import com.printscript.linter.Linter;
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
import java.util.Objects;

public class LinterAdapter implements PrintScriptLinter {
    private final PreConfiguredTokens instance = PreConfiguredTokens.INSTANCE;
    private final Parser parser = new PrintParser();
    private final Loader loader = new Loader();

    @Override
    public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
        final Reader reader = new InputStreamReader(src);
        final Lexer lexer = new Lexer(Objects.equals(version, "1.0") ? instance.getTOKENS_1_0() : instance.getTOKENS_1_1());
        final Iterator<List<Token>> tokens = lexer.lex(reader);
        final Iterator<ASTNode> ast = parser.parse(tokens);
        File file = loader.loadFile(config);
        Gson gson = new Gson();
        LinterConfigAdapter adapter = gson.fromJson(loader.getReader(file), LinterConfigAdapter.class);
        Linter linter = new Linter(adapter.adapt());
        List<String> violations = linter.lint(ast).stream().map(Violation::toString).toList();
        violations.forEach(handler::reportError);

    }
}
