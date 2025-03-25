package model

class SeguroVida(numPoliza: Int,
                 dniTitular: String,
                 importe: Double,
                 val fechaNac: String,
                 val nivelRiesgo: Riesgo,
                 val indemnizacion: Double
) : Seguro(generateId(), dniTitular, importe) {

    companion object {
        private var lastId = 800000
        private fun generateId() = lastId++
    }

    override fun calcularImporteAnioSiguiente(interes: Double): Double {
        val ajusteInteres =
            when (nivelRiesgo) {
            Riesgo.BAJO -> 2.0
            Riesgo.MEDIO -> 5.0
            Riesgo.ALTO -> 10.0
        }
        return importe * (1 + ajusteInteres / 100)
    }

    override fun tipoSeguro() = "SeguroVida"

    override fun serializar(separador: String): String {
        return "${super.serializar(separador)}$separador$importe$separador$fechaNac$separador$nivelRiesgo$separador$indemnizacion$separador${tipoSeguro()}"
    }

}
