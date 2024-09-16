package interpreter;

import output.OutputResult;

public class ErrorReport implements OutputResult {
    private String error;

    public ErrorReport(String error) {
        this.error = error;
    }

    @Override
    public OutputResult saveResult(String s) {
        this.error = s;
        return this;
    }

    @Override
    public String getResult() {
        return error;
    }
}
