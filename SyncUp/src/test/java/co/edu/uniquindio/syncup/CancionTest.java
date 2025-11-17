package co.edu.uniquindio.syncup;

import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para Cancion (RF-016, RF-018)
 */
class CancionTest {

    private Cancion cancion1;
    private Cancion cancion2;

    @BeforeEach
    void setUp() {
        cancion1 = new Cancion(1, "Bohemian Rhapsody", "Queen", "Rock", 1975, 354);
        cancion2 = new Cancion(2, "Imagine", "John Lennon", "Rock", 1971, 183);
    }

    @Test
    void testHashCodeBasadoEnId() {
        Cancion cancion1Copia = new Cancion(1, "Titulo Diferente", "Otro Artista", "Pop", 2000, 200);

        assertEquals(cancion1.hashCode(), cancion1Copia.hashCode());
    }

    @Test
    void testEqualsBasadoEnId() {
        Cancion cancion1Copia = new Cancion(1, "Titulo Diferente", "Otro Artista", "Pop", 2000, 200);

        assertEquals(cancion1, cancion1Copia);
        assertNotEquals(cancion1, cancion2);
    }

    @Test
    void testAtributosCancion() {
        assertEquals(1, cancion1.getId());
        assertEquals("Bohemian Rhapsody", cancion1.getTitulo());
        assertEquals("Queen", cancion1.getArtista());
        assertEquals("Rock", cancion1.getGenero());
        assertEquals(1975, cancion1.getAño());
        assertEquals(354, cancion1.getDuracion());
    }

    @Test
    void testConstructorConUrls() {
        Cancion cancion = new Cancion(
                3,
                "Test Song",
                "Test Artist",
                "Rock",
                2020,
                180,
                "http://portada.jpg",
                "http://youtube.com/test"
        );

        assertEquals("http://portada.jpg", cancion.getPortadaUrl());
        assertEquals("http://youtube.com/test", cancion.getYoutubeUrl());
    }
}