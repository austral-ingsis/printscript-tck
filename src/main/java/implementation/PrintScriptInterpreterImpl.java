package implementation;

import AST.nodes.ASTNode;
import adapter.InputStreamToFileAdapter;
import adapter.PrintEmitterAdapter;
import fileReader.FileReaderIterator;
import interpreter.*;
import iterator.TokenIterator;
import parser.iterator.ASTIterator;
import providers.printProvider.TestPrintProvider;
import token.Token;
import java.io.InputStream;
import java.util.Iterator;

public class PrintScriptInterpreterImpl implements PrintScriptInterpreter {

  @Override
  public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
    try {
      PrintEmitterAdapter printEmitterAdapter = new PrintEmitterAdapter(emitter);


      InputStreamToFileAdapter adapter = new InputStreamToFileAdapter();


      FileReaderIterator fileReaderIterator = new FileReaderIterator(adapter.inputStreamToFile(src));


      Iterator<Token> tokenIterator = new TokenIterator(fileReaderIterator);


      Iterator<ASTNode> ASTNodes = new ASTIterator(tokenIterator);


      new Interpreter(printEmitterAdapter).interpret(ASTNodes);
    } catch (Exception e) {
      handler.reportError(e.getMessage());
    }
  }
}