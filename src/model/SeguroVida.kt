package model

class SeguroVida(numPoliza: Int,
                 dniTitular: String,
                 importe: Double,
                 val fechaNac: String,
                 val nivelRiesgo: NivelRiesgo,
                 val indemnizacion: Double
) : Seguro(generateId(), dniTitular, importe) {

    companion object {
        private var lastId = 800000
        private fun generateId() = lastId++
    }

    override fun calcularImporteAnioSiguiente(interes: Double): Double {
        val ajusteInteres =
            when (nivelRiesgo) {
            NivelRiesgo.BAJO -> 2.0
            NivelRiesgo.MEDIO -> 5.0
            NivelRiesgo.ALTO -> 10.0
        }
        return getImporte() * (1 + ajusteInteres / 100)
    }

    override fun tipoSeguro() = "SeguroVida"

    override fun serializar(): String {
        return "$numPoliza;$dniTitular;${getImporte()};$fechaNac;$nivelRiesgo;$indemnizacion;${tipoSeguro()}"
    }
}
