package linter;

import output.OutputResult;

import java.util.ArrayList;
import java.util.List;

public class OutputReport implements OutputResult {
  private List<String> errorCollector = new ArrayList<>();

  @Override
  public OutputResult saveResult(String s) {
    if (!s.isEmpty()) {
      errorCollector.add(s);
    }
    return this;
  }

  @Override
  public String getResult() {
    return "";
  }

  public List<String> getErrors() {
    return errorCollector;
  }
}
