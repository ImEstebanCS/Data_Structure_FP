package co.edu.uniquindio.syncup;

import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para Usuario (RF-013, RF-015)
 */
class UsuarioTest {

    private Usuario usuario1;
    private Usuario usuario2;

    @BeforeEach
    void setUp() {
        usuario1 = new Usuario("user1", "pass123", "Usuario Uno");
        usuario2 = new Usuario("user2", "pass456", "Usuario Dos");
    }

    @Test
    void testHashCodeBasadoEnUsername() {
        Usuario usuario1Copia = new Usuario("user1", "differentPass", "Nombre Diferente");

        assertEquals(usuario1.hashCode(), usuario1Copia.hashCode());
    }

    @Test
    void testEqualsBasadoEnUsername() {
        Usuario usuario1Copia = new Usuario("user1", "differentPass", "Nombre Diferente");

        assertEquals(usuario1, usuario1Copia);
        assertNotEquals(usuario1, usuario2);
    }

    @Test
    void testAgregarFavorito() {
        Cancion cancion = new Cancion(1, "Test Song", "Test Artist", "Rock", 2020, 180);

        usuario1.agregarFavorito(cancion);

        assertEquals(1, usuario1.getListaFavoritos().size());
        assertTrue(usuario1.getListaFavoritos().contains(cancion));
    }

    @Test
    void testRemoverFavorito() {
        Cancion cancion = new Cancion(1, "Test Song", "Test Artist", "Rock", 2020, 180);

        usuario1.agregarFavorito(cancion);
        assertEquals(1, usuario1.getListaFavoritos().size());

        usuario1.removerFavorito(cancion);
        assertEquals(0, usuario1.getListaFavoritos().size());
    }

    @Test
    void testSeguirUsuario() {
        usuario1.getSeguidos().add(usuario2);
        usuario2.getSeguidores().add(usuario1);

        assertTrue(usuario1.getSeguidos().contains(usuario2));
        assertTrue(usuario2.getSeguidores().contains(usuario1));
    }
}