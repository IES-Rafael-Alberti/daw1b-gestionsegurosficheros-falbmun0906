package data

import model.Perfil
import model.Usuario

open class RepoUsuarioMem : IRepoUsuarios {
    protected val usuarios = mutableListOf<Usuario>()
    override fun agregar(usuario: Usuario): Boolean {
        TODO("Not yet implemented")
    }

    override fun buscar(nombreUsuario: String): Usuario? {
        TODO("Not yet implemented")
    }

    override fun eliminar(usuario: Usuario): Boolean {
        TODO("Not yet implemented")
    }

    override fun eliminar(nombreUsuario: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun obtenerTodos(): List<Usuario> {
        TODO("Not yet implemented")
    }

    override fun obtener(perfil: Perfil): List<Usuario> {
        TODO("Not yet implemented")
    }

    override fun cambiarClave(usuario: Usuario, nuevaClave: String): Boolean {
        TODO("Not yet implemented")
    }

    // agregar(suuario: Usuario): Boolean -> if buscar(usuario.nombre) {}
    // buscar con find
    // eliminar con remove
    //eliminar (nombreUsuario)
    // obtener todos: return usuarios
    // obtener(perfil: Perfil): List<Usuarios> -> return usuarios.filter { it.perfil == perfil }
    // fun cambiarClave(usuario: Usuario, nuevaClave: String): Boolean -> usaurio.cambiarClave(nuevaClave) return true
}