package implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import rules.FormattingRules;

import java.io.IOException;
import java.io.InputStream;

public class Adapter {

    FormattingRules inputStreamToFormattingRules(InputStream configStream) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(configStream, FormattingRules.class);
        } catch (IOException e) {
            System.err.println("Error parsing InputStream to FormattingRules: " + e.getMessage());
            return null;
        }
    }
}
