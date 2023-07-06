package util;

import interpreter.PrintEmitter;
import kotlin.Unit;

import java.util.ArrayList;
import java.util.List;

public class PrintCollector implements PrintEmitter {

    final private List<String> messages = new ArrayList<>();

    @Override
    public Unit print(String message) {
        messages.add(message);
        return null;
    }

    public List<String> getMessages() {
        return messages;
    }
}
