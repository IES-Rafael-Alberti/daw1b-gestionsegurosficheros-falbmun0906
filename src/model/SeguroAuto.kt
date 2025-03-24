package model

class SeguroAuto(numPoliza: Int,
                 dniTitular: String,
                 importe: Double,
                 val descripcion: String,
                 val combustible: String,
                 val tipoAuto: TipoAuto,
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
        return getImporte() * (1 + ajusteInteres / 100)
    }

    override fun tipoSeguro() = "SeguroAuto"

    override fun serializar(): String {
        return "$numPoliza;$dniTitular;${getImporte()};\"$descripcion\";$combustible;$tipoAuto;$tipoCobertura.desc;$asistenciaCarretera;$numPartes;${tipoSeguro()}"
    }
}