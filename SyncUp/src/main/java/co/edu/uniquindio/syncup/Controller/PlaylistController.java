package co.edu.uniquindio.syncup.Controller;

import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import co.edu.uniquindio.syncup.Model.Entidades.Playlist;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import co.edu.uniquindio.syncup.Service.SyncUpService;
import co.edu.uniquindio.syncup.utils.UIComponents;

public class PlaylistController {
    private final SyncUpService service;

    public PlaylistController(SyncUpService service) {
        this.service = service;
    }

    public void crearPlaylist(Usuario usuario, String nombre) {
        service.crearPlaylist(usuario, nombre);
    }

    public void agregarCancionAPlaylist(Usuario usuario, String nombrePlaylist, Cancion cancion) {
        service.agregarCancionAPlaylist(usuario, nombrePlaylist, cancion);
    }

    public void eliminarCancionDePlaylist(Usuario usuario, String nombrePlaylist, Cancion cancion) {
        service.eliminarCancionDePlaylist(usuario, nombrePlaylist, cancion);
    }

    public void agregarFavorito(Usuario usuario, Cancion cancion) {
        Playlist favoritos = service.obtenerListaFavoritos(usuario);
        if (!favoritos.getCanciones().contains(cancion)) {
            favoritos.getCanciones().add(cancion);
            service.guardarUsuarios();
        }
    }

    public void eliminarDeFavoritos(Usuario usuario, Cancion cancion) {
        Playlist favoritos = service.obtenerListaFavoritos(usuario);
        favoritos.getCanciones().remove(cancion);
        service.guardarUsuarios();
    }

    public void quitarFavorito(Usuario usuario, Cancion cancion) {
        eliminarDeFavoritos(usuario, cancion);
    }

    public Playlist obtenerFavoritos(Usuario usuario) {
        return service.obtenerListaFavoritos(usuario);
    }

    public Playlist generarDescubrimientoSemanal(Usuario usuario) {
        return service.generarDescubrimientoSemanal(usuario);
    }

    public void exportarJSON(Playlist playlist) {
        service.exportarPlaylist(playlist, "json");
        UIComponents.mostrarAlertaPersonalizada("Exportado", "Playlist exportada a JSON", "📥");
    }

    public void exportarTXT(Playlist playlist) {
        service.exportarPlaylist(playlist, "txt");
        UIComponents.mostrarAlertaPersonalizada("Exportado", "Playlist exportada a TXT", "📄");
    }

    public void guardarDatos() {
        service.guardarUsuarios();
    }
}