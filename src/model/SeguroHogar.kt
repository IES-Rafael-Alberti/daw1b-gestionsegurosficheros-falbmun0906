package model

class SeguroHogar(numPoliza: Int,
                  dniTitular: String,
                  importe: Double,
                  val metrosCuadrados: Int,
                  val valorContenido: Double,
                  val direccion: String
) : Seguro(generateId(), dniTitular, importe) {


    companion object {
        private var lastId = 100000
        private fun generateId() = lastId++
    }

    override fun calcularImporteAnioSiguiente(interes: Double): Double {
        return importe * (1 + interes / 100)
    }

    override fun tipoSeguro() = "SeguroHogar"

    override fun serializar(separador: String): String {
        return "${super.serializar(separador)};$importe;$metrosCuadrados;$valorContenido;$direccion;${tipoSeguro()}"
    }

}