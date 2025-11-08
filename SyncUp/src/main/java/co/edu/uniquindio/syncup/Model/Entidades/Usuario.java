package co.edu.uniquindio.syncup.Model.Entidades;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Entidad Usuario - RF-013, RF-014, RF-015
 * Representa un usuario de la plataforma
 */
public class Usuario {
    private String username; // único
    private String password;
    private String nombre;
    private LinkedList<Cancion> listaFavoritos; // RF-013
    private List<Usuario> siguiendo;
    private List<Usuario> seguidores;

    public Usuario() {
        this.listaFavoritos = new LinkedList<>();
        this.siguiendo = new ArrayList<>();
        this.seguidores = new ArrayList<>();
    }

    public Usuario(String username, String password, String nombre) {
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.listaFavoritos = new LinkedList<>();
        this.siguiendo = new ArrayList<>();
        this.seguidores = new ArrayList<>();
    }

    // Getters y Setters
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

    public LinkedList<Cancion> getListaFavoritos() {
        return listaFavoritos;
    }

    public void setListaFavoritos(LinkedList<Cancion> listaFavoritos) {
        this.listaFavoritos = listaFavoritos;
    }

    public List<Usuario> getSiguiendo() {
        return siguiendo;
    }

    public void setSiguiendo(List<Usuario> siguiendo) {
        this.siguiendo = siguiendo;
    }

    public List<Usuario> getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(List<Usuario> seguidores) {
        this.seguidores = seguidores;
    }

    // Métodos de favoritos - RF-002
    public void agregarFavorito(Cancion cancion) {
        if (!listaFavoritos.contains(cancion)) {
            listaFavoritos.add(cancion);
        }
    }

    public void removerFavorito(Cancion cancion) {
        listaFavoritos.remove(cancion);
    }

    public boolean esFavorito(Cancion cancion) {
        return listaFavoritos.contains(cancion);
    }

    // Métodos de conexiones sociales - RF-007
    public void seguir(Usuario usuario) {
        if (!siguiendo.contains(usuario) && !this.equals(usuario)) {
            siguiendo.add(usuario);
            usuario.agregarSeguidor(this);
        }
    }

    public void dejarDeSeguir(Usuario usuario) {
        if (siguiendo.contains(usuario)) {
            siguiendo.remove(usuario);
            usuario.removerSeguidor(this);
        }
    }

    private void agregarSeguidor(Usuario usuario) {
        if (!seguidores.contains(usuario)) {
            seguidores.add(usuario);
        }
    }

    private void removerSeguidor(Usuario usuario) {
        seguidores.remove(usuario);
    }

    public boolean estaSiguiendo(Usuario usuario) {
        return siguiendo.contains(usuario);
    }

    /**
     * RF-015: hashCode basado en username
     */
    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    /**
     * RF-015: equals basado en username
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Usuario usuario = (Usuario) obj;
        return Objects.equals(username, usuario.username);
    }

    @Override
    public String toString() {
        return nombre + " (@" + username + ")";
    }
}