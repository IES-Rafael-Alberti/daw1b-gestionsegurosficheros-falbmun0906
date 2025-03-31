package data

import model.Seguro
import model.SeguroAuto
import model.SeguroHogar
import model.SeguroVida
import utils.IUtilFicheros

class RepoSegurosFich(private val rutaArchivo: String,
                      private val fich: IUtilFicheros
): RepoSegurosMem(), ICargarUsuariosIniciales {

    override fun cargarUsuarios(): Boolean {
        TODO("Not yet implemented")
    }

    override fun agregar(seguro: Seguro): Boolean {
        TODO()
    }

    override fun eliminar(seguro: Seguro): Boolean{
        TODO()
    }

    fun cargarSeguros(mapa: Map<String, (List<String>) -> Seguro>): Boolean {
        TODO()
    }

    private fun actualizarContadores(seguros: List<Seguro>) {
        // Actualizar los contadores de polizas del companion object según el tipo de seguro
        val maxHogar = seguros.filter { it.tipoSeguro() == "SeguroHogar" }.maxOfOrNull { it.numPoliza }
        val maxAuto = seguros.filter { it.tipoSeguro() == "SeguroAuto" }.maxOfOrNull { it.numPoliza }
        val maxVida = seguros.filter { it.tipoSeguro() == "SeguroVida" }.maxOfOrNull { it.numPoliza }

        if (maxHogar != null) SeguroHogar.numPolizasHogar = maxHogar
        if (maxAuto != null) SeguroAuto.numPolizasAuto = maxAuto
        if (maxVida != null) SeguroVida.numPolizasVida = maxVida
    }



}