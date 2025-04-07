package org.example.data


import org.example.model.Seguro
import org.example.model.SeguroAuto
import org.example.model.SeguroHogar
import org.example.model.SeguroVida
import org.example.utils.IUtilFicheros
import java.io.File

class RepoSegurosFich(private val rutaArchivo: String,
                      private val fich: IUtilFicheros
): RepoSegurosMem(), ICargarSegurosIniciales {

    override fun agregar(seguro: Seguro): Boolean {
        if (!super.agregar(seguro)) {
            return false
        }
        return fich.agregarLinea(rutaArchivo, seguro.serializar())
    }

    override fun eliminar(seguro: Seguro): Boolean {
        if (fich.escribirArchivo(rutaArchivo, seguros.filter { it != seguro })) {
            return super.eliminar(seguro)
        }
        return false
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