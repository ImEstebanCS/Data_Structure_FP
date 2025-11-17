package co.edu.uniquindio.syncup.Model.Trie;

import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import java.util.ArrayList;
import java.util.List;

public class TrieAutocompletado {
    private NodoTrie raiz;

    public TrieAutocompletado() {
        this.raiz = new NodoTrie();
    }

    public void insertar(String palabra, Cancion cancion) {
        NodoTrie nodo = raiz;
        palabra = palabra.toLowerCase();

        for (char c : palabra.toCharArray()) {
            nodo.getHijos().putIfAbsent(c, new NodoTrie());
            nodo = nodo.getHijos().get(c);
        }

        nodo.setEsFinDePalabra(true);
        nodo.setCancion(cancion);
    }

    public List<Cancion> autocompletar(String prefijo) {
        List<Cancion> resultados = new ArrayList<>();
        NodoTrie nodo = raiz;
        prefijo = prefijo.toLowerCase();

        for (char c : prefijo.toCharArray()) {
            if (!nodo.getHijos().containsKey(c)) {
                return resultados;
            }
            nodo = nodo.getHijos().get(c);
        }

        buscarTodasLasCanciones(nodo, resultados);
        return resultados;
    }

    private void buscarTodasLasCanciones(NodoTrie nodo, List<Cancion> resultados) {
        if (nodo.isEsFinDePalabra() && nodo.getCancion() != null) {
            resultados.add(nodo.getCancion());
        }

        for (NodoTrie hijo : nodo.getHijos().values()) {
            buscarTodasLasCanciones(hijo, resultados);
        }
    }
}