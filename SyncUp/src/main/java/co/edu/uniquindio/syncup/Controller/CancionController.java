package co.edu.uniquindio.syncup.Controller;

import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import co.edu.uniquindio.syncup.Service.SyncUpService;
import java.util.List;

public class CancionController {
    private final SyncUpService service;

    public CancionController(SyncUpService service) {
        this.service = service;
    }

    public void agregarCancion(int id, String titulo, String artista, String genero, int año, double duracion) {
        service.agregarCancion(id, titulo, artista, genero, año, duracion);
    }

    public void actualizarCancion(int id, String titulo, String artista, String genero, int año, double duracion) {
        service.actualizarCancion(id, titulo, artista, genero, año, duracion);
    }

    public void eliminarCancion(int id) {
        service.eliminarCancion(id);
    }

    public List<Cancion> buscarPorTitulo(String titulo) {
        return service.buscarCancionesPorTitulo(titulo);
    }

    public List<Cancion> buscarPorArtista(String artista) {
        return service.buscarCancionesPorArtista(artista);
    }

    public List<Cancion> buscarPorGenero(String genero) {
        return service.buscarCancionesPorGenero(genero);
    }

    public List<Cancion> autocompletar(String prefijo) {
        return service.autocompletarBusqueda(prefijo);
    }

    public List<Cancion> buscarAvanzada(String artista, String genero, int año) {
        return service.buscarAvanzadaPorAtributos(artista, genero, año, false);
    }

    public List<Cancion> buscarAvanzada(String artista, String genero, int año, boolean usarOR) {
        return service.buscarAvanzadaPorAtributos(artista, genero, año, usarOR);
    }

    public List<Cancion> obtenerTodas() {
        return service.obtenerTodasLasCanciones();
    }

    public int obtenerTotal() {
        return service.getCantidadCanciones();
    }
}