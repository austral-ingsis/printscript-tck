package implementation;

import factory.LexerFactory;
import interpreter.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import lexer.Lexer;
import parser.ParserDirector;
import parser.ParserFactory;
import reader.Reader;

public class PrintScriptInterpreterImpl implements PrintScriptInterpreter {

    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        try {
            InputStreamReader streamReader = new InputStreamReader(src);
            Reader myReader = new Reader();


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
            Interpreter interpreter = new Interpreter(parser);  // Suposición: parser.getAST() devuelve el árbol sintáctico generado.
            interpreter.interpret();  // Interpretamos el AST generado

            // Si quieres emitir un resultado después de la ejecución
            emitter.print("Programa ejecutado con éxito");

        } catch (Exception e) {
            // En caso de errores, reportamos usando el handler
            handler.reportError(e.getMessage());
        }
    }
}
