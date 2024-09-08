package interpreter;

import org.example.SyntaxAnalyzer;
import org.example.SyntaxAnalyzerImpl;
import org.example.ast.statement.Statement;
import org.example.result.SyntaxError;
import org.example.result.SyntaxResult;
import org.example.result.SyntaxSuccess;

import java.util.Iterator;

public class SemanticAnalyzerIterator implements Iterator<Statement> {
    private final ErrorHandler handler;
    private final SyntaxAnalyzerIterator syntaxIterator;
    private final SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzerImpl();
    private Statement next;
//    private boolean isBufferedForInterpreter = false;

    SemanticAnalyzerIterator(ErrorHandler handler, SyntaxAnalyzerIterator syntaxIterator) {
        this.handler = handler;
        this.syntaxIterator = syntaxIterator;
    }

    @Override
    public boolean hasNext() {
//        if (isBufferedForInterpreter) return true;
        if (!syntaxIterator.hasNext()) return false;
        else return loadNextAndEvaluateResult();
    }

    private boolean loadNextAndEvaluateResult() {
        SyntaxResult result = syntaxAnalyzer.analyze(syntaxIterator);

        return switch (result) {
            case SyntaxError failure -> {
                handler.reportError(failure.errorMessage());
                yield false;
            }
            case SyntaxSuccess success -> {
                next = success.getStatement();
                yield next != null;
            }
            default -> throw new IllegalStateException("Unexpected result for syntax analyzer: " + result);
        };
    }

    @Override
    public Statement next() {
//        isBufferedForInterpreter = !isBufferedForInterpreter;

        return next;
    }
}