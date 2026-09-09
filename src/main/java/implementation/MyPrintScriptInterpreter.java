package implementation;

import implementation.handlers.PrintEmitterStream;
import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;

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
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MyPrintScriptInterpreter implements PrintScriptInterpreter {

    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        Reader reader = new InputStreamReader(src, StandardCharsets.UTF_8);

        var parser = new PrintScriptParser(
                new PrintScriptLexer(reader),
                List.of(
                        new VariableDeclarationParser(),
                        new AssignmentParser(),
                        new PrintlnStatementParser()),
                new PrecedenceClimbingExpressionParser());

        var evaluator = new ExpressionEvaluator();
        var handlers = new HandlerRegistry(
                List.of(
                        new VariableDeclarationHandler(evaluator),
                        new AssignmentHandler(evaluator),
                        new PrintlnStatementHandler(evaluator, new PrintEmitterStream(emitter))));

        Environment environment = new GlobalEnvironment();
        var interpreter = new printscript.interpreter.PrintScriptInterpreter(parser, environment, handlers);

        while (interpreter.hasNext()) {
            Result<?> result = interpreter.next();
            if (result instanceof Failure<?> failure) {
                failure.diagnostics().forEach(d -> handler.reportError(d.message()));
            }
        }
    }
}