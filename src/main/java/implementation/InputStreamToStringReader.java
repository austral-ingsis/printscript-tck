package implementation;

import java.io.*;
import java.util.stream.Collectors;

public class InputStreamToStringReader {
    public static Reader convert(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream));
    }
}