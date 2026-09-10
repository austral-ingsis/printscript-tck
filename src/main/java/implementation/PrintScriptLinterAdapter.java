package implementation;

import com.printscript.ast.Statement;
import com.printscript.linter.Linter;
import com.printscript.linter.config.ConfigError;
import com.printscript.linter.config.LintConfig;
import com.printscript.linter.report.FindingRenderer;
import com.printscript.report.ErrorRenderer;
import com.printscript.report.Failure;
import com.printscript.report.Result;
import com.printscript.report.Success;

import interpreter.ErrorHandler;
import interpreter.PrintScriptLinter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

public class PrintScriptLinterAdapter implements PrintScriptLinter {

    private static final Pattern IDENTIFIER_FORMAT =
            Pattern.compile("(\"identifier_format\"\\s*:\\s*)\"([^\"]*)\"");

    private final ErrorRenderer errors = new ErrorRenderer();
    private final FindingRenderer findings = new FindingRenderer();

    @Override
    public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
        if (!Pipeline.supports(version)) {
            handler.reportError("Unsupported version: " + version);
            return;
        }

        Linter linter;
        try {
            linter = new Linter(LintConfig.Companion.read(asObjectShape(config)));
        } catch (ConfigError | IOException error) {
            handler.reportError(error.getMessage());
            return;
        }

        final var statements = Pipeline.statements(src);

        while (statements.hasNext()) {
            final Result<Statement> parsed = statements.next();
            if (parsed instanceof Failure failure) {
                handler.reportError(errors.render(failure.getError()));
                return;
            }

            final var statement = ((Success<Statement>) parsed).getValue();

            for (var finding : linter.lint(List.of(statement)).getFindings()) {
                handler.reportError(findings.render(finding));
            }
        }
    }

    private InputStream asObjectShape(InputStream config) throws IOException {
        final var text = new String(config.readAllBytes(), StandardCharsets.UTF_8);
        final var rule = IDENTIFIER_FORMAT.matcher(text);

        if (!rule.find()) {
            return new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        }

        final var style = rule.group(2).toLowerCase().replaceAll("\\s+", "");
        final var settings = new StringBuilder(text)
                .replace(
                        rule.start(),
                        rule.end(),
                        rule.group(1) + "{\"enabled\":true,\"style\":\"" + style + "\"}")
                .toString();

        return new ByteArrayInputStream(settings.getBytes(StandardCharsets.UTF_8));
    }
}
