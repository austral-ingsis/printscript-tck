package interpreter

import ingsis.interpreter.interpretStatement.Input

class InputAdapter(private val input: InputProvider): Input {
    override fun readInput(string: String): String =
        input.input(string)
}