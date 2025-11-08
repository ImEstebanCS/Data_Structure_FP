package co.edu.uniquindio.syncup.Model.Entidades;


import java.util.*;

public class Usuario {
    private String username;
    private String password;
    private String nombre;
    private List<Cancion> listaFavoritos;
    private List<Usuario> siguiendo;
    private List<Usuario> seguidores;

    public Usuario(String username, String password, String nombre) {
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.listaFavoritos = new ArrayList<>();
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

    public List<Cancion> getListaFavoritos() {
        return listaFavoritos;
    }

    public void setListaFavoritos(List<Cancion> listaFavoritos) {
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

    public void agregarFavorito(Cancion cancion) {
        if (!listaFavoritos.contains(cancion)) {
            listaFavoritos.add(cancion);
        }
    }

    public void removeFavorito(Cancion cancion) {
        listaFavoritos.remove(cancion);
    }

    public void seguir(Usuario usuario) {
        if (!siguiendo.contains(usuario)) {
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

    @Override
    public String toString() {
        return "Usuario{" +
                "username='" + username + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Usuario usuario = (Usuario) obj;
        return username.equals(usuario.username);
    }
}