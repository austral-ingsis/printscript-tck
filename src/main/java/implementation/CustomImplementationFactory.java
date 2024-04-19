package implementation;

import edu.austral.ingsis.gradle.common.ast.newast.AST;

import edu.austral.ingsis.gradle.interpreter.*;
import edu.austral.ingsis.gradle.interpreter.util.*;
import edu.austral.ingsis.gradle.lexer.Lexer;
import edu.austral.ingsis.gradle.lexer.director.LexerDirector;
import edu.austral.ingsis.gradle.lexer.iterator.LexerIterator;
import edu.austral.ingsis.gradle.parser.impl.ComposeParser;
import edu.austral.ingsis.gradle.parser.iterator.ParserIterator;
import edu.austral.ingsis.gradle.parser.util.HelperKt;
import interpreter.PrintScriptInterpreter;


import java.util.List;

public class CustomImplementationFactory implements InterpreterFactory {

    @Override
    public PrintScriptInterpreter interpreter() {
        return (src, version, emitter, handler,provider) -> {
            Lexer lexer= new LexerDirector().createComposeLexer(version);
            LexerIterator lexerIterator= new LexerIterator(lexer,src);
            ComposeParser composeParser= HelperKt.createComposeParser();
            ParserIterator parserIterator= new ParserIterator(lexerIterator,composeParser);
            try{
                Context context= new Context();
                List<Interpreter> interpreters= edu.austral.ingsis.gradle.interpreter.util.HelperKt.
                        createInterpreterManager().getInterpreters();

                while (parserIterator.hasNext()){
                    AST ast = parserIterator.next();
                    InterpreterManager interpreterManager=
                            new InterpreterManager(interpreters, new EmitterAdapter(emitter),
                                    new KotlinEnvReader(),
                                    new KotlinInputReader());
                    Interpreter interpreter=interpreterManager.getInterpreter(ast,null);
                    InterpretResult interpretResult= interpreter.interpret(ast,  context,interpreterManager);
                    if (interpretResult instanceof  InterpretResult.ContextResult){
                        context=context.update(((InterpretResult.ContextResult) interpretResult).getContext());
                    }
                }
            }
            catch (Exception e){
                handler.reportError(e.getMessage());
            }
        };
    }
}