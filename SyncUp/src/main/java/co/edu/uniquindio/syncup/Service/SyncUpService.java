package co.edu.uniquindio.syncup.Service;

import co.edu.uniquindio.syncup.Model.Entidades.*;
import co.edu.uniquindio.syncup.Model.Grafos.GrafoDeSimilitud;
import co.edu.uniquindio.syncup.Model.Grafos.GrafoSocial;
import co.edu.uniquindio.syncup.Model.Trie.TrieAutocompletado;

import java.io.*;
import java.util.*;

/**
 * Servicio principal que integra todas las funcionalidades de SyncUp
 */

import java.util.*;

public class SyncUpService {
    private CatalogoCanciones catalogoCanciones;
    private Map<String, Usuario> usuarios;
    private Map<String, Administrador> administradores;
    private TrieAutocompletado trieAutocompletado;
    private GrafoDeSimilitud grafoDesimilitud;
    private GrafoSocial grafoSocial;
    private Map<Usuario, Playlist> playlists;
    private Map<Usuario, Radio> radios;

    public SyncUpService() {
        this.catalogoCanciones = new CatalogoCanciones();
        this.usuarios = new HashMap<>();
        this.administradores = new HashMap<>();
        this.trieAutocompletado = new TrieAutocompletado();
        this.grafoDesimilitud = new GrafoDeSimilitud();
        this.grafoSocial = new GrafoSocial();
        this.playlists = new HashMap<>();
        this.radios = new HashMap<>();
    }

    // ============= MÉTODOS DE USUARIO =============

    public boolean registrarUsuario(String username, String password, String nombre) {
        if (usuarios.containsKey(username)) {
            return false;
        }

        Usuario nuevoUsuario = new Usuario(username, password, nombre);
        usuarios.put(username, nuevoUsuario);
        grafoSocial.agregarUsuario(nuevoUsuario);
        radios.put(nuevoUsuario, new Radio(nombre + "'s Radio"));

        return true;
    }

    public Usuario autenticarUsuario(String username, String password) {
        Usuario usuario = usuarios.get(username);

        if (usuario != null && usuario.getPassword().equals(password)) {
            return usuario;
        }

        return null;
    }

    public List<Usuario> listarUsuarios() {
        return new ArrayList<>(usuarios.values());
    }

    public boolean eliminarUsuario(String username) {
        return usuarios.remove(username) != null;
    }

    public List<Usuario> obtenerSugerenciasDeUsuarios(Usuario usuario, int cantidad) {
        List<Usuario> sugerencias = grafoSocial.encontrarAmigosDeAmigos(usuario);
        return sugerencias.size() > cantidad ? sugerencias.subList(0, cantidad) : sugerencias;
    }

    // ============= MÉTODOS DE CANCIÓN =============

    public void agregarCancion(int id, String titulo, String artista, String genero, int año, double duracion) {
        Cancion cancion = new Cancion(id, titulo, artista, genero, año, duracion);
        catalogoCanciones.agregarCancion(cancion);
        trieAutocompletado.insertarCancion(titulo, cancion);
        grafoDesimilitud.agregarCancion(cancion);
    }

    public void actualizarCancion(int id, String titulo, String artista, String genero, int año, double duracion) {
        Cancion cancionActualizada = new Cancion(id, titulo, artista, genero, año, duracion);
        catalogoCanciones.actualizarCancion(cancionActualizada);
    }

    public void eliminarCancion(int id) {
        Cancion cancion = catalogoCanciones.buscarPorId(id);
        if (cancion != null) {
            catalogoCanciones.eliminarCancion(cancion);
            trieAutocompletado.eliminarCancion(cancion.getTitulo());
        }
    }

    public List<Cancion> buscarCancionesPorTitulo(String titulo) {
        return catalogoCanciones.buscarPorTitulo(titulo);
    }

    public List<Cancion> buscarCancionesPorArtista(String artista) {
        return catalogoCanciones.buscarPorArtista(artista);
    }

    public List<Cancion> buscarCancionesPorGenero(String genero) {
        return catalogoCanciones.buscarPorGenero(genero);
    }

    public List<Cancion> autocompletarBusqueda(String prefijo) {
        return trieAutocompletado.buscarPorPrefijo(prefijo);
    }

    public List<Cancion> buscarAvanzadaPorAtributos(String artista, String genero, int año) {
        List<Cancion> resultado = new ArrayList<>();

        for (Cancion cancion : catalogoCanciones.getCanciones()) {
            boolean cumpleArtista = artista == null || artista.isEmpty() ||
                    cancion.getArtista().toLowerCase().contains(artista.toLowerCase());
            boolean cumpleGenero = genero == null || genero.isEmpty() ||
                    cancion.getGenero().toLowerCase().contains(genero.toLowerCase());
            boolean cumpleAño = año == 0 || cancion.getAño() == año;

            if (cumpleArtista && cumpleGenero && cumpleAño) {
                resultado.add(cancion);
            }
        }

        return resultado;
    }

    // ============= MÉTODOS DE PLAYLIST =============

    public Playlist crearPlaylist(Usuario usuario, String nombre) {
        Playlist playlist = new Playlist(nombre, usuario);
        playlists.put(usuario, playlist);
        return playlist;
    }

    public void agregarCancionAPlaylist(Usuario usuario, Cancion cancion) {
        if (playlists.containsKey(usuario)) {
            playlists.get(usuario).agregarCancion(cancion);
        }
    }

    public void removerCancionDePlaylist(Usuario usuario, Cancion cancion) {
        if (playlists.containsKey(usuario)) {
            playlists.get(usuario).removerCancion(cancion);
        }
    }

    public Playlist obtenerPlaylist(Usuario usuario) {
        return playlists.get(usuario);
    }

    // ============= MÉTODOS DE FAVORITOS =============

    public void agregarFavorito(Usuario usuario, Cancion cancion) {
        usuario.agregarFavorito(cancion);
    }

    public void removerFavorito(Usuario usuario, Cancion cancion) {
        usuario.removeFavorito(cancion);
    }

    public List<Cancion> obtenerFavoritos(Usuario usuario) {
        return usuario.getListaFavoritos();
    }

    public List<Cancion> descargarFavoritosCSV(Usuario usuario) {
        return new ArrayList<>(usuario.getListaFavoritos());
    }

    // ============= MÉTODOS DE CONEXIÓN SOCIAL =============

    public void seguir(Usuario usuario1, Usuario usuario2) {
        usuario1.seguir(usuario2);
        grafoSocial.agregarConexion(usuario1, usuario2);
    }

    public void dejarDeSeguir(Usuario usuario1, Usuario usuario2) {
        usuario1.dejarDeSeguir(usuario2);
        grafoSocial.removerConexion(usuario1, usuario2);
    }

    public List<Usuario> obtenerSeguidores(Usuario usuario) {
        return new ArrayList<>(usuario.getSeguidores());
    }

    public List<Usuario> obtenerSiguiendo(Usuario usuario) {
        return new ArrayList<>(usuario.getSiguiendo());
    }

    public boolean sonistaConectados(Usuario usuario1, Usuario usuario2) {
        return grafoSocial.sonistaConectados(usuario1, usuario2);
    }

    public int obtenerGradoSeparacion(Usuario usuario1, Usuario usuario2) {
        return grafoSocial.obtenerGradoSeparacion(usuario1, usuario2);
    }

    // ============= MÉTODOS DE RECOMENDACIÓN =============

    public List<Cancion> obtenerCancionesRecomendadas(Cancion cancion, int cantidad) {
        return grafoDesimilitud.obtenerCancionesSimilares(cancion, cantidad);
    }

    public void agregarSimilitud(Cancion cancion1, Cancion cancion2, double similaridad) {
        grafoDesimilitud.agregarArista(cancion1, cancion2, similaridad);
    }

    // ============= MÉTODOS DE RADIO =============

    public Radio crearRadio(Usuario usuario, String nombre) {
        Radio radio = new Radio(nombre);
        radios.put(usuario, radio);
        return radio;
    }

    public void iniciarRadio(Usuario usuario, Cancion cancion) {
        if (radios.containsKey(usuario)) {
            radios.get(usuario).iniciarRadio(cancion);
        }
    }

    public void agregarCancionARadio(Usuario usuario, Cancion cancion) {
        if (radios.containsKey(usuario)) {
            radios.get(usuario).agregarCancionACola(cancion);
        }
    }

    public Cancion siguienteCancionRadio(Usuario usuario) {
        if (radios.containsKey(usuario)) {
            return radios.get(usuario).siguienteCancion();
        }
        return null;
    }

    public Radio obtenerRadio(Usuario usuario) {
        return radios.get(usuario);
    }

    // ============= MÉTODOS DE ADMINISTRADOR =============

    public boolean registrarAdministrador(String username, String password, String nombre) {
        if (administradores.containsKey(username)) {
            return false;
        }

        Administrador admin = new Administrador(username, password, nombre);
        administradores.put(username, admin);
        return true;
    }

    public Administrador autenticarAdministrador(String username, String password) {
        Administrador admin = administradores.get(username);

        if (admin != null && admin.getPassword().equals(password)) {
            return admin;
        }

        return null;
    }

    // ============= GETTERS =============

    public CatalogoCanciones getCatalogoCanciones() {
        return catalogoCanciones;
    }

    public TrieAutocompletado getTrieAutocompletado() {
        return trieAutocompletado;
    }

    public GrafoDeSimilitud getGrafoDesimilitud() {
        return grafoDesimilitud;
    }

    public GrafoSocial getGrafoSocial() {
        return grafoSocial;
    }

    public int getCantidadUsuarios() {
        return usuarios.size();
    }

    public int getCantidadCanciones() {
        return catalogoCanciones.getTamaño();
    }
}