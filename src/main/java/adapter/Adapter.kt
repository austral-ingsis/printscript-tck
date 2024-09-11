package adapter

import InterpreterFactory
import controller.LexerVersionController
import interpreter.ErrorHandler
import interpreter.InputAdapter
import interpreter.OutputAdapterJava
import parser.Parser
import token.Token
import token.TokenType
import variable.VariableMap
import java.io.InputStream

class Adapter {
    fun execute(src: InputStream, version: String, emitter: OutputAdapterJava, handler: ErrorHandler, provider: InputAdapter) {
        try {
            val versionController = LexerVersionController()
            val lexer = versionController.getLexer(version, src)

            var currentStatement = mutableListOf<Token>()
            var token = lexer.getNextToken()

            while (token != null) {
                currentStatement.add(token)

                if (token.type == TokenType.SEMICOLON) {
                    processStatement(currentStatement, version, emitter, provider)
                    currentStatement.clear()
                }

                token = lexer.getNextToken()
            }

            // Procesar cualquier statement restante que no termine en ';'
            if (currentStatement.isNotEmpty()) {
                processStatement(currentStatement, version, emitter, provider)
            }
        } catch (e: Exception) {
            handler.reportError(e.message)
        } catch (e: Error) {
            handler.reportError(e.message)
        }
    }

    private fun processStatement(statementTokens: List<Token>, version: String, emitter: OutputAdapterJava, provider: InputAdapter) {
        val parser = Parser(statementTokens)
        val ast = parser.generateAST()
        val interpreter = InterpreterFactory(version , VariableMap(HashMap()), provider).buildInterpreter()
        val interpretedList = interpreter.interpret(ast)
        for (interpreted in interpretedList.second) {
            emitter.print(interpreted)
        }
    }
}