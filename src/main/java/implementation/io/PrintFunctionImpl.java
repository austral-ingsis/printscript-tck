package implementation.io;

import ingsis.printscript.interpreter.PrintFunction;
import ingsis.printscript.utilities.enums.BoolValue;
import ingsis.printscript.utilities.enums.NumValue;
import ingsis.printscript.utilities.enums.StrValue;
import ingsis.printscript.utilities.enums.Value;
import interpreter.PrintEmitter;
import org.jetbrains.annotations.NotNull;

public class PrintFunctionImpl implements PrintFunction {

    PrintEmitter emitter;

    public PrintFunctionImpl(PrintEmitter emitter) {
        this.emitter = emitter;
    }

    @Override
    public void print(@NotNull Value value) {
        if(value instanceof StrValue) {
            this.emitter.print(((StrValue) value).getValue());
        } else if (value instanceof NumValue) {
            Double num = ((NumValue) value).getValue();
            if(num % 1 == 0) {
                this.emitter.print(String.valueOf(num.intValue()));
            } else {
                this.emitter.print(String.valueOf(num));
            }
        } else if (value instanceof BoolValue) {
            this.emitter.print(String.valueOf(((BoolValue) value).getValue()));
        }
    }

}
