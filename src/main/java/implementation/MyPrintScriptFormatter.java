package implementation;

import com.google.gson.JsonObject;
import edu.FormatterResult;
import edu.Runner;
import interpreter.PrintScriptFormatter;

import java.io.*;
import java.util.List;
import java.util.Map;

public class MyPrintScriptFormatter implements PrintScriptFormatter {
  
  @Override
  public void format(InputStream src, String version, InputStream config, Writer writer) {
    Runner runner = new Runner(version);
    JsonObject rules = createConfig(config);
    FormatterResult result = runner.format(src, rules);
    
    try {
      writer.append(filterSlashR(result.getResult()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  private JsonObject createConfig(InputStream config) {
    BufferedReader reader = new BufferedReader(new InputStreamReader(config));
    JsonObject jsonConfig = new JsonObject();
    
    reader.lines().forEach(line -> {
      String[] parts = line.split("\\s*:\\s*");
      if (parts.length == 2) {
        String key = parts[0].trim().replace("\"", "");
        String value = parts[1].trim().replace("\"", "");
        jsonConfig.addProperty(key, value);
      }
    });
    
    return translate(jsonConfig);
  }
  
  private JsonObject translate(JsonObject originalJson) {
    JsonObject translatedJson = new JsonObject();
    
    Map<String, List<String>> translatedRules = Map.of(
        "enforce-spacing-before-colon-in-declaration", List.of(
            "declaration_space_before_colon"),
        "enforce-spacing-around-equals", List.of(
            "assignment_space_before_equals", "assignment_space_after_equals"),
        "enforce-spacing-after-colon-in-declaration", List.of(
            "declaration_space_after_colon"),
        "line-breaks-after-println", List.of(
            "println_new_lines_before_call"),
        "indent-inside-if", List.of(
            "indent")
    );
    
    for (String key : originalJson.keySet()) {
      if (translatedRules.containsKey(key)) {
        for (String newKey : translatedRules.get(key)) {
          translatedJson.add(newKey, originalJson.get(key));
        }
      }
    }
    
    return translatedJson;
  }
  
  private String filterSlashR(String s) {
    return s.replace("\r", "");
  }
}