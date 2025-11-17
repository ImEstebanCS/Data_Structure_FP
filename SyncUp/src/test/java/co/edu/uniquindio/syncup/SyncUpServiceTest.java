package co.edu.uniquindio.syncup;

import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import co.edu.uniquindio.syncup.Model.Entidades.Playlist;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import co.edu.uniquindio.syncup.Service.SyncUpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para SyncUpService (RF-005)
 */
class SyncUpServiceTest {

    private SyncUpService service;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        service = new SyncUpService();
        service.registrarUsuario("testuser", "pass123", "Test User");
        usuario = service.autenticarUsuario("testuser", "pass123");
    }

    @Test
    void testRegistrarYAutenticarUsuario() {
        assertNotNull(usuario);
        assertEquals("testuser", usuario.getUsername());
        assertEquals("Test User", usuario.getNombre());
    }

    @Test
    void testGenerarDescubrimientoSemanal() {
        List<Cancion> todasCanciones = service.obtenerTodasLasCanciones();
        if (!todasCanciones.isEmpty()) {
            service.agregarFavorito(usuario, todasCanciones.get(0));
        }

        Playlist descubrimiento = service.generarDescubrimientoSemanal(usuario);

        assertNotNull(descubrimiento);
        assertEquals("Descubrimiento Semanal", descubrimiento.getNombre());
    }

    @Test
    void testCrearYGestionarPlaylist() {
        Playlist playlist = service.crearPlaylist(usuario, "Mi Playlist");

        assertNotNull(playlist);
        assertEquals("Mi Playlist", playlist.getNombre());
        assertTrue(usuario.getPlaylists().contains(playlist));
    }

    @Test
    void testBuscarCancionesPorTitulo() {
        List<Cancion> resultados = service.buscarCancionesPorTitulo("Bohemian");

        assertFalse(resultados.isEmpty());
        assertTrue(resultados.get(0).getTitulo().contains("Bohemian"));
    }
}