package co.edu.uniquindio.syncup.Controller;

import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import co.edu.uniquindio.syncup.Service.SyncUpService;

import java.util.List;

/**
 * UsuarioController
 * Controlador intermedio para operaciones de usuarios
 */
public class UsuarioController {
    private final SyncUpService service;

    public UsuarioController(SyncUpService service) {
        this.service = service;
    }

    public boolean registrar(String username, String password, String nombre) {
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            return false;
        }
        return service.registrarUsuario(username.trim(), password, nombre.trim());
    }

    public Usuario autenticar(String username, String password) {
        if (username == null || password == null) {
            return null;
        }
        return service.autenticarUsuario(username.trim(), password);
    }

    public List<Usuario> listarUsuarios() {
        return service.listarUsuarios();
    }

    public boolean eliminar(String username) {
        return service.eliminarUsuario(username);
    }

    public void seguir(Usuario usuario1, Usuario usuario2) {
        service.seguir(usuario1, usuario2);
    }

    public void dejarDeSeguir(Usuario usuario1, Usuario usuario2) {
        service.dejarDeSeguir(usuario1, usuario2);
    }

    public List<Usuario> obtenerSeguidores(Usuario usuario) {
        return service.obtenerSeguidores(usuario);
    }

    public List<Usuario> obtenerSiguiendo(Usuario usuario) {
        return service.obtenerSiguiendo(usuario);
    }

    public List<Usuario> obtenerSugerencias(Usuario usuario, int cantidad) {
        return service.obtenerSugerenciasDeUsuarios(usuario, cantidad);
    }

    public boolean sonistaConectados(Usuario usuario1, Usuario usuario2) {
        return service.sonistaConectados(usuario1, usuario2);
    }

    public int obtenerGradoSeparacion(Usuario usuario1, Usuario usuario2) {
        return service.obtenerGradoSeparacion(usuario1, usuario2);
    }

    public int getCantidadUsuarios() {
        return service.getCantidadUsuarios();
    }
}