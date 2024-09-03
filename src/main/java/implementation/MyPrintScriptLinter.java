package implementation;

import com.google.gson.JsonObject;
import edu.Report;
import edu.Runner;
import interpreter.ErrorHandler;
import interpreter.PrintScriptLinter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class MyPrintScriptLinter implements PrintScriptLinter {
  @Override
  public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
    Iterator<String> input = createIterator(src);
    Runner runner = new Runner(version);
    JsonObject c = createConfig(config);
    Report report = runner.analyze(input, c);
    handleReport(report, handler);
  }

  private void handleReport(Report report, ErrorHandler handler) {
    for (String message: report.getMessages()) {
      handler.reportError(message);
    }
  }

  private Iterator<String> createIterator(InputStream src) {
    BufferedReader reader = new BufferedReader(new InputStreamReader(src));
    List<String> lines = reader.lines().collect(Collectors.toList());
    Iterator<String> iterator = lines.iterator();
    return iterator;
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

    return jsonConfig;
  }

}