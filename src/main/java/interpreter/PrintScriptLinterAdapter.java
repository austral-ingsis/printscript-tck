package interpreter;

import runner.Runner;

import java.io.InputStream;

public class PrintScriptLinterAdapter implements PrintScriptLinter{
    /**
     * executes a PrintScript file handling its resulting messages and errors.
     *
     * @param src     Source file.
     * @param version PrintScript version, 1.0 and 1.1 must be supported.
     * @param config  config file.
     * @param handler interface where all syntax and semantic error will be reported.
     */
    @Override
    public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
        Runner runner = new Runner();
        ErrorHandlerAdapter errorHandlerAdapter = new ErrorHandlerAdapter(handler);
        runner.runAnalyze(src, version, config, errorHandlerAdapter);
    }
}
