package org.example.app

import org.example.model.*
import org.example.service.IServSeguros
import org.example.service.IServUsuarios
import org.example.ui.IEntradaSalida
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Clase encargada de gestionar el flujo de menús y opciones de la aplicación,
 * mostrando las acciones disponibles según el perfil del usuario autenticado.
 *
 * @property nombreUsuario Nombre del usuario que ha iniciado sesión.
 * @property perfilUsuario Perfil del usuario: admin, gestion o consulta.
 * @property ui Interfaz de usuario.
 * @property gestorUsuarios Servicio de operaciones sobre usuarios.
 * @property gestorSeguros Servicio de operaciones sobre seguros.
 */
class GestorMenu(
    val nombreUsuario: String,
    val perfilUsuario: String,
    val ui: IEntradaSalida,
    val gestorUsuarios: IServUsuarios,
    val gestorSeguros: IServSeguros
)
{

    /**
     * Inicia un menú según el índice correspondiente al perfil actual.
     *
     * @param indice Índice del menú que se desea mostrar (0 = principal).
     */
    fun iniciarMenu(indice: Int = 0) {
        val (opciones, acciones) = ConfiguracionesApp.obtenerMenuYAcciones(perfilUsuario, indice)
        ejecutarMenu(opciones, acciones)
    }

    /**
     * Formatea el menú en forma numerada.
     */
    private fun formatearMenu(opciones: List<String>): String {
        var cadena = ""
        opciones.forEachIndexed { index, opcion ->
            cadena += "${index + 1}. $opcion\n"
        }
        return cadena
    }

    /**
     * Muestra el menú limpiando pantalla y mostrando las opciones numeradas.
     */
    private fun mostrarMenu(opciones: List<String>) {
        ui.limpiarPantalla()
        ui.mostrar(formatearMenu(opciones), salto = false)
    }

    /**
     * Ejecuta el menú interactivo.
     *
     * @param opciones Lista de opciones que se mostrarán al usuario.
     * @param ejecutar Mapa de funciones por número de opción.
     */
    private fun ejecutarMenu(opciones: List<String>, ejecutar: Map<Int, (GestorMenu) -> Boolean>) {
        do {
            mostrarMenu(opciones)
            val opcion = ui.pedirInfo("Elige opción > ").toIntOrNull()
            if (opcion != null && opcion in 1..opciones.size) {
                // Buscar en el mapa las acciones a ejecutar en la opción de menú seleccionada
                val accion = ejecutar[opcion]
                // Si la accion ejecutada del menú retorna true, debe salir del menú
                if (accion != null && accion(this)) return
            }
            else {
                ui.mostrarError("Opción no válida!")
            }
        } while (true)
    }

    /** Crea un nuevo usuario solicitando los datos necesarios al usuario */
    fun nuevoUsuario() {
        val nombre = ui.pedirInfo("Introduce el nombre del nuevo usuario:")
        val clave = ui.pedirInfoOculta("Introduce la clave del nuevo usuario:")
        val perfil = ui.pedirInfo("Introduce el perfil del usuario (ADMIN/GESTION/CONSULTA):")
            .uppercase(Locale.getDefault())

        // Validamos que el perfil sea correcto
        if (perfil !in listOf("ADMIN", "GESTION", "CONSULTA")) {
            ui.mostrarError("Perfil inválido. Debe ser ADMIN, GESTION o CONSULTA.")
            return
        }

        gestorUsuarios.agregarUsuario(nombre, clave, Perfil.getPerfil(perfil))
        ui.mostrar("Usuario ${nombre} creado exitosamente.", true, false)
    }

    /** Elimina un usuario si existe */
    fun eliminarUsuario() {
        val nombreUsuario = ui.pedirInfo("Introduce el nombre del usuario a eliminar:")
        val usuario = gestorUsuarios.buscarUsuario(nombreUsuario)

        if (usuario != null) {
            gestorUsuarios.eliminarUsuario(nombreUsuario)
            ui.mostrar("Usuario $nombreUsuario eliminado exitosamente.", true, false)
        } else {
            ui.mostrarError("Usuario no encontrado.", true)
        }
    }

    /** Cambia la contraseña del usuario actual */
    fun cambiarClaveUsuario() {
        val usuarioActual =
        val nuevaClave = ui.pedirInfoOculta("Introduce la nueva clave para ${usuarioActual.nombre}:")

        gestorUsuarios.cambiarClave(usuarioActual, nuevaClave)
        ui.mostrar("Contraseña cambiada exitosamente.", true, false)
    }

    /**
     * Mostrar la lista de usuarios (Todos o filstrados por un perfil)
     */
    fun consultarUsuarios() {
        val perfil = ui.pedirInfo("Introduce el perfil de usuario a consultar (ADMIN, GESTION, CONSULTA o TODOS):").uppercase(Locale.getDefault())

        val usuariosFiltrados = if (perfil == "TODOS") {
            gestorUsuarios.consultarTodos()
        } else {
            gestorUsuarios.consultarPorPerfil(Perfil.valueOf(perfil))
        }

        if (usuariosFiltrados.isEmpty()) {
            ui.mostrar("No se encontraron usuarios con ese perfil.", true, false)
        } else {
            usuariosFiltrados.forEach { usuario ->
                ui.mostrar("Nombre: ${usuario.nombre}, Perfil: ${usuario.perfil}", false)
            }
        }
    }

    /**
     * Solicita al usuario un DNI y verifica que tenga el formato correcto: 8 dígitos seguidos de una letra.
     *
     * @return El DNI introducido en mayúsculas.
     */
    private fun pedirDni(): String {
        var dni: String
        do {
            dni = ui.pedirInfo("Introduce el DNI (8 dígitos seguidos de una letra):").uppercase(Locale.getDefault())
        } while (!dni.matches(Regex("\\d{8}[A-Z]")))

        return dni
    }

    /**
     * Solicita al usuario un importe positivo, usado para los seguros.
     *
     * @return El valor introducido como `Double` si es válido.
     */
    private fun pedirImporte(): Double {
        return ui.pedirDouble("Introduce el importe del seguro:", "Importe inválido", "Debes introducir un número válido.", { it > 0 })
    }

    /** Crea un nuevo seguro de hogar solicitando los datos al usuario */
    fun contratarSeguroHogar() {
        val dni = pedirDni()  // Pedir el DNI
        val importe = pedirImporte()  // Pedir el importe del seguro

        val metrosCuadrados = ui.pedirEntero("Introduce los metros cuadrados de la vivienda:",
            "Número inválido", "Debe ser un número válido.", { it > 0 })

        val valorContenido = ui.pedirDouble("Introduce el valor del contenido asegurado de la vivienda:",
            "Valor inválido", "Debe ser un número válido.", { it > 0 })

        val direccion = ui.pedirInfo("Introduce la dirección de la vivienda (calle, número, ciudad):")

        val aniosConstruccion = ui.pedirEntero("Introduce los años de construcción de la vivienda:",
            "Número inválido", "Debe ser un número válido.", { it >= 0 })

        gestorSeguros.contratarSeguroHogar(dni, importe, metrosCuadrados, valorContenido, direccion, aniosConstruccion)

        // Confirmación al usuario
        ui.mostrar("Seguro de hogar contratado exitosamente..", true, false)
    }

    /** Crea un nuevo seguro de auto solicitando los datos al usuario */
    fun contratarSeguroAuto() {
        val dni = pedirDni()  // Pedir el DNI
        val importe = pedirImporte()  // Pedir el importe del seguro

        // Pedir la descripción del seguro de auto
        val descripcion = ui.pedirInfo("Introduce una breve descripción del seguro de auto:")

        // Pedir el tipo de combustible
        val combustible = ui.pedirInfo("Introduce el tipo de combustible del auto (Ejemplo: Gasolina, Diesel, Eléctrico):")

        // Pedir el tipo de auto
        val tipoAuto = ui.pedirInfo("Introduce el tipo de auto (Ejemplo: SUV, Sedan, Compacto):")
        val auto = Auto.getAuto(tipoAuto)

        val tipoCobertura = ui.pedirInfo("Introduce el tipo de cobertura (Ejemplo: Básica, Completa):")
        val cobertura = Cobertura.getCobertura(tipoCobertura)  // Obtener el tipo de cobertura

        val asistenciaCarretera = ui.pedirInfo("¿El seguro incluye asistencia en carretera? (s/n):")
            .lowercase(Locale.getDefault()) == "s"

        val numPartes = ui.pedirEntero("Introduce el número de partes reportados por el cliente:", "Número inválido", "Debe ser un número válido.", { it >= 0 })

        gestorSeguros.contratarSeguroAuto(dni, importe, descripcion, combustible, auto, cobertura, asistenciaCarretera, numPartes)

        ui.mostrar("Seguro de auto contratado exitosamente.", true, false)
    }

    /** Crea un nuevo seguro de vida solicitando los datos al usuario */
    fun contratarSeguroVida() {
        val dni = pedirDni()
        val importe = pedirImporte()

        val fechaNac = ui.pedirInfo("Introduce la fecha de nacimiento (dd/MM/yyyy):")
        val fechaNacimiento = LocalDate.parse(fechaNac, DateTimeFormatter.ofPattern("dd/MM/yyyy"))

        val nivelRiesgo = ui.pedirInfo("Introduce el nivel de riesgo (BAJO, MEDIO, ALTO):").toUpperCase()
        val riesgo = Riesgo.getRiesgo(nivelRiesgo)

        val indemnizacion = ui.pedirDouble("Introduce la indemnización del seguro:", "Indemnización inválida", "Debe ser un número válido.", { it > 0 })

        gestorSeguros.contratarSeguroVida(dni, importe, fechaNacimiento, riesgo, indemnizacion)

        // Confirmación al usuario
        ui.mostrar("Seguro de vida contratado exitosamente.", true, false)
    }

    /** Elimina un seguro si existe por su número de póliza */
    fun eliminarSeguro() {
        val numPoliza = ui.pedirEntero(
            "Introduce el número de póliza del seguro a eliminar:",
            "Número de póliza inválido.",
            "Debe ser un número válido."
        ) { it > 0 }

        if (gestorSeguros.eliminarSeguro(numPoliza)) {
            ui.mostrar("Seguro con póliza $numPoliza eliminado exitosamente.", true, false)
        } else {
            ui.mostrarError("Seguro no encontrado.", true)
        }
    }

    /** Muestra todos los seguros existentes */
    fun consultarSeguros() {
        val tipo = ui.pedirInfo("Introduce el tipo de seguro a consultar (TODOS, HOGAR, AUTO, VIDA):")
            .uppercase(Locale.getDefault())

        val segurosFiltrados = when (tipo) {
            "TODOS" -> gestorSeguros.consultarTodos()
            "HOGAR" -> gestorSeguros.consultarPorTipo("HOGAR")
            "AUTO" -> gestorSeguros.consultarPorTipo("AUTO")
            "VIDA" -> gestorSeguros.consultarPorTipo("VIDA")
            else -> {
                ui.mostrarError("Tipo de seguro inválido.", true)
                return
            }
        }

        if (segurosFiltrados.isEmpty()) {
            ui.mostrar("No se encontraron seguros de ese tipo.", true, false)
        } else {
            segurosFiltrados.forEach { seguro ->
                ui.mostrar("Póliza: ${seguro.numPoliza}, Tipo: ${seguro.tipoSeguro()}", false)
            }
        }
    }

    /** Muestra todos los seguros de tipo hogar */
    fun consultarSegurosHogar() {
        val segurosHogar = gestorSeguros.consultarPorTipo("HOGAR")

        if (segurosHogar.isEmpty()) {
            ui.mostrar("No se encontraron seguros de hogar.", true, false)
        } else {
            segurosHogar.forEach { seguro ->
                val seguroHogar = seguro as SeguroHogar
                ui.mostrar("Póliza: ${seguro.numPoliza}, Dirección: ${seguro.direccion}", false)
            }
        }
    }

    /** Muestra todos los seguros de tipo auto */
    fun consultarSegurosAuto() {
        val segurosAuto = gestorSeguros.consultarPorTipo("AUTO")

        if (segurosAuto.isEmpty()) {
            ui.mostrar("No se encontraron seguros de auto.", true, false)
        } else {
            segurosAuto.forEach { seguro ->
                val seguroAuto = seguro as SeguroAuto
                ui.mostrar("Póliza: ${seguro.numPoliza}, Auto: ${seguro.descripcion}", false)
            }
        }
    }

    /** Muestra todos los seguros de tipo vida */
    fun consultarSegurosVida() {
        val segurosVida = gestorSeguros.consultarPorTipo("VIDA")

        if (segurosVida.isEmpty()) {
            ui.mostrar("No se encontraron seguros de vida.", true, false)
        } else {
            segurosVida.forEach { seguro ->
                val seguroVida = seguro as SeguroVida
                ui.mostrar("Póliza: ${seguro.numPoliza}, Beneficiario: ${seguro.getDniTitular()}", false)
            }
        }
    }
}