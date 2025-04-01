package service

import data.IRepoUsuarios
import model.Perfil
import model.Usuario
import utils.IUtilSeguridad

class GestorUsuarios(private val repoUsuarios: IRepoUsuarios,
                     private val utilSeguridad: IUtilSeguridad
) : IServUsuarios, IUtilSeguridad {

    override fun iniciarSesion(nombre: String, clave: String): Perfil? {
        repoUsuarios.buscar(nombre)
    }

    override fun agregarUsuario(nombre: String, clave: String, perfil: Perfil): Boolean {
        TODO("Not yet implemented")
    }

    override fun eliminarUsuario(nombre: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun cambiarClave(usuario: Usuario, nuevaClave: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun buscarUsuario(nombre: String): Usuario? {
        TODO("Not yet implemented")
    }

    override fun consultarTodos(): List<Usuario> {
        TODO("Not yet implemented")
    }

    override fun consultarPorPerfil(perfil: Perfil): List<Usuario> {
        TODO("Not yet implemented")
    }
}