package adapter

import interpreter.InputAdapter
import interpreter.OutputAdapter
import java.io.InputStream
import Cli
import InterpreterFactory
import emitter.PrinterEmitter
import implementation.LexerSingleton
import interpreter.ErrorHandler
import interpreter.InputProvider
import interpreter.OutputAdapterJava
import parser.Parser
import reader.EnvFileReader
import variable.VariableMap

class Adapter {
    fun execute(src: InputStream, version: String, emitter: OutputAdapterJava, handler: ErrorHandler, provider: InputAdapter) {
        try {
            val lexer = LexerSingleton.getInstance(src)
            val tokens = lexer.getToken()
            val parser = Parser(tokens)
            val ast = parser.generateAST()
            val envVariableMap = EnvFileReader("cli/src/main/kotlin/.envTest").readEnvFile()
            val interpreter = InterpreterFactory(version , VariableMap(HashMap()), envVariableMap, provider).buildInterpreter()
            val interpretedList = interpreter.interpret(ast)
            for (interpreted in interpretedList.second) {
                emitter.print(interpreted)
            }
        } catch (e: Exception) {
            handler.reportError(e.message)
        } catch (e: Error) {
            handler.reportError(e.message)
        }
    }
}