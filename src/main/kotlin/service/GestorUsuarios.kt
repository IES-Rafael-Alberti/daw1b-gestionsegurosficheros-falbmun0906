package org.example.service

import org.example.data.IRepoUsuarios
import org.example.model.Perfil
import org.example.model.Usuario
import org.example.utils.IUtilSeguridad

class GestorUsuarios(private val repoUsuarios: IRepoUsuarios,
                     private val utilSeguridad: IUtilSeguridad
) : IServUsuarios {

    override fun iniciarSesion(nombre: String, clave: String): Perfil? {
        val usuario = repoUsuarios.buscar(nombre)
        if (usuario != null && clave == usuario.clave) {
            return usuario.perfil
        } else return null
    }

    override fun agregarUsuario(nombre: String, clave: String, perfil: Perfil): Boolean {
        val usuario = Usuario(nombre, utilSeguridad.encriptarClave((clave)), perfil)
        if (usuario in repoUsuarios.obtenerTodos())
            return false
        return repoUsuarios.agregar(usuario)
    }

    override fun eliminarUsuario(nombre: String): Boolean {
        return repoUsuarios.eliminar(nombre)
    }

    override fun cambiarClave(usuario: Usuario, nuevaClave: String): Boolean {
        return repoUsuarios.cambiarClave(usuario, nuevaClave)
    }

    override fun buscarUsuario(nombre: String): Usuario? {
        return repoUsuarios.buscar(nombre)
    }

    override fun consultarTodos(): List<Usuario> {
        return repoUsuarios.obtenerTodos()
    }

    override fun consultarPorPerfil(perfil: Perfil): List<Usuario> {
        return repoUsuarios.obtener(perfil)
    }
}