package co.edu.uniquindio.syncup.Model.Trie;

import co.edu.uniquindio.syncup.Model.Entidades.Cancion;

import java.util.ArrayList;
import java.util.List;

/**
 * Árbol de Prefijos (Trie) para autocompletado eficiente
 * RF-023: Implementado como Árbol de Prefijos (Trie)
 * RF-024: Devuelve todas las palabras que comienzan con un prefijo dado
 */

import java.util.*;

public class TrieAutocompletado {
    private NodoTrie raiz;

    public TrieAutocompletado() {
        this.raiz = new NodoTrie();
    }

    public void insertarCancion(String titulo, Cancion cancion) {
        String clave = titulo.toLowerCase();
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

    public void eliminarCancion(String titulo) {
        eliminarRecursivo(raiz, titulo.toLowerCase(), 0);
    }

    private boolean eliminarRecursivo(NodoTrie nodo, String titulo, int indice) {
        if (indice == titulo.length()) {
            if (!nodo.isEsFinPalabra()) {
                return false;
            }
            nodo.setEsFinPalabra(false);
            nodo.setCancion(null);
            return nodo.getHijos().isEmpty();
        }

        char c = titulo.charAt(indice);
        NodoTrie hijo = nodo.obtenerHijo(c);

        if (hijo == null) {
            return false;
        }

        boolean debeEliminar = eliminarRecursivo(hijo, titulo, indice + 1);

        if (debeEliminar) {
            nodo.getHijos().remove(c);
            return nodo.getHijos().isEmpty() && !nodo.isEsFinPalabra();
        }

        return false;
    }

    public List<Cancion> buscarPorPrefijo(String prefijo) {
        List<Cancion> resultado = new ArrayList<>();
        String clave = prefijo.toLowerCase();
        NodoTrie nodo = raiz;

        for (char c : clave.toCharArray()) {
            if (!nodo.tieneHijo(c)) {
                return resultado;
            }
            nodo = nodo.obtenerHijo(c);
        }

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

    public Cancion buscarCancionExacta(String titulo) {
        String clave = titulo.toLowerCase();
        NodoTrie nodo = raiz;

        for (char c : clave.toCharArray()) {
            if (!nodo.tieneHijo(c)) {
                return null;
            }
            nodo = nodo.obtenerHijo(c);
        }

        if (nodo.isEsFinPalabra()) {
            return nodo.getCancion();
        }

        return null;
    }

    public boolean existe(String titulo) {
        return buscarCancionExacta(titulo) != null;
    }
}