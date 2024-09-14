package implementation;

import com.google.gson.Gson;
import com.printscript.formatter.Formatter;
import com.printscript.models.node.ASTNode;
import com.printscript.models.token.Token;
import com.printscript.parser.PrintParser;
import interpreter.PrintScriptFormatter;
import com.printscript.lexer.Lexer;
import com.printscript.parser.Parser;

import java.io.*;
import java.util.Iterator;
import java.util.List;

public class FormatterAdapter implements PrintScriptFormatter {
    private final Lexer lexer = new Lexer();
    private final Parser parser = new PrintParser();
    private Formatter formatter;
    private Writer writer;

    @Override
    public void format(InputStream src, String version, InputStream config, Writer writer) {
        Loader loader = new Loader();
        final Reader reader = new InputStreamReader(src);
        final Iterator<List<Token>> tokens = lexer.lex(reader);
        final Iterator<ASTNode> ast = parser.parse(tokens);
        File file = loader.loadFile(config);
        Gson gson = new Gson();
        FormatterConfigAdapter adapter = gson.fromJson(loader.getReader(file), FormatterConfigAdapter.class);
        formatter = new Formatter(adapter.adapt());
        this.writer = writer;
        ast.forEachRemaining(this::format);
    }

    private void format(ASTNode node) {
        String formatted = formatter.format(node);
        try {
            writer.write(formatted);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
