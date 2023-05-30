package util;

import interpreter.PrintEmitter;
import kotlin.Unit;

import java.util.function.Predicate;

public class PrintCounter implements PrintEmitter {
    private int count = 0;
    private final Predicate<String> valuePredicate;

    public PrintCounter(Predicate<String> valuePredicate) {
        this.valuePredicate = valuePredicate;
    }

    @Override
    public Unit print(String message) {
        if (valuePredicate.test(message)) {
            count++;
        } else {
            throw new UnexpectedMessageException(message);
        }
        return null;
    }

    public int getCount() {
        return count;
    }
}
