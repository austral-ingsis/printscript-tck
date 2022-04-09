package implementation;

import ContentProvider.ContentProvider;
import ContentProvider.FileContentProvider;
import Interpreter.Interpreter;
import Lexer.DefaultLexer;
import Parser.DefaultParser;
import interpreter.ErrorHandler;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;
import org.austral.ingsis.printscript.parser.TokenIterator;

import java.io.File;

public class CustomImplementationFactory implements InterpreterFactory {

    @Override
    public PrintScriptInterpreter interpreter() {
        // your PrintScript implementation should be returned here.
        // make sure to ADAPT your implementation to PrintScriptInterpreter interface.
        //throw new NotImplementedException("Needs implementation"); // TODO: implement

        return new PrintScriptInterpreter() {
            @Override
            public void execute(File src, String version, PrintEmitter emitter, ErrorHandler handler) {
                ContentProvider contentProvider = new FileContentProvider(src);
                String toPrint = "";
                try {
                    toPrint = (new Interpreter()).run((new DefaultParser(TokenIterator.create(contentProvider.getContent(), (new DefaultLexer()).lex(contentProvider)))).parse()).read();
                } catch (Throwable e) {
                    handler.reportError(e.getMessage());
                }
                emitter.print(toPrint);
            }
        };

        // Dummy impl: return (src, version, emitter, handler) -> { };
    }
}