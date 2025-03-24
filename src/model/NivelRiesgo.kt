package model

enum class NivelRiesgo(val interesAplicado: Double) {
    BAJO(2.0), MEDIO(5.0), ALTO(10.0);

    companion object {
        fun getRiesgo(valor: String): NivelRiesgo {
            return entries.find { it.name.equals(valor, ignoreCase = true) } ?: MEDIO
        }
    }
}
