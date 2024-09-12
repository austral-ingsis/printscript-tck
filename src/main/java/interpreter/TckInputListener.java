package interpreter;

import org.example.InputListener;

public class TckInputListener implements InputListener {
	private final InputProvider provider;

	public TckInputListener(InputProvider provider) {
		this.provider = provider;
	}

	@Override
	public String getInput(String s) {
		return provider.input(s);
	}
}
