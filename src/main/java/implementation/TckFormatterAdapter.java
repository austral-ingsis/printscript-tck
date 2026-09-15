package implementation;

import edu.austral.ingsis.printscript.common.OperatorDefinition;
import edu.austral.ingsis.printscript.common.Version;
import edu.austral.ingsis.printscript.common.ast.Statement;
import edu.austral.ingsis.printscript.formatter.FormatterConfig;
import edu.austral.ingsis.printscript.lexer.FilePositionalSource;
import edu.austral.ingsis.printscript.lexer.PrintScriptLexer;
import edu.austral.ingsis.printscript.parser.PrintScriptParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Set;

/**
 * Adapts our real formatter to the TCK's {@code interpreter.PrintScriptFormatter} contract.
 */
public final class TckFormatterAdapter implements interpreter.PrintScriptFormatter {

    @Override
    public void format(InputStream src, String version, InputStream config, Writer writer) {
        Path tempFile = Sources.spoolToTempFile(src);
        try {
            Version parsedVersion = Version.fromLabel(version);
            Set<OperatorDefinition> noPlugins = Set.of();

            String formatted;
            try (FilePositionalSource source = new FilePositionalSource(tempFile)) {
                var lexer = new PrintScriptLexer(noPlugins);
                var parser = new PrintScriptParser(noPlugins, parsedVersion);
                Iterator<Statement> statements = parser.parse(lexer.tokenize(source));

                FormatterConfig formatterConfig = TckConfig.formatterConfigFrom(config);
                formatted =
                        new edu.austral.ingsis.printscript.formatter.PrintScriptFormatter()
                                .format(statements, formatterConfig);
            }
            if (formatted.endsWith("\n")) {
                formatted = formatted.substring(0, formatted.length() - 1);
            }
            writer.write(formatted);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } finally {
            TckInterpreterAdapter.deleteQuietly(tempFile);
        }
    }
}
