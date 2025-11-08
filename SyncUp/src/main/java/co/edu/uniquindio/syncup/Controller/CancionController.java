package co.edu.uniquindio.syncup.Controller;

import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import co.edu.uniquindio.syncup.Service.SyncUpService;

import java.util.List;

public class CancionController {
    private SyncUpService service;

    public CancionController(SyncUpService service) {
        this.service = service;
    }

    public void agregarCancion(int id, String titulo, String artista, String genero, int año, double duracion) {
        if (titulo == null || titulo.isEmpty() || artista == null || artista.isEmpty()) {
            return;
        }
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
        return service.buscarAvanzadaPorAtributos(artista, genero, año);
    }

    public List<Cancion> obtenerRecomendaciones(Cancion cancion, int cantidad) {
        return service.obtenerCancionesRecomendadas(cancion, cantidad);
    }

    public void agregarSimilitud(Cancion cancion1, Cancion cancion2, double similaridad) {
        service.agregarSimilitud(cancion1, cancion2, similaridad);
    }

    public int getCantidadCanciones() {
        return service.getCantidadCanciones();
    }
}