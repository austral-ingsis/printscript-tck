package implementation;

import AST.nodes.ASTNode;
import adapter.InputStreamToFileAdapter;
import adapter.PrintEmitterAdapter;
import fileReader.FileReaderIterator;
import interpreter.*;
import iterator.TokenIterator;
import parser.iterator.ASTIterator;
import token.Token;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class PrintScriptInterpreterImpl implements PrintScriptInterpreter {

  @Override
  public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
    try {
      PrintEmitterAdapter printEmitterAdapter = new PrintEmitterAdapter(emitter);


      InputStreamToFileAdapter adapter = new InputStreamToFileAdapter();


      FileReaderIterator fileReaderIterator = new FileReaderIterator(adapter.inputStreamToFile(src));


      Iterator<Token> tokenIterator = new TokenIterator(fileReaderIterator, version);


      Iterator<ASTNode> ASTNodes = new ASTIterator(tokenIterator);


      new Interpreter(printEmitterAdapter).interpret(ASTNodes);
    }
    catch (Error e) {
      handler.reportError(e.getMessage());
    } catch (IOException e) {
      handler.reportError(e.getMessage());
    }
    catch (Exception e){
      handler.reportError(e.getMessage());
    }
  }
}