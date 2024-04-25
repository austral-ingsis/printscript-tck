package implementation

import interpreter.*
import org.example.PrintScript
import java.io.*

class PrintScriptAdapter : PrintScriptInterpreter {

    override fun execute(
        src: InputStream,
        version: String,
        emitter: PrintEmitter,
        handler: ErrorHandler,
        provider: InputProvider
    ) {
        val printScript = PrintScript()
        try {
            val filePath = inputStreamToFile(src)
            if (provider is QueueInputProvider) {
                provider.setFilePath(filePath)
            }
            val outputFile = File.createTempFile("output", ".txt", File("src/main/resources"))
            if (version == "1.0") {
                printScript.updateRegexRules("src/main/resources/LexerDefaultRegex.json")
            } else if (version == "1.1") {
                printScript.updateRegexRules("src/main/resources/LexerFullRules.json")
            }
            val result = printScript.start(filePath, outputFile.path).trimEnd('\n')
            emitter.print(result)
            outputFile.deleteOnExit()
        } catch (e: Exception) {
            handler.reportError("An error occurred while executing the script: ${e.message}")
        }
    }

    private fun inputStreamToFile(inputStream: InputStream): String {
        val tempFile = File.createTempFile("temp", ".txt", File("src/main/resources"))
        tempFile.deleteOnExit()
        FileOutputStream(tempFile).use { fos ->
            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream.read(buffer).also { length = it } > 0) {
                fos.write(buffer, 0, length)
            }
        }
        return tempFile.absolutePath
    }
}