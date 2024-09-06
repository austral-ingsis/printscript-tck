package adapters;

import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import kotlin.sequences.Sequence;
import main.ParseException;
import main.Parser;
import main.Position;
import main.Token;
import nodes.Node;
import org.example.interpreter.Interpreter;
import org.example.lexer.Lexer;
import utils.InterpreterException;
import utils.InterpreterResult;
import utils.ParsingResult;

import java.io.*;
import java.util.Iterator;
import java.util.Objects;

public class CustomInterpreter implements PrintScriptInterpreter {


    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        if (!Objects.equals(version, "1.0")) {
            handler.reportError("Invalid version");
            throw new InterpreterException("Invalid version");
        }
            BufferedReader reader = new BufferedReader(new InputStreamReader(src));
            Lexer lexer = new Lexer(reader, 0, new Position(1, 1));
            Iterator<Token> tokenIterator = lexer.tokenizeAll(lexer).iterator();
            Parser parser = new Parser(tokenIterator);
            Interpreter interpreter = new Interpreter();

            while (tokenIterator.hasNext()) {
                Sequence<ParsingResult> results = parser.parseExpressions();
                Iterator<InterpreterResult> interpreterResults = interpreter.interpret(results).iterator();
                while (interpreterResults.hasNext()) {
                    InterpreterResult result = interpreterResults.next();
                    if (result.hasException()) {
                        handler.reportError(result.getException().getMessage());
                    } else {
                        emitter.print(result.getPrintln());
                    }
                }

                results = null;
            }

    }
}