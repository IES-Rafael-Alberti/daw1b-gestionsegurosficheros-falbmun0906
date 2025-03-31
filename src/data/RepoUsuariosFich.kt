package data

import model.Usuario

class RepoUsuariosFich(private val rutaArchivo: String,
                       private val fich: IUtilFicheros
) : RepoUsuarioMem(), ICargarUsuariosIniciales {

    override fun agregar(usuario: Usuario): Boolean {
        if (buscar(usuario.nombre) != null) {
            return false
        }
        if (fich.agregarLinea...)
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
        val lineas = fich.leearARchivo(rutaArchivo)

        if (lineas.isNotEmpty()) {
            usuarios.clear()
            for (linea in lineas) {
                val datos = linea.split(";")
                if (datos.size == 3) {
                    usuarios...
                }
            }
        }
    }
}