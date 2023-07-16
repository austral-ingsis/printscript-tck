package implementation;

import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import printscript.language.interpreter.contextProvider.ConsoleContext;
import printscript.language.interpreter.interpreter.Interpreter;
import printscript.language.interpreter.interpreter.InterpreterImpl;
import printscript.language.interpreter.interpreter.InterpreterWithIterator;
import printscript.language.lexer.Lexer;
import printscript.language.lexer.LexerFactory;
import printscript.language.lexer.TokenListIterator;
import printscript.language.parser.ASTIterator;
import printscript.language.parser.Parser;
import printscript.language.parser.ParserFactory;
import printscript.language.token.Token;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

public class PrintScriptInterpreterAdapter implements PrintScriptInterpreter {
    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        Lexer lexer = new LexerFactory().createLexer(version, src);
        TokenListIterator tokenListIterator = new TokenListIterator(lexer);
        Iterator<List<Token>> javaTokenListIterator = getJavaTokenListIterator(tokenListIterator);
        Parser parser = new ParserFactory().createParser(version);
        ASTIterator astIterator = new ASTIterator(parser, javaTokenListIterator);
        ConsoleContextWrapper consoleContextWrapper = new ConsoleContextWrapper(new ConsoleContext(), emitter, provider);
        Interpreter interpreter = new InterpreterImpl(consoleContextWrapper);
        InterpreterWithIterator interpreterWithIterator = new InterpreterWithIterator(interpreter, astIterator);
        try {
            while (interpreterWithIterator.hasNextInterpretation()) {
                interpreterWithIterator.interpretNextAST();
            }
        }
        catch (Error | Exception e) {
            handler.reportError(e.getMessage());
        }
    }

    // Auxiliary method to convert TokenListIterator (that extends Kotlin Iterator) to Iterator<List<Token>>
    private Iterator<List<Token>> getJavaTokenListIterator(TokenListIterator iterator) {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public List<Token> next() {
                return iterator.next();
            }

            @Override
            public void remove() {
                iterator.remove();
            }
        };
    }
}
