package model

enum class Auto {
    COCHE, MOTO, CAMION;

    fun getAuto(valor: String): Auto {
        return entries.find { it.name.equals(valor, ignoreCase = true) } ?: COCHE
    }
}