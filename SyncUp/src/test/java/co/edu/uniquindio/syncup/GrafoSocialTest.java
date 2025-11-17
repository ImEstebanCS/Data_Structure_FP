package co.edu.uniquindio.syncup;

import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import co.edu.uniquindio.syncup.Model.Grafos.GrafoSocial;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para GrafoSocial (RF-021, RF-022 - BFS)
 */
class GrafoSocialTest {

    private GrafoSocial grafo;
    private Usuario usuario1;
    private Usuario usuario2;
    private Usuario usuario3;
    private Usuario usuario4;

    @BeforeEach
    void setUp() {
        grafo = new GrafoSocial();
        usuario1 = new Usuario("user1", "pass1", "Usuario 1");
        usuario2 = new Usuario("user2", "pass2", "Usuario 2");
        usuario3 = new Usuario("user3", "pass3", "Usuario 3");
        usuario4 = new Usuario("user4", "pass4", "Usuario 4");

        grafo.agregarUsuario(usuario1);
        grafo.agregarUsuario(usuario2);
        grafo.agregarUsuario(usuario3);
        grafo.agregarUsuario(usuario4);
    }

    @Test
    void testSeguir() {
        grafo.seguir(usuario1, usuario2);

        assertTrue(usuario1.getSeguidos().contains(usuario2));
        assertTrue(usuario2.getSeguidores().contains(usuario1));
    }

    @Test
    void testDejarDeSeguir() {
        grafo.seguir(usuario1, usuario2);
        grafo.dejarDeSeguir(usuario1, usuario2);

        assertFalse(usuario1.getSeguidos().contains(usuario2));
        assertFalse(usuario2.getSeguidores().contains(usuario1));
    }

    @Test
    void testBFSSugerenciasDeAmigos() {
        grafo.seguir(usuario1, usuario2);
        grafo.seguir(usuario2, usuario3);
        grafo.seguir(usuario2, usuario4);

        List<Usuario> sugerencias = grafo.obtenerSugerenciasDeAmigos(usuario1, 10);

        assertTrue(sugerencias.contains(usuario3) || sugerencias.contains(usuario4));
        assertFalse(sugerencias.contains(usuario1));
        assertFalse(sugerencias.contains(usuario2));
    }

    @Test
    void testBFSEstanConectados() {
        grafo.seguir(usuario1, usuario2);
        grafo.seguir(usuario2, usuario3);

        assertTrue(grafo.estanConectados(usuario1, usuario3));
        assertFalse(grafo.estanConectados(usuario1, usuario4));
    }

    @Test
    void testBFSGradoSeparacion() {
        grafo.seguir(usuario1, usuario2);
        grafo.seguir(usuario2, usuario3);
        grafo.seguir(usuario3, usuario4);

        assertEquals(0, grafo.obtenerGradoSeparacion(usuario1, usuario1));
        assertEquals(1, grafo.obtenerGradoSeparacion(usuario1, usuario2));
        assertEquals(2, grafo.obtenerGradoSeparacion(usuario1, usuario3));
        assertEquals(3, grafo.obtenerGradoSeparacion(usuario1, usuario4));
    }
}