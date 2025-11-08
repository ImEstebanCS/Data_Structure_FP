package co.edu.uniquindio.syncup.Model.Trie;

import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import java.util.HashMap;
import java.util.Map;

/**
 * NodoTrie - RF-023
 * Nodo del árbol de prefijos para autocompletado
 */
public class NodoTrie {
    private Map<Character, NodoTrie> hijos;
    private boolean esFinPalabra;
    private Cancion cancion;

    public NodoTrie() {
        this.hijos = new HashMap<>();
        this.esFinPalabra = false;
        this.cancion = null;
    }

    public Map<Character, NodoTrie> getHijos() {
        return hijos;
    }

    public void setHijos(Map<Character, NodoTrie> hijos) {
        this.hijos = hijos;
    }

    public boolean isEsFinPalabra() {
        return esFinPalabra;
    }

    public void setEsFinPalabra(boolean esFinPalabra) {
        this.esFinPalabra = esFinPalabra;
    }

    public Cancion getCancion() {
        return cancion;
    }

    public void setCancion(Cancion cancion) {
        this.cancion = cancion;
    }

    public NodoTrie obtenerHijo(char c) {
        return hijos.get(c);
    }

    public void agregarHijo(char c, NodoTrie nodo) {
        hijos.put(c, nodo);
    }

    public boolean tieneHijo(char c) {
        return hijos.containsKey(c);
    }

    public boolean tieneHijos() {
        return !hijos.isEmpty();
    }
}