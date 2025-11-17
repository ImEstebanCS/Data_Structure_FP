package co.edu.uniquindio.syncup.view.controllers;

import co.edu.uniquindio.syncup.Controller.*;
import co.edu.uniquindio.syncup.Model.Entidades.*;
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

    public void setControllers(PlaylistController playlistController) {
        this.playlistController = playlistController;
        this.radioController = SyncUpApp.getRadioController();
        this.musicPlayer = SyncUpApp.getMusicPlayer();
        this.usuarioActual = SessionManager.getInstance().getUsuarioActual();
        inicializar();
    }

    private void inicializar() {
        generarDescubrimiento();
    }

    @FXML
    private void generarDescubrimiento() {
        if (recommendationsPane != null) {
            recommendationsPane.getChildren().clear();
        }

        Playlist descubrimiento = playlistController.generarDescubrimientoSemanal(usuarioActual);

        if (infoLabel != null) {
            infoLabel.setText("Descubrimiento Semanal: " + descubrimiento.getCanciones().size() +
                    " canciones recomendadas basadas en tus favoritos");
        }

        for (Cancion cancion : descubrimiento.getCanciones()) {
            VBox card = UIComponents.crearCancionCard(
                    cancion,
                    () -> reproducirCancion(cancion),
                    () -> agregarAFavoritos(cancion),
                    () -> iniciarRadio(cancion)
            );
            if (recommendationsPane != null) {
                recommendationsPane.getChildren().add(card);
            }
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
}