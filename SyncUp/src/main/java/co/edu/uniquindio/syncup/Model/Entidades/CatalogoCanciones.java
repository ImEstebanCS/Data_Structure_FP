package co.edu.uniquindio.syncup.Model.Entidades;

import java.util.*;

public class CatalogoCanciones {
    private Map<Integer, Cancion> canciones;

    public CatalogoCanciones() {
        this.canciones = new HashMap<>();
    }

    public void agregarCancion(Cancion cancion) {
        canciones.put(cancion.getId(), cancion);
    }

    public void eliminarCancion(int id) {
        canciones.remove(id);
    }

    public Cancion buscarPorId(int id) {
        return canciones.get(id);
    }

    public List<Cancion> buscarPorTitulo(String titulo) {
        List<Cancion> resultados = new ArrayList<>();
        for (Cancion cancion : canciones.values()) {
            if (cancion.getTitulo().toLowerCase().contains(titulo.toLowerCase())) {
                resultados.add(cancion);
            }
        }
        return resultados;
    }

    public List<Cancion> buscarPorArtista(String artista) {
        List<Cancion> resultados = new ArrayList<>();
        for (Cancion cancion : canciones.values()) {
            if (cancion.getArtista().toLowerCase().contains(artista.toLowerCase())) {
                resultados.add(cancion);
            }
        }
        return resultados;
    }

    public List<Cancion> buscarPorGenero(String genero) {
        List<Cancion> resultados = new ArrayList<>();
        for (Cancion cancion : canciones.values()) {
            if (cancion.getGenero().equalsIgnoreCase(genero)) {
                resultados.add(cancion);
            }
        }
        return resultados;
    }

    public List<Cancion> busquedaAvanzada(String artista, String genero, int año, boolean usarOR) {
        List<Cancion> resultados = new ArrayList<>();

        for (Cancion cancion : canciones.values()) {
            boolean cumpleArtista = (artista == null || artista.isEmpty() || cancion.getArtista().toLowerCase().contains(artista.toLowerCase()));
            boolean cumpleGenero = (genero == null || genero.isEmpty() || cancion.getGenero().equalsIgnoreCase(genero));
            boolean cumpleAño = (año == 0 || cancion.getAño() == año);

            if (usarOR) {
                if (cumpleArtista || cumpleGenero || cumpleAño) {
                    resultados.add(cancion);
                }
            } else {
                if (cumpleArtista && cumpleGenero && cumpleAño) {
                    resultados.add(cancion);
                }
            }
        }

        return resultados;
    }

    public List<Cancion> obtenerTodasLasCanciones() {
        return new ArrayList<>(canciones.values());
    }

    public int obtenerTotal() {
        return canciones.size();
    }
}