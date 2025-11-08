package co.edu.uniquindio.syncup.Model.Entidades;

import java.util.Objects;

/**
 * Entidad Administrador
 * Representa un administrador del sistema con permisos especiales
 */
public class Administrador {
    private String username;
    private String password;
    private String nombre;

    public Administrador() {
    }

    public Administrador(String username, String password, String nombre) {
        this.username = username;
        this.password = password;
        this.nombre = nombre;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Administrador admin = (Administrador) obj;
        return Objects.equals(username, admin.username);
    }

    @Override
    public String toString() {
        return "Administrador: " + nombre + " (@" + username + ")";
    }
}