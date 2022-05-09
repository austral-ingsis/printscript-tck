package util;

import interpreter.InputProvider;

import java.util.Queue;

public class QueueInputProvider implements org.florresoagli.printscript.InputProviderReader  {

    final private Queue<String> messages;

    public QueueInputProvider(Queue<String> messages) {
        this.messages = messages;
    }

    public String input(String name) {
        return messages.poll();
    }

    @Override
    public String readSingleLine() {
        return messages.poll();
    }
}
