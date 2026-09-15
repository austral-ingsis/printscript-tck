package implementation;

import formatter.ConfigProvider;
import formatter.FormatError;
import formatter.FormatResult;
import formatter.FormatSuccess;
import formatter.FormatterExecutor;
import formatter.formatrules.EnsureNoSpaceAroundEquals;
import formatter.formatrules.EnsureSingleSpace;
import formatter.formatrules.EnsureSpaceAfterColon;
import formatter.formatrules.EnsureSpaceAroundEquals;
import formatter.formatrules.EnsureSpaceBeforeColon;
import formatter.formatrules.EnsureSpacesSurroundingOperations;
import formatter.formatrules.FormatRule;
import formatter.formatrules.FormatRules;
import formatter.formatrules.IfBraceBelowLine;
import formatter.formatrules.IfBraceSameLine;
import formatter.formatrules.IndentsInsideIf;
import formatter.formatrules.LineBreaksAfterPrintLn;
import formatter.formattokens.AssignmentFormatTokenizer;
import formatter.formattokens.ConditionalFormatTokenizer;
import formatter.formattokens.DeclarationFormatTokenizer;
import formatter.formattokens.ExpressionFormatTokenizer;
import formatter.formattokens.FormatTokenizer;
import interpreter.PrintScriptFormatter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FormatterImplementation implements PrintScriptFormatter {

    @Override
    public void format(InputStream src, String version, InputStream config, Writer writer) {
        Path sourceFile = null;
        Path configFile = null;
        try {
            sourceFile = copyToTempFile(src, "printscript-format-src", ".ps");
            configFile = copyToTempFile(config, "printscript-format-config", ".json");

            ConfigProvider defaultConfig = defaultConfigFor(version);
            FormatterExecutor formatterExecutor = new FormatterExecutor(defaultConfig, version);

            FormatResult<String, String> result =
                    formatterExecutor.execute(sourceFile.toFile(), configFile.toAbsolutePath().toString());

            if (result instanceof FormatSuccess) {
                String formatted = ((FormatSuccess<String, String>) result).getValue();
                // El tokenizer de cada statement termina en EOL, asi que el ultimo statement
                // del programa tambien lo arrastra. Ademas, LineBreaksAfterPrintLn no tiene forma
                // de saber si el println que esta formateando es el ultimo statement del programa
                // (cada AST se formatea por separado y se concatena despues), asi que si el ultimo
                // statement es un println con "lines-after-println" > 0, puede agregar varios saltos
                // de mas al final. El golden (leido con BufferedReader.lines()) nunca tiene saltos
                // finales, asi que se recortan todos, no solo uno.
                String lineSeparator = System.lineSeparator();
                while (formatted.endsWith(lineSeparator)) {
                    formatted = formatted.substring(0, formatted.length() - lineSeparator.length());
                }
                writer.write(formatted);
                writer.flush();
            } else if (result instanceof FormatError) {
                String errorMessage = ((FormatError<String, String>) result).getValue();
                System.err.println("Error al formatear: " + errorMessage);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        } finally {
            deleteQuietly(sourceFile);
            deleteQuietly(configFile);
        }
    }

    private Path copyToTempFile(InputStream input, String prefix, String suffix) throws IOException {
        Path tempFile = Files.createTempFile(prefix, suffix);
        Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
        return tempFile;
    }

    private void deleteQuietly(Path path) {
        if (path == null) {
            return;
        }
        try {
            Files.deleteIfExists(path);
        } catch (IOException ignored) {
            // best effort cleanup
        }
    }

    private ConfigProvider defaultConfigFor(String version) {
        DeclarationFormatTokenizer declarationTokenizer = new DeclarationFormatTokenizer();
        AssignmentFormatTokenizer assignmentTokenizer = new AssignmentFormatTokenizer();
        ExpressionFormatTokenizer expressionTokenizer = new ExpressionFormatTokenizer();

        Map<FormatTokenizer, FormatRules> ruleSet = new LinkedHashMap<>();
        ruleSet.put(declarationTokenizer, defaultFormatRules());
        ruleSet.put(assignmentTokenizer, defaultFormatRules());
        ruleSet.put(expressionTokenizer, defaultFormatRules());

        if ("1.1".equals(version)) { //le agrego el conditional tokenizer
            Set<FormatTokenizer> statementFormatters = new LinkedHashSet<>();
            statementFormatters.add(declarationTokenizer);
            statementFormatters.add(assignmentTokenizer);
            statementFormatters.add(expressionTokenizer);

            ConditionalFormatTokenizer conditionalTokenizer = new ConditionalFormatTokenizer(statementFormatters);
            statementFormatters.add(conditionalTokenizer);

            ruleSet.put(conditionalTokenizer, defaultFormatRules());
        }

        return new ConfigProvider(ruleSet);
    }

    /**
     * Reglas con gate "activated" (EnsureNoSpaceAroundEquals, EnsureSpaceBeforeColon,
     * IfBraceSameLine, IfBraceBelowLine, EnsureSingleSpace, EnsureSpacesSurroundingOperations)
     * quedan en false: ese es su estado neutro real, tal como espera applyJsonConfig (lo
     * que el json del TCK no active queda "apagado").
     *
     * IndentsInsideIf y LineBreaksAfterPrintLn son distintas: no tienen gate "activated"
     * en FormatRule.kt, se aplican siempre. LineBreaksAfterPrintLn ahora hace "lines + 1"
     * internamente (0 = separación normal, no colapso), así que su default vuelve a ser 0.
     * IndentsInsideIf sigue necesitando 2 (0 borraría toda la indentación por defecto). Y
     * EnsureSpaceAroundEquals / EnsureSpaceAfterColon, aunque sí tienen gate, se esperan
     * activas por defecto (evidencia repetida en varias familias de fixtures del TCK:
     * goldens con "=" y ": " espaciados sin que ningún config lo pida).
     */
    private FormatRules defaultFormatRules() {
        List<FormatRule> rules = new ArrayList<>();
        rules.add(new EnsureSpaceAroundEquals(true));
        rules.add(new EnsureNoSpaceAroundEquals(false));
        rules.add(new EnsureSpaceBeforeColon(false));
        rules.add(new EnsureSpaceAfterColon(true));
        rules.add(new EnsureSpacesSurroundingOperations(false));
        rules.add(new EnsureSingleSpace(false));
        rules.add(new IfBraceSameLine(false));
        rules.add(new IfBraceBelowLine(false));
        rules.add(new IndentsInsideIf(2));
        rules.add(new LineBreaksAfterPrintLn(0));
        return new FormatRules(rules);
    }
}
