package co.edu.uniquindio.syncup.Model.Grafos;

import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import java.util.*;

/**
 * GrafoSocial - RF-021, RF-022
 * Grafo No Dirigido para modelar conexiones entre usuarios
 * Implementa BFS para encontrar "amigos de amigos"
 */
public class GrafoSocial {
    private Map<Usuario, List<Usuario>> grafo;

    public GrafoSocial() {
        this.grafo = new HashMap<>();
    }

    /**
     * Agrega un usuario al grafo
     */
    public void agregarUsuario(Usuario usuario) {
        if (usuario != null && !grafo.containsKey(usuario)) {
            grafo.put(usuario, new ArrayList<>());
        }
    }

    /**
     * Crea una conexión bidireccional entre dos usuarios
     */
    public void agregarConexion(Usuario usuario1, Usuario usuario2) {
        if (usuario1 == null || usuario2 == null || usuario1.equals(usuario2)) {
            return;
        }

        agregarUsuario(usuario1);
        agregarUsuario(usuario2);

        List<Usuario> conexiones1 = grafo.get(usuario1);
        List<Usuario> conexiones2 = grafo.get(usuario2);

        if (!conexiones1.contains(usuario2)) {
            conexiones1.add(usuario2);
        }

        if (!conexiones2.contains(usuario1)) {
            conexiones2.add(usuario1);
        }
    }

    /**
     * Elimina una conexión entre dos usuarios
     */
    public void removerConexion(Usuario usuario1, Usuario usuario2) {
        if (grafo.containsKey(usuario1)) {
            grafo.get(usuario1).remove(usuario2);
        }
        if (grafo.containsKey(usuario2)) {
            grafo.get(usuario2).remove(usuario1);
        }
    }

    /**
     * RF-022: Encuentra amigos de amigos usando BFS
     * Retorna usuarios que están a distancia 2 del usuario dado
     */
    public List<Usuario> encontrarAmigosDeAmigos(Usuario usuario) {
        List<Usuario> amigosDeAmigos = new ArrayList<>();

        if (!grafo.containsKey(usuario)) {
            return amigosDeAmigos;
        }

        Set<Usuario> visitados = new HashSet<>();
        visitados.add(usuario);

        // Agregar amigos directos a visitados
        List<Usuario> amigosDirectos = grafo.get(usuario);
        visitados.addAll(amigosDirectos);

        // Buscar amigos de cada amigo
        for (Usuario amigo : amigosDirectos) {
            if (grafo.containsKey(amigo)) {
                for (Usuario amigoDelAmigo : grafo.get(amigo)) {
                    if (!visitados.contains(amigoDelAmigo)) {
                        amigosDeAmigos.add(amigoDelAmigo);
                        visitados.add(amigoDelAmigo);
                    }
                }
            }
        }

        return amigosDeAmigos;
    }

    /**
     * RF-022: Busca amigos hasta cierto nivel usando BFS
     * @param usuario Usuario inicial
     * @param niveles Número de niveles a explorar
     * @return Lista de usuarios encontrados
     */
    public List<Usuario> buscarAmigosConBFS(Usuario usuario, int niveles) {
        List<Usuario> amigos = new ArrayList<>();

        if (!grafo.containsKey(usuario) || niveles <= 0) {
            return amigos;
        }

        Queue<Map.Entry<Usuario, Integer>> cola = new LinkedList<>();
        Set<Usuario> visitados = new HashSet<>();

        cola.offer(new AbstractMap.SimpleEntry<>(usuario, 0));
        visitados.add(usuario);

        while (!cola.isEmpty()) {
            Map.Entry<Usuario, Integer> entrada = cola.poll();
            Usuario usuarioActual = entrada.getKey();
            int nivelActual = entrada.getValue();

            if (nivelActual > 0 && nivelActual <= niveles) {
                amigos.add(usuarioActual);
            }

            if (nivelActual < niveles && grafo.containsKey(usuarioActual)) {
                for (Usuario vecino : grafo.get(usuarioActual)) {
                    if (!visitados.contains(vecino)) {
                        visitados.add(vecino);
                        cola.offer(new AbstractMap.SimpleEntry<>(vecino, nivelActual + 1));
                    }
                }
            }
        }

        return amigos;
    }

    /**
     * RF-008: Verifica si dos usuarios están conectados (directa o indirectamente)
     */
    public boolean sonistaConectados(Usuario usuario1, Usuario usuario2) {
        if (!grafo.containsKey(usuario1) || !grafo.containsKey(usuario2)) {
            return false;
        }

        if (usuario1.equals(usuario2)) {
            return true;
        }

        Set<Usuario> visitados = new HashSet<>();
        Queue<Usuario> cola = new LinkedList<>();

        cola.offer(usuario1);
        visitados.add(usuario1);

        while (!cola.isEmpty()) {
            Usuario actual = cola.poll();

            if (actual.equals(usuario2)) {
                return true;
            }

            if (grafo.containsKey(actual)) {
                for (Usuario vecino : grafo.get(actual)) {
                    if (!visitados.contains(vecino)) {
                        visitados.add(vecino);
                        cola.offer(vecino);
                    }
                }
            }
        }

        return false;
    }

    /**
     * Calcula el grado de separación entre dos usuarios
     * @return Número de pasos entre usuarios, o -1 si no están conectados
     */
    public int obtenerGradoSeparacion(Usuario usuario1, Usuario usuario2) {
        if (!grafo.containsKey(usuario1) || !grafo.containsKey(usuario2)) {
            return -1;
        }

        if (usuario1.equals(usuario2)) {
            return 0;
        }

        Map<Usuario, Integer> distancias = new HashMap<>();
        Queue<Usuario> cola = new LinkedList<>();

        for (Usuario u : grafo.keySet()) {
            distancias.put(u, -1);
        }

        distancias.put(usuario1, 0);
        cola.offer(usuario1);

        while (!cola.isEmpty()) {
            Usuario actual = cola.poll();

            if (grafo.containsKey(actual)) {
                for (Usuario vecino : grafo.get(actual)) {
                    if (distancias.get(vecino) == -1) {
                        distancias.put(vecino, distancias.get(actual) + 1);
                        cola.offer(vecino);

                        if (vecino.equals(usuario2)) {
                            return distancias.get(vecino);
                        }
                    }
                }
            }
        }

        return -1; // No hay conexión
    }

    /**
     * Obtiene las conexiones directas de un usuario
     */
    public List<Usuario> obtenerConexiones(Usuario usuario) {
        if (grafo.containsKey(usuario)) {
            return new ArrayList<>(grafo.get(usuario));
        }
        return new ArrayList<>();
    }

    public int getCantidadUsuarios() {
        return grafo.size();
    }

    public Map<Usuario, List<Usuario>> getGrafo() {
        return grafo;
    }

    public void limpiar() {
        grafo.clear();
    }
}