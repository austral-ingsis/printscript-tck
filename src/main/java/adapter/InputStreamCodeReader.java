package adapter;

import org.jetbrains.annotations.NotNull;
import printscript.reader.CharPosition;
import printscript.reader.CodeReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class InputStreamCodeReader implements CodeReader {
    private InputStream inputStream;
    private CharPosition currentPosition;
    private Integer lookahead;

    public InputStreamCodeReader(InputStream inputStream) {
        this.inputStream = inputStream;
        this.currentPosition = new CharPosition(1,1);
        lookahead = readWithoutException();
    }

    @NotNull
    @Override
    public Optional<Character> read() {
        if (lookahead == -1) return Optional.empty();

        char lookaheadChar = (char) lookahead.intValue();
        if (lookaheadChar == '\n') {
            currentPosition = new CharPosition(currentPosition.getLine() + 1, 1);
        } else {
            currentPosition = new CharPosition(currentPosition.getLine(), currentPosition.getCol() +1);
        }
        lookahead = readWithoutException();
        return Optional.of(lookaheadChar);
    }

    @NotNull
    @Override
    public Optional<Character> peek() {
        if (lookahead == -1) return Optional.empty();
        return Optional.of((char) lookahead.intValue());
    }

    @NotNull
    @Override
    public CharPosition currentPosition() {
        return currentPosition;
    }


    private int readWithoutException() {
        try {
            return inputStream.read();
        } catch (IOException e) {
            return -1;
        }
    }
}
