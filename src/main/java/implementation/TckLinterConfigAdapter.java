package implementation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Adaptador puro y desacoplado que traduce la configuración JSON del formato TCK
 * al formato JSON interno esperado por el Linter de PrintScript.
 */
public class TckLinterConfigAdapter {

    /**
     * Convierte un InputStream con la configuración del TCK a un String JSON
     * en el formato interno del Linter.
     *
     * @param tckConfigStream InputStream del archivo de configuración del TCK.
     * @return String JSON con la estructura {"rules": [...]}.
     */
    public String adapt(InputStream tckConfigStream) {
        if (tckConfigStream == null) {
            return "{\"rules\":[]}";
        }
        try {
            String json = new String(tckConfigStream.readAllBytes(), StandardCharsets.UTF_8);
            return adapt(json);
        } catch (IOException e) {
            return "{\"rules\":[]}";
        }
    }

    /**
     * Convierte un String JSON con la configuración del TCK a un String JSON
     * en el formato interno del Linter.
     *
     * @param tckConfigJson Contenido JSON en formato TCK.
     * @return String JSON con la estructura {"rules": [...]}.
     */
    public String adapt(String tckConfigJson) {
        if (tckConfigJson == null || tckConfigJson.trim().isEmpty() || tckConfigJson.trim().equals("{}")) {
            return "{\"rules\":[]}";
        }

        Map<String, String> entries = parseFlatJson(tckConfigJson);
        List<String> rulesJson = new ArrayList<>();

        for (Map.Entry<String, String> entry : entries.entrySet()) {
            String key = entry.getKey().trim();
            String value = entry.getValue().trim();

            if (key.equalsIgnoreCase("identifier_format") || key.equalsIgnoreCase("identifier-format")) {
                String convention = normalizeConvention(value);
                if (convention != null) {
                    rulesJson.add(String.format(
                        "{\"name\":\"identifier-format\",\"enabled\":true,\"params\":{\"convention\":\"%s\"}}",
                        convention
                    ));
                }
            } else if (key.equalsIgnoreCase("mandatory-variable-or-literal-in-println")
                    || key.equalsIgnoreCase("mandatory_variable_or_literal_in_println")) {
                if (Boolean.parseBoolean(value) || value.equalsIgnoreCase("true")) {
                    rulesJson.add("{\"name\":\"println-no-expression\",\"enabled\":true,\"params\":{}}");
                }
            } else if (key.equalsIgnoreCase("mandatory-variable-or-literal-in-readInput")
                    || key.equalsIgnoreCase("mandatory_variable_or_literal_in_readInput")) {
                if (Boolean.parseBoolean(value) || value.equalsIgnoreCase("true")) {
                    rulesJson.add("{\"name\":\"read-input-no-expression\",\"enabled\":true,\"params\":{}}");
                }
            }
        }

        return "{\"rules\":[" + String.join(",", rulesJson) + "]}";
    }

    private String normalizeConvention(String rawValue) {
        String clean = rawValue.replace("\"", "").trim().toLowerCase();
        if (clean.contains("snake")) {
            return "snake_case";
        } else if (clean.contains("camel")) {
            return "camelCase";
        }
        return null;
    }

    private Map<String, String> parseFlatJson(String json) {
        Map<String, String> result = new HashMap<>();
        Pattern pattern = Pattern.compile("\"([^\"]+)\"\\s*:\\s*(\"[^\"]*\"|true|false|[0-9]+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(json);
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2).replace("\"", "");
            result.put(key, value);
        }
        return result;
    }
}
