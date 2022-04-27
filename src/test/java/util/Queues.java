package util;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Queues {
    private Queues() {
    }

    public static <T> Queue<T> toQueue(List<T> list) {
        return new LinkedList<>(list);
    }
}
