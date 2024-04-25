package implementation

import astn.AST
import impl.ParserImpl
import interfaces.Parser
import interpreter.*
import lexer.LexerImpl
import lexer.TokenRegexRule
import interpreter.InterpreterImpl
import org.example.lexer.Lexer
import org.example.utils.JSONManager
import java.io.*
import java.util.*

class PrintScriptAdapter : PrintScriptInterpreter{
    private var lexer: Lexer = LexerImpl(loadLexerRules())
    private val parser: Parser = ParserImpl()
    private val interpreter: Interpreter = InterpreterImpl({ loadInput() }, { enterIfScope() }, { mergeScopes() })
    private var storedVariables: List<MutableMap<String, Value>> = listOf(mutableMapOf())
    private val outputs: Queue<String> = LinkedList()

    override fun execute(
        src: InputStream,
        version: String,
        emitter: PrintEmitter,
        handler: ErrorHandler,
        provider: InputProvider
    ) {
        try {
            val filePath = inputStreamToFile(src)
            updateLexerRules(version)
            val outputFile = writeInputToFile(provider)
            addLinesToQueue(outputFile.path)
            val file = File(filePath)
            var numberLine = 0
            file.forEachLine { line ->
                if (line.isNotBlank()) {
                    processLine(line, numberLine, emitter)
                    while (!lexer.isLineFinished()) {
                        processLine(line, numberLine, emitter)
                    }
                    numberLine++
                }
            }
            if (lexer.stillHaveTokens()) {
                throw Exception("File does not end with a separator")
            }
        } catch (e: OutOfMemoryError) {
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

    private fun updateLexerRules(version: String) {
        val newRulesPath = when (version) {
            "1.0" -> "src/main/resources/LexerDefaultRegex.json"
            "1.1" -> "src/main/resources/LexerFullRules.json"
            else -> throw IllegalArgumentException("Unsupported version: $version")
        }
        val file = File(newRulesPath)
        if (!file.exists()) {
            throw FileNotFoundException("File not found: $newRulesPath")
        }
        val json = file.readText()
        val newRegexRules = JSONManager.jsonToMap<TokenRegexRule>(json)
        lexer = LexerImpl(newRegexRules)
    }

    private fun processLine(line: String, numberLine: Int, emitter: PrintEmitter) {
        val ast = lexAndParse(line, numberLine)
        val result = interpreter.readAST(ast, storedVariables.last())

        result.trim().split("\n").forEach {
            if (it.isNotBlank()) {
                emitter.print(it)
            }
        }
    }
    private fun addLinesToQueue(filePath: String) {
        val file = File(filePath)
        if (file.exists()) {
            file.bufferedReader().useLines { lines ->
                lines.forEach { line ->
                    outputs.add(line)
                }
            }
        } else {
            return
        }
    }

    private fun lexAndParse(line: String, numberLine: Int): AST {
        val tokens = lexer.lex(line, numberLine)
        return parser.parse(tokens)
    }

    private fun loadLexerRules(): Map<String, TokenRegexRule> {
        val file = File("src/main/resources/LexerFullRules.json")
        val json = file.readText()
        return JSONManager.jsonToMap<TokenRegexRule>(json)
    }

    private fun enterIfScope() {
        val newStoredVariables = mutableListOf<MutableMap<String, Value>>()
        newStoredVariables.addAll(storedVariables)
        newStoredVariables.add(storedVariables.last().toMutableMap())
        storedVariables = newStoredVariables
    }

    private fun mergeScopes() {
        val hasToUpdate = storedVariables[storedVariables.size - 2]
        val updated = storedVariables.last()

        for ((key, value) in updated) {
            if (hasToUpdate.containsKey(key)) {
                hasToUpdate[key] = value
            }
        }

        storedVariables = storedVariables.dropLast(1)
    }
    private fun loadInput(): String {
        return if (outputs.isEmpty()) {
            ""
        } else {
            outputs.remove()
        }
    }
    private fun writeInputToFile(provider: InputProvider) : File {
    val outputFile = File.createTempFile("output", ".txt", File("src/main/resources"))
    PrintWriter(outputFile, "UTF-8").use { writer ->
        writer.println(provider.input("file path"))
    }
        return outputFile
}
}