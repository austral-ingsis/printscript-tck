package implementation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.austral.ingsis.printscript.analyzer.AnalyzerConfig;
import edu.austral.ingsis.printscript.analyzer.IdentifierCase;
import edu.austral.ingsis.printscript.formatter.FormatterConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

/**
 * Translates the TCK's own {@code config.json} shape into our {@link FormatterConfig}/{@link
 * AnalyzerConfig}.
 */
final class TckConfig {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private TckConfig() {}

    static FormatterConfig formatterConfigFrom(InputStream config) {
        JsonNode node = readTree(config);
        FormatterConfig defaults = FormatterConfig.defaultConfig();

        boolean singleSpaceSeparation = node.path("mandatory-single-space-separation").asBoolean(false);

        boolean spaceBeforeColon =
                singleSpaceSeparation
                        || node.path("enforce-spacing-before-colon-in-declaration")
                                .asBoolean(defaults.spaceBeforeColon());
        boolean spaceAfterColon =
                node.path("enforce-spacing-after-colon-in-declaration")
                        .asBoolean(defaults.spaceAfterColon());

        boolean spaceAroundEquals = defaults.spaceAroundEquals();
        if (node.has("enforce-spacing-around-equals")) {
            spaceAroundEquals = node.get("enforce-spacing-around-equals").asBoolean();
        } else if (node.has("enforce-no-spacing-around-equals")) {
            spaceAroundEquals = !node.get("enforce-no-spacing-around-equals").asBoolean();
        }

        int newLinesBeforePrintln =
                node.path("line-breaks-after-println").asInt(defaults.newLinesBeforePrintln());
        int indentSize = node.path("indent-inside-if").asInt(defaults.indentSize());

        boolean ifBraceBelowLine = defaults.ifBraceBelowLine();
        if (node.has("if-brace-below-line")) {
            ifBraceBelowLine = node.get("if-brace-below-line").asBoolean();
        } else if (node.has("if-brace-same-line")) {
            ifBraceBelowLine = !node.get("if-brace-same-line").asBoolean();
        }

        return new FormatterConfig(
                spaceBeforeColon,
                spaceAfterColon,
                spaceAroundEquals,
                newLinesBeforePrintln,
                indentSize,
                singleSpaceSeparation,
                ifBraceBelowLine);
    }

    static AnalyzerConfig analyzerConfigFrom(InputStream config) {
        JsonNode node = readTree(config);

        boolean identifierCaseCheckEnabled = node.has("identifier_format");
        IdentifierCase identifierCase =
                identifierCaseCheckEnabled
                        ? identifierCaseFrom(node.get("identifier_format").asText())
                        : IdentifierCase.CAMEL_CASE; // unused: the check above is disabled
        boolean printlnRule = node.path("mandatory-variable-or-literal-in-println").asBoolean(false);
        boolean readRule =
                node.path("mandatory-variable-or-literal-in-readInput").asBoolean(false);

        return new AnalyzerConfig(identifierCaseCheckEnabled, identifierCase, printlnRule, readRule);
    }

    private static IdentifierCase identifierCaseFrom(String text) {
        return switch (text) {
            case "camel case" -> IdentifierCase.CAMEL_CASE;
            case "snake case" -> IdentifierCase.SNAKE_CASE;
            default ->
                    throw new IllegalArgumentException("Unknown identifier_format: '" + text + "'");
        };
    }

    private static JsonNode readTree(InputStream config) {
        try {
            return MAPPER.readTree(config);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
