package implementation;

import interpreter.PrintScriptInterpreter;
import org.austral.ingsis.printscript.common.Token;
import org.austral.ingsis.printscript.interpreter.Interpreter;
import org.austral.ingsis.printscript.interpreter.Lexer;
import org.austral.ingsis.printscript.interpreter.PrintScriptLexer;
import org.austral.ingsis.printscript.interpreter.Printer;
import org.austral.ingsis.printscript.parser.DocumentParser;
import org.austral.ingsis.printscript.parser.model.Document;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class CustomImplementationFactory implements InterpreterFactory {

    @Override
    public PrintScriptInterpreter interpreter() {
        return (src, version, emitter, handler) -> {
            Lexer lexer = PrintScriptLexer.INSTANCE.lexer();
            DocumentParser parser = DocumentParser.Companion.create();
            Printer printer = o -> emitter.print(o.toString());
            Interpreter interpreter = new Interpreter(printer);
            try {
                String content = readFile(src);
                List<Token> tokens = lexer.lex(content);
                Document document = parser.parse(content, tokens);
                interpreter.interpret(document);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                handler.reportError(e.getMessage());
            }
        };
    }

    private String readFile(File src) throws IOException{
        List<String> lines = Files.readAllLines(src.toPath());
        return String.join("\n", lines);
    }
}
