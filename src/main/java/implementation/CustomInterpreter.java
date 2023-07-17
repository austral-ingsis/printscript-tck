package implementation;

import implementation.io.PrintFunctionImpl;
import implementation.io.ReadInputFunctionImpl;
import ingsis.printscript.interpreter.Interpreter;
import ingsis.printscript.lexer.Lexer;
import ingsis.printscript.parser.implementations.Parser;
import ingsis.printscript.utilities.enums.Token;
import ingsis.printscript.utilities.enums.Version;
import ingsis.printscript.utilities.visitor.VisitableAST;
import interpreter.ErrorHandler;
import interpreter.InputProvider;
import interpreter.PrintEmitter;
import interpreter.PrintScriptInterpreter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.List;
import java.util.Objects;

public class CustomInterpreter implements PrintScriptInterpreter {
    @Override
    public void execute(
            InputStream src,
            String version,
            PrintEmitter emitter,
            ErrorHandler handler,
            InputProvider provider
    ) {
        Lexer lexer = new Lexer();
        Version printscriptVersion;
        if(Objects.equals(version, "1.0")) {
            printscriptVersion = Version.VERSION_1_0;
        } else {
            printscriptVersion = Version.VERSION_1_1;
        }
        Parser parser = new Parser(printscriptVersion);
        Interpreter interpreter = Interpreter.Factory.createMock(
                new PrintFunctionImpl(emitter),
                new ReadInputFunctionImpl(provider, emitter)
        );

        BufferedReader reader = new BufferedReader(new InputStreamReader(src));
        String line;
        try {
            while ((line = reader.readLine()) != null) {

                if (line.startsWith("if(")) {
                    String line1 = line;
                    String line2 = reader.readLine();
                    String line3 = reader.readLine();

                    if (line3 != null && line3.trim().equals("} else {")) {
                        String line4 = reader.readLine();
                        String line5 = reader.readLine();

                        line = line1 + line2 + line3 + line4 + line5;
                    } else {
                        line = line1 + line2 + line3;
                    }
                }


                try{
                    List<Token> tokens = lexer.tokenize(line);
                    VisitableAST tree = parser.parse(tokens);
                    interpreter.interpret(tree);
                } catch (Error | Exception e){
                    if(e instanceof OutOfMemoryError) {
                        handler.reportError("Java heap space");
                    } else {
                        handler.reportError(e.getMessage());

                    }
                }
            }
            reader.close();
        } catch (Exception | OutOfMemoryError e) {
            if(e instanceof OutOfMemoryError) {
                handler.reportError("Java heap space");
            }
        }


    }

}
