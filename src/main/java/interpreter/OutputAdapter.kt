package interpreter

class OutputAdapter(private val printEmitter: PrintEmitter): PrintEmitterNuestro {
    override fun print(string: String) {
        printEmitter.print(string)
    }
}