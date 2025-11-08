package co.edu.uniquindio.syncup.Model.Grafos;

import co.edu.uniquindio.syncup.Model.Entidades.Usuario;

import java.util.*;

/**
 * Grafo No Dirigido para modelar conexiones sociales entre usuarios
 * RF-021: Implementado como Grafo No Dirigido
 * RF-022: Usa BFS para encontrar "amigos de amigos"
 */

import java.util.*;

public class GrafoSocial {
    private Map<Usuario, List<Usuario>> grafo;

    public GrafoSocial() {
        this.grafo = new HashMap<>();
    }

    public void agregarUsuario(Usuario usuario) {
        if (!grafo.containsKey(usuario)) {
            grafo.put(usuario, new ArrayList<>());
        }
    }

    public void agregarConexion(Usuario usuario1, Usuario usuario2) {
        agregarUsuario(usuario1);
        agregarUsuario(usuario2);

        if (!grafo.get(usuario1).contains(usuario2)) {
            grafo.get(usuario1).add(usuario2);
        }

        if (!grafo.get(usuario2).contains(usuario1)) {
            grafo.get(usuario2).add(usuario1);
        }
    }

    public void removerConexion(Usuario usuario1, Usuario usuario2) {
        if (grafo.containsKey(usuario1)) {
            grafo.get(usuario1).remove(usuario2);
        }
        if (grafo.containsKey(usuario2)) {
            grafo.get(usuario2).remove(usuario1);
        }
    }

    public List<Usuario> encontrarAmigosDeAmigos(Usuario usuario) {
        List<Usuario> amigosDeAmigos = new ArrayList<>();

        if (!grafo.containsKey(usuario)) {
            return amigosDeAmigos;
        }

        Set<Usuario> visitados = new HashSet<>();
        visitados.add(usuario);

        // Agregar amigos directo a visitados
        for (Usuario amigo : grafo.get(usuario)) {
            visitados.add(amigo);
        }

        // Buscar amigos de amigos
        for (Usuario amigo : grafo.get(usuario)) {
            for (Usuario amigoDelAmigo : grafo.get(amigo)) {
                if (!visitados.contains(amigoDelAmigo)) {
                    amigosDeAmigos.add(amigoDelAmigo);
                    visitados.add(amigoDelAmigo);
                }
            }
        }

        return amigosDeAmigos;
    }

    public List<Usuario> buscarAmigosConBFS(Usuario usuario, int niveles) {
        List<Usuario> amigos = new ArrayList<>();

        if (!grafo.containsKey(usuario)) {
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

            if (nivelActual < niveles) {
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

    public boolean sonistaConectados(Usuario usuario1, Usuario usuario2) {
        if (!grafo.containsKey(usuario1) || !grafo.containsKey(usuario2)) {
            return false;
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

            for (Usuario vecino : grafo.get(actual)) {
                if (!visitados.contains(vecino)) {
                    visitados.add(vecino);
                    cola.offer(vecino);
                }
            }
        }

        return false;
    }

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

        return -1;
    }

    public int getCantidadUsuarios() {
        return grafo.size();
    }

    public Map<Usuario, List<Usuario>> getGrafo() {
        return grafo;
    }
}