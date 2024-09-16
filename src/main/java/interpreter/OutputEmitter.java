package interpreter;

import common.argument.PrintEmitter;
import output.OutputResult;

import java.util.List;

public class OutputEmitter implements OutputResult {
    private final PrintEmitter printEmitter;

    public OutputEmitter(PrintEmitter printEmitter) {
        this.printEmitter = printEmitter;
    }

    @Override
    public OutputResult saveResult(String s) {
        printEmitter.print(s);
        return this;
    }

    @Override
    public String getResult() {
        return "";
    }

}
