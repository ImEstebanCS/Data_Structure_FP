package co.edu.uniquindio.syncup;

import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import co.edu.uniquindio.syncup.Model.Trie.TrieAutocompletado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para TrieAutocompletado (RF-023, RF-024)
 */
class TrieAutocompletadoTest {

    private TrieAutocompletado trie;

    @BeforeEach
    void setUp() {
        trie = new TrieAutocompletado();
    }

    @Test
    void testAutocompletadoConPrefijo() {
        Cancion c1 = new Cancion(1, "Bohemian Rhapsody", "Queen", "Rock", 1975, 354);
        Cancion c2 = new Cancion(2, "Born to Run", "Bruce Springsteen", "Rock", 1975, 270);
        Cancion c3 = new Cancion(3, "Imagine", "John Lennon", "Rock", 1971, 183);

        trie.insertar(c1.getTitulo(), c1);
        trie.insertar(c2.getTitulo(), c2);
        trie.insertar(c3.getTitulo(), c3);

        List<Cancion> resultado = trie.autocompletar("Bo");

        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().anyMatch(c -> c.getTitulo().equals("Bohemian Rhapsody")));
        assertTrue(resultado.stream().anyMatch(c -> c.getTitulo().equals("Born to Run")));
    }

    @org.junit.Test
    void testAutocompletadoSinResultados() {
        Cancion c1 = new Cancion(1, "Bohemian Rhapsody", "Queen", "Rock", 1975, 354);
        trie.insertar(c1.getTitulo(), c1);

        List<Cancion> resultado = trie.autocompletar("XYZ");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void testAutocompletadoCaseInsensitive() {
        Cancion c1 = new Cancion(1, "Bohemian Rhapsody", "Queen", "Rock", 1975, 354);
        trie.insertar(c1.getTitulo(), c1);

        List<Cancion> resultado1 = trie.autocompletar("boh");
        List<Cancion> resultado2 = trie.autocompletar("BOH");
        List<Cancion> resultado3 = trie.autocompletar("Boh");

        assertEquals(1, resultado1.size());
        assertEquals(1, resultado2.size());
        assertEquals(1, resultado3.size());
    }
}