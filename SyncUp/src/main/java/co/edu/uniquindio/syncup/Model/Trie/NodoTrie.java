package co.edu.uniquindio.syncup.Model.Trie;

import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import java.util.HashMap;
import java.util.Map;

public class NodoTrie {
    private Map<Character, NodoTrie> hijos;
    private boolean esFinDePalabra;
    private Cancion cancion;

    public NodoTrie() {
        this.hijos = new HashMap<>();
        this.esFinDePalabra = false;
    }

    public Map<Character, NodoTrie> getHijos() {
        return hijos;
    }

    public void setHijos(Map<Character, NodoTrie> hijos) {
        this.hijos = hijos;
    }

    public boolean isEsFinDePalabra() {
        return esFinDePalabra;
    }

    public void setEsFinDePalabra(boolean esFinDePalabra) {
        this.esFinDePalabra = esFinDePalabra;
    }

    public Cancion getCancion() {
        return cancion;
    }

    public void setCancion(Cancion cancion) {
        this.cancion = cancion;
    }
}