package implementation;

import edu.austral.ingsis.printscript.analyzer.AnalysisFinding;
import edu.austral.ingsis.printscript.analyzer.AnalyzerConfig;
import edu.austral.ingsis.printscript.analyzer.PrintScriptAnalyzer;
import edu.austral.ingsis.printscript.common.OperatorDefinition;
import edu.austral.ingsis.printscript.common.PrintScriptException;
import edu.austral.ingsis.printscript.common.Version;
import edu.austral.ingsis.printscript.common.ast.Statement;
import edu.austral.ingsis.printscript.lexer.PrintScriptLexer;
import edu.austral.ingsis.printscript.lexer.StringPositionalSource;
import edu.austral.ingsis.printscript.parser.PrintScriptParser;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import interpreter.ErrorHandler;
import interpreter.PrintScriptLinter;

/**
 * Adapts our style analyzer to the TCK's {@link PrintScriptLinter} contract. This is purely style
 * checking (naming convention, println/readInput argument shape) — full semantic validation (is
 * every variable declared, are types consistent) is {@code execute}'s concern, not {@code lint}'s,
 * matching how the TCK's own fixtures separate the two.
 */
public final class TckLinterAdapter implements PrintScriptLinter {

    @Override
    public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
        try {
            String source = Sources.readAll(src);
            Version parsedVersion = Version.fromLabel(version);
            Set<OperatorDefinition> noPlugins = Set.of();

            var lexer = new PrintScriptLexer(noPlugins);
            var parser = new PrintScriptParser(noPlugins, parsedVersion);
            Iterator<Statement> statements =
                    parser.parse(lexer.tokenize(new StringPositionalSource(source)));

            AnalyzerConfig analyzerConfig = TckConfig.analyzerConfigFrom(config);
            List<AnalysisFinding> findings =
                    new PrintScriptAnalyzer().analyze(statements, analyzerConfig);
            findings.forEach(finding -> handler.reportError(finding.message()));
        } catch (PrintScriptException e) {
            handler.reportError(TckInterpreterAdapter.describe(e));
        }
    }
}
