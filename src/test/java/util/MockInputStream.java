package util;

import java.io.InputStream;

public class MockInputStream extends InputStream {
    public int lineNumber = 0;
    private int index = 0;
    private final int numberOfLines;
    private final int[] lineBytes;

    public MockInputStream(String line, int numberOfLines) {
        this.numberOfLines = numberOfLines;
        lineBytes = line.chars().toArray();
    }

    @Override
    public int read() {
        if (index >= lineBytes.length) {
            index = 0;
            lineNumber = lineNumber + 1;
        }

        if (lineNumber < numberOfLines) {
            final var byteValue = lineBytes[index];
            index = index + 1;
            return byteValue;
        } else {
            return -1;
        }
    }
}
