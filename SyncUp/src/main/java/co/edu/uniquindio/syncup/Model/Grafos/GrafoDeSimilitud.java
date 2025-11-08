package co.edu.uniquindio.syncup.Model.Grafos;

import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import co.edu.uniquindio.syncup.Model.Trie.NodoTrie;

import java.util.ArrayList;
import java.util.List;

/**
 * Árbol de Prefijos (Trie) para autocompletado eficiente
 * RF-023: Implementado como Árbol de Prefijos (Trie)
 * RF-024: Devuelve todas las palabras que comienzan con un prefijo dado
 */
import java.util.*;

/**
 * Grafo ponderado no dirigido que conecta canciones basándose en su similitud.
 * Implementa algoritmo de Dijkstra para encontrar rutas de menor costo.
 * RF-019, RF-020
 */


import java.util.*;

public class GrafoDeSimilitud {
    private Map<Cancion, Map<Cancion, Double>> grafo;

    public GrafoDeSimilitud() {
        this.grafo = new HashMap<>();
    }

    public void agregarCancion(Cancion cancion) {
        if (!grafo.containsKey(cancion)) {
            grafo.put(cancion, new HashMap<>());
        }
    }

    public void agregarArista(Cancion origen, Cancion destino, double peso) {
        agregarCancion(origen);
        agregarCancion(destino);

        grafo.get(origen).put(destino, peso);
        grafo.get(destino).put(origen, peso);
    }

    public List<Cancion> obtenerCancionesSimilares(Cancion cancion, int cantidad) {
        List<Cancion> similares = new ArrayList<>();

        if (!grafo.containsKey(cancion)) {
            return similares;
        }

        Map<Cancion, Double> distancias = dijkstra(cancion);

        distancias.entrySet().stream()
                .filter(e -> !e.getKey().equals(cancion))
                .sorted(Comparator.comparingDouble(Map.Entry::getValue))
                .limit(cantidad)
                .forEach(e -> similares.add(e.getKey()));

        return similares;
    }

    private Map<Cancion, Double> dijkstra(Cancion inicio) {
        Map<Cancion, Double> distancias = new HashMap<>();
        PriorityQueue<Map.Entry<Cancion, Double>> cola = new PriorityQueue<>(
                Comparator.comparingDouble(Map.Entry::getValue)
        );

        for (Cancion cancion : grafo.keySet()) {
            distancias.put(cancion, Double.MAX_VALUE);
        }

        distancias.put(inicio, 0.0);
        cola.offer(new AbstractMap.SimpleEntry<>(inicio, 0.0));

        while (!cola.isEmpty()) {
            Map.Entry<Cancion, Double> actual = cola.poll();
            Cancion cancionActual = actual.getKey();
            double distActual = actual.getValue();

            if (distActual > distancias.get(cancionActual)) {
                continue;
            }

            for (Map.Entry<Cancion, Double> vecino : grafo.get(cancionActual).entrySet()) {
                Cancion cancionVecina = vecino.getKey();
                double peso = vecino.getValue();
                double nuevaDistancia = distActual + peso;

                if (nuevaDistancia < distancias.get(cancionVecina)) {
                    distancias.put(cancionVecina, nuevaDistancia);
                    cola.offer(new AbstractMap.SimpleEntry<>(cancionVecina, nuevaDistancia));
                }
            }
        }

        return distancias;
    }

    public List<Cancion> rutaMenorCosto(Cancion origen, Cancion destino) {
        Map<Cancion, Double> distancias = dijkstra(origen);
        Map<Cancion, Cancion> padres = new HashMap<>();

        PriorityQueue<Map.Entry<Cancion, Double>> cola = new PriorityQueue<>(
                Comparator.comparingDouble(Map.Entry::getValue)
        );

        for (Cancion cancion : grafo.keySet()) {
            distancias.put(cancion, Double.MAX_VALUE);
        }

        distancias.put(origen, 0.0);
        cola.offer(new AbstractMap.SimpleEntry<>(origen, 0.0));

        while (!cola.isEmpty()) {
            Map.Entry<Cancion, Double> actual = cola.poll();
            Cancion cancionActual = actual.getKey();
            double distActual = actual.getValue();

            if (distActual > distancias.get(cancionActual)) {
                continue;
            }

            for (Map.Entry<Cancion, Double> vecino : grafo.get(cancionActual).entrySet()) {
                Cancion cancionVecina = vecino.getKey();
                double peso = vecino.getValue();
                double nuevaDistancia = distActual + peso;

                if (nuevaDistancia < distancias.get(cancionVecina)) {
                    distancias.put(cancionVecina, nuevaDistancia);
                    padres.put(cancionVecina, cancionActual);
                    cola.offer(new AbstractMap.SimpleEntry<>(cancionVecina, nuevaDistancia));
                }
            }
        }

        List<Cancion> ruta = new ArrayList<>();
        Cancion actual = destino;

        while (actual != null) {
            ruta.add(0, actual);
            actual = padres.get(actual);
        }

        return ruta;
    }

    public int getCantidadCanciones() {
        return grafo.size();
    }

    public Map<Cancion, Map<Cancion, Double>> getGrafo() {
        return grafo;
    }
}