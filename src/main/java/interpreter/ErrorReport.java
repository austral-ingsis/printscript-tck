package interpreter;

import runner.OutputResult;

public class ErrorReport implements OutputResult {
    private final String error;

    public ErrorReport(String error) {
        this.error = error;
    }

    @Override
    public OutputResult saveResult(String s) {
        return new ErrorReport(s);
    }

    @Override
    public String getResult() {
        return error;
    }
}
