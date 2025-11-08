package co.edu.uniquindio.syncup.utils;

import co.edu.uniquindio.syncup.Model.Entidades.Administrador;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;

/**
 * SessionManager
 * Mantiene la información del usuario/administrador actualmente autenticado
 */
public class SessionManager {
    private static SessionManager instance;
    private Usuario usuarioActual;
    private Administrador administradorActual;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
        this.administradorActual = null;
    }

    public void setAdministradorActual(Administrador administrador) {
        this.administradorActual = administrador;
        this.usuarioActual = null;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public Administrador getAdministradorActual() {
        return administradorActual;
    }

    public boolean hayUsuarioActivo() {
        return usuarioActual != null;
    }

    public boolean hayAdministradorActivo() {
        return administradorActual != null;
    }

    public void cerrarSesion() {
        usuarioActual = null;
        administradorActual = null;
    }
}