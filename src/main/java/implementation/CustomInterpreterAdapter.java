package implementation;

import ast.AST;
import ast.AssignationNode;
import ast.DeclarationNode;
import ast.FunctionStatementNode;
import interpreter.*;
import kotlin.reflect.KClass;
import parser.Parser;
import parser.factory.StatementParserFactory;
import parser.result.FailureResult;
import parser.result.ParserResult;
import parser.result.SuccessResult;
import reader.StatementFileReader;
import reader.StatementLineReader;
import token.Token;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class CustomInterpreterAdapter implements PrintScriptInterpreter {
    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        StatementFileReader reader = new StatementFileReader(src, new StatementLineReader());

        Parser parser = StatementParserFactory.INSTANCE.create();
        Interpreter interpreter = new StatementInterpreter(getHandlers());

        Environment environment = new Environment();


        while (reader.hasNextLine()) {
            List<List<Token>> statements = reader.nextLine();

            for (List<Token> statement : statements) {
                ParserResult parserResult;
                try {
                    parserResult = parser.parse(statement, 0);

                } catch (Exception e) {
                    handler.reportError(e.getMessage());
                    return;
                }

                if (parserResult instanceof FailureResult) {
                    handler.reportError(((FailureResult) parserResult).getMessage());
                    return;
                }

                AST ast = ((SuccessResult) parserResult).getValue();

                InterpretOutput output;

                try {
                    output = interpreter.interpret(ast, environment);
                } catch (Exception e) {
                    handler.reportError(e.getMessage());
                    return;
                }

                output.getLogs().forEach(emitter::print);
                environment = output.getEnvironment();
            }
        }
    }

    private Map<KClass<? extends AST>, Interpreter> getHandlers(){
        return Map.of(
            kotlin.jvm.JvmClassMappingKt.getKotlinClass(FunctionStatementNode.class), new FunctionStatementInterpreter(),
            kotlin.jvm.JvmClassMappingKt.getKotlinClass(DeclarationNode.class), new VariableStatementInterpreter(),
            kotlin.jvm.JvmClassMappingKt.getKotlinClass(AssignationNode.class), new VariableStatementInterpreter()
        );
    }
}
