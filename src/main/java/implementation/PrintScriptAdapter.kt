package implementation

import interpreter.ErrorHandler
import interpreter.InputProvider
import interpreter.PrintEmitter
import interpreter.PrintScriptInterpreter
import java.io.*
import org.example.PrintScript


class PrintScriptAdapter : PrintScriptInterpreter {

    override fun execute(src: InputStream, version: String, emitter: PrintEmitter, handler: ErrorHandler, provider: InputProvider) {
        val printScript = PrintScript(::input)
        try {
            val filePath = inputStreamToFile(src)
            val result = printScript.start(filePath).trimEnd('\n')
            if (result.contains("An error")) {
                throw Exception(result)
            }
            emitter.print(result)

        } catch (e: Exception) {
            handler.reportError("An error occurred while executing the script: ${e.message}")
        }
    }

    private fun inputStreamToFile(inputStream: InputStream): String {
        val tempFile = File.createTempFile("temp", ".txt", File("src/main/resources"))
        tempFile.deleteOnExit()
        tempFile.outputStream().use { it.write(inputStream.readBytes()) }
        return tempFile.absolutePath
    }


    private fun input(message: String): String {
        print(message)
        return readlnOrNull() ?: ""
    }
}