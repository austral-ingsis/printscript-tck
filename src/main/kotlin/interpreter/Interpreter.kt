package interpreter

import CatchableTracingInterpreter
import node.ASTNode

class Interpreter {
    private val interpreter = CatchableTracingInterpreter(print = false)

    fun interpret(iterator: Iterator<ASTNode>) {
        interpreter interpret iterator
    }

    fun getLog() = interpreter.getLog()

    fun hasException() = interpreter.hasException()

    fun getException() = interpreter.getException()
}
