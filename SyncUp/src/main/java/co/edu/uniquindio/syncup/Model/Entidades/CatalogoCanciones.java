package co.edu.uniquindio.syncup.Model.Entidades;

import co.edu.uniquindio.syncup.Model.Trie.TrieAutocompletado;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class CatalogoCanciones {
    private List<Cancion> canciones;

    public CatalogoCanciones() {
        this.canciones = new ArrayList<>();
    }

    public void agregarCancion(Cancion cancion) {
        if (!canciones.contains(cancion)) {
            canciones.add(cancion);
        }
    }

    public void eliminarCancion(Cancion cancion) {
        canciones.remove(cancion);
    }

    public void actualizarCancion(Cancion cancion) {
        for (int i = 0; i < canciones.size(); i++) {
            if (canciones.get(i).getId() == cancion.getId()) {
                canciones.set(i, cancion);
                break;
            }
        }
    }

    public List<Cancion> getCanciones() {
        return new ArrayList<>(canciones);
    }

    public Cancion buscarPorId(int id) {
        for (Cancion cancion : canciones) {
            if (cancion.getId() == id) {
                return cancion;
            }
        }
        return null;
    }

    public List<Cancion> buscarPorTitulo(String titulo) {
        List<Cancion> resultado = new ArrayList<>();
        for (Cancion cancion : canciones) {
            if (cancion.getTitulo().toLowerCase().contains(titulo.toLowerCase())) {
                resultado.add(cancion);
            }
        }
        return resultado;
    }

    public List<Cancion> buscarPorArtista(String artista) {
        List<Cancion> resultado = new ArrayList<>();
        for (Cancion cancion : canciones) {
            if (cancion.getArtista().toLowerCase().contains(artista.toLowerCase())) {
                resultado.add(cancion);
            }
        }
        return resultado;
    }

    public List<Cancion> buscarPorGenero(String genero) {
        List<Cancion> resultado = new ArrayList<>();
        for (Cancion cancion : canciones) {
            if (cancion.getGenero().toLowerCase().contains(genero.toLowerCase())) {
                resultado.add(cancion);
            }
        }
        return resultado;
    }

    public int getTamaño() {
        return canciones.size();
    }
}
