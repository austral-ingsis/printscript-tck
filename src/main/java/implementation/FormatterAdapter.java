package implementation;

import com.google.gson.Gson;
import com.printscript.formatter.Formatter;
import com.printscript.lexer.util.PreConfiguredTokens;
import com.printscript.models.node.ASTNode;
import com.printscript.models.token.Token;
import com.printscript.parser.PrintParser;
import interpreter.PrintScriptFormatter;
import com.printscript.lexer.Lexer;
import com.printscript.parser.Parser;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class FormatterAdapter implements PrintScriptFormatter {
    private final PreConfiguredTokens instance = PreConfiguredTokens.INSTANCE;
    private final Parser parser = new PrintParser();
    Loader loader = new Loader();

    @Override
    public void format(InputStream src, String version, InputStream config, Writer writer) {
        final Lexer lexer = new Lexer(Objects.equals(version, "1.0") ? instance.getTOKENS_1_0() : instance.getTOKENS_1_1());
        final Reader reader = new InputStreamReader(src);
        final Iterator<List<Token>> tokens = lexer.lex(reader);
        final Iterator<ASTNode> ast = parser.parse(tokens);
        File file = loader.loadFile(config);
        Gson gson = new Gson();
        FormatterConfigAdapter adapter = gson.fromJson(loader.getReader(file), FormatterConfigAdapter.class);
        Formatter formatter = new Formatter(adapter.adapt());
        String formatted = formatter.format(ast);
        write(writer, formatted);
    }

    private void write(Writer writer, String formatted) {
        try {
            writer.write(formatted);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
