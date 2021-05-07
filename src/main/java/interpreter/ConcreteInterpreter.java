package interpreter;

import Parser.*;

import java.io.File;
import java.util.List;

public class ConcreteInterpreter implements PrintScriptInterpreter{
    @Override
    public void execute(File src, String version, PrintEmitter emitter, ErrorHandler handler) {
        Parser parser = version.equals("1.0")? new PS10Parser() : new PS11Parser();
        String path = src.getPath();
        DebugResult debugResult = parser.debug(path);
        String validationMessage = parser.validate(path);

        if(!validationMessage.equals("No errors encountered")){
            handler.reportError(validationMessage);
        } else {
            List<String> prints = parser.execute(path);
            for (String print : prints) {
                emitter.print(print);
            }
        }
    }
}
