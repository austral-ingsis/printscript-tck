package interpreter

import TracingInterpreter
import node.ASTNode
import tracer.Tracer

class Interpreter(tracer: Tracer) {
    private val interpreter = TracingInterpreter(tracer, print = false)

    fun interpret(iterator: Iterator<ASTNode>) {
        interpreter interpret iterator
    }
}
