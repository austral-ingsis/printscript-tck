package interpreter;

import runner.OutputResult;

import java.util.ArrayList;
import java.util.List;

public class OutputEmitter implements OutputResult {
    private final List<String> prints;

    public OutputEmitter(List<String> prints) {
        this.prints = prints;
    }

    @Override
    public OutputResult saveResult(String s) {
        List<String> newPrints = new ArrayList<>(prints);
        newPrints.add(s);
        return new OutputEmitter(newPrints);
    }

    @Override
    public String getResult() {
        return "";
    }

    public List<String> getPrints() {
        return prints;
    }
}
