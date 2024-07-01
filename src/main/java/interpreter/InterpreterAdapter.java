package interpreter;

import adapters.ParserAdapterToJava;
import adapters.ParserResult;
import ast.AST;
import factory.InterpreterFactoryImpl;
import factory.LexerFactoryImpl;
import interpreter.specializedInterpreter.CallInterpreter;
import kotlin.Pair;
import lexer.Context;
import lexer.Lexer;
import lexer.LexerImpl;
import org.jetbrains.annotations.NotNull;
import stringReader.PartialStringReadingLexer;
import token.TokenInfo;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InterpreterAdapter implements PrintScriptInterpreter {
    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        try {
            byte[] bytes = src.readNBytes(1046);
            Lexer lexer = new LexerFactoryImpl(version).create();
            CallInterpreter callInterpreter = CallInterpreter.INSTANCE;
            new InterpreterFactoryImpl(version).create();
            PartialStringReadingLexer tokenizer =
                    new PartialStringReadingLexer(lexer, "", new Context(0));


            ParserAdapterToJava parser = new ParserAdapterToJava();
            InputReader reader = new AdaptInput(provider);
            Interpreter interpreter = new Interpreter(
                    new Report(new ArrayList<>(), new ArrayList<>()),
                    new HashMap<>(),
                    reader
            );

            while (bytes.length > 0) {
                String code = new String(bytes, StandardCharsets.UTF_8);
                Pair<PartialStringReadingLexer, List<TokenInfo>> result = tokenizer.tokenizeString(code);
                tokenizer = result.getFirst();

                List<TokenInfo> tokens =result.getSecond();
                ParserResult parserResult = parser.parseTokens(tokens);

                if (!parserResult.getSuccess()) {
                    Throwable error = parserResult.getErr();
                    if (error == null) error = new Throwable("unknown parse error");
                    handler.reportError(error.getMessage());
                    return;
                }
                AST ast = parserResult.getAst();
                if (ast == null) {
                    handler.reportError("unknown parse error");
                    return;
                }

                interpreter = interpreter.interpret(ast);
                if (!interpreter.getReport().getErrors().isEmpty()) {
                    for (String error : interpreter.getReport().getErrors())
                       handler.reportError(error);
                    return;
                }
                if (!interpreter.getReport().getOutputs().isEmpty()) {
                    for (String output : interpreter.getReport().getOutputs())
                        emitter.print(output);
                    interpreter = new Interpreter(
                            new Report(new ArrayList<>(), new ArrayList<>()),
                            interpreter.getVariables(),
                            interpreter.getInputReader()
                            );
                }

                bytes = src.readNBytes(1046);
            }

            src.close();
        } catch (Exception | Error exc) {
            handler.reportError(exc.getMessage());
        }
    }

    class AdaptInput implements InputReader{
        private InputProvider provider;
        public AdaptInput(InputProvider provider) {
            this.provider = provider;
        }

        @NotNull
        @Override
        public InputReadResult readInput() {
            return new InputReadResult(provider.input(""), this);
        }
    }
}
