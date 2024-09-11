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

            StringBuilder outputBuilder = new StringBuilder();
            Environment currentEnvironment = new Environment();

            while (asts.hasNext()) {
                StatementType statement = asts.next();
                Pair<StringBuilder, Environment> result = Interpreter.INSTANCE.interpret(statement, version, currentEnvironment);
                String first = result.getFirst().toString().trim();
                outputBuilder.append(first);
                String message = outputBuilder.toString().trim();
                emitter.print(message);
                currentEnvironment = result.getSecond();
            }
        } catch (OutOfMemoryError | Exception e) {
            handler.reportError(e.getMessage());
        }
    }

}
