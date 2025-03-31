package model

abstract class Seguro(val numPoliza: Int,
                      private val dniTitular: String,
                      protected var importe: Double
) : IExportable {

    abstract fun calcularImporteAnioSiguiente(interes: Double): Double

    fun tipoSeguro(): String {
        return this::class.simpleName ?: "Desconocido"
    }

    fun getDniTitular(): String {
        return dniTitular
    }

    override fun serializar(separador: String): String {
        return "$numPoliza$separador$dniTitular$separador$importe"
    }

    override fun hashCode(): Int {
        return numPoliza.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Seguro) return false
        return numPoliza == other.numPoliza
    }
}