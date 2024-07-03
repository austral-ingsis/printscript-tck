package interpreter

import reader.Reader


class InputAdapter(private val input: InputProvider): Reader {
    override fun read(string: String): String =
        input.input(string)
}
