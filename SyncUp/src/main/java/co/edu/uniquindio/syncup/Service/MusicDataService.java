package co.edu.uniquindio.syncup.Service;

import co.edu.uniquindio.syncup.Model.Entidades.Cancion;



import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

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
        return crearCancionesPorDefecto();
    }

    /**
     * Crea un conjunto de canciones por defecto si no se puede cargar el JSON
     */
    private List<Cancion> crearCancionesPorDefecto() {
        List<Cancion> canciones = new ArrayList<>();


        canciones.add(crearCancion(1, "Bohemian Rhapsody", "Queen", "A Night at the Opera",
                "Rock", 1975, 355, 95, "https://www.youtube.com/watch?v=fJ9rUzIMcZQ&list=RDfJ9rUzIMcZQ&start_radio=1"));

        canciones.add(crearCancion(2, "Imagine", "John Lennon", "Imagine",
                "Rock", 1971, 187, 92, "https://www.youtube.com/watch?v=aSoAaeMy_Gw&list=RDaSoAaeMy_Gw&start_radio=1"));

        canciones.add(crearCancion(3, "Hotel California", "Eagles", "Hotel California",
                "Rock", 1976, 390, 90, "https://www.youtube.com/watch?v=dLl4PZtxia8&list=RDdLl4PZtxia8&start_radio=1"));

        canciones.add(crearCancion(4, "Stairway to Heaven", "Led Zeppelin", "Led Zeppelin IV",
                "Rock", 1971, 482, 93, "https://www.youtube.com/watch?v=X791IzOwt3Q&list=RDX791IzOwt3Q&start_radio=1"));

        canciones.add(crearCancion(5, "Billie Jean", "Michael Jackson", "Thriller",
                "Pop", 1982, 294, 94, "https://www.youtube.com/watch?v=Zi_XLOBDo_Y&list=RDZi_XLOBDo_Y&start_radio=1"));

        canciones.add(crearCancion(6, "Like a Rolling Stone", "Bob Dylan", "Highway 61 Revisited",
                "Rock", 1965, 373, 88, "https://www.youtube.com/watch?v=IwOfCgkyEj0&list=RDIwOfCgkyEj0&start_radio=1"));

        canciones.add(crearCancion(7, "Smells Like Teen Spirit", "Nirvana", "Nevermind",
                "Grunge", 1991, 301, 91, "https://www.youtube.com/watch?v=hTWKbfoikeg&list=RDhTWKbfoikeg&start_radio=1"));

        canciones.add(crearCancion(8, "What's Going On", "Marvin Gaye", "What's Going On",
                "Soul", 1971, 233, 89, "https://www.youtube.com/watch?v=o5TmORitlKk&list=RDo5TmORitlKk&start_radio=1"));

        canciones.add(crearCancion(9, "Yesterday", "The Beatles", "Help!",
                "Pop", 1965, 123, 90, "https://www.youtube.com/watch?v=TQemQRL_YVQ&list=RDTQemQRL_YVQ&start_radio=1"));

        canciones.add(crearCancion(10, "Hey Jude", "The Beatles", "Hey Jude",
                "Rock", 1968, 431, 92, "https://www.youtube.com/watch?v=A_MjCqQoLLA&list=RDA_MjCqQoLLA&start_radio=1"));

        // Reggaeton moderno
        canciones.add(crearCancion(11, "MAÑANA SERÁ BONITO", "KAROL G", "Mañana Será Bonito",
                "Reggaeton", 2023, 210, 95, "https://www.youtube.com/watch?v=orNpRKOfjzY&list=RDorNpRKOfjzY&start_radio=1"));

        canciones.add(crearCancion(12, "+57", "KAROL G, Feid", "+57",
                "Reggaeton", 2024, 195, 98, "https://www.youtube.com/watch?v=5r5UePOgMQU&list=RD5r5UePOgMQU&start_radio=1"));

        canciones.add(crearCancion(13, "la luz(Fin)", "Kali Uchis, JHAYCO", "ORQUÍDEAS",
                "Reggaeton", 2024, 180, 89, "https://www.youtube.com/watch?v=5KJDlLvjoHQ&list=RD5KJDlLvjoHQ&start_radio=1"));

        canciones.add(crearCancion(14, "TQG", "KAROL G, Shakira", "Mañana Será Bonito",
                "Reggaeton", 2023, 200, 96, "https://www.youtube.com/watch?v=jZGpkLElSu8&list=RDjZGpkLElSu8&start_radio=1"));

        canciones.add(crearCancion(15, "Un x100to", "Bad Bunny, Grupo Frontera", "nadie sabe lo que va a pasar mañana",
                "Regional Mexicano", 2023, 193, 94, "https://www.youtube.com/watch?v=3inw26U-os4&list=RD3inw26U-os4&start_radio=1"));

        // Pop actual
        canciones.add(crearCancion(16, "Flowers", "Miley Cyrus", "Endless Summer Vacation",
                "Pop", 2023, 200, 93, "https://www.youtube.com/watch?v=G7KNmW9a75Y&list=RDG7KNmW9a75Y&start_radio=1" +
                        ""));

        canciones.add(crearCancion(17, "Anti-Hero", "Taylor Swift", "Midnights",
                "Pop", 2022, 200, 91, "https://www.youtube.com/watch?v=b1kbLwvqugk&list=RDb1kbLwvqugk&start_radio=1"));

        canciones.add(crearCancion(18, "As It Was", "Harry Styles", "Harry's House",
                "Pop", 2022, 167, 92, "https://www.youtube.com/watch?v=H5v3kku4y6Q&list=RDH5v3kku4y6Q&start_radio=1"));

        canciones.add(crearCancion(19, "Blinding Lights", "The Weeknd", "After Hours",
                "Pop", 2019, 200, 95, "https://www.youtube.com/watch?v=4NRXx6U8ABQ&list=RD4NRXx6U8ABQ&start_radio=1"));

        canciones.add(crearCancion(20, "levitating", "Dua Lipa", "Future Nostalgia",
                "Pop", 2020, 203, 93, "https://www.youtube.com/watch?v=TUVcZfQe-Kw&list=RDTUVcZfQe-Kw&start_radio=1"));

        System.out.println("✓ Cargadas " + canciones.size() + " canciones por defecto con URLs de YouTube");
        return canciones;
    }

    // ⭐ MODIFICADO - Ahora incluye youtubeUrl
    private Cancion crearCancion(int id, String titulo, String artista, String album,
                                 String genero, int año, int duracion, int popularidad, String youtubeUrl) {
        Cancion.AudioFeatures features = new Cancion.AudioFeatures(120, 0.75, 0.80);
        String portadaUrl = "https://via.placeholder.com/300x300/1DB954/FFFFFF?text=" +
                titulo.replaceAll(" ", "+");

        Cancion cancion = new Cancion(id, titulo, artista, album, genero, año, duracion,
                portadaUrl, popularidad, features);
        cancion.setYoutubeUrl(youtubeUrl);
        return cancion;
    }

    /**
     * Clase wrapper para deserialización JSON
     */
    private static class CancionesWrapper {
        List<Cancion> canciones;
    }
}