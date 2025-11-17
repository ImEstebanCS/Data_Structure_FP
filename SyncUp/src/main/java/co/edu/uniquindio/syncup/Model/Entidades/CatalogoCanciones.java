package co.edu.uniquindio.syncup.Model.Entidades;

import java.util.*;
import java.util.concurrent.*;

/**
 * Catálogo central de canciones del sistema.
 * Proporciona búsquedas eficientes y concurrentes.
 */
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

    /**
     * RF-027: Búsqueda avanzada con hilos de ejecución.
     * Busca canciones usando múltiples hilos para optimizar el rendimiento.
     *
     * @param artista Artista a buscar (null o vacío para ignorar)
     * @param genero Género a buscar (null o vacío para ignorar)
     * @param año Año a buscar (0 para ignorar)
     * @param usarOR true para lógica OR, false para lógica AND
     * @return Lista de canciones que cumplen los criterios
     */
    public List<Cancion> busquedaAvanzada(String artista, String genero, int año, boolean usarOR) {
        List<Cancion> todasLasCanciones = new ArrayList<>(canciones.values());

        if (todasLasCanciones.isEmpty()) {
            return new ArrayList<>();
        }

        List<Cancion> resultados = Collections.synchronizedList(new ArrayList<>());

        int numHilos = Math.min(4, todasLasCanciones.size());
        int cancionesPorHilo = (int) Math.ceil((double) todasLasCanciones.size() / numHilos);

        ExecutorService executor = Executors.newFixedThreadPool(numHilos);
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < numHilos; i++) {
            int inicio = i * cancionesPorHilo;
            int fin = Math.min((i + 1) * cancionesPorHilo, todasLasCanciones.size());

            if (inicio >= todasLasCanciones.size()) {
                break;
            }

            List<Cancion> sublista = todasLasCanciones.subList(inicio, fin);

            Future<?> future = executor.submit(() -> {
                for (Cancion cancion : sublista) {
                    if (evaluarCriterios(cancion, artista, genero, año, usarOR)) {
                        resultados.add(cancion);
                    }
                }
            });

            futures.add(future);
        }

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("Error en búsqueda concurrente: " + e.getMessage());
            }
        }

        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        return new ArrayList<>(resultados);
    }

    /**
     * Evalúa si una canción cumple con los criterios de búsqueda.
     */
    private boolean evaluarCriterios(Cancion cancion, String artista, String genero, int año, boolean usarOR) {
        boolean cumpleArtista = (artista == null || artista.trim().isEmpty() ||
                cancion.getArtista().toLowerCase().contains(artista.toLowerCase()));
        boolean cumpleGenero = (genero == null || genero.trim().isEmpty() ||
                cancion.getGenero().equalsIgnoreCase(genero));
        boolean cumpleAño = (año == 0 || cancion.getAño() == año);

        if (usarOR) {
            return cumpleArtista || cumpleGenero || cumpleAño;
        } else {
            return cumpleArtista && cumpleGenero && cumpleAño;
        }
    }

    public List<Cancion> obtenerTodasLasCanciones() {
        return new ArrayList<>(canciones.values());
    }

    public int obtenerTotal() {
        return canciones.size();
    }

    public Map<Integer, Cancion> getCanciones() {
        return canciones;
    }
}