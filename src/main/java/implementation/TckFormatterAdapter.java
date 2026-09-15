package implementation;

import edu.austral.ingsis.printscript.common.OperatorDefinition;
import edu.austral.ingsis.printscript.common.Version;
import edu.austral.ingsis.printscript.common.ast.Statement;
import edu.austral.ingsis.printscript.formatter.FormatterConfig;
import edu.austral.ingsis.printscript.lexer.PrintScriptLexer;
import edu.austral.ingsis.printscript.lexer.StringPositionalSource;
import edu.austral.ingsis.printscript.parser.PrintScriptParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Set;

/**
 * Adapts our real formatter to the TCK's {@code interpreter.PrintScriptFormatter} contract.
 *
 * <p>One detail the golden fixtures require: our {@code PrintScriptFormatter} always ends its
 * output with a trailing newline (one {@code '\n'} per statement, including the last); the TCK's
 * {@code golden.ps} files don't have one. That trailing newline is trimmed here, right before
 * writing — the real formatter's own output/tests are unaffected, this is purely an adapter-level
 * concern.
 */
public final class TckFormatterAdapter implements interpreter.PrintScriptFormatter {

    @Override
    public void format(InputStream src, String version, InputStream config, Writer writer) {
        try {
            String source = Sources.readAll(src);
            Version parsedVersion = Version.fromLabel(version);
            Set<OperatorDefinition> noPlugins = Set.of();

            var lexer = new PrintScriptLexer(noPlugins);
            var parser = new PrintScriptParser(noPlugins, parsedVersion);
            Iterator<Statement> statements =
                    parser.parse(lexer.tokenize(new StringPositionalSource(source)));

            FormatterConfig formatterConfig = TckConfig.formatterConfigFrom(config);
            String formatted =
                    new edu.austral.ingsis.printscript.formatter.PrintScriptFormatter()
                            .format(statements, formatterConfig);
            if (formatted.endsWith("\n")) {
                formatted = formatted.substring(0, formatted.length() - 1);
            }
            writer.write(formatted);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
