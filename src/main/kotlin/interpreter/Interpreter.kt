package interpreter

import TracingInterpreter
import node.ASTNode

class Interpreter {
    private val interpreter = TracingInterpreter(print = false)

    fun interpret(iterator: Iterator<ASTNode>) {
        interpreter interpret iterator
    }

    fun getLog() = interpreter.getLog()
}
