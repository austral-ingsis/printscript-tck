package implementation;

import edu.austral.ingsis.gradle.common.ast.newast.AST;

import edu.austral.ingsis.gradle.interpreter.*;
import edu.austral.ingsis.gradle.interpreter.factory.InterpreterList;
import edu.austral.ingsis.gradle.interpreter.util.*;
import edu.austral.ingsis.gradle.iterator.FileBuffer;
import edu.austral.ingsis.gradle.iterator.LexerIterator;
import edu.austral.ingsis.gradle.iterator.ParserIterator;
import edu.austral.ingsis.gradle.lexer.Lexer;
import edu.austral.ingsis.gradle.lexer.director.LexerDirector;
import edu.austral.ingsis.gradle.parser.impl.ComposeParser;
import edu.austral.ingsis.gradle.parser.util.HelperKt;
import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

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
            ComposeParser composeParser = HelperKt.createComposeParser();
            ParserIterator parserIterator = new ParserIterator(lexerIterator, composeParser);
            edu.austral.ingsis.gradle.interpreter.factory.InterpreterFactory
                    interpreterFactory =
                    new edu.austral.ingsis.gradle.interpreter.factory.
                            InterpreterFactory(
                            new InterpreterList().getInterpreters(),
                            new EmitterAdapter(emitter),
                            new KotlinEnvReader(),
                            new InputReaderAdapter(provider)
                    );
            AST ast = null;
            while (parserIterator.hasNext()) {
                try {
                    ast = parserIterator.next();
                } catch (Error | Exception e) {
                    if (e instanceof OutOfMemoryError) {
                        handler.reportError("Java heap space");
                    } else {
                        handler.reportError(e.getMessage());
                    }
                }
                try {
                    InterpretResult result = interpreterFactory.interpret(ast);
                    if (result instanceof InterpretResult.ContextResult) {
                        interpreterFactory.updateContext(((InterpretResult.ContextResult) result).getContext());

                    }
                } catch (Error | Exception e) {
                    handler.reportError(e.getMessage());
                }
            }
        } catch (Error | Exception e) {
            handler.reportError(e.getMessage());
        }
    }
}