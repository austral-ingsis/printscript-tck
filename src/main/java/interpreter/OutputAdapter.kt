package interpreter

import ingsis.utils.OutputEmitter

class OutputAdapter(private val printEmitter: PrintEmitter): OutputEmitter {
    override fun print(string: String) {
        printEmitter.print(string)
    }
}