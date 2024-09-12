package implementation;

import interpreter.*;
import kotlin.Pair;
import lexer.Lexer;
import nodes.Expression;
import nodes.StatementType;
import parser.Parser;
import position.Position;
import position.visitor.Environment;
import token.Token;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

public class PrintScriptInterpreterImpl implements PrintScriptInterpreter {

    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        try {
            Iterator<Token> tokens = new Lexer(src, version);
            Parser asts = new Parser(tokens, version,provider.input("input"));

            Environment currentEnvironment = createEnvironmentFromMap(System.getenv());
            asts.setEnv(currentEnvironment);

            while (asts.hasNext()) {
                StatementType statement = asts.next();
                Pair<StringBuilder, Environment> result = Interpreter.INSTANCE.interpret(statement, version, currentEnvironment, provider.input("input"));

                String first = result.getFirst().toString().trim();
                String cleanedOutput = removeSurroundingQuotes(first);

                if (!cleanedOutput.isEmpty()) {
                    emitter.print(cleanedOutput);
                }

                currentEnvironment = result.getSecond();
                asts.setEnv(currentEnvironment);
            }
        } catch (OutOfMemoryError | Exception e) {
            handler.reportError(e.getMessage());
        }
    }

    // Sacar de aca y hacerlo en el Interpreter nuestro
    private String removeSurroundingQuotes(String str) {
        if (str.length() >= 2 && str.startsWith("\"") && str.endsWith("\"")) {
            return str.substring(1, str.length() - 1).trim();
        }
        return str.trim();
    }

    private Environment createEnvironmentFromMap(Map<String, String> envVarsMap) {
        Environment env = new Environment();

        for (Map.Entry<String, String> entry : envVarsMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            StatementType.Variable variable = new StatementType.Variable("const", key, new Expression.Literal(value, new Position(0,0)),  determineDataType(value),new Position(0,0));

            env = env.define(variable);
        }

        return env;
    }

    private String determineDataType(String value) {
        return "string";
    }

}
