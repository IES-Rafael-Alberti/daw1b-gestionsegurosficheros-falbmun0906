package org.example.utils

import org.example.model.IExportable
import java.io.File
import java.io.IOException

class Ficheros : IUtilFicheros {

    override fun leerArchivo(ruta: String): List<String> {
        return try {
            val archivo = File(ruta)
            if (!archivo.exists()) {
                throw IOException("El archivo no existe: $ruta")
            }
            archivo.readLines()
        } catch (e: Exception) {
            throw Exception("Se produjo un error al leer el archivo \"$ruta\": ${e.message}")
        }
    }

    override fun agregarLinea(ruta: String, linea: String): Boolean {
        return try {
            val archivo = File(ruta)
            archivo.appendText("$linea\n")
            true
        } catch (e: IOException) {
            throw e
        }
    }

    override fun <T: IExportable> escribirArchivo(ruta: String, elementos: List<T>): Boolean {
        val archivo = File(ruta)
        return try {
            val contenido = elementos.joinToString("\n") { it.serializar() }
            archivo.writeText(contenido)
            true
        } catch (e: IOException) {
            throw e
        }
    }

    override fun existeFichero(ruta: String): Boolean {
        return File(ruta).exists()
    }

    override fun existeDirectorio(ruta: String): Boolean {
        return File(ruta).isDirectory
    }
}