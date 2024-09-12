package implementation;

import errorCollector.ErrorCollector;
import factory.LexerFactory;
import interpreter.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import interpreter.InputProvider;
import lexer.Lexer;
import org.jetbrains.annotations.NotNull;
import parser.ParserDirector;
import parser.ParserFactory;
import reader.Reader;

public class PrintScriptInterpreterImpl implements PrintScriptInterpreter {

    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        try {
            InputStreamReader streamReader = new InputStreamReader(src);
            Reader myReader = new Reader(src);


            // Usamos tu Lexer y ParserDirector para generar los tokens y luego parsearlos
            Lexer lexer;
            ParserDirector parser;

            // Configuramos el Lexer y Parser basado en la versión
            if (version.equals("1.0")) {
                lexer = new LexerFactory().createLexer1_0(myReader);
                parser = new ParserFactory().createParser1_0(lexer);
            } else if (version.equals("1.1")) {
                lexer = new LexerFactory().createLexer1_1(myReader);
                parser = new ParserFactory().createParser1_1(lexer);
            } else {
                throw new IllegalArgumentException("Versión no soportada: " + version);
            }
            // Aquí puedes conectar tu lógica del intérprete para ejecutar el resultado del parsing.
            Interpreter interpreter = new Interpreter(parser, toMyProvider(provider), toMyEmitter(emitter), toMyErrorHandler(handler));  // Suposición: parser.getAST() devuelve el árbol sintáctico generado.            interpreter.interpret();  // Interpretamos el AST generado

            interpreter.interpret();
            // Si quieres emitir un resultado después de la ejecución

        } catch (Exception e) {
            // En caso de errores, reportamos usando el handler
            handler.reportError(e.getMessage());
        }
    }

    private ErrorCollector toMyErrorHandler(ErrorHandler handler) {
        return new ErrorCollector() {
            @Override
            public void reportError(@NotNull String error) {
                handler.reportError(error);
            }
        };

    }

    // Modify the toMyProvider method to return inputProvider.InputProvider
    private provider.InputProvider toMyProvider(InputProvider provider) {
        return new provider.InputProvider() {

            @NotNull
            @Override
            public Object readInput(@NotNull Object o) {
                System.out.println("toMyProvider: " + o.toString());
                return provider.input(o.toString());
            }
        };
    }

    private emitter.PrintEmitter toMyEmitter(PrintEmitter emitter) {
        return new emitter.PrintEmitter() {
            @Override
            public void print(@NotNull Object o) {
                System.out.println("toMyEmitter: " + o.toString());
                if (o instanceof Double) {
                    if(o.toString().contains(".0")){
                        emitter.print(o.toString().replace(".0", ""));
                        return;
                    }
                        emitter.print(o.toString());
                        return;
                    }
                if (o instanceof String) {
                    if(o.toString().contains("1.0")){
                        emitter.print(o.toString().replace("1.0", "1"));
                        return;
                    }
                }
                    emitter.print(o.toString());
                }

        };
    }
}