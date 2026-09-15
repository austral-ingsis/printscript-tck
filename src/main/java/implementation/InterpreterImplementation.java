package implementation;

import engine.*;
import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;

import java.io.InputStream;
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
        try {
            String sourceCode = new String(src.readAllBytes(),
                    StandardCharsets.UTF_8);

            Engine engine = new Engine();
            LoggerAdapter logger = new LoggerAdapter();
            PrintEmitterAdapter printEmitter = new PrintEmitterAdapter();

            EngineResult result = engine.execute(
                    sourceCode,
                    new EngineIO(
                            printEmitter,
                            new InputProviderAdapter(printEmitter, provider),
                            new EnvProviderAdapter()
                            ),
                    logger,
                    new ExecutionContext(),
                    version);
            List<String> logs = printEmitter.getPrints();

            if (result.getExitCode() == ExitCode.FAILURE) {
                if (logs.isEmpty()) {
                    handler.reportError("Execution failed");
                } else {
                    for (String error : logs) {
                        handler.reportError(error);
                    }
                }
            } else {
                if (!logs.isEmpty()) {
                    for (String log : logs) {
                        emitter.print(log);
                    }
                }
            }

        } catch (Exception e) {
            handler.reportError(e.getMessage() != null ? e.getMessage() : "Error executing script");
        }
    }
}
