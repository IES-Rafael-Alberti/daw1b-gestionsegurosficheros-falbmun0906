package model

class Usuario private constructor (val nombre: String, var clave: String, val perfil: Perfil) : IExportable {

    companion object {
        fun crearUsuario(datos: List<String>): Usuario {
            return Usuario(datos[0], datos[1], Perfil.getPerfil(datos[2]))
        }
    }

    fun cambiarClave(nuevaClaveEncriptada: String) {
        clave = nuevaClaveEncriptada
    }

    override fun serializar(separador: String): String {
        TODO("Not yet implemented")
    }
}
