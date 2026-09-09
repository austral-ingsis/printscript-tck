package implementation;

import executor.Engine;
import executor.ExecutionContext;
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
        try{
            String sourceCode = new String(src.readAllBytes(),
                    StandardCharsets.UTF_8);

            Engine engine = new Engine();
            LoggerAdapter logger = new LoggerAdapter();

            engine.execute(sourceCode, logger, new ExecutionContext());
            List<String> logs = logger.getLogs(); //queda puenteado nuestros logs con los logs de ellos

            logs.remove(logs.getLast());

            for(String log: logs){
                 emitter.print(log);
            }

        }
        catch (Exception e){
            throw new RuntimeException("Could not read InputStream");
        }


    }
}
