package implementation;

import interpreter.ErrorHandler;
import interpreter.PrintScriptLinter;
import printscript.linter.DiagnosticReadResult;
import printscript.linter.DiagnosticSource;
import printscript.statement.StatementSource;
import printscript.v1.linter.PrintScriptV11LinterConfiguration;
import printscript.v1.linter.PrintScriptV11LinterConfigurationResult;
import printscript.v1.linter.PrintScriptV11LinterFactory;
import printscript.v1.linter.PrintScriptV1LinterConfiguration;
import printscript.v1.linter.PrintScriptV1LinterConfigurationResult;
import printscript.v1.linter.PrintScriptV1LinterFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

final class PrintScriptLinterAdapter implements PrintScriptLinter {

    @Override
    public void lint(
            InputStream source,
            String version,
            InputStream configuration,
            ErrorHandler errorHandler
    ) {
        Objects.requireNonNull(configuration, "configuration");
        Objects.requireNonNull(errorHandler, "errorHandler");

        final StatementSource statements = PrintScriptPipeline.statementsFrom(source, version);
        final String configurationJson = readUtf8(configuration);
        final DiagnosticSource diagnostics = diagnosticsFrom(
                version,
                statements,
                configurationJson
        );

        reportRemainingDiagnostics(diagnostics, errorHandler);
    }

    private DiagnosticSource diagnosticsFrom(
            String version,
            StatementSource statements,
            String configurationJson
    ) {
        if (LanguageVersion.isVersionOne(version)) {
            final PrintScriptV1LinterConfiguration configuration =
                    versionOneConfigurationFrom(configurationJson);
            return PrintScriptV1LinterFactory.create(configuration).lint(statements);
        }

        final PrintScriptV11LinterConfiguration configuration =
                versionOneOneConfigurationFrom(configurationJson);
        return PrintScriptV11LinterFactory.create(configuration).lint(statements);
    }

    private PrintScriptV1LinterConfiguration versionOneConfigurationFrom(String json) {
        final PrintScriptV1LinterConfigurationResult result =
                PrintScriptV1LinterFactory.configurationFrom(json);

        if (result instanceof PrintScriptV1LinterConfigurationResult.Success success) {
            return success.getConfiguration();
        }

        if (result instanceof PrintScriptV1LinterConfigurationResult.Failure failure) {
            throw new IllegalArgumentException(String.valueOf(failure.getError()));
        }

        throw new IllegalStateException("Unknown linter configuration result");
    }

    private PrintScriptV11LinterConfiguration versionOneOneConfigurationFrom(String json) {
        final PrintScriptV11LinterConfigurationResult result =
                PrintScriptV11LinterFactory.configurationFrom(json);

        if (result instanceof PrintScriptV11LinterConfigurationResult.Success success) {
            return success.getConfiguration();
        }

        if (result instanceof PrintScriptV11LinterConfigurationResult.Failure failure) {
            throw new IllegalArgumentException(String.valueOf(failure.getError()));
        }

        throw new IllegalStateException("Unknown linter configuration result");
    }

    private String readUtf8(InputStream source) {
        try {
            return new String(source.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException error) {
            throw new UncheckedIOException(error);
        }
    }

    private void reportRemainingDiagnostics(DiagnosticSource source, ErrorHandler errorHandler) {
        DiagnosticSource remainingSource = source;

        while (true) {
            final DiagnosticReadResult result = remainingSource.nextDiagnostic();

            if (result instanceof DiagnosticReadResult.EndOfInput) {
                return;
            }

            if (result instanceof DiagnosticReadResult.Failure failure) {
                errorHandler.reportError(String.valueOf(failure.getError()));
                return;
            }

            if (result instanceof DiagnosticReadResult.Success success) {
                errorHandler.reportError(String.valueOf(success.getDiagnostic()));
                remainingSource = success.getRemainingSource();
                continue;
            }

            throw new IllegalStateException("Unknown diagnostic result");
        }
    }
}
