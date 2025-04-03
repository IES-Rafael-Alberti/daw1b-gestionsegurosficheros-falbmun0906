import org.example.app.CargadorInicial
import org.example.app.GestorMenu
import org.example.data.*
import org.example.service.GestorSeguros
import org.example.service.GestorUsuarios
import org.example.service.IServSeguros
import org.example.service.IServUsuarios
import org.example.ui.Consola
import org.example.ui.IEntradaSalida
import org.example.utils.Ficheros
import org.example.utils.Seguridad
import java.util.*

fun main() {

    // 1. Crear dos variables con las rutas de los archivos de texto donde se almacenan los usuarios y seguros.
    // Estos ficheros se usarán solo si el programa se ejecuta en modo de almacenamiento persistente.
    val rutaUsuarios = "usuarios.txt"
    val rutaSeguros = "seguros.txt"

    // 2. Instanciamos los componentes base del sistema: la interfaz de usuario, el gestor de ficheros y el módulo de seguridad.
    val ui = Consola()
    val fichero = Ficheros()
    val seguridad = Seguridad()

    // 3. Limpiamos la pantalla antes de comenzar, para que la interfaz esté despejada al usuario.
    ui.limpiarPantalla()

    // 4. Preguntamos al usuario si desea iniciar en modo simulación.
    println("¿Deseas iniciar en modo SIMULACIÓN (Sí/No)?")
    val modoSimulacion = readLine()?.lowercase(Locale.getDefault()) == "sí"

    // 5. Declaramos los repositorios de usuarios y seguros.
    val repositorioUsuarios: IRepoUsuarios
    val repositorioSeguros: IRepoSeguros

    // 6. Si se ha elegido modo simulación, se usan repositorios en memoria.
    // Si se ha elegido almacenamiento persistente, se instancian los repositorios que usan ficheros.
    if (modoSimulacion) {
        repositorioUsuarios = RepoUsuariosMem()
        repositorioSeguros = RepoSegurosMem()
    } else {
        repositorioUsuarios = RepoUsuariosFich(rutaUsuarios, fichero)
        repositorioSeguros = RepoSegurosFich(rutaSeguros, fichero)
    }

    // 7. Instanciamos los servicios de lógica de negocio, inyectando los repositorios y el componente de seguridad.
    val servicioUsuarios = GestorUsuarios(repositorioUsuarios, seguridad)
    val servicioSeguros = GestorSeguros(repositorioSeguros)

// 8. Se inicia el proceso de autenticación.
    val usuarioAutenticado = if (repositorioUsuarios.obtenerTodos().isEmpty()) {

        println("No hay usuarios registrados. Por favor, crea un nuevo usuario.")

        // Pedimos los datos del nuevo usuario.
        val nombre = readLine() ?: ""
        val clave = readLine() ?: ""
        val perfil = "ADMIN" // O pide al usuario que ingrese el perfil, por ejemplo, con una entrada adicional

        // Creamos una instancia del GestorMenu, pasándole todos los parámetros necesarios.
        val gestorMenu = GestorMenu(
            nombreUsuario = nombre,
            perfilUsuario = perfil,
            ui = ui,
            gestorUsuarios = servicioUsuarios,
            gestorSeguros = servicioSeguros
        )

        // Creamos el nuevo usuario y lo guardamos en el repositorio de usuarios.
        gestorMenu.nuevoUsuario()  // Este método debería agregar el nuevo usuario al repositorio de usuarios.

        // Ahora que el usuario está creado, solicitamos login.
        println("Ingresa tu usuario y contraseña para continuar:")
        val usuario = readLine() ?: ""
        val contraseña = readLine() ?: ""

        // Aquí se debería hacer el login usando el servicio de usuarios, pero no lo habías implementado en el bloque original:
        servicioUsuarios.iniciarSesion(usuario, contraseña)
    } else {
        // Si ya existen usuarios, pedimos login.
        println("Por favor, ingresa tu usuario y contraseña.")
        val usuario = readLine() ?: ""
        val contraseña = readLine() ?: ""
        // Realizamos el login con el servicio de usuarios.
        servicioUsuarios.iniciarSesion(usuario, contraseña)
    }

// 9. Si el login fue exitoso (no es null), se inicia el menú correspondiente al perfil del usuario autenticado.
    if (usuarioAutenticado != null) {
        // Determinar el perfil del usuario (admin, gestion, consulta).
        val perfil = usuarioAutenticado.perfil
        val nombreUsuario = usuarioAutenticado.nombre  // Asumiendo que el nombre se obtiene del usuario autenticado

        // 10. Se lanza el menú principal, iniciarMenu(0), pasándole toda la información necesaria.
        val gestorMenu = GestorMenu(
            nombreUsuario = nombreUsuario,
            perfilUsuario = perfil,
            ui = ui, // Interfaz de usuario (asegúrate de tener una implementación de IEntradaSalida)
            gestorUsuarios = servicioUsuarios, // Gestor de usuarios
            gestorSeguros = servicioSeguros // Gestor de seguros
        )
        gestorMenu.iniciarMenu(0)  // Iniciamos el menú principal, pasándole el índice 0 y el perfil del usuario
    } else {
        println("Autenticación fallida. Por favor, intenta nuevamente.")
    }
}