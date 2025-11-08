package co.edu.uniquindio.syncup.Controller;

import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import co.edu.uniquindio.syncup.Model.Entidades.Playlist;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import co.edu.uniquindio.syncup.Service.SyncUpService;
import java.util.List;

public class PlaylistController {
    private final SyncUpService service;

    public PlaylistController(SyncUpService service) {
        this.service = service;
    }

    public Playlist crearPlaylist(Usuario usuario, String nombre) {
        return service.crearPlaylist(usuario, nombre);
    }

    public void agregarCancion(Usuario usuario, Cancion cancion) {
        service.agregarCancionAPlaylist(usuario, cancion);
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

    public Playlist generarDescubrimientoSemanal(Usuario usuario) {
        return service.generarDescubrimientoSemanal(usuario);
    }
}