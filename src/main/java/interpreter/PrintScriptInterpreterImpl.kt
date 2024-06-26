package interpreter

import interpreter.ExecuteInterpreter.Companion.getInterpreterByVersion
import interpreter.response.ErrorResponse
import interpreter.response.SuccessResponse
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.PrintStream
import lexer.Lexer.Companion.getLexerByVersion
import lexer.TokenProvider
import parser.Parser.Companion.getParserByVersion

class PrintScriptInterpreterImpl : PrintScriptInterpreter {
    override fun execute(
        src: InputStream?,
        version: String?,
        emitter: PrintEmitter?,
        handler: ErrorHandler?,
        provider: InputProvider?
    ) {
        val lexer = getLexerByVersion(version!!)
        val tokenProvider = TokenProvider(src!!, lexer)
        val parser = getParserByVersion(version)
        val interpreter = getInterpreterByVersion(version)

        val output = ByteArrayOutputStream()
        val input = provider?.input("")
        if (!input.isNullOrBlank()) {
            System.setIn(input.byteInputStream())
            System.setOut(PrintStream(output))
        }

        try {
            while (tokenProvider.hasNextStatement()) {
                val statement = tokenProvider.readStatement()
                val ast = parser.generateAST(statement)
                if (ast != null) {
                    val interpreterResponse = interpreter.interpretAST(listOf(ast))
                    if (interpreterResponse is SuccessResponse) {
                        if (interpreterResponse.message != null) {
                            if (!input.isNullOrBlank()) {
                                emitter!!.print(output.toString().trim())
                            }
                            emitter!!.print((interpreterResponse).message!!.trim())
                        }
                    } else if (interpreterResponse is ErrorResponse) {
                        handler!!.reportError(interpreterResponse.message)
                    }
                } else {
                    handler!!.reportError("Invalid statement")
                }
            }
        } catch (e: Throwable) {
            handler!!.reportError(e.message)
        }

    }
}