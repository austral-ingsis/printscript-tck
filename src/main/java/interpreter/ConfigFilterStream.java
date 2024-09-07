package interpreter;
import java.io.*;
import java.util.Arrays;

public class ConfigFilterStream extends FilterInputStream {
	private final byte[] searchBytes;
	private final byte[] replacementBytes;
	private byte[] buffer;
	private int bufferIndex;
	private int bufferLength;

	public ConfigFilterStream(InputStream in, String search, String replacement) {
		super(in);
		this.searchBytes = search.getBytes();
		this.replacementBytes = replacement.getBytes();
		this.buffer = new byte[32768];
	}

	@Override
	public int read() throws IOException {
		if (bufferIndex >= bufferLength) {
			bufferLength = in.read(buffer);
			bufferIndex = 0;
			if (bufferLength == -1) {
				return -1;
			}
		}

		if (matches(bufferIndex)) {
			System.arraycopy(replacementBytes, 0, buffer, bufferIndex, replacementBytes.length);
			bufferIndex += replacementBytes.length - searchBytes.length;
		} else {
			bufferIndex++;
		}

		return buffer[bufferIndex];
	}

	private boolean matches(int index) {
		for (int i = 0; i < searchBytes.length; i++) {
			if (buffer[index + i] != searchBytes[i]) {
				return false;
			}
		}
		return true;
	}
}