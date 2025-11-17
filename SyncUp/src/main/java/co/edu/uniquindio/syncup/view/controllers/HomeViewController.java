package co.edu.uniquindio.syncup.view.controllers;

import co.edu.uniquindio.syncup.Controller.*;
import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import co.edu.uniquindio.syncup.Service.MusicPlayer;
import co.edu.uniquindio.syncup.SyncUpApp;
import co.edu.uniquindio.syncup.utils.SessionManager;
import co.edu.uniquindio.syncup.utils.UIComponents;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.*;

public class HomeViewController {

    @FXML private Label welcomeLabel;
    @FXML private FlowPane recentSongsPane;
    @FXML private FlowPane recommendationsPane;

    private UsuarioController usuarioController;
    private CancionController cancionController;
    private PlaylistController playlistController;
    private RadioController radioController;
    private Usuario usuarioActual;
    private MusicPlayer musicPlayer;
    private static List<Cancion> historialReproduccion = new ArrayList<>();

    public void setControllers(UsuarioController usuarioController, CancionController cancionController,
                               PlaylistController playlistController, RadioController radioController) {
        this.usuarioController = usuarioController;
        this.cancionController = cancionController;
        this.playlistController = playlistController;
        this.radioController = radioController;
<<<<<<< Updated upstream

=======
        this.musicPlayer = SyncUpApp.getMusicPlayer();
>>>>>>> Stashed changes
        inicializar();
    }

    private void inicializar() {
        usuarioActual = SessionManager.getInstance().getUsuarioActual();
        if (usuarioActual != null) {
            welcomeLabel.setText("Bienvenido, " + usuarioActual.getNombre());
            cargarContenido();
        }
    }

    private void cargarContenido() {
        cargarCancionesRecientes();
        cargarRecomendaciones();
    }

    private void cargarCancionesRecientes() {
        if (recentSongsPane == null) return;
        recentSongsPane.getChildren().clear();

        List<Cancion> recientes;

        if (historialReproduccion.isEmpty()) {
            // Si no hay historial, mostrar canciones aleatorias
            List<Cancion> todas = cancionController.obtenerTodas();
            recientes = todas.subList(0, Math.min(10, todas.size()));
        } else {
            // Mostrar últimas 10 del historial
            int inicio = Math.max(0, historialReproduccion.size() - 10);
            recientes = new ArrayList<>(historialReproduccion.subList(inicio, historialReproduccion.size()));
            Collections.reverse(recientes);
        }

        for (Cancion cancion : recientes) {
            VBox card = UIComponents.crearCancionCard(
                    cancion,
                    () -> reproducirCancion(cancion),
                    () -> agregarAFavoritos(cancion),
                    () -> iniciarRadio(cancion)
            );
            recentSongsPane.getChildren().add(card);
        }
    }

    private void cargarRecomendaciones() {
        if (recommendationsPane == null) return;
        recommendationsPane.getChildren().clear();

        // Obtener recomendaciones basadas en favoritos
        List<Cancion> favoritos = usuarioActual.getListaFavoritos();
        List<Cancion> todasLasCanciones = cancionController.obtenerTodas();
        List<Cancion> recomendaciones = new ArrayList<>();

        if (favoritos.isEmpty()) {
            // Si no hay favoritos, mostrar canciones aleatorias
            Collections.shuffle(todasLasCanciones);
            recomendaciones = todasLasCanciones.subList(0, Math.min(10, todasLasCanciones.size()));
        } else {
            // Recomendar canciones del mismo género que los favoritos
            Set<String> generosFavoritos = new HashSet<>();
            for (Cancion fav : favoritos) {
                generosFavoritos.add(fav.getGenero());
            }

            for (Cancion cancion : todasLasCanciones) {
                if (generosFavoritos.contains(cancion.getGenero()) && !favoritos.contains(cancion)) {
                    recomendaciones.add(cancion);
                    if (recomendaciones.size() >= 10) break;
                }
            }

            // Si no hay suficientes, completar con canciones aleatorias
            if (recomendaciones.size() < 10) {
                for (Cancion cancion : todasLasCanciones) {
                    if (!recomendaciones.contains(cancion) && !favoritos.contains(cancion)) {
                        recomendaciones.add(cancion);
                        if (recomendaciones.size() >= 10) break;
                    }
                }
            }
        }

        for (Cancion cancion : recomendaciones) {
            VBox card = UIComponents.crearCancionCard(
                    cancion,
                    () -> reproducirCancion(cancion),
                    () -> agregarAFavoritos(cancion),
                    () -> iniciarRadio(cancion)
            );
            recommendationsPane.getChildren().add(card);
        }
    }

    private void reproducirCancion(Cancion cancion) {
        if (musicPlayer != null) {
            musicPlayer.reproducir(cancion);
            agregarAlHistorial(cancion);
            UIComponents.mostrarAlertaPersonalizada(
                    "Reproduciendo en YouTube",
                    "🎵 " + cancion.getTitulo() + "\n🎤 " + cancion.getArtista(),
                    "▶️"
            );
        }
    }

    private void agregarAFavoritos(Cancion cancion) {
        playlistController.agregarFavorito(usuarioActual, cancion);
        UIComponents.mostrarAlertaPersonalizada("Favorito", "Agregado: " + cancion.getTitulo(), "❤️");
    }

    private void iniciarRadio(Cancion cancion) {
        radioController.iniciarRadio(usuarioActual, cancion);
        UIComponents.mostrarAlertaPersonalizada("Radio Iniciada", "Desde: " + cancion.getTitulo(), "📻");
    }

    public static void agregarAlHistorial(Cancion cancion) {
        historialReproduccion.add(cancion);
    }
}