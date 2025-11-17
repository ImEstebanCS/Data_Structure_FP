package co.edu.uniquindio.syncup.Service;


import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import java.awt.Desktop;
import java.net.URI;

/**
 * MusicPlayer - Versión YouTube
 * Abre YouTube en el navegador para reproducir canciones
 */
public class MusicPlayer {
    private Cancion cancionActual;

    public MusicPlayer() {
        this.cancionActual = null;
    }

    /**
     * Abre YouTube en el navegador con la canción
     */
    public void reproducir(Cancion cancion) {
        if (cancion == null || cancion.getYoutubeUrl() == null || cancion.getYoutubeUrl().isEmpty()) {
            System.out.println("❌ No hay canción o URL válida para reproducir");
            return;
        }

        try {
            String url = cancion.getYoutubeUrl();

            // Verificar si Desktop es soportado
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();

                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(new URI(url));
                    cancionActual = cancion;
                    System.out.println("🎵 Abriendo YouTube: " + cancion.getTitulo() + " - " + cancion.getArtista());
                    System.out.println("🌐 URL: " + url);
                } else {
                    System.out.println("❌ El navegador no está soportado en este sistema");
                }
            } else {
                System.out.println("❌ Desktop no está soportado en este sistema");
            }

        } catch (Exception e) {
            System.out.println("❌ Error al abrir YouTube: " + e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * Obtiene la canción actual
     */
    public Cancion getCancionActual() {
        return cancionActual;
    }

    /**
     * Detiene (solo guarda null, ya que el navegador se maneja aparte)
     */
    public void detener() {
        cancionActual = null;
        System.out.println("⏹ Reproducción detenida");
    }

    /**
     * Verifica si hay una canción cargada
     */
    public boolean hayCancionCargada() {
        return cancionActual != null;
    }
}