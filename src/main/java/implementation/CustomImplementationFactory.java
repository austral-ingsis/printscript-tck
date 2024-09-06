package implementation;

import adapter.Adapter;
import ast.ASTNode;
import controller.LexerVersionController;
import interfaces.Lexer;
import interpreter.*;
import parser.Parser;
import token.Token;

import java.io.IOException;
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
                List<Token> tokens = lexer.getToken();
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
        // your PrintScript linter should be returned here.
        // make sure to ADAPT your linter to PrintScriptLinter interface.
        throw new NotImplementedException("Needs implementation"); // TODO: implement
    }
}