import org.example.app.*
import org.example.service.*
import org.example.ui.*
import org.example.utils.*
import org.example.data.*

fun main() {
    // Definimos las rutas de los archivos para usuarios y seguros. Solo se utilizarán si se elige el modo persistente.
    val rutaUsuario = "src/main/kotlin/data/res/Usuarios.txt"
    val rutaSeguros = "src/main/kotlin/data/res/Seguros.txt"

    // Inicializamos los componentes principales: interfaz de usuario y gestor de archivos.
    val ui = Consola()
    val gestionFicheros = Ficheros(ui)

    // Limpiar la consola antes de comenzar.
    ui.limpiarPantalla()

    // Determinar si el sistema debe operar en modo simulación (sin persistencia).
    val enSimulacion = ui.preguntar("INICIAR MODO SIMULACIÓN? s/n > ")

    // Declaración de repositorios para usuarios y seguros, se asignan según el modo elegido.
    val repoUsuarios: IRepoUsuarios
    val repoSeguros: IRepoSeguros

    if (enSimulacion) {
        // Uso de repositorios en memoria para la simulación.
        repoUsuarios = RepoUsuariosMem()
        repoSeguros = RepoSegurosMem()
    } else {
        // Repositorios con persistencia en archivos, se cargan los datos iniciales.
        repoUsuarios = RepoUsuariosFich(rutaUsuario, gestionFicheros)
        repoSeguros = RepoSegurosFich(rutaSeguros, gestionFicheros)

        val cargador = CargadorInicial(repoUsuarios, repoSeguros, ui)
        cargador.cargarUsuarios()
        cargador.cargarSeguros()
    }

    // Instancia de los gestores encargados de la lógica del negocio.
    val gestorUsuarios = GestorUsuarios(repoUsuarios, Seguridad())
    val gestorSeguros = GestorSeguros(repoSeguros)

    // Control de acceso: autenticación de usuario o creación del usuario administrador inicial.
    val acceso = ControlAcceso(rutaUsuario, gestorUsuarios, ui, gestionFicheros)
    val resultadoLogin = acceso.autenticar()

    // Si el login fue exitoso, se lanza el menú correspondiente al perfil del usuario.
    resultadoLogin?.let { (usuario, tipo) ->
        val menu = GestorMenu(usuario, tipo, ui, gestorUsuarios, gestorSeguros)
        menu.iniciarMenu()
    }
}