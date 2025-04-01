package model

class SeguroAuto : Seguro {

/*    numPoliza: Int,
    dniTitular: String,
    importe: Double,*/

    val descripcion: String
    val combustible: String
    val tipoAuto: Auto
    val tipoCobertura: Cobertura
    val asistenciaCarretera: Boolean
    val numPartes: Int


    // val tipoCobertura: Cobertura = Cobertura.getCobertura(tipoCobertura)

    /*constructor(numPoliza: Int, dniTitular: String, importe: Double) super(numPoliza, dniTitular, importe) {

    }*/
    companion object {
        private var numPolizasAuto = 400000
        private fun generateId() = numPolizasAuto++
    }

    constructor(dniTitular: String, importe: Double, descripcion: String, combustible: String, tipoAuto: Auto, tipoCobertura: Cobertura, asistenciaCarretera: Boolean, numPartes: Int) :
            super(numPoliza = SeguroAuto.generateId(), dniTitular, importe) {
        this.descripcion = descripcion
        this.combustible = combustible
        this.tipoAuto = tipoAuto
        this.tipoCobertura = tipoCobertura
        this.asistenciaCarretera = asistenciaCarretera
        this.numPartes = numPartes
    }

    private constructor(numPoliza: Int, dniTitular: String, importe: Double, descripcion: String, combustible: String, tipoAuto: Auto, tipoCobertura: Cobertura, asistenciaCarretera: Boolean, numPartes: Int) :
            super(numPoliza, dniTitular, importe) {
        this.descripcion = descripcion
        this.combustible = combustible
        this.tipoAuto = tipoAuto
        this.tipoCobertura = tipoCobertura
        this.asistenciaCarretera = asistenciaCarretera
        this.numPartes = numPartes
    }

    override fun calcularImporteAnioSiguiente(interes: Double): Double {
        val ajusteInteres = interes + (2 * numPartes)
        return importe * (1 + ajusteInteres / 100)
    }

    override fun serializar(separador: String): String {
        return "${super.serializar(separador)}$separador\"$descripcion\"$separador$combustible;$tipoAuto;${tipoCobertura.desc}$separador$asistenciaCarretera$separador$numPartes$separador${tipoSeguro()}"
    }
}