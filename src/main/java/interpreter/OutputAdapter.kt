package interpreter

class OutputAdapter(private val printEmitter: PrintEmitter): PrintEmitter {
    override fun print(string: String) {
        printEmitter.print(string)
    }
}