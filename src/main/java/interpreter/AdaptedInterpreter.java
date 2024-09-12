package interpreter;

import runner.Operations;

import java.io.InputStream;
import java.util.Iterator;


public class AdaptedInterpreter implements PrintScriptInterpreter{

    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        Iterator<String> iterator = new InputProviderIterator(provider);
        Operations runner = new Operations(src, version, iterator);
        try {
            Iterator<String> output = runner.execute();
            try {
                while (output.hasNext()) {
                    emitter.print(output.next());
                }
            }
            catch (OutOfMemoryError e) {
                output = null;
                runner = null;
                emitter = null;
                src = null;
                handler.reportError(e.getMessage());
                return;
            }
        }  catch (Exception e) {
            handler.reportError(e.getMessage());
        }
    }
}
