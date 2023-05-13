package interpreter;

import java.io.InputStream;
import app.*;

public class PrintScriptInterpreterImpl implements PrintScriptInterpreter{
    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        PrintScriptApp app = new MyPrintScriptApp();
    }
}
