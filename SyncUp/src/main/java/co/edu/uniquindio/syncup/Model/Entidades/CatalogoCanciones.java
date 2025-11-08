package co.edu.uniquindio.syncup.Model.Entidades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Catálogo de Canciones
 * Gestiona todas las canciones disponibles en el sistema
 */
public class CatalogoCanciones {
    private List<Cancion> canciones;
    private Map<Integer, Cancion> cancionesPorId;

    public CatalogoCanciones() {
        this.canciones = new ArrayList<>();
        this.cancionesPorId = new HashMap<>();
    }

    public void agregarCancion(Cancion cancion) {
        if (cancion != null && !canciones.contains(cancion)) {
            canciones.add(cancion);
            cancionesPorId.put(cancion.getId(), cancion);
        }
    }

    public void eliminarCancion(Cancion cancion) {
        if (cancion != null) {
            canciones.remove(cancion);
            cancionesPorId.remove(cancion.getId());
        }
    }

    public void actualizarCancion(Cancion cancion) {
        if (cancion != null) {
            Cancion existente = cancionesPorId.get(cancion.getId());
            if (existente != null) {
                int index = canciones.indexOf(existente);
                if (index >= 0) {
                    canciones.set(index, cancion);
                    cancionesPorId.put(cancion.getId(), cancion);
                }
            }
        }
    }

    public Cancion buscarPorId(int id) {
        return cancionesPorId.get(id);
    }

    public List<Cancion> buscarPorTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            return new ArrayList<>();
        }
        String tituloLower = titulo.toLowerCase();
        return canciones.stream()
                .filter(c -> c.getTitulo().toLowerCase().contains(tituloLower))
                .collect(Collectors.toList());
    }

    public List<Cancion> buscarPorArtista(String artista) {
        if (artista == null || artista.trim().isEmpty()) {
            return new ArrayList<>();
        }
        String artistaLower = artista.toLowerCase();
        return canciones.stream()
                .filter(c -> c.getArtista().toLowerCase().contains(artistaLower))
                .collect(Collectors.toList());
    }

    public List<Cancion> buscarPorGenero(String genero) {
        if (genero == null || genero.trim().isEmpty()) {
            return new ArrayList<>();
        }
        String generoLower = genero.toLowerCase();
        return canciones.stream()
                .filter(c -> c.getGenero().toLowerCase().contains(generoLower))
                .collect(Collectors.toList());
    }

    public List<Cancion> buscarPorAlbum(String album) {
        if (album == null || album.trim().isEmpty()) {
            return new ArrayList<>();
        }
        String albumLower = album.toLowerCase();
        return canciones.stream()
                .filter(c -> c.getAlbum().toLowerCase().contains(albumLower))
                .collect(Collectors.toList());
    }

    public List<Cancion> getCanciones() {
        return new ArrayList<>(canciones);
    }

    public int getTamaño() {
        return canciones.size();
    }

    public boolean contieneCancion(int id) {
        return cancionesPorId.containsKey(id);
    }

    public void limpiar() {
        canciones.clear();
        cancionesPorId.clear();
    }
}