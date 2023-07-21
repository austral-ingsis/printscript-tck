package implementation

import consumer.*
import interpreter.ErrorHandler
import interpreter.InputProvider
import interpreter.PrintEmitter

class InterpreterRunner {
    fun runConsumerInterpreter(consumer: ASTNodeConsumerInterpreter, emitter: PrintEmitter, handler: ErrorHandler,  provider: InputProvider) {
        var result = consumer.consume()
        while (result !is ConsumerResponseEnd) {
            when (result) {
                is ConsumerResponseSuccess -> {
                    if (result.msg != null) {
                        emitter.print(result.msg!!)
                    }
                }

                is ConsumerResponseError -> {
                    handler.reportError(result.error)
                    return
                }

                is ConsumerResponseInput -> {
                    emitter.print(result.msg)
                    val input: String = provider.input(result.msg)
                    consumer.getValue(input)
                }
            }
            result = consumer.consume()
        }
    }
}