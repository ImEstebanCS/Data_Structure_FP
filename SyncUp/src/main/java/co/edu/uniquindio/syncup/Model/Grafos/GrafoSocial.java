package co.edu.uniquindio.syncup.Model.Grafos;

import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import java.util.*;

public class GrafoSocial {
    private Map<Usuario, List<Usuario>> adyacencias;

    public GrafoSocial() {
        this.adyacencias = new HashMap<>();
    }

    public void agregarUsuario(Usuario usuario) {
        adyacencias.putIfAbsent(usuario, new ArrayList<>());
    }

    public void eliminarUsuario(Usuario usuario) {
        adyacencias.values().forEach(e -> e.remove(usuario));
        adyacencias.remove(usuario);
    }

    public void seguir(Usuario seguidor, Usuario seguido) {
        adyacencias.putIfAbsent(seguidor, new ArrayList<>());
        adyacencias.putIfAbsent(seguido, new ArrayList<>());

        if (!adyacencias.get(seguidor).contains(seguido)) {
            adyacencias.get(seguidor).add(seguido);
            seguidor.getSiguiendo().add(seguido);
            seguido.getSeguidores().add(seguidor);
        }
    }

    public void dejarDeSeguir(Usuario seguidor, Usuario seguido) {
        if (adyacencias.containsKey(seguidor)) {
            adyacencias.get(seguidor).remove(seguido);
            seguidor.getSiguiendo().remove(seguido);
            seguido.getSeguidores().remove(seguidor);
        }
    }
    /**
     * RF-022: Obtiene sugerencias de usuarios usando BFS.
     * Encuentra "amigos de amigos" que el usuario no sigue actualmente.
     *
     * @param usuario Usuario para el cual buscar sugerencias
     * @param limite Número máximo de sugerencias a retornar
     * @return Lista de usuarios sugeridos
     */
    public List<Usuario> obtenerSeguidores(Usuario usuario) {
        return new ArrayList<>(usuario.getSeguidores());
    }

    public List<Usuario> obtenerSiguiendo(Usuario usuario) {
        return new ArrayList<>(usuario.getSiguiendo());
    }

    public List<Usuario> obtenerSugerenciasDeAmigos(Usuario usuario, int limite) {
        Set<Usuario> sugerencias = new HashSet<>();
        List<Usuario> siguiendo = obtenerSiguiendo(usuario);

        for (Usuario amigo : siguiendo) {
            List<Usuario> amigosDeAmigos = obtenerSiguiendo(amigo);
            for (Usuario sugerido : amigosDeAmigos) {
                if (!sugerido.equals(usuario) && !siguiendo.contains(sugerido)) {
                    sugerencias.add(sugerido);
                }
            }
        }

        List<Usuario> resultado = new ArrayList<>(sugerencias);
        return resultado.subList(0, Math.min(limite, resultado.size()));
    }

    public boolean estanConectados(Usuario usuario1, Usuario usuario2) {
        Set<Usuario> visitados = new HashSet<>();
        Queue<Usuario> cola = new LinkedList<>();

        cola.add(usuario1);
        visitados.add(usuario1);

        while (!cola.isEmpty()) {
            Usuario actual = cola.poll();

            if (actual.equals(usuario2)) {
                return true;
            }

            List<Usuario> vecinos = adyacencias.getOrDefault(actual, new ArrayList<>());
            for (Usuario vecino : vecinos) {
                if (!visitados.contains(vecino)) {
                    visitados.add(vecino);
                    cola.add(vecino);
                }
            }
        }

        return false;
    }

    public int obtenerGradoSeparacion(Usuario usuario1, Usuario usuario2) {
        if (usuario1.equals(usuario2)) {
            return 0;
        }

        Map<Usuario, Integer> distancias = new HashMap<>();
        Queue<Usuario> cola = new LinkedList<>();

        cola.add(usuario1);
        distancias.put(usuario1, 0);

        while (!cola.isEmpty()) {
            Usuario actual = cola.poll();
            int distanciaActual = distancias.get(actual);

            if (actual.equals(usuario2)) {
                return distanciaActual;
            }

            List<Usuario> vecinos = adyacencias.getOrDefault(actual, new ArrayList<>());
            for (Usuario vecino : vecinos) {
                if (!distancias.containsKey(vecino)) {
                    distancias.put(vecino, distanciaActual + 1);
                    cola.add(vecino);
                }
            }
        }

        return -1;
    }
}