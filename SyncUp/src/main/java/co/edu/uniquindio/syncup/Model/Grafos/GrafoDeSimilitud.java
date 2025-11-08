package co.edu.uniquindio.syncup.Model.Grafos;

import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import java.util.*;

/**
 * GrafoDeSimilitud - RF-019, RF-020
 * Grafo Ponderado No Dirigido que conecta canciones por similitud
 * Implementa algoritmo de Dijkstra para encontrar rutas de menor costo (mayor similitud)
 */
public class GrafoDeSimilitud {
    private Map<Cancion, Map<Cancion, Double>> grafo;

    public GrafoDeSimilitud() {
        this.grafo = new HashMap<>();
    }

    /**
     * Agrega una canción al grafo
     */
    public void agregarCancion(Cancion cancion) {
        if (cancion != null && !grafo.containsKey(cancion)) {
            grafo.put(cancion, new HashMap<>());
        }
    }

    /**
     * Agrega una arista ponderada entre dos canciones
     * @param origen Primera canción
     * @param destino Segunda canción
     * @param peso Peso de similitud (menor = más similar)
     */
    public void agregarArista(Cancion origen, Cancion destino, double peso) {
        if (origen == null || destino == null) {
            return;
        }

        agregarCancion(origen);
        agregarCancion(destino);

        // Grafo no dirigido: agregar en ambas direcciones
        grafo.get(origen).put(destino, peso);
        grafo.get(destino).put(origen, peso);
    }

    /**
     * Obtiene las canciones más similares usando Dijkstra
     * @param cancion Canción de referencia
     * @param cantidad Número de canciones similares a retornar
     * @return Lista de canciones similares ordenadas por similitud
     */
    public List<Cancion> obtenerCancionesSimilares(Cancion cancion, int cantidad) {
        List<Cancion> similares = new ArrayList<>();

        if (!grafo.containsKey(cancion)) {
            return similares;
        }

        // Ejecutar Dijkstra desde la canción origen
        Map<Cancion, Double> distancias = dijkstra(cancion);

        // Ordenar por distancia y tomar las primeras 'cantidad' canciones
        distancias.entrySet().stream()
                .filter(e -> !e.getKey().equals(cancion)) // Excluir la canción origen
                .sorted(Comparator.comparingDouble(Map.Entry::getValue))
                .limit(cantidad)
                .forEach(e -> similares.add(e.getKey()));

        return similares;
    }

    /**
     * RF-020: Implementación del algoritmo de Dijkstra
     * Encuentra las rutas de menor costo desde una canción origen
     */
    private Map<Cancion, Double> dijkstra(Cancion inicio) {
        Map<Cancion, Double> distancias = new HashMap<>();
        PriorityQueue<Map.Entry<Cancion, Double>> cola = new PriorityQueue<>(
                Comparator.comparingDouble(Map.Entry::getValue)
        );

        // Inicializar distancias como infinito
        for (Cancion cancion : grafo.keySet()) {
            distancias.put(cancion, Double.MAX_VALUE);
        }

        // La distancia al nodo inicial es 0
        distancias.put(inicio, 0.0);
        cola.offer(new AbstractMap.SimpleEntry<>(inicio, 0.0));

        while (!cola.isEmpty()) {
            Map.Entry<Cancion, Double> actual = cola.poll();
            Cancion cancionActual = actual.getKey();
            double distActual = actual.getValue();

            // Si encontramos una distancia mayor, la ignoramos
            if (distActual > distancias.get(cancionActual)) {
                continue;
            }

            // Explorar vecinos
            Map<Cancion, Double> vecinos = grafo.get(cancionActual);
            if (vecinos != null) {
                for (Map.Entry<Cancion, Double> vecino : vecinos.entrySet()) {
                    Cancion cancionVecina = vecino.getKey();
                    double peso = vecino.getValue();
                    double nuevaDistancia = distActual + peso;

                    // Si encontramos un camino más corto, actualizamos
                    if (nuevaDistancia < distancias.get(cancionVecina)) {
                        distancias.put(cancionVecina, nuevaDistancia);
                        cola.offer(new AbstractMap.SimpleEntry<>(cancionVecina, nuevaDistancia));
                    }
                }
            }
        }

        return distancias;
    }

    /**
     * Encuentra la ruta de menor costo entre dos canciones
     */
    public List<Cancion> rutaMenorCosto(Cancion origen, Cancion destino) {
        if (!grafo.containsKey(origen) || !grafo.containsKey(destino)) {
            return new ArrayList<>();
        }

        Map<Cancion, Double> distancias = new HashMap<>();
        Map<Cancion, Cancion> padres = new HashMap<>();
        PriorityQueue<Map.Entry<Cancion, Double>> cola = new PriorityQueue<>(
                Comparator.comparingDouble(Map.Entry::getValue)
        );

        // Inicializar
        for (Cancion cancion : grafo.keySet()) {
            distancias.put(cancion, Double.MAX_VALUE);
        }
        distancias.put(origen, 0.0);
        cola.offer(new AbstractMap.SimpleEntry<>(origen, 0.0));

        while (!cola.isEmpty()) {
            Map.Entry<Cancion, Double> actual = cola.poll();
            Cancion cancionActual = actual.getKey();
            double distActual = actual.getValue();

            if (cancionActual.equals(destino)) {
                break; // Encontramos el destino
            }

            if (distActual > distancias.get(cancionActual)) {
                continue;
            }

            Map<Cancion, Double> vecinos = grafo.get(cancionActual);
            if (vecinos != null) {
                for (Map.Entry<Cancion, Double> vecino : vecinos.entrySet()) {
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
        }

        // Reconstruir ruta
        List<Cancion> ruta = new ArrayList<>();
        Cancion actual = destino;

        while (actual != null) {
            ruta.add(0, actual);
            actual = padres.get(actual);
        }

        // Si la ruta no incluye el origen, no hay camino
        if (ruta.isEmpty() || !ruta.get(0).equals(origen)) {
            return new ArrayList<>();
        }

        return ruta;
    }

    public int getCantidadCanciones() {
        return grafo.size();
    }

    public Map<Cancion, Map<Cancion, Double>> getGrafo() {
        return grafo;
    }

    public void limpiar() {
        grafo.clear();
    }
}