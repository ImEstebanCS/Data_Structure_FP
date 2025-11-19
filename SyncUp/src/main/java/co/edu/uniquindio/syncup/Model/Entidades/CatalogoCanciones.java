package co.edu.uniquindio.syncup.Model.Entidades;

import java.util.*;
import java.util.concurrent.*;

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

    public List<Cancion> obtenerTodasLasCanciones() {
        return new ArrayList<>(canciones);
    }

    public int obtenerTotal() {
        return canciones.size();
    }

    /**
     * RF-004 y RF-027: Búsqueda avanzada con hilos de ejecución.
     * Busca canciones usando múltiples hilos para optimizar el rendimiento.
     * Soporta operadores lógicos AND y OR según RF-004.
     *
     * @param artista Artista a buscar (null o vacío para ignorar)
     * @param genero Género a buscar (null o vacío para ignorar)
     * @param año Año a buscar (0 para ignorar)
     * @param usarOR true para lógica OR, false para lógica AND
     * @return Lista de canciones que cumplen los criterios
     */
    public List<Cancion> busquedaAvanzada(String artista, String genero, int año, boolean usarOR) {
        if (canciones.isEmpty()) {
            return new ArrayList<>();
        }

        // RF-027: Usar Collections.synchronizedList para acceso thread-safe
        List<Cancion> resultados = Collections.synchronizedList(new ArrayList<>());

        // RF-027: Dividir trabajo en múltiples hilos (máximo 4)
        int numHilos = Math.min(4, canciones.size());
        int cancionesPorHilo = (int) Math.ceil((double) canciones.size() / numHilos);

        ExecutorService executor = Executors.newFixedThreadPool(numHilos);
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < numHilos; i++) {
            int inicio = i * cancionesPorHilo;
            int fin = Math.min((i + 1) * cancionesPorHilo, canciones.size());

            if (inicio >= canciones.size()) {
                break;
            }

            // RF-027: Crear copia real de la sublista para evitar problemas de concurrencia
            final List<Cancion> sublista = new ArrayList<>(canciones.subList(inicio, fin));

            Future<?> future = executor.submit(() -> {
                try {
                    for (Cancion cancion : sublista) {
                        if (evaluarCriterios(cancion, artista, genero, año, usarOR)) {
                            resultados.add(cancion);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error en hilo de búsqueda: " + e.getMessage());
                    e.printStackTrace();
                }
            });

            futures.add(future);
        }

        // RF-027: Esperar a que todos los hilos terminen
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("Error en búsqueda concurrente: " + e.getMessage());
                e.printStackTrace();
            }
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    System.err.println("El ExecutorService no terminó correctamente");
                }
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        return new ArrayList<>(resultados);
    }

    /**
     * RF-004: Evalúa si una canción cumple con los criterios de búsqueda.
     * Soporta operadores lógicos AND y OR.
     * Solo evalúa los criterios que se proporcionaron realmente.
     */
    private boolean evaluarCriterios(Cancion cancion, String artista, String genero, int año, boolean usarOR) {
        // Determinar qué criterios se proporcionaron realmente
        boolean tieneArtista = (artista != null && !artista.trim().isEmpty());
        boolean tieneGenero = (genero != null && !genero.trim().isEmpty());
        boolean tieneAño = (año > 0);

        // Si no se proporcionó ningún criterio, retornar todas las canciones
        if (!tieneArtista && !tieneGenero && !tieneAño) {
            return true;
        }

        // Lista para almacenar los resultados de cada criterio proporcionado
        List<Boolean> resultadosCriterios = new ArrayList<>();

        // Evaluar solo los criterios que se proporcionaron
        if (tieneArtista) {
            boolean cumple = cancion.getArtista().toLowerCase().contains(artista.toLowerCase());
            resultadosCriterios.add(cumple);
        }

        if (tieneGenero) {
            boolean cumple = cancion.getGenero().equalsIgnoreCase(genero);
            resultadosCriterios.add(cumple);
        }

        if (tieneAño) {
            boolean cumple = cancion.getAño() == año;
            resultadosCriterios.add(cumple);
        }

        // RF-004: Aplicar lógica OR o AND según corresponda
        if (usarOR) {
            // OR: al menos uno de los criterios proporcionados debe cumplirse
            return resultadosCriterios.stream().anyMatch(b -> b);
        } else {
            // AND: todos los criterios proporcionados deben cumplirse
            return resultadosCriterios.stream().allMatch(b -> b);
        }
    }
}
