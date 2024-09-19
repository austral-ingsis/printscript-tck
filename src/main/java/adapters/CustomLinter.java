package adapters;

import dataObjects.LinterResult;
import factories.LinterFactory;
import interpreter.ErrorHandler;
import interpreter.PrintScriptLinter;
import utils.PercentageCollector;

import java.io.InputStream;
import java.util.Iterator;

public class CustomLinter implements PrintScriptLinter {
    @Override
    public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
        System.gc();

        PercentageCollector collector = new PercentageCollector();
        Iterator<LinterResult> results = new LinterFactory().lintCode(src, version, config, collector).iterator();

        while (results.hasNext()) {
            LinterResult result = results.next();
            if (result.hasError()) {
                handler.reportError(result.getMessage());
            }
        }

        System.gc();
    }
}
