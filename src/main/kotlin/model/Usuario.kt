package org.example.model

class Usuario (val nombre: String,
               clave: String,
               val perfil: Perfil
) : IExportable {

    var clave: String = clave
        protected set

    companion object {
        fun crearUsuario(datos: List<String>): Usuario {
            return Usuario(
                datos[0],
                datos[1],
                Perfil.getPerfil(datos[2]))
        }
    }

    fun cambiarClave(nuevaClaveEncriptada: String) {
        clave = nuevaClaveEncriptada
    }

    override fun serializar(separador: String): String {
        return "$nombre$separador$clave$separador$perfil"
    }

    override fun toString(): String {
        return "Usuario (nombre='$nombre', clave='$clave', perfil='$perfil')"
    }
}
