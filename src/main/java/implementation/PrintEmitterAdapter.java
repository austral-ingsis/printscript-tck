package implementation;

import engine.EnginePrintEmitter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PrintEmitterAdapter implements EnginePrintEmitter {
    private List<String> prints = new ArrayList<>();
    public List<String> getPrints(){
        return prints;
    }
    @Override
    public void print(@NotNull String s) {
        prints.add(s);
    }
}
