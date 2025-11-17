package co.edu.uniquindio.syncup;

import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import co.edu.uniquindio.syncup.Model.Grafos.GrafoDeSimilitud;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para GrafoDeSimilitud (RF-019, RF-020 - Dijkstra)
 */
class GrafoDeSimilitudTest {

    private GrafoDeSimilitud grafo;
    private Cancion cancion1;
    private Cancion cancion2;
    private Cancion cancion3;

    @BeforeEach
    void setUp() {
        grafo = new GrafoDeSimilitud();
        cancion1 = new Cancion(1, "Rock Song 1", "Artist A", "Rock", 1980, 200);
        cancion2 = new Cancion(2, "Rock Song 2", "Artist A", "Rock", 1982, 210);
        cancion3 = new Cancion(3, "Pop Song", "Artist B", "Pop", 2000, 180);
    }

    @Test
    void testAgregarCancion() {
        grafo.agregarCancion(cancion1);

        assertEquals(1, grafo.getCantidadCanciones());
    }

    @Test
    void testAgregarArista() {
        grafo.agregarCancion(cancion1);
        grafo.agregarCancion(cancion2);
        grafo.agregarArista(cancion1, cancion2, 0.8);

        assertEquals(2, grafo.getCantidadCanciones());
    }

    @Test
    void testObtenerCancionesSimilares() {
        grafo.agregarCancion(cancion1);
        grafo.agregarCancion(cancion2);
        grafo.agregarCancion(cancion3);

        grafo.agregarArista(cancion1, cancion2, 10.0);
        grafo.agregarArista(cancion1, cancion3, 50.0);

        List<Cancion> similares = grafo.obtenerCancionesSimilares(cancion1, 2);

        assertNotNull(similares);
        assertTrue(similares.size() <= 2);
        assertFalse(similares.contains(cancion1));
    }

    @Test
    void testDijkstraRutaMenorCosto() {
        grafo.agregarCancion(cancion1);
        grafo.agregarCancion(cancion2);
        grafo.agregarCancion(cancion3);

        grafo.agregarArista(cancion1, cancion2, 10.0);
        grafo.agregarArista(cancion2, cancion3, 15.0);

        List<Cancion> ruta = grafo.rutaMenorCosto(cancion1, cancion3);

        assertNotNull(ruta);
        assertTrue(ruta.contains(cancion1));
        assertTrue(ruta.contains(cancion3));
    }
}