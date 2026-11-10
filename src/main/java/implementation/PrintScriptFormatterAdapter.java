package implementation;

import interpreter.PrintScriptFormatter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import org.printscript.rules.RuleConfig;
import org.printscript.runner.LanguageServices;
import org.printscript.runner.LanguageVersion;

public class PrintScriptFormatterAdapter implements PrintScriptFormatter {

    @Override
    public void format(InputStream src, String version, InputStream config, Writer writer) {
        try {
            LanguageServices services = LanguageServices.printScript();
            LanguageVersion languageVersion = LanguageVersion.fromLabel(version);

            Reader configReader = new InputStreamReader(config, StandardCharsets.UTF_8);
            RuleConfig rules = RuleConfig.fromJson(configReader);

            Reader srcReader = new InputStreamReader(src, StandardCharsets.UTF_8);
            services.formatting().format(srcReader, languageVersion, rules, writer);
        } catch (Exception e) {
            throw new RuntimeException("Formatter error: " + e.getMessage(), e);
        }
    }
}
