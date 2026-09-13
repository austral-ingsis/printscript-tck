package adapter

import interpreter.InputProvider
import interpreter.PrintEmitter
import org.printscript.interpreter.io.PrintScriptIO

class EmitterIO(
    private val emitter: PrintEmitter,
    private val provider: InputProvider,
) : PrintScriptIO {
    override fun print(message: String) = emitter.print(message)

    // El prompt no se imprime aca: lo imprime readInput, para que aparezca en la
    // salida del programa una sola vez.
    override fun read(prompt: String): String = provider.input(prompt)

    // El TCK inyecta las variables con `environment` en el build.gradle, asi que
    // salen del entorno real del proceso de test.
    override fun env(name: String): String? = System.getenv(name)
}
