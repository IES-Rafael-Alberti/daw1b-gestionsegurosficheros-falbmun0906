package org.example.data

import org.example.model.Usuario
import org.example.utils.IUtilFicheros
import java.io.File

class RepoUsuariosFich(private val rutaArchivo: String,
                       private val fich: IUtilFicheros
) : RepoUsuariosMem(), ICargarUsuariosIniciales {

    override fun agregar(usuario: Usuario): Boolean {
        if (!super.agregar(usuario)) {
            return false
        }
        return fich.agregarLinea(rutaArchivo, usuario.serializar())
    }

    override fun eliminar(usuario: Usuario): Boolean {
        if (fich.escribirArchivo<Usuario>(rutaArchivo, usuarios.filter { it != usuario }.map { it.serializar() })) {
            return super.eliminar(usuario)
        }
        return false
    }

    override fun cambiarClave(usuario: Usuario, nuevaClave: String): Boolean {
        usuario.cambiarClave(nuevaClave)
        return fich.escribirArchivo<Usuario>(rutaArchivo, usuarios.map { it.serializar() })
    }

    override fun cargarUsuarios(): Boolean {
        val archivo = File(rutaArchivo)

        if (archivo.exists() && archivo.isFile) {
            val listaStrings = archivo.readLines()

            for (linea in listaStrings) {
                val datos = linea.split(";")

                try {
                    require(datos.size == 3)
                } catch (e: IllegalArgumentException) {
                    return false
                }

                val usuario = Usuario.crearUsuario(datos)
                usuarios.add(usuario)
            }
            return true
        }
        return false
    }
}