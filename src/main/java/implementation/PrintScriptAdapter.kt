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
            val outputFile = File.createTempFile("output", ".txt", File("src/main/resources"))
            val writer = PrintWriter(outputFile, "UTF-8")
            writer.println(provider.input("file path"))
            writer.close()

            if (version == "1.0") {
                printScript.updateRegexRules("src/main/resources/LexerDefaultRegex.json")
            } else if (version == "1.1") {
                printScript.updateRegexRules("src/main/resources/LexerFullRules.json")
            }
            val result = printScript.start(filePath, outputFile.path).trimEnd('\n')
            val output = separateOutput(result)
            for (line in output) {
                emitter.print(line)
            }
            outputFile.delete()
        } catch (e: OutOfMemoryError ) {
            handler.reportError("Java heap space")
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

    private fun separateOutput(output: String): List<String> {
        return output.split("\n")
    }
}