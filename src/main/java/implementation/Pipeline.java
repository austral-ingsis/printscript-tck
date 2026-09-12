package implementation;

import com.printscript.app.Dialect;
import com.printscript.lexer.Lexer;
import com.printscript.lexer.StreamSourceReader;
import com.printscript.parser.Parser;
import com.printscript.pipeline.StatementStream;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

final class Pipeline {

    private Pipeline() {
    }

    static Dialect dialect(String version) {
        return Dialect.Companion.of(version);
    }

    static Lexer lexer(InputStream src, Dialect dialect) {
        final var reader = new InputStreamReader(src, StandardCharsets.UTF_8);

        return new Lexer(new StreamSourceReader(reader), dialect.getRecognizers());
    }

    static StatementStream statements(InputStream src, Dialect dialect) {
        final var parser = new Parser(dialect.getSyntaxes(), dialect.getParselets());

        return new StatementStream(
                lexer(src, dialect)::tokens,
                parser::parse,
                dialect.getBoundary());
    }
}
