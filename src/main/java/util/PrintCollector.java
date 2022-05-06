package util;

import interpreter.PrintEmitter;

import java.util.ArrayList;
import java.util.List;

public class PrintCollector implements PrintEmitter, org.florresoagli.printscript.Observer {

    final private List<String> messages = new ArrayList<>();


    public PrintCollector() {
        super();
    }

    @Override
    public void print(String message) {
        messages.add(message);
    }


    public List<String> getMessages() {
        return messages;
    }

    @Override
    public void update(List<String> result) {
        messages.addAll(result);
    }

    @Override
    public List<String> getList() {
        return messages;
    }
}
