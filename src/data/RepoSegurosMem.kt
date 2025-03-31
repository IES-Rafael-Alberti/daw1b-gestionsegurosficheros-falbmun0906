package data

import model.Seguro
import model.Usuario

open class RepoSegurosMem() : IRepoSeguros {

    protected val seguros = mutableListOf<Seguro>()

    override fun agregar(seguro: Seguro): Boolean {
        TODO("Not yet implemented")
    }

    override fun buscar(numPoliza: Int): Seguro? {
        TODO("Not yet implemented")
    }

    override fun eliminar(seguro: Seguro): Boolean {
        TODO("Not yet implemented")
    }

    override fun eliminar(numPoliza: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun obtenerTodos(): List<Seguro> {
        TODO("Not yet implemented")
    }

    override fun obtener(tipoSeguro: String): List<Seguro> {
        TODO("Not yet implemented")
    }
}