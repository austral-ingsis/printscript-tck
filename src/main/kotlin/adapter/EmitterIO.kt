package adapter

import interpreter.InputProvider
import interpreter.PrintEmitter
import org.printscript.interpreter.io.PrintScriptIO

class EmitterIO(
    private val emitter: PrintEmitter,
    private val provider: InputProvider,
) : PrintScriptIO {
    override fun print(message: String) = emitter.print(message)

    // Todavia no lo llama nadie: se enciende cuando 1.1 traiga readInput.
    override fun read(prompt: String): String = provider.input(prompt)
}
