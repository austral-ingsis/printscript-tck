package interpreter;





import ast.Scope;
import kotlin.Pair;
import kotlin.Result;
import lexer.Context;
import lexer.Lexer;
import lexer.LexerImpl;
import parser.MyParser;
import parser.Parser;
import stringReader.PartialStringReadingLexer;
import token.TokenInfo;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class InterpreterAdapter implements PrintScriptInterpreter {
    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        try {
            byte[] bytes = src.readNBytes(1046);
            //new PartialStringReadingLexer()
            Lexer lexer = new LexerImpl();
            PartialStringReadingLexer tokenizer =
                    new PartialStringReadingLexer(lexer, "", new Context(0));

            Parser parser = new MyParser();

            while (bytes.length > 0) {
                String code = new String(bytes, StandardCharsets.UTF_8);
                Pair<PartialStringReadingLexer, List<TokenInfo>> result = tokenizer.tokenizeString(code);
                tokenizer = result.getFirst();

                List<TokenInfo> tokens =result.getSecond();

                //parser.
                //Result<Scope> ast = parser.parseTokens(tokens);


                bytes = src.readNBytes(1046);
            }
        } catch (Exception err) {
            handler.reportError(err.getMessage());
        }
    }
}
