package co.edu.uniquindio.syncup.Service;

import co.edu.uniquindio.syncup.Model.Entidades.*;
import co.edu.uniquindio.syncup.Model.Grafos.GrafoDeSimilitud;
import co.edu.uniquindio.syncup.Model.Grafos.GrafoSocial;
import co.edu.uniquindio.syncup.Model.Trie.TrieAutocompletado;

import java.util.*;

/**
 * SyncUpService
 * Servicio principal que integra todas las funcionalidades del sistema
 */
public class SyncUpService {
    // RF-014: HashMap para acceso O(1) a usuarios
    private final Map<String, Usuario> usuarios;
    private final Map<String, Administrador> administradores;

    // Estructuras de datos principales
    private final CatalogoCanciones catalogoCanciones;
    private final TrieAutocompletado trieAutocompletado;
    private final GrafoDeSimilitud grafoDeSimilitud;
    private final GrafoSocial grafoSocial;

    // Datos por usuario
    private final Map<Usuario, Playlist> playlists;
    private final Map<Usuario, Radio> radios;

    // Servicio de datos
    private final MusicDataService musicDataService;

    public SyncUpService() {
        this.usuarios = new HashMap<>();
        this.administradores = new HashMap<>();
        this.catalogoCanciones = new CatalogoCanciones();
        this.trieAutocompletado = new TrieAutocompletado();
        this.grafoDeSimilitud = new GrafoDeSimilitud();
        this.grafoSocial = new GrafoSocial();
        this.playlists = new HashMap<>();
        this.radios = new HashMap<>();
        this.musicDataService = new MusicDataService();

        inicializarSistema();
    }

    /**
     * Inicializa el sistema cargando datos y configurando similitudes
     */
    private void inicializarSistema() {
        cargarCancionesIniciales();
        configurarSimilitudesIniciales();
        crearAdministradorPorDefecto();
    }

    /**
     * Carga canciones desde el servicio de datos
     */
    private void cargarCancionesIniciales() {
        List<Cancion> canciones = musicDataService.cargarCanciones();
        for (Cancion cancion : canciones) {
            catalogoCanciones.agregarCancion(cancion);
            trieAutocompletado.insertarCancion(cancion.getTitulo(), cancion);
            grafoDeSimilitud.agregarCancion(cancion);
        }
        System.out.println("✓ Sistema inicializado con " + canciones.size() + " canciones");
    }

    /**
     * Configura similitudes iniciales entre canciones del mismo género
     * Optimizado: solo calcula similitudes para canciones del mismo género o artista
     */
    private void configurarSimilitudesIniciales() {
        List<Cancion> canciones = catalogoCanciones.getCanciones();

        // Optimización: solo comparar canciones que comparten género o artista
        for (int i = 0; i < canciones.size(); i++) {
            Cancion c1 = canciones.get(i);
            
            for (int j = i + 1; j < canciones.size(); j++) {
                Cancion c2 = canciones.get(j);

                // Filtrar: solo calcular si comparten género o artista
                boolean mismoGenero = c1.getGenero().equalsIgnoreCase(c2.getGenero());
                boolean mismoArtista = c1.getArtista().equalsIgnoreCase(c2.getArtista());
                
                if (mismoGenero || mismoArtista) {
                    double similitud = calcularSimilitud(c1, c2);
                    // Umbral ajustado: menor distancia = más similar
                    // Solo agregar si la distancia es menor a 0.6 (más similares)
                    if (similitud < 0.6) {
                        grafoDeSimilitud.agregarArista(c1, c2, similitud);
                    }
                }
            }
        }
    }

    /**
     * Calcula similitud entre dos canciones
     */
    private double calcularSimilitud(Cancion c1, Cancion c2) {
        double sim = 1.0;

        // Mismo género reduce la distancia (aumenta similitud)
        if (c1.getGenero().equalsIgnoreCase(c2.getGenero())) {
            sim -= 0.3;
        }

        // Mismo artista reduce más la distancia
        if (c1.getArtista().equalsIgnoreCase(c2.getArtista())) {
            sim -= 0.4;
        }

        // Años cercanos reducen la distancia
        int difAños = Math.abs(c1.getAño() - c2.getAño());
        if (difAños <= 5) {
            sim -= 0.2;
        }

        return Math.max(0.1, sim); // Mínimo 0.1
    }

    /**
     * Crea un administrador por defecto
     */
    private void crearAdministradorPorDefecto() {
        registrarAdministrador("admin", "admin123", "Administrador");
    }

    // ==================== USUARIOS - RF-001, RF-002 ====================

    /**
     * RF-001: Registrar usuario
     */
    public boolean registrarUsuario(String username, String password, String nombre) {
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            return false;
        }

        if (usuarios.containsKey(username)) {
            return false; // Ya existe
        }

        Usuario nuevoUsuario = new Usuario(username, password, nombre);
        usuarios.put(username, nuevoUsuario);
        grafoSocial.agregarUsuario(nuevoUsuario);
        radios.put(nuevoUsuario, new Radio(nombre + "'s Radio"));

        return true;
    }

    /**
     * RF-001: Autenticar usuario
     */
    public Usuario autenticarUsuario(String username, String password) {
        Usuario usuario = usuarios.get(username);

        if (usuario != null && usuario.getPassword().equals(password)) {
            return usuario;
        }

        return null;
    }

    /**
     * RF-002: Gestionar favoritos
     */
    public void agregarFavorito(Usuario usuario, Cancion cancion) {
        if (usuario != null && cancion != null) {
            usuario.agregarFavorito(cancion);
        }
    }

    public void removerFavorito(Usuario usuario, Cancion cancion) {
        if (usuario != null && cancion != null) {
            usuario.removerFavorito(cancion);
        }
    }

    public List<Cancion> obtenerFavoritos(Usuario usuario) {
        if (usuario != null) {
            return new ArrayList<>(usuario.getListaFavoritos());
        }
        return new ArrayList<>();
    }

    // ==================== BÚSQUEDA - RF-003, RF-004 ====================

    /**
     * RF-003: Buscar por autocompletado usando Trie
     */
    public List<Cancion> autocompletarBusqueda(String prefijo) {
        if (prefijo == null || prefijo.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return trieAutocompletado.buscarPorPrefijo(prefijo.trim());
    }

    /**
     * RF-004: Búsqueda avanzada por múltiples atributos con lógica AND y OR
     * @param artista Filtro por artista (puede ser null o vacío)
     * @param genero Filtro por género (puede ser null o vacío)
     * @param año Filtro por año (0 significa sin filtro)
     * @param usarOR true para usar lógica OR, false para usar lógica AND
     */
    public List<Cancion> buscarAvanzadaPorAtributos(String artista, String genero, int año, boolean usarOR) {
        List<Cancion> resultado = new ArrayList<>();

        // Normalizar valores vacíos
        boolean tieneArtista = artista != null && !artista.trim().isEmpty();
        boolean tieneGenero = genero != null && !genero.trim().isEmpty();
        boolean tieneAño = año > 0;

        // Si no hay ningún filtro, retornar todas las canciones
        if (!tieneArtista && !tieneGenero && !tieneAño) {
            return new ArrayList<>(catalogoCanciones.getCanciones());
        }

        for (Cancion cancion : catalogoCanciones.getCanciones()) {
            boolean cumpleArtista = !tieneArtista ||
                    cancion.getArtista().toLowerCase().contains(artista.toLowerCase().trim());
            boolean cumpleGenero = !tieneGenero ||
                    cancion.getGenero().toLowerCase().contains(genero.toLowerCase().trim());
            boolean cumpleAño = !tieneAño || cancion.getAño() == año;

            boolean cumpleCriterios;
            if (usarOR) {
                // Lógica OR: cumple si al menos uno de los criterios especificados se cumple
                cumpleCriterios = (tieneArtista && cumpleArtista) ||
                                 (tieneGenero && cumpleGenero) ||
                                 (tieneAño && cumpleAño);
            } else {
                // Lógica AND: cumple si todos los criterios especificados se cumplen
                cumpleCriterios = (!tieneArtista || cumpleArtista) &&
                                 (!tieneGenero || cumpleGenero) &&
                                 (!tieneAño || cumpleAño);
            }

            if (cumpleCriterios) {
                resultado.add(cancion);
            }
        }

        return resultado;
    }

    /**
     * RF-004: Búsqueda avanzada por múltiples atributos (método legacy con AND por defecto)
     */
    public List<Cancion> buscarAvanzadaPorAtributos(String artista, String genero, int año) {
        return buscarAvanzadaPorAtributos(artista, genero, año, false);
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

    // ==================== RECOMENDACIONES - RF-005, RF-006 ====================

    /**
     * RF-005: Generar playlist "Descubrimiento Semanal"
     */
    public Playlist generarDescubrimientoSemanal(Usuario usuario) {
        Playlist descubrimiento = new Playlist("Descubrimiento Semanal", usuario,
                "Basado en tus gustos musicales");

        List<Cancion> favoritos = usuario.getListaFavoritos();
        Set<Cancion> recomendadas = new HashSet<>();

        // Obtener recomendaciones basadas en favoritos
        for (Cancion favorita : favoritos) {
            List<Cancion> similares = grafoDeSimilitud.obtenerCancionesSimilares(favorita, 5);
            recomendadas.addAll(similares);
        }

        // Limitar a 30 canciones
        recomendadas.stream()
                .filter(c -> !favoritos.contains(c))
                .limit(30)
                .forEach(descubrimiento::agregarCancion);

        return descubrimiento;
    }

    /**
     * RF-006: Iniciar Radio basada en canción
     */
    public void iniciarRadio(Usuario usuario, Cancion cancionSemilla) {
        if (!radios.containsKey(usuario)) {
            radios.put(usuario, new Radio(usuario.getNombre() + "'s Radio"));
        }

        Radio radio = radios.get(usuario);
        radio.iniciarRadio(cancionSemilla);

        // Agregar canciones similares a la cola
        List<Cancion> similares = grafoDeSimilitud.obtenerCancionesSimilares(cancionSemilla, 20);
        for (Cancion similar : similares) {
            radio.agregarCancionACola(similar);
        }
    }

    public Radio obtenerRadio(Usuario usuario) {
        return radios.get(usuario);
    }

    // ==================== RED SOCIAL - RF-007, RF-008 ====================

    /**
     * RF-007: Seguir/dejar de seguir usuarios
     */
    public void seguir(Usuario usuario1, Usuario usuario2) {
        if (usuario1 != null && usuario2 != null) {
            usuario1.seguir(usuario2);
            grafoSocial.agregarConexion(usuario1, usuario2);
        }
    }

    public void dejarDeSeguir(Usuario usuario1, Usuario usuario2) {
        if (usuario1 != null && usuario2 != null) {
            usuario1.dejarDeSeguir(usuario2);
            grafoSocial.removerConexion(usuario1, usuario2);
        }
    }

    /**
     * RF-008: Sugerencias de usuarios usando BFS
     */
    public List<Usuario> obtenerSugerenciasDeUsuarios(Usuario usuario, int cantidad) {
        List<Usuario> sugerencias = grafoSocial.encontrarAmigosDeAmigos(usuario);
        return sugerencias.size() > cantidad ?
                sugerencias.subList(0, cantidad) : sugerencias;
    }

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
        Cancion existente = catalogoCanciones.buscarPorId(id);

        if (existente != null) {
            // Actualizar solo los campos editables, manteniendo el resto
            existente.setTitulo(titulo);
            existente.setArtista(artista);
            existente.setGenero(genero);
            existente.setAño(año);
            existente.setDuracion(duracion);

            // No es necesario llamar a actualizarCancion porque ya modificamos el objeto existente
            // Pero si quieres mantener consistencia con tu diseño:
            catalogoCanciones.actualizarCancion(existente);
        } else {
            throw new IllegalArgumentException("No se encontró la canción con ID: " + id);
        }
    }
    public void eliminarCancion(int id) {
        Cancion cancion = catalogoCanciones.buscarPorId(id);
        if (cancion != null) {
            catalogoCanciones.eliminarCancion(cancion);
            trieAutocompletado.eliminarCancion(cancion.getTitulo());
        }
    }

    /**
     * RF-011: Listar y eliminar usuarios,actualizar
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
    /**
     * Actualizar datos de un usuario existente
     * Si se cambia el username, se elimina el viejo y se crea uno nuevo
     */
    public void actualizarUsuario(String usernameAntiguo, String usernameNuevo, String password, String nombre) {
        Usuario usuario = usuarios.get(usernameAntiguo);

        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado: " + usernameAntiguo);
        }

        // Si el username cambió, hay que manejar el HashMap
        if (!usernameAntiguo.equals(usernameNuevo)) {
            // Verificar que el nuevo username no exista
            if (usuarios.containsKey(usernameNuevo)) {
                throw new IllegalArgumentException("El username '" + usernameNuevo + "' ya existe");
            }

            // Remover el usuario con la clave antigua
            usuarios.remove(usernameAntiguo);

            // Actualizar el username del objeto
            usuario.setUsername(usernameNuevo);
            usuario.setPassword(password);
            usuario.setNombre(nombre);

            // Agregar con la nueva clave
            usuarios.put(usernameNuevo, usuario);

            // Actualizar el grafo social si es necesario
            // (esto depende de cómo esté implementado tu grafoSocial)
        } else {
            // Solo actualizar password y nombre
            usuario.setPassword(password);
            usuario.setNombre(nombre);
        }
    }

    // ==================== GETTERS ====================

    public CatalogoCanciones getCatalogoCanciones() {
        return catalogoCanciones;
    }

    public int getCantidadCanciones() {
        return catalogoCanciones.getTamaño();
    }

    public int getCantidadUsuarios() {
        return usuarios.size();
    }

    public GrafoDeSimilitud getGrafoDeSimilitud() {
        return grafoDeSimilitud;
    }

    public GrafoSocial getGrafoSocial() {
        return grafoSocial;
    }

    public TrieAutocompletado getTrieAutocompletado() {
        return trieAutocompletado;
    }
}