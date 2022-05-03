package util;

import interpreter.ErrorHandler;

import java.util.ArrayList;
import java.util.List;

public class ErrorCollector implements ErrorHandler, org.florresoagli.printscript.Observer {

    final private List<String> errors = new ArrayList<>();

    @Override
    public void reportError(String error) {
        errors.add(error);
    }

    public List<String> getErrors() {
        return errors;
    }

    @Override
    public void update(List<String> result) {
        errors.addAll(result);
    }

    @Override
    public List<String> getList() {
        return errors;
    }
}
