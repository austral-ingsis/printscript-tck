package implementation;

import com.printscript.lexer.Lexer;
import com.printscript.lexer.StreamSourceReader;
import com.printscript.lexer.recognizer.TokenRecognizers;
import com.printscript.parser.Parser;
import com.printscript.parser.syntax.StatementSyntaxes;
import com.printscript.pipeline.StatementStream;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Set;

final class Pipeline {

    private static final Set<String> SUPPORTED_VERSIONS = Set.of("1.0", "1.1");

    private Pipeline() {
    }

    static boolean supports(String version) {
        return SUPPORTED_VERSIONS.contains(version);
    }

    static Lexer lexer(InputStream src) {
        final var reader = new InputStreamReader(src, StandardCharsets.UTF_8);

        return new Lexer(new StreamSourceReader(reader), TokenRecognizers.INSTANCE.getDEFAULT());
    }

    static StatementStream statements(InputStream src) {
        final var parser = new Parser(StatementSyntaxes.INSTANCE.getDEFAULT());

        return new StatementStream(lexer(src)::tokens, parser::parse);
    }
}
