package co.edu.uniquindio.syncup.Service;

import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * MusicDataService
 * Servicio para cargar datos de canciones desde archivos JSON locales
 */
public class MusicDataService {
    private final Gson gson;
    private static final String CANCIONES_JSON = "/data/canciones.json";

    public MusicDataService() {
        this.gson = new Gson();
    }

    /**
     * Carga todas las canciones desde el archivo JSON
     * @return Lista de canciones
     */
    public List<Cancion> cargarCanciones() {
        try {
            InputStream inputStream = getClass().getResourceAsStream(CANCIONES_JSON);

            if (inputStream == null) {
                System.err.println("No se pudo encontrar el archivo: " + CANCIONES_JSON);
                return crearCancionesPorDefecto();
            }

            InputStreamReader reader = new InputStreamReader(inputStream);
            Type listType = new TypeToken<CancionesWrapper>(){}.getType();
            CancionesWrapper wrapper = gson.fromJson(reader, listType);

            reader.close();

            return wrapper != null && wrapper.canciones != null ?
                    wrapper.canciones : crearCancionesPorDefecto();

        } catch (Exception e) {
            System.err.println("Error al cargar canciones: " + e.getMessage());
            e.printStackTrace();
            return crearCancionesPorDefecto();
        }
    }

    /**
     * Crea un conjunto de canciones por defecto si no se puede cargar el JSON
     */
    private List<Cancion> crearCancionesPorDefecto() {
        List<Cancion> canciones = new ArrayList<>();

        // Canciones de prueba
        canciones.add(crearCancion(1, "Bohemian Rhapsody", "Queen", "A Night at the Opera",
                "Rock", 1975, 355, 95));
        canciones.add(crearCancion(2, "Imagine", "John Lennon", "Imagine",
                "Rock", 1971, 187, 92));
        canciones.add(crearCancion(3, "Hotel California", "Eagles", "Hotel California",
                "Rock", 1976, 390, 90));
        canciones.add(crearCancion(4, "Stairway to Heaven", "Led Zeppelin", "Led Zeppelin IV",
                "Rock", 1971, 482, 93));
        canciones.add(crearCancion(5, "Billie Jean", "Michael Jackson", "Thriller",
                "Pop", 1982, 294, 94));
        canciones.add(crearCancion(6, "Like a Rolling Stone", "Bob Dylan", "Highway 61 Revisited",
                "Rock", 1965, 373, 88));
        canciones.add(crearCancion(7, "Smells Like Teen Spirit", "Nirvana", "Nevermind",
                "Grunge", 1991, 301, 91));
        canciones.add(crearCancion(8, "What's Going On", "Marvin Gaye", "What's Going On",
                "Soul", 1971, 233, 89));
        canciones.add(crearCancion(9, "Yesterday", "The Beatles", "Help!",
                "Pop", 1965, 123, 90));
        canciones.add(crearCancion(10, "Hey Jude", "The Beatles", "Hey Jude",
                "Rock", 1968, 431, 92));

        // Reggaeton moderno
        canciones.add(crearCancion(11, "MAÑANA SERÁ BONITO", "KAROL G", "Mañana Será Bonito",
                "Reggaeton", 2023, 210, 95));
        canciones.add(crearCancion(12, "+57", "KAROL G, Feid", "+57",
                "Reggaeton", 2024, 195, 98));
        canciones.add(crearCancion(13, "la luz(Fin)", "Kali Uchis, JHAYCO", "ORQUÍDEAS",
                "Reggaeton", 2024, 180, 89));
        canciones.add(crearCancion(14, "TQG", "KAROL G, Shakira", "Mañana Será Bonito",
                "Reggaeton", 2023, 200, 96));
        canciones.add(crearCancion(15, "Un x100to", "Bad Bunny, Grupo Frontera", "nadie sabe lo que va a pasar mañana",
                "Regional Mexicano", 2023, 193, 94));

        // Pop actual
        canciones.add(crearCancion(16, "Flowers", "Miley Cyrus", "Endless Summer Vacation",
                "Pop", 2023, 200, 93));
        canciones.add(crearCancion(17, "Anti-Hero", "Taylor Swift", "Midnights",
                "Pop", 2022, 200, 91));
        canciones.add(crearCancion(18, "As It Was", "Harry Styles", "Harry's House",
                "Pop", 2022, 167, 92));
        canciones.add(crearCancion(19, "Blinding Lights", "The Weeknd", "After Hours",
                "Pop", 2019, 200, 95));
        canciones.add(crearCancion(20, "levitating", "Dua Lipa", "Future Nostalgia",
                "Pop", 2020, 203, 93));

        System.out.println("✓ Cargadas " + canciones.size() + " canciones por defecto");
        return canciones;
    }

    private Cancion crearCancion(int id, String titulo, String artista, String album,
                                 String genero, int año, int duracion, int popularidad) {
        Cancion.AudioFeatures features = new Cancion.AudioFeatures(120, 0.75, 0.80);
        String portadaUrl = "https://via.placeholder.com/300x300/1DB954/FFFFFF?text=" +
                titulo.replaceAll(" ", "+");

        return new Cancion(id, titulo, artista, album, genero, año, duracion,
                portadaUrl, popularidad, features);
    }

    /**
     * Clase wrapper para deserialización JSON
     */
    private static class CancionesWrapper {
        List<Cancion> canciones;
    }
}