package implementation;

public record Triple<T, T1, T2>(T emitter, T1 handler, T2 provider) {
}
