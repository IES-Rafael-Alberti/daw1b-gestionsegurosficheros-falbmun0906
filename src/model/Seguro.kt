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
        return "$numPoliza$separador$dniTitular$separador$importe"
    }

    override fun hashCode(): Int {
        return numPoliza
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val seguro = other as Seguro
        return numPoliza == seguro.numPoliza
    }
}