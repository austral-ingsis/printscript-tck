package interpreter;
import interpreter.InputProvider;

import java.util.Queue;

public class QueueInputProvider implements InputProvider {

    final private Queue<String> messages;
    private String filePath;

    public QueueInputProvider(Queue<String> messages) {
        this.messages = messages;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String input(String name) {
        if ("file path".equals(name)) {
            return filePath;
        }
        return messages.poll();
    }
}