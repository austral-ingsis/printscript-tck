package implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import interpreter.LinterConfigAdapter;
import interpreter.ErrorHandler;
import interpreter.ErrorHandlerAdapter;
import interpreter.PrintScriptLinter;
import runner.Runner;

import java.io.InputStream;

public class PrintScriptLinterImpl implements PrintScriptLinter {
    @Override
    public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
        final Runner runner = new Runner(version);

        final ErrorHandlerAdapter errorHandler = new ErrorHandlerAdapter(handler);
        final ObjectMapper mapper = new ObjectMapper();
        try {
            LinterConfigAdapter linterConfigAdapter = mapper.readValue(config, LinterConfigAdapter.class);
            runner.analyze(src, errorHandler, linterConfigAdapter.getConfig());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
