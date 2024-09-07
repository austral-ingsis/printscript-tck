package interpreter;

import org.example.*;
import org.example.ast.AstComponent;
import org.example.ast.DeclarationType;
import org.example.factory.InterpreterFactory;
import org.example.lexerresult.LexerSuccess;
import org.example.observer.BrokerObserver;
import org.example.observer.PrintBrokerObserver;
import org.example.result.SyntaxResult;
import org.example.token.Token;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Interpreter implements PrintScriptInterpreter{

//  private final Lexer lexer = new PrintScriptLexer();
//  private final SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzerImpl();
//  private final SemanticAnalyzer semanticAnalyzer = createSemanticAnalyzer();
//  private final PrintBrokerObserver observer = new PrintBrokerObserver();
//  private final org.example.Interpreter interpreter = createInterpreter(observer);



  @Override
  public void execute(InputStream src, String version, PrintEmitter emitter, ErrorHandler handler, InputProvider provider) {
//    if(!version.equals("1.0")) return;
//
//    try{
//      BufferedReader reader = new BufferedReader(new InputStreamReader(src));
//      processLines(reader, emitter, handler);
//
//    } catch (IOException e){
//      handler.reportError("Error reading file");
//    }
//
//

  }
//
//  // Private
//  private SemanticAnalyzer createSemanticAnalyzer() {
//    final MapEnvironment env =
//        new MapEnvironment(
//          new HashMap<>(),
//          Set.of(
//            new Signature("println", List.of(DeclarationType.NUMBER)),
//            new Signature("println", List.of(DeclarationType.STRING))));
//
//    return new SemanticAnalyzerImpl(env);
//  }
//
//  private org.example.Interpreter createInterpreter(BrokerObserver<String> observer) {
//    InterpreterFactory factory = new InterpreterFactory();
//    factory.addObserver(observer);
//    return factory.create();
//  }
//
//  private void processLines(BufferedReader reader, PrintEmitter emitter, ErrorHandler handler) throws IOException{
//    List<Token> tokens = new ArrayList<>();
//    while(reader.ready()) {
//      Result lexerResult = lexer.lex(reader.readLine());
//
//      if (!lexerResult.isSuccessful()) {
//        handler.reportError(lexerResult.errorMessage());
//        return;
//      }
//      tokens.addAll(((LexerSuccess)lexerResult).getTokens());
//    }
//
//    SyntaxResult syntaxResult = syntaxAnalyzer.analyze(tokens);
//
//    if(!syntaxResult.isSuccessful()){
//      handler.reportError(syntaxResult.errorMessage());
//      return;
//    }
//
//    List<AstComponent> components = syntaxResult.getComponents();
//
//    Result semanticResult = semanticAnalyzer.analyze(components);
//    if(!semanticResult.isSuccessful()){
//      handler.reportError(semanticResult.errorMessage());
//      return;
//    }
//
//    interpreter.interpret(components);
//    emitter.print(observer.getPrintedOutput());
//    }

}
