package co.edu.uniquindio.syncup.view.controllers;

import co.edu.uniquindio.syncup.Controller.PlaylistController;
import co.edu.uniquindio.syncup.Controller.RadioController;
import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import co.edu.uniquindio.syncup.Model.Entidades.Playlist;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import co.edu.uniquindio.syncup.Service.MusicPlayer;
import co.edu.uniquindio.syncup.SyncUpApp;
import co.edu.uniquindio.syncup.utils.SessionManager;
import co.edu.uniquindio.syncup.utils.UIComponents;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class RecommendationsViewController {

    @FXML private FlowPane recommendationsPane;
    @FXML private Label infoLabel;

    private PlaylistController playlistController;
    private RadioController radioController;
    private MusicPlayer musicPlayer;
    private Usuario usuarioActual;

    // ✅ MÉTODO UNIFICADO - Solo un setControllers
    public void setControllers(PlaylistController playlistController) {
        System.out.println("🎯 [RecommendationsViewController] Inicializando...");
        this.playlistController = playlistController;
        this.radioController = SyncUpApp.getRadioController();
        this.musicPlayer = SyncUpApp.getMusicPlayer();
        this.usuarioActual = SessionManager.getInstance().getUsuarioActual();

        if (usuarioActual != null) {
            System.out.println("✅ Usuario cargado: " + usuarioActual.getNombre());
            inicializar();
        } else {
            System.out.println("⚠️ No hay usuario en sesión");
        }
    }

    private void inicializar() {
        generarDescubrimiento();
    }

    @FXML
    private void generarDescubrimiento() {
        if (recommendationsPane == null) {
            System.out.println("⚠️ recommendationsPane es null");
            return;
        }

        recommendationsPane.getChildren().clear();
        System.out.println("🎯 Generando descubrimiento semanal...");

        try {
            Playlist descubrimiento = playlistController.generarDescubrimientoSemanal(usuarioActual);

            if (descubrimiento == null || descubrimiento.getCanciones().isEmpty()) {
                if (infoLabel != null) {
                    infoLabel.setText("No hay recomendaciones disponibles");
                }
                System.out.println("⚠️ No se pudieron generar recomendaciones");
                return;
            }

            int totalCanciones = descubrimiento.getCanciones().size();

            if (infoLabel != null) {
                infoLabel.setText("Descubrimiento Semanal: " + totalCanciones +
                        " canciones recomendadas basadas en tus favoritos");
            }

            System.out.println("✅ Generadas " + totalCanciones + " recomendaciones");

            for (Cancion cancion : descubrimiento.getCanciones()) {
                VBox card = UIComponents.crearCancionCard(
                        cancion,
                        () -> reproducirCancion(cancion),
                        () -> agregarAFavoritos(cancion),
                        () -> iniciarRadio(cancion)
                );
                recommendationsPane.getChildren().add(card);
            }

            System.out.println("✅ Cards de recomendaciones creados");

        } catch (Exception e) {
            System.err.println("❌ Error al generar descubrimiento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void reproducirCancion(Cancion cancion) {
        if (musicPlayer != null) {
            musicPlayer.reproducir(cancion);
            UIComponents.mostrarAlertaPersonalizada(
                    "Reproduciendo en YouTube",
                    "🎵 " + cancion.getTitulo() + "\n🎤 " + cancion.getArtista(),
                    "▶️"
            );
            System.out.println("▶️ Reproduciendo: " + cancion.getTitulo());
        } else {
            UIComponents.mostrarAlertaPersonalizada("Error", "El reproductor no está disponible", "❌");
        }
    }

    private void agregarAFavoritos(Cancion cancion) {
        playlistController.agregarFavorito(usuarioActual, cancion);
        UIComponents.mostrarAlertaPersonalizada("Favorito", "Agregado: " + cancion.getTitulo(), "❤️");
        System.out.println("❤️ Agregado a favoritos: " + cancion.getTitulo());
    }

    private void iniciarRadio(Cancion cancion) {
        radioController.iniciarRadio(usuarioActual, cancion);
        UIComponents.mostrarAlertaPersonalizada("Radio Iniciada", "Desde: " + cancion.getTitulo(), "📻");
        System.out.println("📻 Radio iniciada desde: " + cancion.getTitulo());
    }
}