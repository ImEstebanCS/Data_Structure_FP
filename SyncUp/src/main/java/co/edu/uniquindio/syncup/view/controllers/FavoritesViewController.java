package co.edu.uniquindio.syncup.view.controllers;

import co.edu.uniquindio.syncup.Controller.PlaylistController;
import co.edu.uniquindio.syncup.Controller.RadioController;
import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import co.edu.uniquindio.syncup.Model.Entidades.Playlist;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import co.edu.uniquindio.syncup.Service.MusicPlayer;
import co.edu.uniquindio.syncup.SyncUpApp;
import co.edu.uniquindio.syncup.utils.PlaylistExporter;
import co.edu.uniquindio.syncup.utils.SessionManager;
import co.edu.uniquindio.syncup.utils.UIComponents;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Window;

import java.util.List;

public class FavoritesViewController {

    @FXML private Label totalFavoritosLabel;
    @FXML private VBox favoritosContainer;
    @FXML private Button exportarJSONBtn;
    @FXML private Button exportarTXTBtn;
    @FXML private Button exportarCSVBtn;

    private PlaylistController playlistController;
    private RadioController radioController;
    private MusicPlayer musicPlayer;
    private Usuario usuarioActual;

    // ✅ MÉTODO UNIFICADO - Solo un setControllers
    public void setControllers(PlaylistController playlistController) {
        System.out.println("❤️ [FavoritesViewController] Inicializando...");
        this.playlistController = playlistController;
        this.radioController = SyncUpApp.getRadioController();
        this.musicPlayer = SyncUpApp.getMusicPlayer();
        inicializar();
    }

    private void inicializar() {
        usuarioActual = SessionManager.getInstance().getUsuarioActual();

        if (usuarioActual == null) {
            System.out.println("⚠️ No hay usuario en sesión");
            return;
        }

        System.out.println("✅ Usuario cargado: " + usuarioActual.getNombre());
        cargarFavoritos();
    }

    private void cargarFavoritos() {
        if (favoritosContainer == null) {
            System.out.println("⚠️ favoritosContainer es null");
            return;
        }

        favoritosContainer.getChildren().clear();

        List<Cancion> favoritos = usuarioActual.getListaFavoritos();

        if (favoritos == null || favoritos.isEmpty()) {
            totalFavoritosLabel.setText("0 canciones favoritas");
            Label mensajeVacio = new Label("No tienes canciones favoritas aún");
            mensajeVacio.setStyle("-fx-text-fill: #B3B3B3; -fx-font-size: 14px;");
            favoritosContainer.getChildren().add(mensajeVacio);

            // Deshabilitar botones de exportación
            if (exportarJSONBtn != null) exportarJSONBtn.setDisable(true);
            if (exportarTXTBtn != null) exportarTXTBtn.setDisable(true);
            if (exportarCSVBtn != null) exportarCSVBtn.setDisable(true);

            System.out.println("⚠️ No hay favoritos para mostrar");
            return;
        }

        totalFavoritosLabel.setText(favoritos.size() + " canciones favoritas");
        System.out.println("✅ Cargando " + favoritos.size() + " canciones favoritas");

        int posicion = 1;
        for (Cancion cancion : favoritos) {
            HBox cancionItem = crearCancionItem(cancion, posicion);
            favoritosContainer.getChildren().add(cancionItem);
            posicion++;
        }

        // Habilitar botones de exportación
        if (exportarJSONBtn != null) exportarJSONBtn.setDisable(false);
        if (exportarTXTBtn != null) exportarTXTBtn.setDisable(false);
        if (exportarCSVBtn != null) exportarCSVBtn.setDisable(false);

        System.out.println("✅ Favoritos cargados correctamente");
    }

    private HBox crearCancionItem(Cancion cancion, int posicion) {
        HBox item = new HBox(15);
        item.setAlignment(Pos.CENTER_LEFT);
        item.getStyleClass().add("list-item");
        item.setPadding(new Insets(15));
        item.setPrefHeight(80);

        Label posLabel = new Label(String.valueOf(posicion));
        posLabel.setStyle("-fx-text-fill: #B3B3B3; -fx-font-size: 18px; -fx-font-weight: bold;");
        posLabel.setMinWidth(40);
        posLabel.setAlignment(Pos.CENTER);

        Label icono = new Label("❤️");
        icono.setStyle("-fx-font-size: 32px;");

        VBox infoBox = new VBox(5);
        Label titulo = new Label(cancion.getTitulo());
        titulo.getStyleClass().add("list-item-title");
        titulo.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 15px; -fx-font-weight: bold;");

        Label artista = new Label(cancion.getArtista() + " • " + cancion.getGenero() + " • " + cancion.getAño());
        artista.getStyleClass().add("list-item-subtitle");
        artista.setStyle("-fx-text-fill: #B3B3B3; -fx-font-size: 13px;");

        infoBox.getChildren().addAll(titulo, artista);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox botonesBox = new HBox(10);
        botonesBox.setAlignment(Pos.CENTER_RIGHT);

        Button playBtn = UIComponents.crearBotonCircular("▶", "#1DB954", () -> reproducirCancion(cancion), 40);
        Button quitarBtn = UIComponents.crearBotonCircular("💔", "#E91429", () -> quitarDeFavoritos(cancion), 40);
        Button radioBtn = UIComponents.crearBotonCircular("📻", "#FFA500", () -> iniciarRadio(cancion), 40);

        botonesBox.getChildren().addAll(playBtn, quitarBtn, radioBtn);

        item.getChildren().addAll(posLabel, icono, infoBox, spacer, botonesBox);

        item.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                reproducirCancion(cancion);
            }
        });

        return item;
    }

    @FXML
    private void exportarJSON() {
        List<Cancion> favoritos = usuarioActual.getListaFavoritos();
        if (favoritos != null && !favoritos.isEmpty()) {
            Playlist playlistTemp = new Playlist("Favoritos_" + usuarioActual.getUsername(), usuarioActual);
            for (Cancion c : favoritos) {
                playlistTemp.agregarCancion(c);
            }

            Window window = favoritosContainer.getScene().getWindow();
            PlaylistExporter.exportarPlaylistJSON(playlistTemp, window);
            System.out.println("✅ Exportado a JSON");
        }
    }

    @FXML
    private void exportarTXT() {
        List<Cancion> favoritos = usuarioActual.getListaFavoritos();
        if (favoritos != null && !favoritos.isEmpty()) {
            Playlist playlistTemp = new Playlist("Favoritos_" + usuarioActual.getUsername(), usuarioActual);
            for (Cancion c : favoritos) {
                playlistTemp.agregarCancion(c);
            }

            Window window = favoritosContainer.getScene().getWindow();
            PlaylistExporter.exportarPlaylistTXT(playlistTemp, window);
            System.out.println("✅ Exportado a TXT");
        }
    }

    @FXML
    private void exportarCSV() {
        List<Cancion> favoritos = usuarioActual.getListaFavoritos();
        if (favoritos != null && !favoritos.isEmpty()) {
            Window window = favoritosContainer.getScene().getWindow();
            PlaylistExporter.exportarCancionesCSV(
                    favoritos,
                    "Favoritos_" + usuarioActual.getUsername(),
                    window
            );
            System.out.println("✅ Exportado a CSV");
        }
    }

    private void quitarDeFavoritos(Cancion cancion) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Quitar '" + cancion.getTitulo() + "' de tus favoritos?");

        if (confirmacion.showAndWait().get() == ButtonType.OK) {
            playlistController.quitarFavorito(usuarioActual, cancion);
            cargarFavoritos();
            UIComponents.mostrarAlertaPersonalizada(
                    "Eliminado de Favoritos",
                    cancion.getTitulo() + " eliminado de favoritos",
                    "💔"
            );
            System.out.println("💔 Eliminado de favoritos: " + cancion.getTitulo());
        }
    }

    private void reproducirCancion(Cancion cancion) {
        if (musicPlayer != null) {
            musicPlayer.reproducir(cancion);
            UIComponents.mostrarAlertaPersonalizada(
                    "Reproduciendo en YouTube",
                    "🎵 " + cancion.getTitulo() + "\n" +
                            "🎤 " + cancion.getArtista() + "\n" +
                            "🎸 " + cancion.getGenero() + "\n\n" +
                            "Se abrirá YouTube en tu navegador",
                    "▶️"
            );
            System.out.println("▶️ Reproduciendo: " + cancion.getTitulo());
        } else {
            UIComponents.mostrarAlertaPersonalizada("Error", "El reproductor no está disponible", "❌");
        }
    }

    private void iniciarRadio(Cancion cancion) {
        radioController.iniciarRadio(usuarioActual, cancion);
        UIComponents.mostrarAlertaPersonalizada(
                "Radio Iniciada",
                "Radio iniciada desde:\n" + cancion.getTitulo() + "\n\n" +
                        "Se generó una cola de reproducción con canciones similares",
                "📻"
        );
        System.out.println("📻 Radio iniciada desde: " + cancion.getTitulo());
    }
}