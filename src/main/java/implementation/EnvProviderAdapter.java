package implementation;

import engine.EnvProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnvProviderAdapter implements EnvProvider {
    @Nullable
    @Override
    public String readEnv(@NotNull String s) {
        return System.getenv(s);
    }
}
