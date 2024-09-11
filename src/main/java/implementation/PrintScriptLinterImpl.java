package implementation;

import interpreter.ErrorHandler;
import interpreter.Interpreter;
import interpreter.PrintScriptLinter;
import kotlin.Pair;
import lexer.Lexer;
import linter.Linter;
import linter.LinterResult;
import nodes.StatementType;
import parser.Parser;
import position.visitor.Environment;
import token.Token;

import java.io.InputStream;
import java.util.Iterator;

public class PrintScriptLinterImpl implements PrintScriptLinter {
    @Override
    public void lint(InputStream src, String version, InputStream config, ErrorHandler handler) {
//        try {
//            Iterator<Token> tokens = new Lexer(src, version);
//            Iterator<StatementType> asts = new Parser(tokens, version);
//            LinterResult result = new Linter(config).lint(asts, version);
//            if (!result.isValid()){
//                handler.reportError(result.getMessage());
//            }
//        } catch (Exception e) {
//            handler.reportError(e.getMessage());
//        }
    }
}
