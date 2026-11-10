package implementation;

import interpreter.ErrorHandler;
import interpreter.PrintScriptLinter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.printscript.analyzer.violation.Violation;
import org.printscript.rules.RuleConfig;
import org.printscript.runner.LanguageServices;
import org.printscript.runner.LanguageVersion;

public class PrintScriptLinterAdapter implements PrintScriptLinter {

    @Override
    public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
        try {
            LanguageServices services = LanguageServices.printScript();
            LanguageVersion languageVersion = LanguageVersion.fromLabel(version);

            Reader configReader = new InputStreamReader(config, StandardCharsets.UTF_8);
            RuleConfig rules = RuleConfig.fromJson(configReader);

            Reader srcReader = new InputStreamReader(src, StandardCharsets.UTF_8);
            List<Violation> violations = services.analysis().analyze(srcReader, languageVersion, rules);

            for (Violation violation : violations) {
                handler.reportError(violation.toString());
            }

        } catch (Exception e) {
            handler.reportError(e.getMessage());
        }
    }
}
