package data

import model.Usuario
import utils.IUtilFicheros
import utils.utilFicheros
import java.io.File

class RepoUsuariosFich(private val rutaArchivo: String,
                       private val fich: IUtilFicheros
) : RepoUsuarioMem(), ICargarUsuariosIniciales {

    override fun agregar(usuario: Usuario): Boolean {
        if (!super.agregar(usuario)) {
            return false
        }
        return utilFicheros.agregarLinea(rutaArchivo, usuario.serializar())
    }

    override fun eliminar(usuario: Usuario): Boolean {
        if (fich.escribirArchivo(rutaArchivo, usuarios.filter { it != usuario })) {
            return super.eliminar(usuario)
        }
        return false
    }

    override fun cambiarClave(usuario: Usuario, nuevaClave: String): Boolean {
        usuario.cambiarClave(nuevaClave)
        return fich.escribirArchivo(rutaArchivo, usuarios)
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