package model

abstract class Seguro(val numPoliza: Int,
                      private val dniTitular: String,
                      protected var importe: Double) : IExportable {

    abstract fun calcularImporteAnioSiguiente(interes: Double): Double
    abstract fun tipoSeguro(): String

    fun getDniTitular(): String {
        return dniTitular
    }

    override fun serializar(separador: String): String {
        return "$numPoliza;$dniTitular;$importe"
    }
}