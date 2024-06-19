package implementation;

import astbuilder.ASTBuilderFailure;
import astbuilder.ASTBuilderResult;
import astbuilder.ASTBuilderSuccess;
import astbuilder.ASTProviderFactory;
import interpreter.*;
import lexer.Lexer;
import parser.Parser;
import utils.ASTNode;
import utils.PrintScriptChunkReader;
import utils.Token;

import java.io.*;
import java.util.*;

public class PrintscriptAdapter implements PrintScriptInterpreter {

    @Override
    public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
        try {
            InterpreterImpl interpreter = new InterpreterImpl(new HashMap<>(), version, new OutputAdapter(emitter), new InputAdapter(provider));
            PrintScriptChunkReader chunkReader = new PrintScriptChunkReader();

            BufferedReader reader = new BufferedReader(new InputStreamReader(src));
            String line;
            while((line = reader.readLine()) != null) {
                if (line.contains("if")) {
                    line = handleIf(line, reader);
                }
                List<String> chunkLines = chunkReader.readChunksFromString(line);
                List<ASTNode> astNodes = validate(chunkLines, version, handler);
                for (ASTNode ast : astNodes) {
                    interpreter = interpreter.interpret(ast);
                }
            }

        }catch(OutOfMemoryError e) {
            handler.reportError("Java heap space");
        }
        catch (Exception | Error e){
            handler.reportError(e.getMessage());
        }
    }

    private List<ASTNode> validate(List<String> fileChunks, String version, ErrorHandler handler) {
        List<ASTNode> successfulASTs = new ArrayList<>();
        int chunkStartLine = 1;
        for (String chunk : fileChunks) {
            Lexer lexer = new Lexer(chunk, chunkStartLine, getTokenRegex(version));
            List<Token> tokens = lexer.tokenize();
            chunkStartLine = lexer.getCurrentLineIndex() + 1;
            Parser parser = new Parser();
            ASTBuilderResult ast = parser.parse(new ASTProviderFactory(tokens, version));
            switch (ast) {
                case ASTBuilderSuccess success -> {
                    successfulASTs.add(success.getAstNode());
                }
                case ASTBuilderFailure failure -> {
                    if (!failure.getErrorMessage().equals("Empty tokens")) {
                        handler.reportError(failure.getErrorMessage());
                        return new ArrayList<>();
                    }
                }
            }
        }
        return successfulASTs;
    }

    private String handleIf(String line, BufferedReader reader) throws IOException {
        while (!line.contains("}")) {
            line += reader.readLine();
        }
        String newLine = reader.readLine();

        if(newLine.contains("else")) {
            while (!newLine.contains("}")) {
                newLine += reader.readLine();
            }
        }
        line += " " + newLine + " ";
        return line;
    }

    private String getTokenRegex(String version) {
        return "src/main/java/resources/tokens" + version + ".json";
    }
}
