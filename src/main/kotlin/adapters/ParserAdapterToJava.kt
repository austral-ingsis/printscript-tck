package adapters

import ast.AST
import ast.Scope
import parser.MyParser
import token.TokenInfo

class ParserAdapterToJava {
    val parser: MyParser = MyParser()

    fun parseTokens(tokens: List<TokenInfo>) : ParserResult {
        val result : Result<Scope> = parser.parseTokens(tokens)
        return ParserResult(result.isSuccess, result.getOrNull(), result.exceptionOrNull())
    }
}

class ParserResult(val success: Boolean, val ast: AST?, val err: Throwable?)
