package implementation;

import engine.InputProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Scanner;

public class InputProviderAdapter implements InputProvider {

    private final Scanner scanner;

    public InputProviderAdapter() {
        this.scanner = new Scanner(System.in);
    }

    @NotNull
    @Override
    public String readInput(@NotNull String s) {
        System.out.print(s);
        String input = scanner.nextLine();
        return input != null ? input : "";
    }
}
