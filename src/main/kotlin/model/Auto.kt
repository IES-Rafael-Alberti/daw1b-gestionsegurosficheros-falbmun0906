package org.example.model

enum class Auto {
    COCHE, MOTO, CAMION;

    companion object {
        fun getAuto(valor: String): Auto {
            return entries.find { it.name.equals(valor, ignoreCase = true) } ?: COCHE
        }
    }

}