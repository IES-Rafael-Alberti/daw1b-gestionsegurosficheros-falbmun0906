package model

class SeguroVida : Seguro {

    val fechaNac: String
    val nivelRiesgo: Riesgo
    val indemnizacion: Double

    // Parsear fecha. DateTimeFormatter.ofPattern("dd/MM/aaaa")

    companion object {
        private var numPolizasVida = 800000
        private fun generateId() = numPolizasVida++
    }

    constructor(dniTitular: String, importe: Double, fechaNac: String, nivelRiesgo: Riesgo, indemnizacion: Double) :
            super(numPoliza = SeguroVida.generateId(), dniTitular, importe) {
        this.fechaNac = fechaNac
        this.nivelRiesgo = nivelRiesgo
        this.indemnizacion = indemnizacion
    }

    private constructor(numPoliza: Int, dniTitular: String, importe: Double, fechaNac: String, nivelRiesgo: Riesgo, indemnizacion: Double) :
            super(numPoliza, dniTitular, importe) {
        this.fechaNac = fechaNac
        this.nivelRiesgo = nivelRiesgo
        this.indemnizacion = indemnizacion
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

    override fun serializar(separador: String): String {
        return "${super.serializar(separador)}$separador$importe$separador$fechaNac$separador$nivelRiesgo$separador$indemnizacion$separador${tipoSeguro()}"
    }
}
