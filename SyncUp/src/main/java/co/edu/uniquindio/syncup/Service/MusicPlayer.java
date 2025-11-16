package co.edu.uniquindio.syncup.Service;



import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

public class MusicPlayer {
    private MediaPlayer mediaPlayer;
    private Cancion cancionActual;
    private boolean estaPausado;

    public MusicPlayer() {
        this.mediaPlayer = null;
        this.cancionActual = null;
        this.estaPausado = false;
    }

    /**
     * Reproduce una canción
     */
    public void reproducir(Cancion cancion) {
        if (cancion == null || cancion.getRutaArchivo() == null || cancion.getRutaArchivo().isEmpty()) {
            System.out.println("❌ No hay canción o ruta válida para reproducir");
            return;
        }

        try {
            // Detener la canción actual si hay una
            detener();

            // Crear Media desde la ruta del archivo
            String ruta = cancion.getRutaArchivo();

            Media media;
            if (ruta.startsWith("/")) {
                // Ruta de recursos
                String url = getClass().getResource(ruta).toExternalForm();
                media = new Media(url);
            } else {
                // Ruta de archivo normal
                File archivo = new File(ruta);
                if (!archivo.exists()) {
                    System.out.println("❌ Archivo de audio no encontrado: " + ruta);
                    return;
                }
                media = new Media(archivo.toURI().toString());
            }

            mediaPlayer = new MediaPlayer(media);
            cancionActual = cancion;
            estaPausado = false;

            // Configurar eventos
            mediaPlayer.setOnReady(() -> {
                System.out.println("▶ Reproduciendo: " + cancion.getTitulo() + " - " + cancion.getArtista());
                mediaPlayer.play();
            });

            mediaPlayer.setOnEndOfMedia(() -> {
                System.out.println("✓ Canción terminada: " + cancion.getTitulo());
            });

            mediaPlayer.setOnError(() -> {
                System.out.println("❌ Error al reproducir: " + mediaPlayer.getError().getMessage());
            });

        } catch (Exception e) {
            System.out.println("❌ Error al cargar la canción: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Pausa la reproducción
     */
    public void pausar() {
        if (mediaPlayer != null && estaReproduciendo()) {
            mediaPlayer.pause();
            estaPausado = true;
            System.out.println("⏸ Reproducción pausada");
        }
    }

    /**
     * Continúa la reproducción después de pausar
     */
    public void continuar() {
        if (mediaPlayer != null && estaPausado) {
            mediaPlayer.play();
            estaPausado = false;
            System.out.println("▶ Reproducción continuada");
        }
    }

    /**
     * Alterna entre play y pause
     */
    public void togglePlayPause() {
        if (estaReproduciendo()) {
            pausar();
        } else if (estaPausado) {
            continuar();
        }
    }

    /**
     * Detiene completamente la reproducción y libera recursos
     */
    public void detener() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
            estaPausado = false;
            System.out.println("⏹ Reproducción detenida");
        }
    }

    /**
     * Verifica si está reproduciendo
     */
    public boolean estaReproduciendo() {
        return mediaPlayer != null &&
                mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    /**
     * Ajusta el volumen (0.0 a 1.0)
     */
    public void setVolumen(double volumen) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(Math.max(0.0, Math.min(1.0, volumen)));
        }
    }

    /**
     * Obtiene el volumen actual
     */
    public double getVolumen() {
        return mediaPlayer != null ? mediaPlayer.getVolume() : 0.5;
    }

    /**
     * Obtiene la canción actual
     */
    public Cancion getCancionActual() {
        return cancionActual;
    }

    /**
     * Obtiene el tiempo actual de reproducción en segundos
     */
    public double getTiempoActual() {
        return mediaPlayer != null ? mediaPlayer.getCurrentTime().toSeconds() : 0.0;
    }

    /**
     * Obtiene la duración total en segundos
     */
    public double getDuracionTotal() {
        return mediaPlayer != null ? mediaPlayer.getTotalDuration().toSeconds() : 0.0;
    }

    /**
     * Busca a una posición específica (en segundos)
     */
    public void buscar(double segundos) {
        if (mediaPlayer != null) {
            mediaPlayer.seek(Duration.seconds(segundos));
        }
    }

    /**
     * Verifica si hay una canción cargada
     */
    public boolean hayCancionCargada() {
        return cancionActual != null;
    }
}