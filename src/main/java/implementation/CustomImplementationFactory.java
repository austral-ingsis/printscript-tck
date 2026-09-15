package implementation;

import interpreter.*;

import ast.PrintScriptVersion;
import ast.Program;
import formatter.FormattingConfigLoader;
import formatter.FormattingRules;
import interpreter.PrintScriptLinter;
import lexer.LexerConfigurations;
import lexer.StreamLexer;
import linter.config.LintConfig;
import parser.ConfigurableParser;
import parser.grammar.GrammarConfigurations;
import result.CompilerError;
import result.Result;
import semantic.SemanticAnalyzer;
import semantic.SemanticConfigurations;
import token.Token;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class CustomImplementationFactory implements PrintScriptFactory {

    @Override
    public PrintScriptInterpreter interpreter() {
        return (src, version, emitter, handler, provider) -> {
            try {
                PrintScriptVersion psVersion = PrintScriptVersion.Companion.fromString(version);
                Program program = loadProgram(src, psVersion, handler);
                if (program == null) return;

                // Adaptamos el PrintEmitter del TCK a tu interfaz Output
                Output output = emitter::print;

                // Adaptamos el InputProvider del TCK al tuyo
                interpreter.InputProvider myInputProvider = provider::input;

                var config = InterpreterConfigurations.INSTANCE.getConfiguration(psVersion);
                new ConfigurableInterpreter(output, config, myInputProvider, new SystemEnvProvider()).run(program);

            } catch (Exception e) {
                handler.reportError(e.getMessage() != null ? e.getMessage() : e.toString());
            }
        };
    }

    @Override
    public interpreter.PrintScriptFormatter formatter() {
        return (src, version, config, writer) -> {
            try {
                PrintScriptVersion psVersion = PrintScriptVersion.Companion.fromString(version);
                Program program = loadProgram(src, psVersion, message -> {}); // o un handler real
                if (program == null) return;

                // Cargá las reglas de formato desde el config (InputStream)
                // Si tenés FormattingConfigLoader que lee de InputStream, usalo acá.
                // Si no, usá el default por ahora:
                FormattingRules rules = FormattingConfigLoader.INSTANCE.loadDefault();
                // TODO: si el config del TCK viene con reglas, parsealo y usalo

                String formatted = new formatter.PrintScriptFormatter(rules).format(program);
                writer.write(formatted);
                writer.flush();

            } catch (Exception e) {
                // el formatter del TCK no tiene ErrorHandler, así que solo no escribimos o logueamos
                e.printStackTrace();
            }
        };
    }

    @Override
    public PrintScriptLinter linter() {
        return (src, version, config, handler) -> {
            try {
                PrintScriptVersion psVersion = PrintScriptVersion.Companion.fromString(version);
                Program program = loadProgram(src, psVersion, handler);
                if (program == null) return;

                // Igual que el formatter: si podés cargar LintConfig desde el InputStream, mejor.
                // Por ahora usamos el default:
                LintConfig lintConfig = new LintConfig();
                // TODO: parsear el config del TCK si hace falta

                var notifications = new linter.PrintScriptLinter(lintConfig).lint(program);
                for (var n : notifications) {
                    handler.reportError(
                            n.getSeverity() + ": " +
                                    n.getPosition().getLine() + ":" + n.getPosition().getColumn() + " - " +
                                    n.getMessage()
                    );
                }

            } catch (Exception e) {
                handler.reportError(e.getMessage() != null ? e.getMessage() : e.toString());
            }
        };
    }

    /** Replica la lógica de tu loadProgram del CLI */
    private Program loadProgram(InputStream src, PrintScriptVersion version, ErrorHandler handler) {
        try {
            Reader reader = new BufferedReader(new InputStreamReader(src, StandardCharsets.UTF_8));

            var lexerConfig = LexerConfigurations.INSTANCE.getConfiguration(version);
            var tokenResult = new StreamLexer(reader, lexerConfig).tokenize();

            if (tokenResult instanceof Result.Failure<?> failure) {
                reportError(failure.getError(), handler);
                return null;
            }

            var tokens = ((Result.Success<?>) tokenResult).getValue();

            var parserConfig = GrammarConfigurations.INSTANCE.getConfiguration(version);
            var parseResult = new ConfigurableParser(parserConfig).parse((List<Token>) tokens);

            if (parseResult instanceof Result.Failure<?> failure) {
                reportError(failure.getError(), handler);
                return null;
            }

            Program program = (Program) ((Result.Success<?>) parseResult).getValue();

            // Semantic check
            var semanticConfig = SemanticConfigurations.INSTANCE.getConfiguration(version);
            var semanticErrors = new SemanticAnalyzer(semanticConfig).analyze(program);

            if (!semanticErrors.isEmpty()) {
                for (Object err : semanticErrors) {
                    reportError(err, handler);
                }
                return null;
            }

            return program;

        } catch (Exception e) {
            handler.reportError(e.getMessage() != null ? e.getMessage() : e.toString());
            return null;
        }
    }

    private void reportError(Object error, ErrorHandler handler) {
        if (error instanceof CompilerError compilerError) {
            handler.reportError(compilerError.getMessage());
        } else if (error instanceof List<?> list) {
            for (Object e : list) {
                reportError(e, handler);
            }
        } else {
            handler.reportError(String.valueOf(error));
        }
    }
}