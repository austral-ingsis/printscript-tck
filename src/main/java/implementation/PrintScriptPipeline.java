package implementation;

import printscript.lexer.Lexer;
import printscript.parser.Parser;
import printscript.source.SourceReader;
import printscript.source.SourceReaderCreationResult;
import printscript.source.SourceReaderFactory;
import printscript.statement.StatementSource;
import printscript.token.TokenSource;
import printscript.v1.lexer.PrintScriptV11FormattingLexerFactory;
import printscript.v1.lexer.PrintScriptV11LexerFactory;
import printscript.v1.lexer.PrintScriptV1FormattingLexerFactory;
import printscript.v1.lexer.PrintScriptV1LexerFactory;
import printscript.v1.parser.PrintScriptV11ParserFactory;
import printscript.v1.parser.PrintScriptV1ParserFactory;

import java.io.InputStream;
import java.util.Objects;

final class PrintScriptPipeline {

    private PrintScriptPipeline() {
    }

    static StatementSource statementsFrom(InputStream source, String version) {
        Objects.requireNonNull(source, "source");
        LanguageVersion.requireSupported(version);

        final SourceReader sourceReader = sourceReaderFrom(source);
        final Lexer lexer = lexerFor(version);
        final Parser parser = parserFor(version);

        return parser.parse(lexer.tokenize(sourceReader));
    }

    static TokenSource formattingTokensFrom(InputStream source, String version) {
        Objects.requireNonNull(source, "source");
        LanguageVersion.requireSupported(version);

        final SourceReader sourceReader = sourceReaderFrom(source);
        return formattingLexerFor(version).tokenize(sourceReader);
    }

    private static Lexer lexerFor(String version) {
        if (LanguageVersion.isVersionOne(version)) {
            return PrintScriptV1LexerFactory.create();
        }

        return PrintScriptV11LexerFactory.create();
    }

    private static Lexer formattingLexerFor(String version) {
        if (LanguageVersion.isVersionOne(version)) {
            return PrintScriptV1FormattingLexerFactory.create();
        }

        return PrintScriptV11FormattingLexerFactory.create();
    }

    private static Parser parserFor(String version) {
        if (LanguageVersion.isVersionOne(version)) {
            return PrintScriptV1ParserFactory.create();
        }

        return PrintScriptV11ParserFactory.create();
    }

    private static SourceReader sourceReaderFrom(InputStream source) {
        final SourceReaderCreationResult creation = SourceReaderFactory.fromInputStream(source);

        if (creation instanceof SourceReaderCreationResult.Success success) {
            return success.getReader();
        }

        if (creation instanceof SourceReaderCreationResult.Failure failure) {
            throw new IllegalArgumentException(String.valueOf(failure.getError()));
        }

        throw new IllegalStateException("Unknown source reader creation result");
    }
}
