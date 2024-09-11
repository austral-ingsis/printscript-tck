package interpreter;

import runner.OutputResult;

import java.util.List;

public class OutputEmitter implements OutputResult {
    private final List<String> prints;

    public OutputEmitter(List<String> prints) {
        this.prints = prints;
    }

    @Override
    public OutputResult saveResult(String s) {
        prints.add(s);
        return new OutputEmitter(prints);
    }

    @Override
    public String getResult() {
        return "";
    }

    public List<String> getPrints() {
        return prints;
    }
}
