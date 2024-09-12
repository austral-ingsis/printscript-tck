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
import java.nio.charset.Charset

class Adapter {
    fun execute(src: InputStream, version: String, emitter: OutputAdapterJava, handler: ErrorHandler, provider: InputAdapter) {
        try {
            val versionController = LexerVersionController()
            val lexer = versionController.getLexer(version, src)

            val variableMap = VariableMap(HashMap())

            var currentStatement = mutableListOf<Token>()
            var token = lexer.getNextToken()

            var openBrackets = 0

            while (token != null) {
                currentStatement.add(token)

                if (token.type == TokenType.OPEN_BRACKET) {
                    openBrackets++
                } else if (token.type == TokenType.CLOSE_BRACKET) {
                    openBrackets--
                } else if (token.type == TokenType.SEMICOLON && openBrackets == 0) {
                    processStatement(currentStatement, version, emitter, provider, variableMap)
                    currentStatement.clear()
                }

                token = lexer.getNextToken()
            }

// Procesar cualquier statement restante que no termine en ';'
            if (currentStatement.isNotEmpty() && openBrackets == 0) {
                processStatement(currentStatement, version, emitter, provider, variableMap)
            }
        } catch (e: Exception) {
            handler.reportError(e.message)
        } catch (e: OutOfMemoryError) {
            handler.reportError(e.message)
        }
    }

    private fun processStatement(statementTokens: List<Token>, version: String, emitter: OutputAdapterJava, provider: InputAdapter, variableMap: VariableMap) {
        val parser = Parser(statementTokens)
        val ast = parser.generateAST()
        val interpreter = InterpreterFactory(version, variableMap, provider).buildInterpreter()
        val interpretedList = interpreter.interpret(ast)
        for (interpreted in interpretedList.second) {
            emitter.print(interpreted)
        }
    }


}