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

import java.io.InputStream;
import java.util.List;

public class PrintScriptLinterAdapter implements PrintScriptLinter {

    private final ErrorRenderer errors = new ErrorRenderer();
    private final FindingRenderer findings = new FindingRenderer();

    @Override
    public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
        final var dialect = Pipeline.dialect(version);
        if (dialect == null) {
            handler.reportError("Unsupported version: " + version);
            return;
        }

        Linter linter;
        try {
            linter = new Linter(LintConfig.Companion.read(config));
        } catch (ConfigError error) {
            handler.reportError(error.getMessage());
            return;
        }

        final var statements = Pipeline.statements(src, dialect);

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
}
