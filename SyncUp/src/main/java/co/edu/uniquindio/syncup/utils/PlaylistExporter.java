package co.edu.uniquindio.syncup.utils;

import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import co.edu.uniquindio.syncup.Model.Entidades.Playlist;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PlaylistExporter {

    public static void exportarPlaylistJSON(Playlist playlist, Window window) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportar Playlist");
        fileChooser.setInitialFileName(sanitizarNombre(playlist.getNombre()) + ".json");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivo JSON", "*.json")
        );

        File archivo = fileChooser.showSaveDialog(window);

        if (archivo != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
                String json = convertirPlaylistAJSON(playlist);
                writer.write(json);
                UIComponents.mostrarAlertaPersonalizada(
                        "Exportación Exitosa",
                        "Playlist exportada correctamente a:\n" + archivo.getAbsolutePath(),
                        "✅"
                );
            } catch (IOException e) {
                UIComponents.mostrarAlertaPersonalizada(
                        "Error",
                        "No se pudo exportar la playlist: " + e.getMessage(),
                        "❌"
                );
            }
        }
    }

    public static void exportarPlaylistTXT(Playlist playlist, Window window) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportar Playlist");
        fileChooser.setInitialFileName(sanitizarNombre(playlist.getNombre()) + ".txt");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivo de Texto", "*.txt")
        );

        File archivo = fileChooser.showSaveDialog(window);

        if (archivo != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
                String txt = convertirPlaylistATXT(playlist);
                writer.write(txt);
                UIComponents.mostrarAlertaPersonalizada(
                        "Exportación Exitosa",
                        "Playlist exportada correctamente a:\n" + archivo.getAbsolutePath(),
                        "✅"
                );
            } catch (IOException e) {
                UIComponents.mostrarAlertaPersonalizada(
                        "Error",
                        "No se pudo exportar la playlist: " + e.getMessage(),
                        "❌"
                );
            }
        }
    }

    public static void exportarCancionesCSV(List<Cancion> canciones, String nombreArchivo, Window window) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportar Canciones");
        fileChooser.setInitialFileName(sanitizarNombre(nombreArchivo) + ".csv");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivo CSV", "*.csv")
        );

        File archivo = fileChooser.showSaveDialog(window);

        if (archivo != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
                String csv = convertirCancionesACSV(canciones);
                writer.write(csv);
                UIComponents.mostrarAlertaPersonalizada(
                        "Exportación Exitosa",
                        canciones.size() + " canciones exportadas correctamente a:\n" + archivo.getAbsolutePath(),
                        "✅"
                );
            } catch (IOException e) {
                UIComponents.mostrarAlertaPersonalizada(
                        "Error",
                        "No se pudo exportar las canciones: " + e.getMessage(),
                        "❌"
                );
            }
        }
    }

    private static String convertirPlaylistAJSON(Playlist playlist) {
        StringBuilder json = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        json.append("{\n");
        json.append("  \"nombre\": \"").append(escaparJSON(playlist.getNombre())).append("\",\n");
        json.append("  \"fecha_exportacion\": \"").append(LocalDateTime.now().format(formatter)).append("\",\n");
        json.append("  \"total_canciones\": ").append(playlist.getCanciones().size()).append(",\n");
        json.append("  \"canciones\": [\n");

        List<Cancion> canciones = playlist.getCanciones();
        for (int i = 0; i < canciones.size(); i++) {
            Cancion c = canciones.get(i);
            json.append("    {\n");
            json.append("      \"id\": ").append(c.getId()).append(",\n");
            json.append("      \"titulo\": \"").append(escaparJSON(c.getTitulo())).append("\",\n");
            json.append("      \"artista\": \"").append(escaparJSON(c.getArtista())).append("\",\n");
            json.append("      \"genero\": \"").append(escaparJSON(c.getGenero())).append("\",\n");
            json.append("      \"año\": ").append(c.getAño()).append(",\n");
            json.append("      \"duracion\": ").append(c.getDuracion()).append(",\n");
            json.append("      \"portada_url\": \"").append(escaparJSON(c.getPortadaUrl())).append("\",\n");
            json.append("      \"youtube_url\": \"").append(escaparJSON(c.getYoutubeUrl())).append("\"\n");
            json.append("    }");
            if (i < canciones.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }

        json.append("  ]\n");
        json.append("}\n");

        return json.toString();
    }

    private static String convertirPlaylistATXT(Playlist playlist) {
        StringBuilder txt = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        txt.append("═══════════════════════════════════════════════════════════════\n");
        txt.append("  SYNCUP - PLAYLIST EXPORTADA\n");
        txt.append("═══════════════════════════════════════════════════════════════\n\n");
        txt.append("Playlist: ").append(playlist.getNombre()).append("\n");
        txt.append("Fecha de exportación: ").append(LocalDateTime.now().format(formatter)).append("\n");
        txt.append("Total de canciones: ").append(playlist.getCanciones().size()).append("\n\n");
        txt.append("═══════════════════════════════════════════════════════════════\n\n");

        int posicion = 1;
        for (Cancion c : playlist.getCanciones()) {
            txt.append(String.format("%3d. ", posicion));
            txt.append(c.getTitulo()).append("\n");
            txt.append("     Artista: ").append(c.getArtista()).append("\n");
            txt.append("     Género: ").append(c.getGenero()).append("\n");
            txt.append("     Año: ").append(c.getAño()).append("\n");
            txt.append("     Duración: ").append(formatearDuracion(c.getDuracion())).append("\n");
            txt.append("     YouTube: ").append(c.getYoutubeUrl()).append("\n");
            txt.append("\n");
            posicion++;
        }

        txt.append("═══════════════════════════════════════════════════════════════\n");
        txt.append("  Fin de la playlist\n");
        txt.append("═══════════════════════════════════════════════════════════════\n");

        return txt.toString();
    }

    private static String convertirCancionesACSV(List<Cancion> canciones) {
        StringBuilder csv = new StringBuilder();

        csv.append("ID,Título,Artista,Género,Año,Duración (seg),URL Portada,URL YouTube\n");

        for (Cancion c : canciones) {
            csv.append(c.getId()).append(",");
            csv.append("\"").append(escaparCSV(c.getTitulo())).append("\",");
            csv.append("\"").append(escaparCSV(c.getArtista())).append("\",");
            csv.append("\"").append(escaparCSV(c.getGenero())).append("\",");
            csv.append(c.getAño()).append(",");
            csv.append(c.getDuracion()).append(",");
            csv.append("\"").append(escaparCSV(c.getPortadaUrl())).append("\",");
            csv.append("\"").append(escaparCSV(c.getYoutubeUrl())).append("\"");
            csv.append("\n");
        }

        return csv.toString();
    }

    private static String escaparJSON(String texto) {
        if (texto == null) return "";
        return texto.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private static String escaparCSV(String texto) {
        if (texto == null) return "";
        return texto.replace("\"", "\"\"");
    }

    private static String sanitizarNombre(String nombre) {
        return nombre.replaceAll("[^a-zA-Z0-9_\\-]", "_");
    }

    private static String formatearDuracion(double segundos) {
        int minutos = (int) segundos / 60;
        int segs = (int) segundos % 60;
        return String.format("%d:%02d", minutos, segs);
    }
}