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
            Pair<StringBuilder, Environment> result = Interpreter.INSTANCE.interpret(asts, version);
            emitter.print(result.component1().toString());
        } catch (Exception e) {
            handler.reportError(e.getMessage());
        }
    }

}
