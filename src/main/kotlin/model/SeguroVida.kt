package org.example.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SeguroVida : Seguro {

    val fechaNac: LocalDate
    val nivelRiesgo: Riesgo
    val indemnizacion: Double

    companion object {
        var numPolizasVida = 800000
        private fun generateId() = numPolizasVida++

        fun crearSeguro(datos: List<String>): SeguroVida {
            val numPoliza = datos[0].toInt()
            val dniTitular = datos[1]
            val importe = datos[2].toDouble()
            val fechaNac = LocalDate.parse(datos[3], DateTimeFormatter.ofPattern("dd/MM/aaaa"))
            val nivelRiesgo = Riesgo.getRiesgo(datos[4])
            val indemnizacion = datos[5].toDouble()

            return SeguroVida(numPoliza, dniTitular, importe, fechaNac, nivelRiesgo, indemnizacion)
        }
    }

    constructor(dniTitular: String, importe: Double, fechaNac: LocalDate, nivelRiesgo: Riesgo, indemnizacion: Double) :
            super(numPoliza = SeguroVida.generateId(), dniTitular, importe) {
        this.fechaNac = fechaNac
        this.nivelRiesgo = nivelRiesgo
        this.indemnizacion = indemnizacion
    }

    private constructor(numPoliza: Int, dniTitular: String, importe: Double, fechaNac: LocalDate, nivelRiesgo: Riesgo, indemnizacion: Double) :
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
