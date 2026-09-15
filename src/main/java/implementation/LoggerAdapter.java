package implementation;

import engine.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LoggerAdapter implements Logger {

    private final List<String> logs = new ArrayList<>();

    @Override
    public void log(@NotNull String s) {
        logs.add(s);
    }

    public List<String> getLogs(){
        return logs;
    }
}
