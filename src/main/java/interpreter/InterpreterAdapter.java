package interpreter;





import java.io.InputStream;
import java.nio.charset.StandardCharsets;


public class InterpreterAdapter implements PrintScriptInterpreter {
    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        try {
            byte[] bytes = src.readNBytes(1046);
            //new PartialStringReadingLexer()
            while (bytes.length > 0) {
                String code = new String(bytes, StandardCharsets.UTF_8);


                bytes = src.readNBytes(1046);
            }
        } catch (Exception err) {
            handler.reportError(err.getMessage());
        }
    }
}
