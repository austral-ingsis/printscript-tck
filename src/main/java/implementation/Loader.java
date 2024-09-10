package implementation;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Loader {
    public File loadFile(InputStream stream) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            File file = File.createTempFile("config", ".tmp");
            file.deleteOnExit();
            OutputStream output = new FileOutputStream(file);
            String read;
            while ((read = reader.readLine()) != null) {
                output.write(read.getBytes(StandardCharsets.UTF_8));
            }
            return file;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public Reader getReader(File file) {
        try {
            return new FileReader(file);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
