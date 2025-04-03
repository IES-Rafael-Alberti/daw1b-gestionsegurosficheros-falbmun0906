package org.example.ui

import jdk.internal.org.jline.reader.EndOfFileException
import jdk.internal.org.jline.reader.LineReaderBuilder
import jdk.internal.org.jline.reader.UserInterruptException
import jdk.internal.org.jline.terminal.TerminalBuilder

class Consola : IEntradaSalida {

    override fun mostrar(msj: String, salto: Boolean, pausa: Boolean) {
        print("$msj${if (salto) "\n" else ""}")
        if (pausa) pausar()

    }

    override fun mostrarError(msj: String, pausa: Boolean) {
        val msjError = if (!msj.startsWith("ERROR -")) "ERROR - $msj" else msj
        mostrar(msjError, pausa = pausa)
    }

    override fun pedirInfo(msj: String): String {
        if (msj.isNotEmpty()) {
            mostrar(msj, false)
        }
        return readln().trim()
    }

    override fun pedirInfo(msj: String, error: String, debeCumplir: (String) -> Boolean): String {
        val valor = pedirInfo(msj)
        require(debeCumplir(valor)) { error }
        return valor
    }

    override fun pedirDouble(
        prompt: String,
        error: String,
        errorConv: String,
        debeCumplir: (Double) -> Boolean
    ): Double {
        while (true) {
            val entrada = pedirInfo(prompt)
            val numero = entrada.toDoubleOrNull()

            if (numero == null) {
                mostrarError(errorConv, true)
                continue
            }

            if (debeCumplir(numero)) {
                return numero
            } else {
                mostrarError(error, true)
            }
        }
    }

    override fun pedirEntero(prompt: String, error: String, errorConv: String, debeCumplir: (Int) -> Boolean): Int {
        while (true) {
            val entrada = pedirInfo(prompt)
            val numero = entrada.toIntOrNull()

            if (numero == null) {
                mostrarError(errorConv, true)
                continue
            }

            if (debeCumplir(numero)) {
                return numero
            } else {
                mostrarError(error, true)
            }
        }
    }

    override fun pedirInfoOculta(prompt: String): String {
        return try {
            val terminal = TerminalBuilder.builder()
                .dumb(true) // Para entornos no interactivos como IDEs
                .build()

            val reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .build()

            reader.readLine(prompt, '*') // Oculta la contraseña con '*'
        } catch (e: UserInterruptException) {
            mostrarError("Entrada cancelada por el usuario (Ctrl + C).", pausa = false)
            ""
        } catch (e: EndOfFileException) {
            mostrarError("Se alcanzó el final del archivo (EOF ó Ctrl+D).", pausa = false)
            ""
        } catch (e: Exception) {
            mostrarError("Problema al leer la contraseña: ${e.message}", pausa = false)
            ""
        }
    }

    override fun pausar(msj: String) {
        mostrar(msj, true, false)
        readln()
    }

    override fun limpiarPantalla(numSaltos: Int) {
        if (System.console() != null) {
            mostrar("\u001b[H\u001b[2J", false)
            System.out.flush()
        } else {
            repeat(numSaltos) {
                mostrar("")
            }
        }
    }

    fun validarSiNo(valor: String): Boolean {
        return valor.lowercase() in listOf("s", "n")
    }

    override fun preguntar(mensaje: String): Boolean {
        var respuesta: String? = null
        do {
            if (respuesta != null) mostrarError("Respuesta incorrecta. Inténtelo de nuevo...")
            respuesta = pedirInfo("$mensaje (s/n: ").lowercase()
        } while (respuesta.toString() !in listOf("s", "n"))
        return respuesta =="s"
    }

    fun preguntar2(mensaje: String): Boolean {
        var respuesta : String
        do {
            respuesta = try {
                pedirInfo("$mensaje (s/n): ", "Respuesta incorrecta. Inténtelo de nuevo...") {
                    it.lowercase() in listOf("s", "n")
                }

                // pedirInfo("$mensaje (s/n): ", "Respuesta incorrecta. Inténtelo de nuevo...", ::validarSiNo)

            } catch (e: IllegalArgumentException) {
                mostrarError(e.message.toString())
                ""
            }
        } while (respuesta.isEmpty())
        return respuesta =="s"
    }
}