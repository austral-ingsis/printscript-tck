package adapter

import interpreter.InputProvider
import interpreter.PrintEmitter
import org.printscript.interpreter.io.PrintScriptIO

// El TCK parte la entrada/salida en dos interfaces --una para imprimir, otra para
// leer-- y nosotros la tenemos unificada en PrintScriptIO. Este es el Adapter que
// las junta: cada metodo delega en la mitad que le corresponde.
//
// No guarda nada. Si guardara los mensajes, el test de 32K lineas se quedaria sin
// memoria por culpa nuestra y no del emitter que el TCK eligio, que es lo que ese
// test esta midiendo.
class EmitterIO(
    private val emitter: PrintEmitter,
    private val provider: InputProvider,
) : PrintScriptIO {
    override fun print(message: String) = emitter.print(message)

    // Todavia no lo llama nadie: se enciende cuando 1.1 traiga readInput.
    override fun read(prompt: String): String = provider.input(prompt)
}
