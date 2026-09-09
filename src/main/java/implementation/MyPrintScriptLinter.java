package implementation;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import interpreter.ErrorHandler;
import interpreter.PrintScriptLinter;

import printscript.analyzer.AnalyzerRules;
import printscript.analyzer.AnalyzerRulesLoader;
import printscript.analyzer.PrintScriptAnalyzer;
import printscript.common.result.Failure;
import printscript.common.result.Result;
import printscript.interpreter.handler.AssignmentHandler;
import printscript.interpreter.handler.HandlerRegistry;
import printscript.interpreter.handler.PrintlnStatementHandler;
import printscript.interpreter.handler.VariableDeclarationHandler;
import printscript.interpreter.runtime.Environment;
import printscript.interpreter.runtime.ExpressionEvaluator;
import printscript.interpreter.runtime.GlobalEnvironment;
import printscript.lexer.PrintScriptLexer;
import printscript.parser.AssignmentParser;
import printscript.parser.PrecedenceClimbingExpressionParser;
import printscript.parser.PrintScriptParser;
import printscript.parser.PrintlnStatementParser;
import printscript.parser.VariableDeclarationParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MyPrintScriptLinter implements PrintScriptLinter {

    @Override
    public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
        JsonObject original = JsonParser.parseReader(new InputStreamReader(config, StandardCharsets.UTF_8)).getAsJsonObject();
        AnalyzerRules rules = new AnalyzerRulesLoader().load(new StringReader(translate(original).toString()));

        Reader reader = new InputStreamReader(src, StandardCharsets.UTF_8);
        var parser = new PrintScriptParser(
                new PrintScriptLexer(reader),
                List.of(
                        new VariableDeclarationParser(),
                        new AssignmentParser(),
                        new PrintlnStatementParser()),
                new PrecedenceClimbingExpressionParser());

        var evaluator = new ExpressionEvaluator();
        PrintStream silentOut = new PrintStream(OutputStream.nullOutputStream());
        var handlers = new HandlerRegistry(
                List.of(
                        new VariableDeclarationHandler(evaluator),
                        new AssignmentHandler(evaluator),
                        new PrintlnStatementHandler(evaluator, silentOut)));

        Environment environment = new GlobalEnvironment();
        var interpreter = new printscript.interpreter.PrintScriptInterpreter(parser, environment, handlers);

        var analyzer = new PrintScriptAnalyzer(interpreter, rules);
        while (analyzer.hasNext()) {
            Result<?> result = analyzer.next();
            if (result instanceof Failure<?> failure) {
                failure.diagnostics().forEach(d -> handler.reportError(d.message()));
            }
        }
        analyzer.diagnostics().forEach(d -> handler.reportError(d.message()));
    }

    private JsonObject translate(JsonObject original) {
        JsonObject translated = new JsonObject();

        if (original.has("identifier_format")) {
            if ("snake case".equalsIgnoreCase(original.get("identifier_format").getAsString())) {
                translated.addProperty("identifier_format", "snake_case");
            }
        } else {
            translated.addProperty("identifier_case_enabled", false);
        }

        if (original.has("mandatory-variable-or-literal-in-println")) {
            translated.addProperty(
                    "println_identifier_or_literal_only",
                    original.get("mandatory-variable-or-literal-in-println").getAsBoolean());
        }

        return translated;
    }
}