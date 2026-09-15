package implementation;

import engine.*;
import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class InterpreterImplementation implements PrintScriptInterpreter {
    @Override
    public void execute(
            InputStream src,
            String version,
            PrintEmitter emitter,
            ErrorHandler handler,
            InputProvider provider
    ) {
        try (Reader reader = new BufferedReader(new InputStreamReader(src, StandardCharsets.UTF_8))) {
            Engine engine = new Engine();
            LoggerAdapter logger = new LoggerAdapter();

            EngineResult result = engine.execute(
                    reader,
                    new EngineIO(
                            emitter::print,
                            prompt -> {
                                emitter.print(prompt);
                                return provider.input(prompt);
                            },
                            new EnvProviderAdapter()
                    ),
                    logger,
                    new ExecutionContext(),
                    version
            );

            if (result.getExitCode() == ExitCode.FAILURE) {
                List<String> logs = logger.getLogs();
                if (logs.isEmpty()) {
                    handler.reportError("Execution failed");
                } else {
                    for (String error : logs) {
                        handler.reportError(error);
                    }
                }
            }

        } catch (Throwable t) {
            handler.reportError(t.getMessage() != null ? t.getMessage() : "Error executing script");
        }
    }
}

