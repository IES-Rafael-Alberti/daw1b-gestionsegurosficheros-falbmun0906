package org.example.model

class SeguroHogar : Seguro {

    val metrosCuadrados: Int
    val valorContenido: Double
    val direccion: String
    val aniosConstruccion: Int

    companion object {
        var numPolizasHogar = 100000
        private fun generateId() = numPolizasHogar++

        fun crearSeguro(datos: List<String>): SeguroHogar {
            val numPoliza = datos[0].toInt()
            val dniTitular = datos[1]
            val importe = datos[2].toDouble()
            val metrosCuadrados = datos[3].toInt()
            val valorContenido = datos[4].toDouble()
            val direccion = datos[5]
            val aniosConstruccion = datos[6].toInt()

            return SeguroHogar(numPoliza, dniTitular, importe, metrosCuadrados, valorContenido, direccion, aniosConstruccion)
        }
    }

    constructor(dniTitular: String, importe: Double, metrosCuadrados: Int, valorContenido: Double, direccion: String, aniosConstruccion: Int) :
            super(numPoliza = generateId(), dniTitular, importe) {
        this.metrosCuadrados = metrosCuadrados
        this.valorContenido = valorContenido
        this.direccion = direccion
        this.aniosConstruccion = aniosConstruccion
    }

    private constructor(numPoliza: Int, dniTitular: String, importe: Double, metrosCuadrados: Int, valorContenido: Double, direccion: String, aniosConstruccion: Int) :
            super(numPoliza, dniTitular, importe) {
        this.metrosCuadrados = metrosCuadrados
        this.valorContenido = valorContenido
        this.direccion = direccion
        this.aniosConstruccion = aniosConstruccion
    }

    override fun calcularImporteAnioSiguiente(interes: Double): Double {
        return importe * (1 + interes / 100)
    }

    override fun serializar(separador: String): String {
        return "${super.serializar(separador)}$separador$importe$separador$metrosCuadrados$separador$valorContenido$separador$direccion$separador${tipoSeguro()}"
    }

}