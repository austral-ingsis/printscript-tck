package implementation;

import formatter.ConfigProvider;
import formatter.FormatError;
import formatter.FormatResult;
import formatter.FormatSuccess;
import formatter.FormatterExecutor;
import formatter.formatrules.EnsureNoSpaceAroundEquals;
import formatter.formatrules.EnsureSpaceAfterColon;
import formatter.formatrules.EnsureSpaceAroundEquals;
import formatter.formatrules.EnsureSpaceBeforeColon;
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

    /**
     * Config por defecto con todas las reglas desactivadas (false/0), tal como espera
     * applyJsonConfig: lo que el json del TCK no active queda en su default "apagado".
     */
    private ConfigProvider defaultConfigFor(String version) {
        DeclarationFormatTokenizer declarationTokenizer = new DeclarationFormatTokenizer();
        AssignmentFormatTokenizer assignmentTokenizer = new AssignmentFormatTokenizer();
        ExpressionFormatTokenizer expressionTokenizer = new ExpressionFormatTokenizer();

        Map<FormatTokenizer, FormatRules> ruleSet = new LinkedHashMap<>();
        ruleSet.put(declarationTokenizer, defaultFormatRules());
        ruleSet.put(assignmentTokenizer, defaultFormatRules());
        ruleSet.put(expressionTokenizer, defaultFormatRules());

        if ("1.1".equals(version)) {
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

    private FormatRules defaultFormatRules() {
        List<FormatRule> rules = new ArrayList<>();
        rules.add(new EnsureSpaceAroundEquals(false));
        rules.add(new EnsureNoSpaceAroundEquals(false));
        rules.add(new EnsureSpaceBeforeColon(false));
        rules.add(new EnsureSpaceAfterColon(false));
        rules.add(new IfBraceSameLine(false));
        rules.add(new IfBraceBelowLine(false));
        rules.add(new IndentsInsideIf(0));
        rules.add(new LineBreaksAfterPrintLn(0));
        return new FormatRules(rules);
    }
}
