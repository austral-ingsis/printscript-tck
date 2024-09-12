package interpreter

import reader.Reader

class InputAdapter(private val input: InputProvider, private val printer: PrintEmitter): Reader {
    override fun read(message: String): String {
        printer.print(message)
        return input.input(message)
    }
}
