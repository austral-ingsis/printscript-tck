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
import printscript.interpreter.handler.IfStatementHandler;
import printscript.interpreter.handler.PrintlnStatementHandler;
import printscript.interpreter.handler.StatementHandler;
import printscript.interpreter.handler.VariableDeclarationHandler;
import printscript.interpreter.runtime.Environment;
import printscript.interpreter.runtime.ExpressionEvaluator;
import printscript.interpreter.runtime.GlobalEnvironment;
import printscript.interpreter.runtime.InputSource;
import printscript.lexer.PrintScriptLexer;
import printscript.parser.AssignmentParser;
import printscript.parser.IfStatementParser;
import printscript.parser.PrecedenceClimbingExpressionParser;
import printscript.parser.PrintScriptParser;
import printscript.parser.PrintlnStatementParser;
import printscript.parser.StatementParser;
import printscript.parser.VariableDeclarationParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MyPrintScriptInterpreter implements PrintScriptInterpreter {

    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        try {
            Reader reader = new InputStreamReader(src, StandardCharsets.UTF_8);
            InputSource inputSource = prompt -> {
                emitter.print(prompt);
                return provider.input(prompt);
            };

            List<StatementParser> statementParsers = new ArrayList<>();
            statementParsers.add(new VariableDeclarationParser());
            statementParsers.add(new AssignmentParser());
            statementParsers.add(new PrintlnStatementParser());
            statementParsers.add(new IfStatementParser(() -> statementParsers));

            var parser = new PrintScriptParser(
                    new PrintScriptLexer(reader, version),
                    statementParsers,
                    new PrecedenceClimbingExpressionParser());

            var evaluator = new ExpressionEvaluator(inputSource);
            List<StatementHandler> statementHandlers = new ArrayList<>();
            HandlerRegistry handlers = new HandlerRegistry(statementHandlers);
            statementHandlers.add(new VariableDeclarationHandler(evaluator));
            statementHandlers.add(new AssignmentHandler(evaluator));
            statementHandlers.add(new PrintlnStatementHandler(evaluator, new PrintEmitterStream(emitter)));
            statementHandlers.add(new IfStatementHandler(evaluator, () -> handlers));

            Environment environment = new GlobalEnvironment();
            var interpreter = new printscript.interpreter.PrintScriptInterpreter(parser, environment, handlers);

            while (interpreter.hasNext()) {
                Result<?> result = interpreter.next();
                if (result instanceof Failure<?> failure) {
                    failure.diagnostics().forEach(d -> handler.reportError(d.message()));
                }
            }
        } catch (OutOfMemoryError e) {
            handler.reportError(e.getMessage());
        }
    }
}