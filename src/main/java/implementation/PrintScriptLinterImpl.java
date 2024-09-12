package implementation;

import config.LinterConfigLoader;
import factory.LexerFactory;
import factory.LinterFactory;
import interpreter.ErrorHandler;
import interpreter.PrintScriptLinter;
import kotlin.sequences.Sequence;
import lexer.Lexer;
import linter.Linter;
import linter.LinterError;
import parser.DummyASTProvider;
import parser.ParserDirector;
import parser.ParserFactory;
import reader.Reader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

public class PrintScriptLinterImpl implements PrintScriptLinter {

    @Override
    public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
        try {
            InputStreamReader sourceReader = new InputStreamReader(src);
            Reader myReader = new Reader(src);

            // Crear la instancia del linter usando LinterFactory basado en la versión
            Lexer lexer = new LexerFactory().createLexer1_0(myReader);
            ParserDirector parser = new ParserFactory().createParser1_0(lexer);
            Linter linter = new LinterFactory().createLinter1_0(parser);

            // Cargar la configuración del linter usando InputStream para el config
            LinterConfigLoader configLoader = new LinterConfigLoader(parser, config, null);  // Provide the InputStream and set filePath to null
            Linter linterConfig = configLoader.load();  // Aquí retorna directamente un objeto Linter

            // Obtener los errores del linter y reportarlos al handler
            Sequence<LinterError> linterErrors = linterConfig.lint();
            for (Iterator<LinterError> it = linterErrors.iterator(); it.hasNext();) {
                LinterError error = it.next();
                handler.reportError(error.getMessage());
            }

        } catch (Exception e) {
            // Si ocurre alguna excepción, reportarla al ErrorHandler
            handler.reportError("Exception: " + e.getMessage());
        }
    }
}
