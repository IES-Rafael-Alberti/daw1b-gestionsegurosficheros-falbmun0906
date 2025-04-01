package data

import model.Seguro
import model.SeguroAuto
import model.SeguroHogar
import model.SeguroVida
import utils.IUtilFicheros
import utils.utilFicheros
import java.io.File

class RepoSegurosFich(private val rutaArchivo: String,
                      private val fich: IUtilFicheros
): RepoSegurosMem(), ICargarSegurosIniciales {

    override fun agregar(seguro: Seguro): Boolean {
        if (!super.agregar(seguro)) {
            return false
        }
        return utilFicheros.agregarLinea(rutaArchivo, seguro.serializar())
    }

    override fun eliminar(seguro: Seguro): Boolean{
        if (!super.eliminar(seguro)) {
            return false
        }
        return utilFicheros.escribirArchivo(rutaArchivo, obtenerTodos().map { it.serializar() })
    }

    override fun cargarSeguros(mapa: Map<String, (List<String>) -> Seguro>): Boolean {
        val archivo = File(rutaArchivo)

        if (archivo.exists() && archivo.isFile) {
            val listaStrings = archivo.readLines()

            for (linea in listaStrings) {
                val datos = linea.split(";")

                try {
                    require(datos.size >= 2)
                } catch (e: IllegalArgumentException) {
                    return false
                }

                val tipoSeguro = datos.last()
                val constructorSeguro = mapa[tipoSeguro] ?: return false

                val seguro = constructorSeguro(datos.dropLast(1))
                seguros.add(seguro)
            }

            actualizarContadores(seguros)
            return true
        }
        return false
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