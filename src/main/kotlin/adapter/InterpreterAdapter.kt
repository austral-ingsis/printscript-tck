package adapter

import interpreter.ErrorHandler
import interpreter.InputProvider
import interpreter.PrintEmitter
import interpreter.PrintScriptInterpreter
import java.io.InputStream
import org.printscript.common.Result
import org.printscript.lexer.source.StreamSourceReader
import org.printscript.runner.ExecuteRunner

class InterpreterAdapter : PrintScriptInterpreter {
    override fun execute(
        src: InputStream,
        version: String,
        emitter: PrintEmitter,
        handler: ErrorHandler,
        provider: InputProvider,
    ) {
        when (val parsed = Version.of(version)) {
            null -> handler.reportError("Version desconocida: $version")
            else -> run(parsed, src, emitter, handler, provider)
        }
    }

    private fun run(
        version: Version,
        src: InputStream,
        emitter: PrintEmitter,
        handler: ErrorHandler,
        provider: InputProvider,
    ) {
        try {
            val io = EmitterIO(emitter, provider)
            val result = ExecuteRunner(version, io).execute { StreamSourceReader.of(src) }

            if (result is Result.Failure) {
                handler.reportError(result.error.message)
            }
        } catch (error: OutOfMemoryError) {
            handler.reportError(error.message ?: "Java heap space")
        }
    }
}
