package interpreter;

import org.example.MapEnvironment;
import org.example.Result;
import org.example.SemanticAnalyzer;
import org.example.SemanticAnalyzerImpl;
import org.example.SemanticFailure;
import org.example.SemanticSuccess;
import org.example.Signature;
import org.example.ast.DeclarationType;
import org.example.ast.statement.Statement;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class InterpreterIterator implements Iterator<Statement> {
    private final ErrorHandler handler;
    private final SemanticAnalyzerIterator semanticIterator;
    private final SemanticAnalyzer semanticAnalyzer = createSemanticAnalyzer();

    public InterpreterIterator(InputStream src, ErrorHandler handler) {
        this.handler = handler;
        this.semanticIterator = new SemanticAnalyzerIterator(
                handler,
                new SyntaxAnalyzerIterator(src, handler)
        );
    }

    private SemanticAnalyzer createSemanticAnalyzer() {
        final MapEnvironment env =
                new MapEnvironment(
                        new HashMap<>(),
                        Set.of(
                                new Signature("println", List.of(DeclarationType.NUMBER)),
                                new Signature("println", List.of(DeclarationType.STRING))));

        return new SemanticAnalyzerImpl(env);
    }

    @Override
    public boolean hasNext() {
        if (!semanticIterator.hasNext()) return false;
        else return loadNextAndEvaluateResult();
    }

    private boolean loadNextAndEvaluateResult() {
        Result result = semanticAnalyzer.analyze(semanticIterator);

        return switch (result) {
            case SemanticFailure failure -> {
                handler.reportError(failure.errorMessage());
                yield false;
            }
            case SemanticSuccess ignored -> true;
            default -> throw new IllegalStateException("Unexpected result for semantic analyzer: " + result);
        };
    }

    @Override
    public Statement next() {
        return semanticIterator.next();
    }
}