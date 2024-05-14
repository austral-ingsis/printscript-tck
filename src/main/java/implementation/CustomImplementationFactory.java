package implementation;

import edu.austral.ingsis.gradle.common.ast.AST;
import edu.austral.ingsis.gradle.interpreter.ComposeInterpreter;
import edu.austral.ingsis.gradle.interpreter.Interpreter;
import edu.austral.ingsis.gradle.interpreter.util.*;
import edu.austral.ingsis.gradle.iterator.FileBuffer;
import edu.austral.ingsis.gradle.iterator.LexerIterator;
import edu.austral.ingsis.gradle.iterator.ParserIterator;
import edu.austral.ingsis.gradle.lexer.Lexer;
import edu.austral.ingsis.gradle.lexer.director.LexerDirector;
import edu.austral.ingsis.gradle.parser.builder.BuilderKt;
import edu.austral.ingsis.gradle.parser.impl.ComposeParser;
import edu.austral.ingsis.gradle.parser.util.HelperKt;
import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;


import java.io.InputStream;


public class CustomImplementationFactory implements InterpreterFactory {

    @Override
    public PrintScriptInterpreter interpreter() {
        return (src, version, emitter, handler, provider) -> {
            execute(version, emitter, provider, src, handler);
        };
    }

    private void execute(String version, PrintEmitter emitter, InputProvider provider, InputStream src, ErrorHandler handler) {
        try {
            FileBuffer fileBuffer = new FileBuffer(src);
            Lexer lexer = new LexerDirector().createComposeLexer(version);
            LexerIterator lexerIterator = new LexerIterator(lexer, fileBuffer.getFileBuffered());
            ComposeParser composeParser = BuilderKt.createComposeParser();
            ParserIterator parserIterator = new ParserIterator(lexerIterator, composeParser);
            ComposeInterpreter interpreter = new ComposeInterpreter(
                    new InterpreterList().getInterpreters(),
                    new EmitterAdapter(emitter),
                    new KotlinEnvReader(),
                    new InputReaderAdapter(provider),
                    new Context());

            while (parserIterator.hasNext()) {
                AST ast = parserIterator.next();
                if (ast != null) {
                    interpreter = interpreter.interpretAndUpdateContext(ast);
                }
            }
        } catch (Error | Exception e) {
            handler.reportError(e.getMessage());
        }
    }
}