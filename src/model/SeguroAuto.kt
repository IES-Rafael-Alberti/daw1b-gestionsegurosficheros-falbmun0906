package model

class SeguroAuto(numPoliza: Int,
                 dniTitular: String,
                 importe: Double,
                 val descripcion: String,
                 val combustible: String,
                 val tipoAuto: Auto,
                 tipoCobertura: String,
                 val asistenciaCarretera: Boolean,
                 val numPartes: Int
) : Seguro(generateId(), dniTitular, importe) {

    val tipoCobertura: Cobertura = Cobertura.getCobertura(tipoCobertura)

    companion object {
        private var lastId = 400000
        private fun generateId() = lastId++
    }

    override fun calcularImporteAnioSiguiente(interes: Double): Double {
        val ajusteInteres = interes + (2 * numPartes)
        return importe * (1 + ajusteInteres / 100)
    }

    override fun tipoSeguro() = "SeguroAuto"

    override fun serializar(separador: String): String {
        return "${super.serializar(separador)};\"$descripcion\";$combustible;$tipoAuto;$tipoCobertura.desc;$asistenciaCarretera;$numPartes;${tipoSeguro()}"
    }
}