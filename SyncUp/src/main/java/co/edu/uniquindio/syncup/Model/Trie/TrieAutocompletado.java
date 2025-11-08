package co.edu.uniquindio.syncup.Model.Trie;

import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import java.util.ArrayList;
import java.util.List;

/**
 * TrieAutocompletado - RF-023, RF-024
 * Árbol de Prefijos para búsquedas eficientes con autocompletado
 */
public class TrieAutocompletado {
    private NodoTrie raiz;

    public TrieAutocompletado() {
        this.raiz = new NodoTrie();
    }

    /**
     * Inserta una canción en el Trie usando su título como clave
     */
    public void insertarCancion(String titulo, Cancion cancion) {
        if (titulo == null || titulo.isEmpty() || cancion == null) {
            return;
        }

        String clave = titulo.toLowerCase().trim();
        NodoTrie nodo = raiz;

        for (char c : clave.toCharArray()) {
            if (!nodo.tieneHijo(c)) {
                nodo.agregarHijo(c, new NodoTrie());
            }
            nodo = nodo.obtenerHijo(c);
        }

        nodo.setEsFinPalabra(true);
        nodo.setCancion(cancion);
    }

    /**
     * Elimina una canción del Trie
     */
    public void eliminarCancion(String titulo) {
        if (titulo == null || titulo.isEmpty()) {
            return;
        }
        eliminarRecursivo(raiz, titulo.toLowerCase().trim(), 0);
    }

    private boolean eliminarRecursivo(NodoTrie nodo, String titulo, int indice) {
        if (indice == titulo.length()) {
            if (!nodo.isEsFinPalabra()) {
                return false;
            }
            nodo.setEsFinPalabra(false);
            nodo.setCancion(null);
            return !nodo.tieneHijos();
        }

        char c = titulo.charAt(indice);
        NodoTrie hijo = nodo.obtenerHijo(c);

        if (hijo == null) {
            return false;
        }

        boolean debeEliminar = eliminarRecursivo(hijo, titulo, indice + 1);

        if (debeEliminar) {
            nodo.getHijos().remove(c);
            return !nodo.tieneHijos() && !nodo.isEsFinPalabra();
        }

        return false;
    }

    /**
     * RF-024: Busca todas las canciones que comienzan con el prefijo dado
     */
    public List<Cancion> buscarPorPrefijo(String prefijo) {
        List<Cancion> resultado = new ArrayList<>();

        if (prefijo == null || prefijo.isEmpty()) {
            return resultado;
        }

        String clave = prefijo.toLowerCase().trim();
        NodoTrie nodo = raiz;

        // Navegar hasta el final del prefijo
        for (char c : clave.toCharArray()) {
            if (!nodo.tieneHijo(c)) {
                return resultado; // No hay coincidencias
            }
            nodo = nodo.obtenerHijo(c);
        }

        // Recolectar todas las palabras desde este nodo
        buscarRecursivo(nodo, resultado);
        return resultado;
    }

    private void buscarRecursivo(NodoTrie nodo, List<Cancion> resultado) {
        if (nodo.isEsFinPalabra() && nodo.getCancion() != null) {
            resultado.add(nodo.getCancion());
        }

        for (NodoTrie hijo : nodo.getHijos().values()) {
            buscarRecursivo(hijo, resultado);
        }
    }

    /**
     * Busca una canción exacta por título
     */
    public Cancion buscarCancionExacta(String titulo) {
        if (titulo == null || titulo.isEmpty()) {
            return null;
        }

        String clave = titulo.toLowerCase().trim();
        NodoTrie nodo = raiz;

        for (char c : clave.toCharArray()) {
            if (!nodo.tieneHijo(c)) {
                return null;
            }
            nodo = nodo.obtenerHijo(c);
        }

        return nodo.isEsFinPalabra() ? nodo.getCancion() : null;
    }

    /**
     * Verifica si existe un título en el Trie
     */
    public boolean existe(String titulo) {
        return buscarCancionExacta(titulo) != null;
    }

    /**
     * Limpia todo el Trie
     */
    public void limpiar() {
        this.raiz = new NodoTrie();
    }
}