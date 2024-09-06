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
import utils.ParsingResult;

import java.io.*;
import java.util.Objects;

public class CustomInterpreter implements PrintScriptInterpreter {

    private static class EmitterOutputStream extends OutputStream {
        private final PrintEmitter emitter;
        private final StringBuilder buffer = new StringBuilder();

        public EmitterOutputStream(PrintEmitter emitter) {
            this.emitter = emitter;
        }

        @Override
        public void write(int b) throws IOException {
            if (b == '\n') {
                emitter.print(buffer.toString());
                buffer.setLength(0);
            } else {
                buffer.append((char) b);
            }
        }

        @Override
        public void flush() throws IOException {
            if (buffer.length() > 0) {
                emitter.print(buffer.toString());
                buffer.setLength(0);
            }
        }
    }

    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        if (!Objects.equals(version, "1.0")) {
            handler.reportError("Invalid version");
            throw new InterpreterException("Invalid version");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(src));
        Lexer lexer = new Lexer(reader, 0, new Position(1, 1));
        Sequence<Token> tokens = lexer.tokenizeAll(lexer);
        Parser parser = new Parser(tokens.iterator());
        Sequence<ParsingResult> asts = parser.parseExpressions();

        Interpreter interpreter = new Interpreter();

        PrintStream oldOut = System.out;
        PrintStream ps = new PrintStream(new EmitterOutputStream(emitter));

        System.setOut(ps);

        try {
            interpreter.interpret(asts);
        } catch (ParseException e) {
            handler.reportError(e.getMessage());
        } finally {
            System.setOut(oldOut);
            ps.flush();
        }
    }
}