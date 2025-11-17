package co.edu.uniquindio.syncup.Controller;

import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import co.edu.uniquindio.syncup.Service.SyncUpService;

import java.util.ArrayList;
import java.util.List;

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
        service.eliminarUsuario(username);
        return true;
    }

    public void seguir(Usuario seguidor, Usuario seguido) {
        service.seguir(seguidor, seguido);
    }

    public void dejarDeSeguir(Usuario seguidor, Usuario seguido) {
        service.dejarDeSeguir(seguidor, seguido);
    }

    public List<Usuario> obtenerSiguiendo(Usuario usuario) {
        return service.obtenerSiguiendo(usuario);
    }

    // ✅ MÉTODO AGREGADO - Este era el método faltante
    public List<Usuario> obtenerSeguidores(Usuario usuario) {
        return service.obtenerSeguidores(usuario);
    }

    public List<Usuario> obtenerSugerencias(Usuario usuarioActual, int cantidad) {
        return service.obtenerSugerenciasDeUsuarios(usuarioActual, cantidad);
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

    public Usuario obtenerUsuarioPorUsername(String username) {
        return service.obtenerUsuarioPorUsername(username);
    }
}