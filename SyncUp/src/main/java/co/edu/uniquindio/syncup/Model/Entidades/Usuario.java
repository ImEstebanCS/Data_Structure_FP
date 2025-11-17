package co.edu.uniquindio.syncup.Model.Entidades;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String username;
    private String password;
    private String nombre;
    private Playlist listaFavoritosPlaylist;  // SOLO UNA estructura para favoritos
    private List<Playlist> playlists;
    private List<Usuario> seguidos;
    private List<Usuario> seguidores;
    private Playlist playlistActual;
    private Radio radioActual;

    public Usuario(String username, String password, String nombre) {
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.playlists = new ArrayList<>();
        this.seguidos = new ArrayList<>();
        this.seguidores = new ArrayList<>();
        // ✅ CORREGIDO: Usar constructor con 2 parámetros
        this.listaFavoritosPlaylist = new Playlist("Favoritos", this);
    }

    // Getters y Setters básicos
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

    // FAVORITOS - Métodos unificados
    public List<Cancion> getListaFavoritos() {
        if (listaFavoritosPlaylist == null) {
            // ✅ CORREGIDO
            listaFavoritosPlaylist = new Playlist("Favoritos", this);
        }
        return listaFavoritosPlaylist.getCanciones();
    }

    public Playlist getListaFavoritosPlaylist() {
        if (listaFavoritosPlaylist == null) {
            // ✅ CORREGIDO
            listaFavoritosPlaylist = new Playlist("Favoritos", this);
        }
        return listaFavoritosPlaylist;
    }

    public void setListaFavoritosPlaylist(Playlist listaFavoritos) {
        this.listaFavoritosPlaylist = listaFavoritos;
    }

    public void agregarFavorito(Cancion cancion) {
        if (listaFavoritosPlaylist == null) {
            // ✅ CORREGIDO
            listaFavoritosPlaylist = new Playlist("Favoritos", this);
        }
        if (!listaFavoritosPlaylist.getCanciones().contains(cancion)) {
            listaFavoritosPlaylist.agregarCancion(cancion);
        }
    }

    public void removerFavorito(Cancion cancion) {
        if (listaFavoritosPlaylist != null) {
            listaFavoritosPlaylist.getCanciones().remove(cancion);
        }
    }

    // PLAYLISTS
    public List<Playlist> getPlaylists() {
        if (playlists == null) {
            playlists = new ArrayList<>();
        }
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    public void agregarPlaylist(Playlist playlist) {
        if (playlists == null) {
            playlists = new ArrayList<>();
        }
        playlists.add(playlist);
    }

    // SEGUIDOS Y SEGUIDORES
    public List<Usuario> getSeguidos() {
        if (seguidos == null) {
            seguidos = new ArrayList<>();
        }
        return seguidos;
    }

    public void setSeguidos(List<Usuario> seguidos) {
        this.seguidos = seguidos;
    }

    // ✅ MÉTODO AGREGADO - Este era el método faltante
    public List<Usuario> getSiguiendo() {
        return getSeguidos(); // Alias para mantener compatibilidad
    }

    public List<Usuario> getSeguidores() {
        if (seguidores == null) {
            seguidores = new ArrayList<>();
        }
        return seguidores;
    }

    public void setSeguidores(List<Usuario> seguidores) {
        this.seguidores = seguidores;
    }

    // PLAYLIST Y RADIO ACTUAL
    public Playlist getPlaylistActual() {
        return playlistActual;
    }

    public void setPlaylistActual(Playlist playlistActual) {
        this.playlistActual = playlistActual;
    }

    public Radio getRadioActual() {
        return radioActual;
    }

    public void setRadioActual(Radio radioActual) {
        this.radioActual = radioActual;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return username != null && username.equals(usuario.username);
    }

    @Override
    public int hashCode() {
        return username != null ? username.hashCode() : 0;
    }
}