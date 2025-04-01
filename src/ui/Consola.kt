package ui

class Consola : IEntradaSalida {

    override fun mostrar(msj: String, salto: Boolean, pausa: Boolean) {
        print("$msj${if (salto) "\n" else ""}")
        if (pausa) pausar()

    }

    override fun mostrarError(msj: String, pausa: Boolean) {
        val msjError = if (!msj.startsWith("ERROR -")) "ERROR - $msj" else msj
        mostrar(msjError, pausa = pausa)
    }

    override fun pedirInfo(msj: String): String {
        if (msj.isNotEmpty()) {
            mostrar(msj, false)
        }
        return readln().trim()
    }

    override fun pedirInfo(msj: String, error: String, debeCumplir: (String) -> Boolean): String {
        val valor = pedirInfo(msj)
        require(debeCumplir(valor)) { error }
        return valor
    }

    override fun pedirDouble(
        prompt: String,
        error: String,
        errorConv: String,
        debeCumplir: (Double) -> Boolean
    ): Double {
        TODO("Not yet implemented")
    }

    override fun pedirEntero(prompt: String, error: String, errorConv: String, debeCumplir: (Int) -> Boolean): Int {
        TODO("Not yet implemented")
    }

    override fun pedirInfoOculta(prompt: String): String {
        TODO("Not yet implemented")
    }

    override fun pausar(msj: String) {
        TODO("Not yet implemented")
    }

    override fun limpiarPantalla(numSaltos: Int) {
        TODO("Not yet implemented")
    }

    fun validarSiNo(valor: String): Boolean {
        return valor.lowercase() in listOf("s", "n")
    }

    override fun preguntar(mensaje: String): Boolean {
        var respuesta: String? = null
        do {
            if (respuesta != null) mostrarError("Respuesta incorrecta. Inténtelo de nuevo...")
            respuesta = pedirInfo("$mensaje (s/n: ").lowercase()
        } while (respuesta.toString() !in listOf("s", "n"))
        return respuesta =="s"
    }

    fun preguntar2(mensaje: String): Boolean {
        var respuesta : String
        do {
            respuesta = try {
                pedirInfo("$mensaje (s/n): ", "Respuesta incorrecta. Inténtelo de nuevo...") {
                    it.lowercase() in listOf("s", "n")
                }

                // pedirInfo("$mensaje (s/n): ", "Respuesta incorrecta. Inténtelo de nuevo...", ::validarSiNo)

            } catch (e: IllegalArgumentException) {
                mostrarError(e.message.toString())
                ""
            }
        } while (respuesta.isEmpty())
        return respuesta =="s"
    }
}