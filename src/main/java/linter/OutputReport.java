package linter;

import output.OutputResult;

import java.util.ArrayList;
import java.util.List;

public class OutputReport implements OutputResult<String> {
  private List<String> errorCollector = new ArrayList<>();

  @Override
  public void saveResult(String s) {
    if (!s.isEmpty()) {
      errorCollector.add(s);
    }
  }

  @Override
  public String getResult() {
    return "";
  }

  public List<String> getErrors() {
    return errorCollector;
  }
}
