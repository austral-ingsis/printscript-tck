package adapters;

import interpreter.ErrorHandler;
import interpreter.PrintScriptLinter;
import kotlin.sequences.Sequence;
import lexer.Lexer;
import lexer.LexerFactory;
import main.*;
import main.kotlin.main.Linter;

import utils.LinterResult;
import utils.ParsingResult;
import utils.PercentageCollector;

import java.io.InputStream;
import java.util.Iterator;

public class CustomLinter implements PrintScriptLinter {
    @Override
    public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
        LinterConfig configuration = ConfigParser.Companion.parseConfig(config);
        PercentageCollector collector = new PercentageCollector();
        int length = 0;
        try {
             length = src.available();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Lexer lexer = new LexerFactory().createLexer(src, version, collector);
        Sequence<Token> tokens = lexer.tokenize();
        Parser parser = new ParserFactory().createParser(version, tokens.iterator());
        Sequence<ParsingResult> astNodes = parser.parseExpressions();
        Iterator<LinterResult> errors = new Linter().lint(astNodes, configuration).iterator();
        while (errors.hasNext()) {
            handler.reportError(errors.next().getMessage());
        }
    }
}
