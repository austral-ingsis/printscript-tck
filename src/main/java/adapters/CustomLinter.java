package adapters;

import interpreter.ErrorHandler;
import interpreter.PrintScriptLinter;
import kotlin.sequences.Sequence;
import main.Parser;
import main.Position;
import main.Token;
import main.kotlin.main.ConfigParser;
import main.kotlin.main.Linter;
import main.kotlin.main.LinterConfig;
import main.kotlin.main.LinterResult;
import org.example.lexer.Lexer;
import utils.ParsingResult;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

public class CustomLinter implements PrintScriptLinter {
    @Override
    public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
        if (version.equals("1.1")) {
            throw new UnsupportedOperationException("Version 1.1 is not supported");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(src));
        LinterConfig configuration = ConfigParser.Companion.parseConfig(config);
        Lexer lexer = new Lexer(reader, 0, new Position(1,1));
        Sequence<Token> tokens = lexer.tokenizeAll(lexer);
        Parser parser = new Parser(tokens.iterator());
        Sequence<ParsingResult> astNodes = parser.parseExpressions();
        Iterator<LinterResult> errors = new Linter().lint(astNodes, configuration).iterator();
        while (errors.hasNext()) {
            handler.reportError(errors.next().getMessage());
        }
    }
}
