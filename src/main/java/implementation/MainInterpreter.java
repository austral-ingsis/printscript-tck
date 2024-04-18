package implementation;

import edu.austral.ingsis.gradle.common.ast.newast.AST;
import edu.austral.ingsis.gradle.common.ast.newast.BooleanNodeType;
import edu.austral.ingsis.gradle.common.ast.newast.NumberNodeType;
import edu.austral.ingsis.gradle.common.ast.newast.StringNodeType;
import edu.austral.ingsis.gradle.interpreter.*;
import edu.austral.ingsis.gradle.interpreter.util.*;
import edu.austral.ingsis.gradle.parser.iterator.ParserIterator;

import java.util.Arrays;
import java.util.List;

public class MainInterpreter {
    ParserIterator parserIterator;
    public MainInterpreter(ParserIterator parserIterator){
        this.parserIterator=parserIterator;
    }
    public void execute(){
        Context context= new Context();
        List<Interpreter> interpreters= Arrays.asList(
                new BlockNodeInterpreter(),
                new DeclarationInterpreter(),
                new StringLiteralInterpreter(),
                new NumberLiteralInterpreter(),
                new BooleanLiteralInterpreter(),
                new SumInterpreter(NumberNodeType.INSTANCE),
                new SumInterpreter(StringNodeType.INSTANCE),
                new SubtractInterpreter(NumberNodeType.INSTANCE),
                new SubtractInterpreter(StringNodeType.INSTANCE),
                new MultiplyInterpreter(NumberNodeType.INSTANCE),
                new MultiplyInterpreter(StringNodeType.INSTANCE),
                new DivideInterpreter(NumberNodeType.INSTANCE),
                new DivideInterpreter(StringNodeType.INSTANCE),
                new DeclarationInterpreter(),
                new DeclarationAssignationInterpreter(),
                new IdentifierInterpreter(),
                new IfElseStatementInterpreter(),
                new IfStatementInterpreter(),
                new PrintLnInterpreter(),
                new ReadEnvInterpreter(StringNodeType.INSTANCE),
                new ReadEnvInterpreter(NumberNodeType.INSTANCE),
                new ReadEnvInterpreter(BooleanNodeType.INSTANCE),
                new ReadInputInterpreter(StringNodeType.INSTANCE),
                new ReadInputInterpreter(NumberNodeType.INSTANCE),
                new ReadInputInterpreter(BooleanNodeType.INSTANCE),
                new ReassignationInterpreter()
        );

        while (parserIterator.hasNext()){
            AST ast = parserIterator.next();
            InterpreterManager interpreterManager=
                    new InterpreterManager(interpreters, new KotlinPrinter(),
                            new KotlinEnvReader(),
                            new KotlinInputReader());
            Interpreter interpreter=interpreterManager.getInterpreter(ast,null);
            InterpretResult interpretResult= interpreter.interpret(ast,  context,interpreterManager);
            if (interpretResult instanceof  InterpretResult.ContextResult){
                context=context.update(((InterpretResult.ContextResult) interpretResult).getContext());
            }
        }
    }
}
