package interpreter;

import org.example.observer.PrintBrokerObserver;

public class PrintEmitterObserver extends PrintBrokerObserver {
	private final PrintEmitter emitter;

	public PrintEmitterObserver(PrintEmitter emitter) {
		this.emitter = emitter;
	}

	@Override
	public void updateChanges(String s) {
		emitter.print(s.strip()); // beautiful hermoso
	}
}
