package adapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class InputStreamToFileAdapter {

  public static File inputStreamToFile(InputStream inputStream) throws IOException {

    Path tempFilePath = Files.createTempFile("temp", ".tmp");


    try (FileOutputStream fileOutputStream = new FileOutputStream(tempFilePath.toFile())) {
      byte[] buffer = new byte[1024];
      int bytesRead;

      while ((bytesRead = inputStream.read(buffer)) != -1) {
        fileOutputStream.write(buffer, 0, bytesRead);
      }
    }

    File finalFile = new File("temp");
    Files.move(tempFilePath, finalFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

    return finalFile;
  }
}
