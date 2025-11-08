package co.edu.uniquindio.syncup.Controller;

import java.util.List;

import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import co.edu.uniquindio.syncup.Model.Entidades.Playlist;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import co.edu.uniquindio.syncup.Service.SyncUpService;

public class PlaylistController {
    private SyncUpService service;

    public PlaylistController(SyncUpService service) {
        this.service = service;
    }

    public Playlist crearPlaylist(Usuario usuario, String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            return null;
        }
        return service.crearPlaylist(usuario, nombre);
    }

    public void agregarCancion(Usuario usuario, Cancion cancion) {
        service.agregarCancionAPlaylist(usuario, cancion);
    }

    public void removerCancion(Usuario usuario, Cancion cancion) {
        service.removerCancionDePlaylist(usuario, cancion);
    }

    public Playlist obtenerPlaylist(Usuario usuario) {
        return service.obtenerPlaylist(usuario);
    }

    public void agregarFavorito(Usuario usuario, Cancion cancion) {
        service.agregarFavorito(usuario, cancion);
    }

    public void removerFavorito(Usuario usuario, Cancion cancion) {
        service.removerFavorito(usuario, cancion);
    }

    public List<Cancion> obtenerFavoritos(Usuario usuario) {
        return service.obtenerFavoritos(usuario);
    }

    public List<Cancion> descargarFavoritosCSV(Usuario usuario) {
        return service.descargarFavoritosCSV(usuario);
    }
}