package co.edu.uniquindio.syncup.Service;

import co.edu.uniquindio.syncup.Model.Entidades.*;
import co.edu.uniquindio.syncup.Model.Grafos.GrafoDeSimilitud;
import co.edu.uniquindio.syncup.Model.Grafos.GrafoSocial;
import co.edu.uniquindio.syncup.Model.Trie.TrieAutocompletado;

import java.util.*;

public class SyncUpService {
    private final CatalogoCanciones catalogoCanciones;
    private final Map<String, Usuario> usuarios;
    private final Map<String, Administrador> administradores;
    private final GrafoDeSimilitud grafoDeSimilitud;
    private final GrafoSocial grafoSocial;
    private final TrieAutocompletado trieAutocompletado;

    public SyncUpService() {
        this.catalogoCanciones = new CatalogoCanciones();
        this.usuarios = new HashMap<>();
        this.administradores = new HashMap<>();
        this.grafoDeSimilitud = new GrafoDeSimilitud();
        this.grafoSocial = new GrafoSocial();
        this.trieAutocompletado = new TrieAutocompletado();

        inicializarDatos();
    }

    private void inicializarDatos() {
        System.out.println("Inicializando datos del sistema...");

        Administrador admin = new Administrador("admin", "admin123", "Administrador Principal");
        administradores.put(admin.getUsername(), admin);

        cargarCancionesIniciales();
        construirGrafoDeSimilitud();
        construirTrie();

        System.out.println("✓ Datos inicializados correctamente");
    }

    private void cargarCancionesIniciales() {
        catalogoCanciones.agregarCancion(new Cancion(1, "Bohemian Rhapsody", "Queen", "Rock", 1975, 354,
                "https://i.scdn.co/image/ab67616d0000b273e319baafd16e84f0408af2a0",
                "https://www.youtube.com/watch?v=fJ9rUzIMcZQ"));

        catalogoCanciones.agregarCancion(new Cancion(2, "Imagine", "John Lennon", "Rock", 1971, 183,
                "https://i.scdn.co/image/ab67616d0000b2737b2e7a6e7b7b0e0e0e0e0e0e",
                "https://www.youtube.com/watch?v=YkgkThdzX-8"));

        catalogoCanciones.agregarCancion(new Cancion(3, "Hotel California", "Eagles", "Rock", 1976, 391,
                "https://i.scdn.co/image/ab67616d0000b273e72569c2fbf3b3e8e8e8e8e8",
                "https://www.youtube.com/watch?v=09839DpTctU"));

        catalogoCanciones.agregarCancion(new Cancion(4, "Stairway to Heaven", "Led Zeppelin", "Rock", 1971, 482,
                "https://i.scdn.co/image/ab67616d0000b273c8b444df094279e70cd0ed64",
                "https://www.youtube.com/watch?v=QkF3oxziUI4"));

        catalogoCanciones.agregarCancion(new Cancion(5, "Billie Jean", "Michael Jackson", "Pop", 1982, 294,
                "https://i.scdn.co/image/ab67616d0000b273de1f24209e0b5d2419d5c239",
                "https://www.youtube.com/watch?v=Zi_XLOBDo_Y"));

        catalogoCanciones.agregarCancion(new Cancion(6, "Like a Rolling Stone", "Bob Dylan", "Rock", 1965, 369,
                "https://i.scdn.co/image/ab67616d0000b273df1e48f7d9c4b7f8e8e8e8e8",
                "https://www.youtube.com/watch?v=IwOfCgkyEj0"));

        catalogoCanciones.agregarCancion(new Cancion(7, "Smells Like Teen Spirit", "Nirvana", "Rock", 1991, 301,
                "https://i.scdn.co/image/ab67616d0000b273e175a19e5d8d7e5e5e5e5e5e",
                "https://www.youtube.com/watch?v=hTWKbfoikeg"));

        catalogoCanciones.agregarCancion(new Cancion(8, "What's Going On", "Marvin Gaye", "Soul", 1971, 232,
                "https://i.scdn.co/image/ab67616d0000b273f3f3f3f3f3f3f3f3f3f3f3f3",
                "https://www.youtube.com/watch?v=H-kA3UtBj4M"));

        catalogoCanciones.agregarCancion(new Cancion(9, "Yesterday", "The Beatles", "Pop", 1965, 125,
                "https://i.scdn.co/image/ab67616d0000b273e7e7e7e7e7e7e7e7e7e7e7e7",
                "https://www.youtube.com/watch?v=wXTJBr9tt8Q"));

        catalogoCanciones.agregarCancion(new Cancion(10, "Hey Jude", "The Beatles", "Pop", 1968, 431,
                "https://i.scdn.co/image/ab67616d0000b273f5f5f5f5f5f5f5f5f5f5f5f5",
                "https://www.youtube.com/watch?v=A_MjCqQoLLA"));

        catalogoCanciones.agregarCancion(new Cancion(11, "MAÑANA SERA BONITO", "KAROL G", "Reggaeton", 2023, 225,
                "https://i.scdn.co/image/ab67616d0000b2730c471c36970b9406233842a5",
                "https://www.youtube.com/watch?v=oeDeJZpB6l8"));

        catalogoCanciones.agregarCancion(new Cancion(12, "+57", "KAROL G, Feid", "Reggaeton", 2024, 197,
                "https://i.scdn.co/image/ab67616d0000b27385dc5bc6ea5d5b0b5e5e5e5e",
                "https://www.youtube.com/watch?v=VQdYY6KkJMU"));

        catalogoCanciones.agregarCancion(new Cancion(13, "la luz(fin)", "Kali Uchis, JHAYCO", "R&B", 2023, 218,
                "https://i.scdn.co/image/ab67616d0000b273c0c0c0c0c0c0c0c0c0c0c0c0",
                "https://www.youtube.com/watch?v=example13"));

        catalogoCanciones.agregarCancion(new Cancion(14, "TQG", "KAROL G, Shakira", "Reggaeton", 2023, 202,
                "https://i.scdn.co/image/ab67616d0000b273d4d4d4d4d4d4d4d4d4d4d4d4",
                "https://www.youtube.com/watch?v=RlPNh_PBZb4"));

        System.out.println("✓ " + catalogoCanciones.obtenerTotal() + " canciones cargadas");
    }

    private void construirGrafoDeSimilitud() {
        List<Cancion> canciones = catalogoCanciones.obtenerTodasLasCanciones();

        for (Cancion cancion : canciones) {
            grafoDeSimilitud.agregarCancion(cancion);
        }

<<<<<<< Updated upstream
    /**
     * Configura similitudes iniciales entre canciones del mismo género
     */
    private void configurarSimilitudesIniciales() {
        List<Cancion> canciones = catalogoCanciones.getCanciones();

=======
>>>>>>> Stashed changes
        for (int i = 0; i < canciones.size(); i++) {
            for (int j = i + 1; j < canciones.size(); j++) {
                Cancion c1 = canciones.get(i);
                Cancion c2 = canciones.get(j);

<<<<<<< Updated upstream
                double similitud = calcularSimilitud(c1, c2);
                if (similitud < 0.7) { // Solo agregar si son suficientemente similares
=======
                int similitud = calcularSimilitud(c1, c2);
                if (similitud > 0) {
>>>>>>> Stashed changes
                    grafoDeSimilitud.agregarArista(c1, c2, similitud);
                }
            }
        }

        System.out.println("✓ Grafo de similitud construido");
    }

    private int calcularSimilitud(Cancion c1, Cancion c2) {
        int similitud = 0;

        if (c1.getGenero().equals(c2.getGenero())) {
            similitud += 50;
        }

        if (c1.getArtista().equals(c2.getArtista())) {
            similitud += 30;
        }

        int diferenciaAños = Math.abs(c1.getAño() - c2.getAño());
        if (diferenciaAños <= 5) {
            similitud += (5 - diferenciaAños) * 4;
        }

        return similitud;
    }

    private void construirTrie() {
        for (Cancion cancion : catalogoCanciones.obtenerTodasLasCanciones()) {
            trieAutocompletado.insertar(cancion.getTitulo(), cancion);
        }
        System.out.println("✓ Trie de autocompletado construido");
    }

    // ==================== MÉTODOS DE USUARIOS ====================

    public boolean registrarUsuario(String username, String password, String nombre) {
        if (usuarios.containsKey(username)) {
            return false;
        }

        Usuario nuevoUsuario = new Usuario(username, password, nombre);
        usuarios.put(username, nuevoUsuario);
        grafoSocial.agregarUsuario(nuevoUsuario);

        System.out.println("✓ Usuario registrado: " + username);
        return true;
    }

    public Usuario autenticarUsuario(String username, String password) {
        Usuario usuario = usuarios.get(username);
        if (usuario != null && usuario.getPassword().equals(password)) {
            return usuario;
        }
        return null;
    }

    public void actualizarUsuario(String usernameOriginal, String nuevoUsername, String nuevaPassword, String nuevoNombre) {
        Usuario usuario = usuarios.get(usernameOriginal);

        if (usuario != null) {
            if (!usernameOriginal.equals(nuevoUsername)) {
                usuarios.remove(usernameOriginal);
                usuario.setUsername(nuevoUsername);
                usuarios.put(nuevoUsername, usuario);
            }

            usuario.setPassword(nuevaPassword);
            usuario.setNombre(nuevoNombre);

            System.out.println("✓ Usuario actualizado: " + nuevoUsername);
        }
    }

    public boolean eliminarUsuario(String username) {
        Usuario usuario = usuarios.remove(username);
        if (usuario != null) {
            grafoSocial.eliminarUsuario(usuario);
            System.out.println("✓ Usuario eliminado: " + username);
            return true;
        }
        return false;
    }

    public List<Usuario> listarTodosLosUsuarios() {
        return new ArrayList<>(usuarios.values());
    }

    public List<Usuario> listarUsuarios() {
        return listarTodosLosUsuarios();
    }

    public Usuario obtenerUsuarioPorUsername(String username) {
        return usuarios.get(username);
    }

    public int getCantidadUsuarios() {
        return usuarios.size();
    }

    // ==================== MÉTODOS DE ADMINISTRADORES ====================

    public boolean registrarAdministrador(String username, String password, String nombre) {
        if (administradores.containsKey(username)) {
            return false;
        }

        Administrador nuevoAdmin = new Administrador(username, password, nombre);
        administradores.put(username, nuevoAdmin);

        System.out.println("✓ Administrador registrado: " + username);
        return true;
    }

    public Administrador autenticarAdministrador(String username, String password) {
        Administrador admin = administradores.get(username);
        if (admin != null && admin.getPassword().equals(password)) {
            return admin;
        }
        return null;
    }

    // ==================== MÉTODOS DE GRAFO SOCIAL ====================

    public void seguir(Usuario seguidor, Usuario seguido) {
        seguidor.getSeguidos().add(seguido);
        seguido.getSeguidores().add(seguidor);
        grafoSocial.seguir(seguidor, seguido);
        System.out.println("✓ " + seguidor.getUsername() + " ahora sigue a " + seguido.getUsername());
    }

    public void dejarDeSeguir(Usuario seguidor, Usuario seguido) {
        seguidor.getSeguidos().remove(seguido);
        seguido.getSeguidores().remove(seguidor);
        grafoSocial.dejarDeSeguir(seguidor, seguido);
        System.out.println("✓ " + seguidor.getUsername() + " dejó de seguir a " + seguido.getUsername());
    }

    public List<Usuario> obtenerSeguidores(Usuario usuario) {
        return new ArrayList<>(usuario.getSeguidores());
    }

    public List<Usuario> obtenerSiguiendo(Usuario usuario) {
        return new ArrayList<>(usuario.getSeguidos());
    }

    public List<Usuario> obtenerSugerenciasDeAmigos(Usuario usuario, int limite) {
        return grafoSocial.obtenerSugerenciasDeAmigos(usuario, limite);
    }

    public List<Usuario> obtenerSugerenciasDeUsuarios(Usuario usuarioActual, int cantidad) {
        List<Usuario> todos = listarUsuarios();
        List<Usuario> sugerencias = new ArrayList<>();

        for (Usuario u : todos) {
            if (!u.equals(usuarioActual) && !usuarioActual.getSeguidos().contains(u)) {
                sugerencias.add(u);
                if (sugerencias.size() >= cantidad) break;
            }
        }
        return sugerencias;
    }

    public boolean sonistaConectados(Usuario usuario1, Usuario usuario2) {
        return grafoSocial.estanConectados(usuario1, usuario2);
    }

    public int obtenerGradoSeparacion(Usuario usuario1, Usuario usuario2) {
        return grafoSocial.obtenerGradoSeparacion(usuario1, usuario2);
    }

    // ==================== MÉTODOS DE CANCIONES ====================

    public void agregarCancion(int id, String titulo, String artista, String genero, int año, double duracion) {
        Cancion nuevaCancion = new Cancion(id, titulo, artista, genero, año, duracion,
                "https://via.placeholder.com/150",
                "https://www.youtube.com/results?search_query=" + titulo.replace(" ", "+"));

        catalogoCanciones.agregarCancion(nuevaCancion);
        grafoDeSimilitud.agregarCancion(nuevaCancion);
        trieAutocompletado.insertar(titulo, nuevaCancion);

        for (Cancion existente : catalogoCanciones.obtenerTodasLasCanciones()) {
            if (existente.getId() != id) {
                int similitud = calcularSimilitud(nuevaCancion, existente);
                if (similitud > 0) {
                    grafoDeSimilitud.agregarArista(nuevaCancion, existente, similitud);
                }
            }
        }

        System.out.println("✓ Canción agregada: " + titulo);
    }

    public void actualizarCancion(int id, String nuevoTitulo, String nuevoArtista, String nuevoGenero, int nuevoAño, double nuevaDuracion) {
        Cancion cancion = catalogoCanciones.buscarPorId(id);

        if (cancion != null) {
            cancion.setTitulo(nuevoTitulo);
            cancion.setArtista(nuevoArtista);
            cancion.setGenero(nuevoGenero);
            cancion.setAño(nuevoAño);
            cancion.setDuracion(nuevaDuracion);

            System.out.println("✓ Canción actualizada: " + nuevoTitulo);
        }
    }

    public void eliminarCancion(int id) {
        Cancion cancion = catalogoCanciones.buscarPorId(id);
        if (cancion != null) {
            catalogoCanciones.eliminarCancion(id);
            System.out.println("✓ Canción eliminada: " + cancion.getTitulo());
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

    public List<Cancion> autocompletarCanciones(String prefijo) {
        return trieAutocompletado.autocompletar(prefijo);
    }

    public List<Cancion> autocompletarBusqueda(String prefijo) {
        return autocompletarCanciones(prefijo);
    }

    public List<Cancion> busquedaAvanzada(String artista, String genero, int año, boolean usarOR) {
        return catalogoCanciones.busquedaAvanzada(artista, genero, año, usarOR);
    }

    public List<Cancion> buscarAvanzadaPorAtributos(String artista, String genero, int año, boolean usarOR) {
        return busquedaAvanzada(artista, genero, año, usarOR);
    }

    public List<Cancion> obtenerTodasLasCanciones() {
        return catalogoCanciones.obtenerTodasLasCanciones();
    }

    public int getCantidadCanciones() {
        return catalogoCanciones.obtenerTotal();
    }

<<<<<<< Updated upstream
    public List<Usuario> obtenerSeguidores(Usuario usuario) {
        return usuario != null ? usuario.getSeguidores() : new ArrayList<>();
    }

    public List<Usuario> obtenerSiguiendo(Usuario usuario) {
        return usuario != null ? usuario.getSiguiendo() : new ArrayList<>();
    }

    public boolean sonistaConectados(Usuario usuario1, Usuario usuario2) {
        return grafoSocial.sonistaConectados(usuario1, usuario2);
    }

    public int obtenerGradoSeparacion(Usuario usuario1, Usuario usuario2) {
        return grafoSocial.obtenerGradoSeparacion(usuario1, usuario2);
    }

    // ==================== PLAYLISTS ====================

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

    public Playlist obtenerPlaylist(Usuario usuario) {
        return playlists.get(usuario);
    }

    // ==================== ADMINISTRADOR - RF-010, RF-011, RF-012 ====================

    public boolean registrarAdministrador(String username, String password, String nombre) {
        if (administradores.containsKey(username)) {
            return false;
        }
        administradores.put(username, new Administrador(username, password, nombre));
        return true;
    }

    public Administrador autenticarAdministrador(String username, String password) {
        Administrador admin = administradores.get(username);
        return (admin != null && admin.getPassword().equals(password)) ? admin : null;
    }

    /**
     * RF-010: Gestionar catálogo
     */
    public void agregarCancion(int id, String titulo, String artista, String genero, int año, double duracion) {
        Cancion cancion = new Cancion(id, titulo, artista, genero, año, duracion);
        catalogoCanciones.agregarCancion(cancion);
        trieAutocompletado.insertarCancion(titulo, cancion);
        grafoDeSimilitud.agregarCancion(cancion);
    }

    public void actualizarCancion(int id, String titulo, String artista, String genero, int año, double duracion) {
        Cancion cancion = new Cancion(id, titulo, artista, genero, año, duracion);
        catalogoCanciones.actualizarCancion(cancion);
    }

    public void eliminarCancion(int id) {
        Cancion cancion = catalogoCanciones.buscarPorId(id);
        if (cancion != null) {
            catalogoCanciones.eliminarCancion(cancion);
            trieAutocompletado.eliminarCancion(cancion.getTitulo());
        }
    }

    /**
     * RF-011: Listar y eliminar usuarios
     */
    public List<Usuario> listarUsuarios() {
        return new ArrayList<>(usuarios.values());
    }

    public boolean eliminarUsuario(String username) {
        Usuario usuario = usuarios.remove(username);
        if (usuario != null) {
            playlists.remove(usuario);
            radios.remove(usuario);
            return true;
        }
        return false;
    }

    // ==================== GETTERS ====================

=======
>>>>>>> Stashed changes
    public CatalogoCanciones getCatalogoCanciones() {
        return catalogoCanciones;
    }

    // ==================== MÉTODOS DE PLAYLISTS ====================

    public Playlist crearPlaylist(Usuario usuario, String nombre) {
        Playlist nuevaPlaylist = new Playlist(nombre, usuario);
        usuario.agregarPlaylist(nuevaPlaylist);
        System.out.println("✓ Playlist creada: " + nombre);
        return nuevaPlaylist;
    }

    public void agregarCancionAPlaylist(Usuario usuario, String nombrePlaylist, Cancion cancion) {
        Playlist playlist = obtenerPlaylistPorNombre(usuario, nombrePlaylist);
        if (playlist != null) {
            playlist.agregarCancion(cancion);
            System.out.println("✓ Canción agregada a playlist: " + cancion.getTitulo());
        }
    }

    public void eliminarCancionDePlaylist(Usuario usuario, String nombrePlaylist, Cancion cancion) {
        Playlist playlist = obtenerPlaylistPorNombre(usuario, nombrePlaylist);
        if (playlist != null) {
            playlist.getCanciones().remove(cancion);
            System.out.println("✓ Canción eliminada de playlist");
        }
    }

    public Playlist obtenerPlaylist(Usuario usuario) {
        return usuario.getPlaylistActual();
    }

    public List<Playlist> obtenerPlaylistsDeUsuario(Usuario usuario) {
        if (usuario == null || usuario.getPlaylists() == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(usuario.getPlaylists());
    }

    public Playlist obtenerPlaylistPorNombre(Usuario usuario, String nombre) {
        if (usuario == null || nombre == null || usuario.getPlaylists() == null) {
            return null;
        }

        for (Playlist playlist : usuario.getPlaylists()) {
            if (playlist.getNombre().equals(nombre)) {
                return playlist;
            }
        }

        return null;
    }

    public void eliminarPlaylist(Usuario usuario, String nombrePlaylist) {
        if (usuario == null || nombrePlaylist == null || usuario.getPlaylists() == null) {
            return;
        }

        Playlist playlistAEliminar = null;
        for (Playlist playlist : usuario.getPlaylists()) {
            if (playlist.getNombre().equals(nombrePlaylist)) {
                playlistAEliminar = playlist;
                break;
            }
        }

        if (playlistAEliminar != null) {
            usuario.getPlaylists().remove(playlistAEliminar);
            System.out.println("✓ Playlist '" + nombrePlaylist + "' eliminada correctamente");
        }
    }

    public void quitarCancionDePlaylist(Playlist playlist, Cancion cancion) {
        if (playlist == null || cancion == null) {
            return;
        }

        playlist.getCanciones().remove(cancion);
        System.out.println("✓ Canción '" + cancion.getTitulo() + "' eliminada de la playlist '" + playlist.getNombre() + "'");
    }

    // ==================== MÉTODOS DE FAVORITOS ====================

    public void agregarFavorito(Usuario usuario, Cancion cancion) {
        usuario.agregarFavorito(cancion);
        System.out.println("✓ Canción agregada a favoritos: " + cancion.getTitulo());
    }

    public void removerFavorito(Usuario usuario, Cancion cancion) {
        usuario.removerFavorito(cancion);
        System.out.println("✓ Canción eliminada de favoritos: " + cancion.getTitulo());
    }

    public List<Cancion> obtenerFavoritos(Usuario usuario) {
        return usuario.getListaFavoritos();
    }

    public Playlist obtenerListaFavoritos(Usuario usuario) {
        if (usuario.getListaFavoritosPlaylist() == null) {
            Playlist favoritos = new Playlist("Favoritos", usuario);
            usuario.setListaFavoritosPlaylist(favoritos);
        }
        return usuario.getListaFavoritosPlaylist();
    }

    // ==================== MÉTODOS DE DESCUBRIMIENTO Y RADIO ====================

    public Playlist generarDescubrimientoSemanal(Usuario usuario) {
        Playlist descubrimiento = new Playlist("Descubrimiento Semanal", usuario);

        List<Cancion> favoritos = usuario.getListaFavoritos();

        if (favoritos.isEmpty()) {
            List<Cancion> todasLasCanciones = catalogoCanciones.obtenerTodasLasCanciones();
            Collections.shuffle(todasLasCanciones);
            for (int i = 0; i < Math.min(10, todasLasCanciones.size()); i++) {
                descubrimiento.agregarCancion(todasLasCanciones.get(i));
            }
            return descubrimiento;
        }

        Set<Cancion> cancionesRecomendadas = new HashSet<>();

        for (Cancion favorita : favoritos) {
            List<Cancion> similares = grafoDeSimilitud.obtenerCancionesSimilares(favorita, 5);
            cancionesRecomendadas.addAll(similares);
        }

        cancionesRecomendadas.removeAll(favoritos);

        List<Cancion> listaRecomendadas = new ArrayList<>(cancionesRecomendadas);
        Collections.shuffle(listaRecomendadas);

        for (int i = 0; i < Math.min(15, listaRecomendadas.size()); i++) {
            descubrimiento.agregarCancion(listaRecomendadas.get(i));
        }

        System.out.println("✓ Descubrimiento semanal generado con " + descubrimiento.getCanciones().size() + " canciones");
        return descubrimiento;
    }

    public Radio iniciarRadio(Usuario usuario, Cancion cancionSemilla) {
        Radio radio = new Radio("Radio de " + cancionSemilla.getTitulo(), cancionSemilla);

        List<Cancion> similares = grafoDeSimilitud.obtenerCancionesSimilares(cancionSemilla, 20);

        for (Cancion cancion : similares) {
            radio.agregarALaCola(cancion);
        }

        usuario.setRadioActual(radio);

        System.out.println("✓ Radio iniciada con " + radio.getColaReproduccion().size() + " canciones");
        return radio;
    }

    public Radio obtenerRadio(Usuario usuario) {
        return usuario.getRadioActual();
    }

    // ==================== MÉTODOS AUXILIARES ====================

    public void guardarUsuarios() {
        System.out.println("✓ Datos guardados (en memoria)");
    }

    public void exportarPlaylist(Playlist playlist, String formato) {
        System.out.println("✓ Playlist '" + playlist.getNombre() + "' exportada en formato: " + formato);
    }
}