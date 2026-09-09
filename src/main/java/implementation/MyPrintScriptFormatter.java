package implementation;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import interpreter.PrintScriptFormatter;

import printscript.formatter.FormattingRules;
import printscript.formatter.FormattingRulesLoader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class MyPrintScriptFormatter implements PrintScriptFormatter {

    private static final Map<String, List<String>> KEY_TRANSLATION = Map.of(
            "enforce-spacing-around-equals", List.of("assignment_space_before_equals", "assignment_space_after_equals"),
            "enforce-spacing-before-colon-in-declaration", List.of("declaration_space_before_colon"),
            "enforce-spacing-after-colon-in-declaration", List.of("declaration_space_after_colon"),
            "line-breaks-after-println", List.of("println_new_lines_before_call")
    );

    @Override
    public void format(InputStream src, String version, InputStream config, Writer writer) {
        JsonObject original = JsonParser.parseReader(new InputStreamReader(config, StandardCharsets.UTF_8)).getAsJsonObject();
        JsonObject translated = translate(original);

        FormattingRules rules = new FormattingRulesLoader().load(new StringReader(translated.toString()));

        Reader srcReader = new InputStreamReader(src, StandardCharsets.UTF_8);
        new printscript.formatter.PrintScriptFormatter(rules).format(srcReader, writer);
    }

    private JsonObject translate(JsonObject original) {
        JsonObject translated = new JsonObject();

        if (original.has("enforce-no-spacing-around-equals") && original.get("enforce-no-spacing-around-equals").getAsBoolean()) {
            translated.addProperty("assignment_space_before_equals", false);
            translated.addProperty("assignment_space_after_equals", false);
        }

        for (String key : original.keySet()) {
            if (KEY_TRANSLATION.containsKey(key)) {
                for (String newKey : KEY_TRANSLATION.get(key)) {
                    translated.add(newKey, original.get(key));
                }
            }
        }

        return translated;
    }
}