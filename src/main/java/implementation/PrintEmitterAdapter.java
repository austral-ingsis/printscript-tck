package implementation;

import engine.PrintEmitter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PrintEmitterAdapter implements PrintEmitter {
    private List<String> prints = new ArrayList<>();
    public List<String> getPrints(){
        return prints;
    }
    @Override
    public void print(@NotNull String s) {
        prints.add(s);
    }
}
