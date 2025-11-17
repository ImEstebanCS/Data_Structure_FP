package co.edu.uniquindio.syncup.view.controllers;

import co.edu.uniquindio.syncup.Controller.*;
import co.edu.uniquindio.syncup.Model.Entidades.*;
import co.edu.uniquindio.syncup.Service.MusicPlayer;
import co.edu.uniquindio.syncup.SyncUpApp;
import co.edu.uniquindio.syncup.utils.SessionManager;
import co.edu.uniquindio.syncup.utils.UIComponents;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class LibraryViewController {

    @FXML private TextField nombrePlaylistField;
    @FXML private ListView<String> playlistsListView;
    @FXML private Label playlistTituloLabel;
    @FXML private Label totalCancionesLabel;
    @FXML private VBox cancionesContainer;
    @FXML private Button exportarJSONBtn;
    @FXML private Button exportarTXTBtn;

    private PlaylistController playlistController;
    private CancionController cancionController;
<<<<<<< Updated upstream
    private PlaylistController playlistController;
=======
    private RadioController radioController;
    private Usuario usuarioActual;
    private Playlist playlistSeleccionada;
    private MusicPlayer musicPlayer;
>>>>>>> Stashed changes

    public void setControllers(UsuarioController usuarioController, CancionController cancionController,
                               PlaylistController playlistController, RadioController radioController) {
        this.cancionController = cancionController;
        this.playlistController = playlistController;
<<<<<<< Updated upstream
=======
        this.radioController = radioController;
        this.usuarioActual = SessionManager.getInstance().getUsuarioActual();
        this.musicPlayer = SyncUpApp.getMusicPlayer();
>>>>>>> Stashed changes
        inicializar();
    }

    private void inicializar() {
        cargarPlaylists();

        playlistsListView.getSelectionModel().selectedItemProperty().addListener((obs, old, nuevo) -> {
            if (nuevo != null) {
                cargarPlaylistSeleccionada(nuevo);
            }
        });
    }

    private void cargarPlaylists() {
        playlistsListView.getItems().clear();
        for (Playlist p : usuarioActual.getPlaylists()) {
            playlistsListView.getItems().add(p.getNombre());
        }
    }

    private void cargarPlaylistSeleccionada(String nombre) {
        for (Playlist p : usuarioActual.getPlaylists()) {
            if (p.getNombre().equals(nombre)) {
                playlistSeleccionada = p;
                playlistTituloLabel.setText(p.getNombre());
                totalCancionesLabel.setText(p.getCanciones().size() + " canciones");
                exportarJSONBtn.setDisable(false);
                exportarTXTBtn.setDisable(false);

                cancionesContainer.getChildren().clear();
                for (Cancion c : p.getCanciones()) {
                    cancionesContainer.getChildren().add(crearCancionItem(c));
                }
                break;
            }
        }
    }

    private HBox crearCancionItem(Cancion cancion) {
        HBox item = new HBox(15);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setStyle("-fx-background-color: rgba(40, 40, 40, 0.6); -fx-background-radius: 8; -fx-padding: 10;");
        item.setPrefHeight(60);

        Label info = new Label(cancion.getTitulo() + " - " + cancion.getArtista());
        info.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 14px;");
        HBox.setHgrow(info, Priority.ALWAYS);

        Button playBtn = new Button("▶");
        playBtn.setStyle("-fx-background-color: #1DB954; -fx-text-fill: white; -fx-background-radius: 20; -fx-pref-width: 35; -fx-pref-height: 35; -fx-cursor: hand;");
        playBtn.setOnAction(e -> musicPlayer.reproducir(cancion));

        Button removeBtn = new Button("🗑");
        removeBtn.setStyle("-fx-background-color: #E91429; -fx-text-fill: white; -fx-background-radius: 20; -fx-pref-width: 35; -fx-pref-height: 35; -fx-cursor: hand;");
        removeBtn.setOnAction(e -> {
            playlistSeleccionada.getCanciones().remove(cancion);
            playlistController.guardarDatos();
            cargarPlaylistSeleccionada(playlistSeleccionada.getNombre());
        });

        item.getChildren().addAll(info, playBtn, removeBtn);
        return item;
    }

    @FXML
    private void crearPlaylist() {
        String nombre = nombrePlaylistField.getText().trim();
        if (!nombre.isEmpty()) {
            playlistController.crearPlaylist(usuarioActual, nombre);
            nombrePlaylistField.clear();
            cargarPlaylists();
            UIComponents.mostrarAlertaPersonalizada("Éxito", "Playlist creada: " + nombre, "✅");
        }
    }

    @FXML
    private void eliminarPlaylist() {
        if (playlistSeleccionada != null) {
            usuarioActual.getPlaylists().remove(playlistSeleccionada);
            playlistController.guardarDatos();
            cargarPlaylists();
            cancionesContainer.getChildren().clear();
            playlistTituloLabel.setText("Selecciona una playlist");
            UIComponents.mostrarAlertaPersonalizada("Eliminada", "Playlist eliminada", "🗑️");
        }
    }

    @FXML
    private void exportarJSON() {
        if (playlistSeleccionada != null) {
            playlistController.exportarJSON(playlistSeleccionada);
        }
    }

    @FXML
    private void exportarTXT() {
        if (playlistSeleccionada != null) {
            playlistController.exportarTXT(playlistSeleccionada);
        }
    }
}