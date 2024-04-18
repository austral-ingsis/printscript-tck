package implementation;

import edu.austral.ingsis.gradle.interpreter.util.InterpreterManager;
import edu.austral.ingsis.gradle.lexer.Lexer;
import edu.austral.ingsis.gradle.lexer.director.LexerDirector;
import edu.austral.ingsis.gradle.lexer.iterator.LexerIterator;
import edu.austral.ingsis.gradle.parser.impl.ComposeParser;
import edu.austral.ingsis.gradle.parser.iterator.ParserIterator;
import edu.austral.ingsis.gradle.parser.util.HelperKt;
import interpreter.PrintScriptInterpreter;

import javax.swing.text.html.parser.Parser;

public class CustomImplementationFactory implements InterpreterFactory {

    @Override
    public PrintScriptInterpreter interpreter() {
        return (src, version, emitter, handler,provider) -> {
            Lexer lexer= new LexerDirector().createComposeLexer(version);
            LexerIterator lexerIterator= new LexerIterator(lexer,src);
            ComposeParser composeParser= HelperKt.createComposeParser();
            ParserIterator parserIterator= new ParserIterator(lexerIterator,composeParser);
            try{
                new MainInterpreter(parserIterator).execute();
            }
            catch (Exception e){
                handler.reportError(e.getMessage());
            }
        };
    }
}