package interpreter;
import runner.Observer;
import runner.Runner;
import java.io.InputStream;
import java.io.Writer;
import java.util.List;

public class PrintScriptFormatterAdapter implements PrintScriptFormatter{
    /**
     * executes a PrintScript file handling its resulting messages and errors.
     *
     * @param src     Source file.
     * @param version PrintScript version, 1.0 and 1.1 must be supported.
     * @param config  config file.
     * @param writer  Writer, where the formatted output should be written
     */
    @Override
    public void format(InputStream src, String version, InputStream config, Writer writer) {
        List<Observer> emptyList = List.of();
        Runner runner = new Runner(emptyList);
        ErrorHandlerAdapter errorHandlerAdapter = new ErrorHandlerAdapter(new ErrorHandler() {
            @Override
            public void reportError(String message) {
            }
        });
        runner.runFormat(src, writer, config, errorHandlerAdapter);
    }
}
