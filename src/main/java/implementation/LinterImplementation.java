package implementation;

import interpreter.ErrorHandler;
import interpreter.PrintScriptLinter;
import linter.Linter;
import linter.Warning;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class LinterImplementation implements PrintScriptLinter {

    @Override
    public void lint(
            InputStream src,
            String version,
            InputStream config,
            ErrorHandler handler
    ) {
        try {
            String sourceCode = new String(src.readAllBytes(),
                    StandardCharsets.UTF_8);

            TckLinterConfigAdapter adapter = new TckLinterConfigAdapter();
            String internalConfigJson = adapter.adapt(config);
            Linter linter = Linter.Companion.fromJson(internalConfigJson, version);
            List<Warning> warningList = linter.analyse(sourceCode);
            for(Warning warning: warningList){
                handler.reportError(warning.getMessage());
            }

        } catch (IOException e) {
            handler.reportError("Error al leer el archivo: " + e.
                    getMessage());
        }
    }
}