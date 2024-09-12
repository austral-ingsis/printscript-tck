package adapters;

import factories.LinterFactory;
import interpreter.ErrorHandler;
import interpreter.PrintScriptLinter;
import main.kotlin.main.Linter;
import utils.LinterResult;


import java.io.InputStream;
import java.util.Iterator;


public class CustomLinter implements PrintScriptLinter {
    @Override
    public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
        Iterator<LinterResult> results = new LinterFactory().lintCode(src, version, config);

        for (Iterator<LinterResult> it = results; it.hasNext(); ) {
            LinterResult result = it.next();
            if (result.hasError()) {
                handler.reportError(result.getMessage());
            }
        }
    }
}
