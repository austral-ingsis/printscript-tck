package implementation;

import interpreter.PrintEmitter;
import org.example.OutputEmitter;

class OutPrintEmitter implements OutputEmitter {

    private final PrintEmitter theirs;

    public OutPrintEmitter(PrintEmitter theirs) {
        this.theirs = theirs;
    }

    @Override
    public void capture(String output) {
        theirs.print(output);
    }
}
