package implementation;

import adapter.AdaptedErrorHandler;
import adapter.AdaptedInputChannel;
import adapter.AdaptedPrintChannel;
import adapter.InputStreamCodeReader;
import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import printscript.*;
import printscript.reader.CodeReader;
import printscript.tck.PrintScript;

import java.io.InputStream;

public class PrintScriptInterpreterImpl implements PrintScriptInterpreter {
    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        try {
            CodeReader codeReaderAdapted = new InputStreamCodeReader(src);
            PrintChannel channelAdapter = new AdaptedPrintChannel(emitter);
            printscript.ErrorHandler errorHandlerAdapted = new AdaptedErrorHandler(handler);
            InputChannel inputChannelAdapted = new AdaptedInputChannel(provider);
            PrintScript.INSTANCE.execute(version, codeReaderAdapted, channelAdapter, errorHandlerAdapted, inputChannelAdapted);
        } catch (OutOfMemoryError e) {
            handler.reportError(e.toString());
        }

    }
}
