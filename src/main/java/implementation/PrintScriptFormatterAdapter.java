package implementation;

import interpreter.PrintScriptFormatter;
import printscript.formatter.FormattedChunkReadResult;
import printscript.formatter.FormattedSource;
import printscript.token.TokenSource;
import printscript.v1.formatter.PrintScriptV11FormatterFactory;
import printscript.v1.formatter.PrintScriptV1FormatterFactory;
import printscript.v1.formatter.configuration.PrintScriptV11FormatterConfiguration;
import printscript.v1.formatter.configuration.PrintScriptV11FormatterConfigurationResult;
import printscript.v1.formatter.configuration.PrintScriptV1FormatterConfiguration;
import printscript.v1.formatter.configuration.PrintScriptV1FormatterConfigurationResult;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

final class PrintScriptFormatterAdapter implements PrintScriptFormatter {

    @Override
    public void format(InputStream source, String version, InputStream configuration, Writer writer) {
        Objects.requireNonNull(configuration, "configuration");
        Objects.requireNonNull(writer, "writer");

        final TokenSource tokens = PrintScriptPipeline.tokensFrom(source, version);
        final String configurationJson = readUtf8(configuration);
        final FormattedSource formattedSource = formattedSourceFrom(
                version,
                tokens,
                configurationJson
        );

        writeRemainingChunks(formattedSource, writer);
    }

    private FormattedSource formattedSourceFrom(
            String version,
            TokenSource tokens,
            String configurationJson
    ) {
        if (LanguageVersion.isVersionOne(version)) {
            final PrintScriptV1FormatterConfiguration configuration =
                    versionOneConfigurationFrom(configurationJson);
            return PrintScriptV1FormatterFactory.create(configuration).format(tokens);
        }

        final PrintScriptV11FormatterConfiguration configuration =
                versionOneOneConfigurationFrom(configurationJson);
        return PrintScriptV11FormatterFactory.create(configuration).format(tokens);
    }

    private PrintScriptV1FormatterConfiguration versionOneConfigurationFrom(String json) {
        final PrintScriptV1FormatterConfigurationResult result =
                PrintScriptV1FormatterConfiguration.fromJson(json);

        if (result instanceof PrintScriptV1FormatterConfigurationResult.Success success) {
            return success.getConfiguration();
        }

        if (result instanceof PrintScriptV1FormatterConfigurationResult.Failure failure) {
            throw new IllegalArgumentException(String.valueOf(failure.getError()));
        }

        throw new IllegalStateException("Unknown formatter configuration result");
    }

    private PrintScriptV11FormatterConfiguration versionOneOneConfigurationFrom(String json) {
        final PrintScriptV11FormatterConfigurationResult result =
                PrintScriptV11FormatterConfiguration.fromJson(json);

        if (result instanceof PrintScriptV11FormatterConfigurationResult.Success success) {
            return success.getConfiguration();
        }

        if (result instanceof PrintScriptV11FormatterConfigurationResult.Failure failure) {
            throw new IllegalArgumentException(String.valueOf(failure.getError()));
        }

        throw new IllegalStateException("Unknown formatter configuration result");
    }

    private String readUtf8(InputStream source) {
        try {
            return new String(source.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException error) {
            throw new UncheckedIOException(error);
        }
    }

    private void writeRemainingChunks(FormattedSource source, Writer writer) {
        FormattedSource remainingSource = source;

        while (true) {
            final FormattedChunkReadResult result = remainingSource.nextFormattedChunk();

            if (result instanceof FormattedChunkReadResult.EndOfInput) {
                return;
            }

            if (result instanceof FormattedChunkReadResult.Failure failure) {
                throw new IllegalStateException(String.valueOf(failure.getError()));
            }

            if (result instanceof FormattedChunkReadResult.Success success) {
                write(success.getFormattedText(), writer);
                remainingSource = success.getRemainingSource();
                continue;
            }

            throw new IllegalStateException("Unknown formatted chunk result");
        }
    }

    private void write(String text, Writer writer) {
        try {
            writer.write(text);
        } catch (IOException error) {
            throw new UncheckedIOException(error);
        }
    }
}
