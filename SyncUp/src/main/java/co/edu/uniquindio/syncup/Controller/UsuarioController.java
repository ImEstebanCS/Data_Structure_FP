package co.edu.uniquindio.syncup.Controller;

import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import co.edu.uniquindio.syncup.Service.SyncUpService;

import java.util.List;


public class UsuarioController {
    private final SyncUpService service;

    public UsuarioController(SyncUpService service) {
        this.service = service;
    }

    public boolean registrar(String username, String password, String nombre) {
        return service.registrarUsuario(username, password, nombre);
    }

    public Usuario autenticar(String username, String password) {
        return service.autenticarUsuario(username, password);
    }

    public void actualizar(String usernameOriginal, String nuevoUsername, String nuevaPassword, String nuevoNombre) {
        service.actualizarUsuario(usernameOriginal, nuevoUsername, nuevaPassword, nuevoNombre);
    }

    public boolean eliminar(String username) {
        return service.eliminarUsuario(username);
    }

    public List<Usuario> listarUsuarios() {
        return service.listarUsuarios();
    }

    public int getCantidadUsuarios() {
        return service.getCantidadUsuarios();
    }

    /**
     * RF-022: Obtiene sugerencias de usuarios usando BFS (amigos de amigos).
     * Delega al service que ejecuta el algoritmo BFS en el GrafoSocial.
     */
    public List<Usuario> obtenerSugerenciasDeUsuarios(Usuario usuarioActual, int cantidad) {
        return service.obtenerSugerenciasDeUsuarios(usuarioActual, cantidad);
    }

    /**
     * RF-022: Alias de obtenerSugerenciasDeUsuarios para compatibilidad.
     */
    public List<Usuario> obtenerSugerencias(Usuario usuarioActual, int cantidad) {
        return service.obtenerSugerenciasDeUsuarios(usuarioActual, cantidad);
    }

    /**
     * RF-022: Verifica si dos usuarios están conectados usando BFS.
     */
    public boolean estanConectados(Usuario usuario1, Usuario usuario2) {
        return service.estanConectados(usuario1, usuario2);
    }

    /**
     * RF-022: Calcula grados de separación entre usuarios usando BFS.
     */
    public int obtenerGradoSeparacion(Usuario usuario1, Usuario usuario2) {
        return service.obtenerGradoSeparacion(usuario1, usuario2);
    }

    public void agregarFavorito(Usuario usuario, Cancion cancion) {
        service.agregarFavorito(usuario, cancion);
    }

    public void seguir(Usuario seguidor, Usuario seguido) {
        service.seguir(seguidor, seguido);
    }

    public void dejarDeSeguir(Usuario seguidor, Usuario seguido) {
        service.dejarDeSeguir(seguidor, seguido);
    }

    public List<Usuario> obtenerSeguidores(Usuario usuario) {
        return service.obtenerSeguidores(usuario);
    }

    public List<Usuario> obtenerSiguiendo(Usuario usuario) {
        return service.obtenerSiguiendo(usuario);
    }

    /**
     * RF-014: Obtiene un usuario por su username con acceso O(1).
     */
    public Usuario obtenerUsuarioPorUsername(String username) {
        return service.obtenerUsuarioPorUsername(username);
    }
}