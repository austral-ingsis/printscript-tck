package implementation;

import ContentProvider.ContentProvider;
import ContentProvider.FileContentProvider;
import Interpreter.Interpreter;
import Interpreter.IInputProvider;
import Interpreter.IPrintEmitter;
import Lexer.DefaultLexer;
import Lexer.TokenizerV1_0;
import Lexer.TokenizerV1_1;
import Parser.ProgramParserV1_0;
import Parser.ProgramParserV1_1;
import interpreter.ErrorHandler;
import interpreter.InputProvider;
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

        return (src, version, emitter, handler, provider) -> {
            ContentProvider contentProvider = new FileContentProvider(src);
            try {
                if(version.equals("1.0")) emitter.print((new Interpreter()).run((new ProgramParserV1_0(TokenIterator.create(contentProvider.getContent(), (new DefaultLexer(new TokenizerV1_0())).lex(contentProvider)))).parse()).read());
                else if(version.equals("1.1")) (new Interpreter()).run((new ProgramParserV1_1(TokenIterator.create(contentProvider.getContent(), (new DefaultLexer(new TokenizerV1_1())).lex(contentProvider)))).parse(), provider::input, emitter::print).read();
            } catch (Throwable e) {
                handler.reportError(e.getMessage());
            }
        };

        // Dummy impl: return (src, version, emitter, handler) -> { };
    }
}