package implementation;

import com.printscript.formatter.Formatter;
import com.printscript.models.node.ASTNode;
import com.printscript.models.token.Token;
import interpreter.PrintScriptFormatter;
import printScreen.lexer.com.printscript.lexer.Lexer;
import printScreen.parser.com.printscript.parser.IParser;
import printScreen.parser.com.printscript.parser.Parser;

import java.io.*;
import java.util.Iterator;
import java.util.List;

public class FormatterAdapter implements PrintScriptFormatter {
    private final Lexer lexer = new Lexer();
    private final IParser parser = new Parser();
    private final Formatter formatter = Formatter.INSTANCE;
    private File file;
    private Writer writer;

    @Override
    public void format(InputStream src, String version, InputStream config, Writer writer) {
        final Reader reader = new InputStreamReader(src);
        final Iterator<List<Token>> tokens = lexer.lex(reader);
        final Iterator<ASTNode> ast = parser.parse(tokens);
        file = new Loader().loadFile(config);
        this.writer = writer;
        ast.forEachRemaining(this::format);
    }

    private void format(ASTNode node) {
        String formatted = formatter.format(node, file);
        try {
            writer.write(formatted);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
