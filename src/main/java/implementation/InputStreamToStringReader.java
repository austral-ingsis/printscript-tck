package implementation;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.stream.Collectors;

public class InputStreamToStringReader {
    public static StringReader convert(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String content = reader.lines().collect(Collectors.joining("\n"));
        return new StringReader(content);
    }
}