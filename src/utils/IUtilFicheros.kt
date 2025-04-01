package utils

import model.Usuario

interface IUtilFicheros {
    abstract fun leerArchivo(rutaArchivo: String): Any
    abstract fun escribirArchivo(rutaArchivo: String, filter: List<Usuario>): Boolean
    abstract fun existeFichero(rutaArchivo: String): Boolean
}