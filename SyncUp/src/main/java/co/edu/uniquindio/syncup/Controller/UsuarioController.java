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

    public List<Usuario> obtenerSugerenciasDeUsuarios(Usuario usuarioActual, int cantidad) {
        return service.obtenerSugerenciasDeUsuarios(usuarioActual, cantidad);
    }

    public void agregarFavorito(Usuario usuario, Cancion cancion) {
        service.agregarFavorito(usuario, cancion);
    }

    public void seguir(Usuario seguidor, Usuario seguido) {
        service.seguir(seguidor, seguido);
    }

    public List<Usuario> obtenerSeguidores(Usuario usuario) {
        return service.obtenerSeguidores(usuario);
    }

    public List<Usuario> obtenerSiguiendo(Usuario usuario) {
        return service.obtenerSiguiendo(usuario);
    }

    public void dejarDeSeguir(Usuario usuarioActual, Usuario usuario) {
        service.dejarDeSeguir(usuarioActual, usuario);
    }

    public List<Usuario> obtenerSugerencias(Usuario usuarioActual, int i) {
        return List.of();
    }

    public Usuario obtenerUsuarioPorUsername(String usernameActual) {
        return null;
    }
}