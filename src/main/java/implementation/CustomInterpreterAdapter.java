package implementation;

import ast.*;
import interpreter.*;
import interpreter.readInputFunction.ReadInputFunction;
import kotlin.reflect.KClass;
import parser.Parser;
import parser.factory.StatementParserFactory;
import parser.result.FailureResult;
import parser.result.ParserResult;
import parser.result.SuccessResult;
import reader.StatementFileReader;
import reader.StatementLineReader;
import token.Token;
import version.Version;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class CustomInterpreterAdapter implements PrintScriptInterpreter {
    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        try {
            Version adaptedVersion = adaptVersion(version);
            ReadInputFunction adaptedInputProvider = new InputProviderAdapter(provider);

            StatementFileReader reader = new StatementFileReader(src, adaptedVersion);

            Parser parser = StatementParserFactory.INSTANCE.create(adaptedVersion);
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
                        output = interpreter.interpret(ast, environment, adaptedInputProvider);
                    } catch (Exception e) {
                        handler.reportError(e.getMessage());
                        return;
                    }

                    output.getLogs().forEach(emitter::print);
                    environment = output.getEnvironment();
                }
            }
        } catch (Error | Exception e) {
            handler.reportError(e.getMessage());
        }
    }

    private Map<KClass<? extends AST>, Interpreter> getHandlers(){
        return Map.of(
            kotlin.jvm.JvmClassMappingKt.getKotlinClass(FunctionStatementNode.class), new FunctionStatementInterpreter(),
            kotlin.jvm.JvmClassMappingKt.getKotlinClass(DeclarationNode.class), new VariableStatementInterpreter(),
            kotlin.jvm.JvmClassMappingKt.getKotlinClass(AssignationNode.class), new VariableStatementInterpreter(),
            kotlin.jvm.JvmClassMappingKt.getKotlinClass(IfStatement.class), new IfStatementInterpreter()
        );
    }

    private Version adaptVersion(String version) {
        if (version.equals("1.0")) {
            return Version.V1;
        } else if (version.equals("1.1")) {
            return Version.V2;
        } else {
            throw new IllegalArgumentException("Version not supported");
        }
    }
}
