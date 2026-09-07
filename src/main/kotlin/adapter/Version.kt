package adapter

// El TCK pasa la version como String y en runtime. Convertirla a un tipo es lo
// primero que hace cada adaptador: despues de esto el when es exhaustivo y el
// compilador avisa si aparece una version que no se contempla.
enum class Version(val id: String) {
    V10("1.0"),
    V11("1.1"),
    ;

    companion object {
        // Null y no excepcion: una version desconocida es un dato invalido que
        // llega de afuera, no un bug. Cada adaptador decide como reportarlo.
        fun of(id: String): Version? = entries.firstOrNull { it.id == id }
    }
}
