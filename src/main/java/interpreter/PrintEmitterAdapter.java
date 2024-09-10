package interpreter;

public class PrintEmitterAdapter implements lib.PrintEmitter {
    private PrintEmitter printEmitter;

    public PrintEmitterAdapter(PrintEmitter printEmitter) {
        this.printEmitter = printEmitter;
    }
    @Override
    public void print(String message) {
        printEmitter.print(message);
    }
}
