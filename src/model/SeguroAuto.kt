package model

class SeguroAuto(numPoliza: Int,
                 dniTitular: String,
                 importe: Double,
                 val descripcion: String,
                 val combustible: String,
                 val tipoAuto: Auto,
                 val tipoCobertura: Cobertura,
                 val asistenciaCarretera: Boolean,
                 val numPartes: Int
) : Seguro(generateId(), dniTitular, importe) {

    // val tipoCobertura: Cobertura = Cobertura.getCobertura(tipoCobertura)

    /*constructor(numPoliza: Int, dniTitular: String, importe: Double) super(numPoliza, dniTitular, importe) {

    }*/
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
        return "${super.serializar(separador)}$separador\"$descripcion\"$separador$combustible;$tipoAuto;${tipoCobertura.desc}$separador$asistenciaCarretera$separador$numPartes$separador${tipoSeguro()}"
    }
}