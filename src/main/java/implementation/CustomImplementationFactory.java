package implementation;

import adapter.Adapter;
import adapter.linter.ConfigLoaderAdapter;
import ast.ASTNode;
import config.ConfigLoader;
import controller.LexerVersionController;
import interfaces.Lexer;
import interpreter.*;
import parser.Parser;
import sca.StaticCodeAnalyzer;
import sca.StaticCodeAnalyzerError;
import token.Token;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CustomImplementationFactory implements PrintScriptFactory {
    Adapter adapter = new Adapter();
    OutputAdapterJava outputAdapter;
    InputAdapter inputAdapter;

    @Override
    public PrintScriptInterpreter interpreter() {
        return (src, version, emitter, handler, provider) -> {
            try {
                this.outputAdapter = new OutputAdapterJava(emitter);
                this.inputAdapter = new InputAdapter(provider);
                adapter.execute(src, version, outputAdapter, handler, inputAdapter);
            } catch (Exception | Error e) {
                handler.reportError(e.getMessage());
            }
        };
    }

    @Override
    public PrintScriptFormatter formatter() {
        return (src, version, config, writer) -> {
            try {
                LexerVersionController versionControl = new LexerVersionController();
                Lexer lexer = versionControl.getLexer(version, src);
                List<Token> tokens = new ArrayList<Token>();
                Token token = lexer.getNextToken();
                while (token != null) {
                    tokens.add(token);
                    token = lexer.getNextToken();
                }
                Parser parser = new Parser(tokens);

                List<ASTNode> astNodes = parser.generateAST();
                Formatter formatter = new Formatter(config);
                writer.write(formatter.format(astNodes));

            } catch (Exception | Error e) {
                try {
                    writer.write(e.getMessage());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };

        // Dummy impl: return (src, version, config, writer) -> { };
    }

    @Override
    public PrintScriptLinter linter() {
        return (src, version, config, handler) -> {
            try {
                LexerVersionController versionControl = new LexerVersionController();
                Lexer lexer = versionControl.getLexer(version, src);
                List<Token> tokens = new ArrayList<Token>();
                Token token = lexer.getNextToken();
                while (token != null) {
                    tokens.add(token);
                    token = lexer.getNextToken();
                }
                Parser parser = new Parser(tokens);

                List<ASTNode> astNodes = parser.generateAST();

                ConfigLoader configLoader = new ConfigLoaderAdapter(config);
                StaticCodeAnalyzer staticCodeAnalyzer = new StaticCodeAnalyzer(configLoader);

                List<StaticCodeAnalyzerError> errors = staticCodeAnalyzer.analyze(astNodes);
                for (StaticCodeAnalyzerError error : errors) {
                    handler.reportError(error.toString());
                }
            } catch (Exception | Error e) {
                handler.reportError(e.getMessage());
            }
        };
    }
}