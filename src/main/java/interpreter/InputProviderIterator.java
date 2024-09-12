package interpreter;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class InputProviderIterator implements Iterator<String> {
    private final InputProvider provider;
    private String nextInput;

    public InputProviderIterator(InputProvider provider) {
        this.provider = provider;
        this.nextInput = provider.input(null);
    }

    @Override
    public boolean hasNext() {
        return nextInput != null;
    }

    @Override
    public String next() {
        if (nextInput == null) {
            throw new NoSuchElementException();
        }
        String currentInput = nextInput;
        nextInput = provider.input(null);
        return currentInput;
    }
}