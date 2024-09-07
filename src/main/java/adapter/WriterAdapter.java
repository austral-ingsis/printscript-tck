package adapter;

import fileWriter.OutputProvider;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

public class WriterAdapter implements OutputProvider {

  private Writer writer;

  public WriterAdapter(Writer writer) {
    this.writer = writer;
  }

  @Override
  public void write(Iterator<String> iterator) throws IOException {
    while (iterator.hasNext()) {
      writer.write(iterator.next());
    }
    writer.flush();
    writer.close();
  }

  @Override
  public String getOutput() {
    return "";
  }
}
