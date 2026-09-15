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
 * AnalyzerConfig}. The TCK's field names (kebab-case, and structured differently — e.g. two
 * separate booleans for "force spacing on/off" around {@code =} instead of one field) are fixed by
 * its own test fixtures, not by us, so this translation lives here rather than reusing our own
 * {@code FormatterConfigLoader}/{@code AnalyzerConfigLoader}, which read <em>our</em> schema.
 *
 * <p>Three formatter keys have no field to map onto at all: {@code
 * mandatory-single-space-separation}, {@code mandatory-space-surrounding-operations} and {@code
 * mandatory-line-break-after-statement} are all things our formatter already does unconditionally
 * (see {@code ExpressionFormatter}/{@code PrintScriptFormatter}) — there's nothing to toggle, and
 * every TCK fixture that sets them to {@code true} already gets that behavior for free. A fourth,
 * {@code if-brace-below-line}, has no field on purpose: the consigna requires the opening brace of
 * an {@code if} to stay on the same line as the keyword, unconditionally — it's deliberately not
 * configurable in this implementation, so a fixture requesting Allman-style braces cannot pass.
 */
final class TckConfig {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private TckConfig() {}

    static FormatterConfig formatterConfigFrom(InputStream config) {
        JsonNode node = readTree(config);
        FormatterConfig defaults = FormatterConfig.defaultConfig();

        // Colon spacing: fall back to our own default when the key is absent. The TCK's own
        // formatter fixtures disagree with each other on the implicit baseline here (some expect
        // "x:number", others "x: number", for a key neither fixture's config.json even mentions) -
        // there's no single value that satisfies every fixture, so this keeps our own default,
        // consistent with our own test suite, rather than chasing one fixture's assumption at the
        // cost of another's.
        boolean spaceBeforeColon =
                node.path("enforce-spacing-before-colon-in-declaration")
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

        return new FormatterConfig(
                spaceBeforeColon,
                spaceAfterColon,
                spaceAroundEquals,
                newLinesBeforePrintln,
                indentSize);
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
