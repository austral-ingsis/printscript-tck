package implementation;

import interpreter.Interpreter;
import interpreter.InterpreterBuilder;
import interpreter.PrintScriptInterpreter;
import lexer.Lexer;
import lexer.LexerBuilder;
import org.austral.ingsis.printscript.common.Token;
import org.austral.ingsis.printscript.parser.TokenIterator;
import parser.DefaultParser;
import parser.ParserBuilder;
import parser.ast.node.NodeGroupResult;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class CustomImplementationFactory implements InterpreterFactory {

    @Override
    public PrintScriptInterpreter interpreter() {

        // your PrintScript implementation should be returned here.
        // make sure to ADAPT your implementation to PrintScriptInterpreter interface.

        // Dummy impl: return (src, version, emitter, handler) -> { };

        return (src, version, emitter, handler,provider) -> {
            LexerBuilder lexerBuilder = new LexerBuilder();
            ParserBuilder parserBuilder = new ParserBuilder();
            InterpreterBuilder interpreterBuilder = new InterpreterBuilder();

            try{

                String content = Files.readString(src.toPath(), StandardCharsets.US_ASCII);

                if(version.equals("1.0")) {
                    Lexer lexer = lexerBuilder.buildLexer_V10();
                    List<Token> tokens = lexer.getTokens(content);
                    DefaultParser parser =
                            parserBuilder.buildParser_V10(TokenIterator.Companion.create(content, tokens));
                    NodeGroupResult node = parser.createNode();
                    Interpreter interpreter = interpreterBuilder.buildInterpreter_V10(emitter::print, provider::input);
                    interpreter.interpret(node).getContent();

                }else if(version.equals("1.1")) {
                    Lexer lexer = lexerBuilder.buildLexer_V11();
                    List<Token> tokens = lexer.getTokens(content);
                    DefaultParser parser =
                            parserBuilder.buildParser_V11(TokenIterator.Companion.create(content, tokens));
                    NodeGroupResult node = parser.createNode();
                    Interpreter interpreter = interpreterBuilder.buildInterpreter_V11(emitter::print, provider::input);
                    interpreter.interpret(node).getContent();

                }
            }catch(Exception e){
                handler.reportError(e.getMessage());
            }
        };



    }
}