package implementation;

import interpreter.*;
import kotlin.Pair;
import lexer.Lexer;
import nodes.StatementType;
import parser.Parser;
import position.visitor.Environment;
import token.Token;

import java.io.InputStream;
import java.util.Iterator;

public class PrintScriptInterpreterImpl implements PrintScriptInterpreter {

    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        try {
            Iterator<Token> tokens = new Lexer(src, version);
            Iterator<StatementType> asts = new Parser(tokens, version);

            Environment currentEnvironment = new Environment();

            while (asts.hasNext()) {
                StatementType statement = asts.next();
                Pair<StringBuilder, Environment> result = Interpreter.INSTANCE.interpret(statement, version, currentEnvironment);

                String first = result.getFirst().toString().trim();
                String cleanedOutput = removeSurroundingQuotes(first);

                if (!cleanedOutput.isEmpty()) {
                    emitter.print(cleanedOutput);
                }

                currentEnvironment = result.getSecond();
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
}
