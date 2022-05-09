package util;

import interpreter.InputProvider;
import org.florresoagli.printscript.Reader$class;

import java.util.Queue;

public class QueueInputProvider implements org.florresoagli.printscript.Reader  {

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
