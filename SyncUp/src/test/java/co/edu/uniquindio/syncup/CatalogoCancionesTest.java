package co.edu.uniquindio.syncup;

import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import co.edu.uniquindio.syncup.Model.Entidades.CatalogoCanciones;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para CatalogoCanciones (RF-004, RF-027)
 */
class CatalogoCancionesTest {

    private CatalogoCanciones catalogo;

    @BeforeEach
    void setUp() {
        catalogo = new CatalogoCanciones();

        catalogo.agregarCancion(new Cancion(1, "Rock Song", "Queen", "Rock", 1980, 200));
        catalogo.agregarCancion(new Cancion(2, "Pop Song", "Michael Jackson", "Pop", 1985, 210));
        catalogo.agregarCancion(new Cancion(3, "Another Rock", "Queen", "Rock", 1982, 180));
    }

    @Test
    void testBusquedaAvanzadaAND() {
        List<Cancion> resultados = catalogo.busquedaAvanzada("Queen", "Rock", 0, false);

        assertEquals(2, resultados.size());
        assertTrue(resultados.stream().allMatch(c -> c.getArtista().equals("Queen") && c.getGenero().equals("Rock")));
    }

    @Test
    void testBusquedaAvanzadaOR() {
        List<Cancion> resultados = catalogo.busquedaAvanzada("Queen", "Pop", 0, true);

        assertEquals(3, resultados.size());
    }

    @Test
    void testBusquedaAvanzadaPorAño() {
        List<Cancion> resultados = catalogo.busquedaAvanzada("", "", 1980, false);

        assertEquals(1, resultados.size());
        assertEquals(1980, resultados.get(0).getAño());
    }

    @Test
    void testBusquedaAvanzadaConcurrente() {
        for (int i = 4; i <= 100; i++) {
            catalogo.agregarCancion(new Cancion(i, "Song " + i, "Artist " + i, "Rock", 2000 + i, 180));
        }

        List<Cancion> resultados = catalogo.busquedaAvanzada("", "Rock", 0, false);

        assertTrue(resultados.size() >= 2);
    }
}