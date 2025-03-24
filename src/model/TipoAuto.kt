package model

enum class TipoAuto {
    COCHE, MOTO, CAMION;

    fun getAuto(valor: String): TipoAuto {
        return values().find { it.name.equals(valor, ignoreCase = true) } ?: COCHE
    }
}