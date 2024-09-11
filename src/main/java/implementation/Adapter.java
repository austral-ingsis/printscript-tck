package implementation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import rules.FormattingRules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Adapter {

    public String inputStreamToString(InputStream inputStream) {
        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("Error reading from InputStream: " + e.getMessage());
        }
        return result.toString();
    }

    public FormattingRules adaptConfig(InputStream configStream) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Parse the InputStream to a JsonNode object
            JsonNode jsonNode = objectMapper.readTree(configStream);

            // Map the values from the JSON to the FormattingRules fields
            boolean spaceBeforeColon = jsonNode.has("spaceBeforeColon") && jsonNode.get("spaceBeforeColon").asBoolean(false);
            boolean spaceAfterColon = jsonNode.has("spaceAfterColon") && jsonNode.get("spaceAfterColon").asBoolean(false);
            boolean spaceAroundAssignment = jsonNode.has("enforce-spacing-around-equals") && jsonNode.get("enforce-spacing-around-equals").asBoolean(false);
            int newlineBeforePrintln = jsonNode.has("newlineBeforePrintln") ? jsonNode.get("newlineBeforePrintln").asInt(0) : 0;
            int blockIndentation = jsonNode.has("blockIndentation") ? jsonNode.get("blockIndentation").asInt(4) : 4;

            // Create and return the FormattingRules object
            return new FormattingRules(
                    spaceBeforeColon,
                    spaceAfterColon,
                    spaceAroundAssignment,
                    newlineBeforePrintln,
                    blockIndentation
            );

        } catch (IOException e) {
            System.err.println("Error reading config InputStream: " + e.getMessage());
        }

        // Return default values if an exception occurs
        return new FormattingRules(false, false, false, 0, 4);
    }
}
