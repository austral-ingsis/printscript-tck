package adapter

import interpreter.InputAdapter
import interpreter.OutputAdapter
import java.io.InputStream
import Cli
import implementation.LexerSingleton
import interpreter.ErrorHandler
import interpreter.InputProvider
import interpreter.OutputAdapterJava
import parser.Parser
import reader.EnvFileReader
import strategy.InterpreterManagerImplStrategy
import variable.VariableMap

class Adapter {
    fun execute(src: InputStream, version: String, emitter: OutputAdapterJava, handler: ErrorHandler, provider: InputAdapter) {
        try {
            val lexer = LexerSingleton.getInstance(src)
            val tokens = lexer.getToken()
            val parser = Parser(tokens)
            val ast = parser.generateAST()

            val envVariableMap = EnvFileReader("cli/src/main/kotlin/.envTest").readEnvFile()
            val interpreter = InterpreterManagerImplStrategy(VariableMap(HashMap()), envVariableMap)
            emitter.print(interpreter.interpret(ast))
        } catch (e: Exception) {
            handler.reportError(e.message ?: "Unknown error")
        } catch (e: Error) {
            handler.reportError(e.message ?: "Unknown error")
        }

    }
}