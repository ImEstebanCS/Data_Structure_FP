package co.edu.uniquindio.syncup.view.controllers;

import co.edu.uniquindio.syncup.Controller.PlaylistController;
import co.edu.uniquindio.syncup.Controller.RadioController;
import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import co.edu.uniquindio.syncup.Model.Entidades.Radio;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import co.edu.uniquindio.syncup.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Queue;

/**
 * RadioViewController
 * Vista para mostrar la cola de reproducción de Radio
 */


import co.edu.uniquindio.syncup.Service.MusicPlayer;
import co.edu.uniquindio.syncup.SyncUpApp;

public class RadioViewController {

    @FXML private Label radioNombreLabel;
    @FXML private Label infoLabel;
    @FXML private Label semillaTituloLabel;
    @FXML private Label semillaArtistaLabel;
    @FXML private Label colaCountLabel;
    @FXML private VBox colaPane;

    private RadioController radioController;
    private PlaylistController playlistController;
    private Usuario usuarioActual;
    private MusicPlayer musicPlayer; // ⭐ NUEVO

    public void setControllers(RadioController radioController, PlaylistController playlistController) {
        this.radioController = radioController;
        this.playlistController = playlistController;
        this.musicPlayer = SyncUpApp.getMusicPlayer(); // ⭐ NUEVO
        inicializar();
    }

    private void inicializar() {
        usuarioActual = SessionManager.getInstance().getUsuarioActual();
        cargarRadio();
    }

    private void cargarRadio() {
        if (usuarioActual == null) {
            return;
        }

        Radio radio = radioController.obtenerRadio(usuarioActual);

        if (radio == null || radio.getCancionSemilla() == null) {
            // No hay radio iniciada
            radioNombreLabel.setText("Mi Radio");
            infoLabel.setText("Inicia una radio desde cualquier canción usando el botón 📻");
            semillaTituloLabel.setText("No hay radio activa");
            semillaArtistaLabel.setText("Selecciona una canción para comenzar");
            colaCountLabel.setText("0 canciones en cola");
            colaPane.getChildren().clear();
            return;
        }

        // Mostrar información de la radio
        radioNombreLabel.setText(radio.getNombre());
        Cancion semilla = radio.getCancionSemilla();
        semillaTituloLabel.setText(semilla.getTitulo());
        semillaArtistaLabel.setText(semilla.getArtista());
        infoLabel.setText("Radio basada en: " + semilla.getTitulo() + " de " + semilla.getArtista());

        // Cargar cola de reproducción
        cargarColaReproduccion(radio);
    }

    private void cargarColaReproduccion(Radio radio) {
        colaPane.getChildren().clear();

        Queue<Cancion> cola = radio.getColaReproduccion();
        int totalCanciones = cola.size();

        if (totalCanciones == 0) {
            colaCountLabel.setText("0 canciones en cola");
            Label mensaje = new Label("La cola de reproducción está vacía");
            mensaje.setStyle("-fx-text-fill: #B3B3B3; -fx-font-size: 14px;");
            colaPane.getChildren().add(mensaje);
            return;
        }

        colaCountLabel.setText(totalCanciones + " canciones en cola");

        int posicion = 1;
        for (Cancion cancion : cola) {
            HBox cancionItem = crearItemCola(cancion, posicion);
            colaPane.getChildren().add(cancionItem);
            posicion++;
        }
    }

    private HBox crearItemCola(Cancion cancion, int posicion) {
        HBox item = new HBox(15);
        item.setStyle("-fx-background-color: #181818; -fx-background-radius: 8; -fx-padding: 15;");
        item.setPrefHeight(70);

        // Número de posición
        Label posLabel = new Label(String.valueOf(posicion));
        posLabel.setStyle("-fx-text-fill: #B3B3B3; -fx-font-size: 16px; -fx-font-weight: bold;");
        posLabel.setMinWidth(30);
        posLabel.setAlignment(javafx.geometry.Pos.CENTER);

        // Ícono
        Label icono = new Label("🎵");
        icono.setStyle("-fx-font-size: 30px;");

        // Información de la canción
        VBox infoBox = new VBox(5);
        Label titulo = new Label(cancion.getTitulo());
        titulo.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-font-weight: bold;");
        Label artista = new Label(cancion.getArtista() + " • " + cancion.getGenero() + " • " + cancion.getAño());
        artista.setStyle("-fx-text-fill: #B3B3B3; -fx-font-size: 12px;");
        infoBox.getChildren().addAll(titulo, artista);

        // Spacer
        javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        // Botones de acción
        HBox botonesBox = new HBox(10);
        botonesBox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);

        // ⭐ NUEVO - Botón de reproducir
        Button playBtn = new Button("▶");
        playBtn.setStyle("-fx-background-color: #1DB954; -fx-text-fill: #FFFFFF; -fx-background-radius: 15; -fx-cursor: hand; -fx-font-size: 14px;");
        playBtn.setPrefSize(35, 35);
        playBtn.setOnAction(e -> {
            reproducirCancion(cancion);
        });

        Button favoritoBtn = new Button("❤️");
        favoritoBtn.setStyle("-fx-background-color: #E91429; -fx-text-fill: #FFFFFF; -fx-background-radius: 15; -fx-cursor: hand;");
        favoritoBtn.setPrefSize(35, 35);
        favoritoBtn.setOnAction(e -> {
            playlistController.agregarFavorito(usuarioActual, cancion);
            mostrarAlerta("Agregado", cancion.getTitulo() + " agregada a favoritos");
        });

        Button radioBtn = new Button("📻");
        radioBtn.setStyle("-fx-background-color: #FFA500; -fx-text-fill: #FFFFFF; -fx-background-radius: 15; -fx-cursor: hand;");
        radioBtn.setPrefSize(35, 35);
        radioBtn.setOnAction(e -> {
            radioController.iniciarRadio(usuarioActual, cancion);
            mostrarAlerta("Radio Reiniciada", "Radio reiniciada desde: " + cancion.getTitulo());
            cargarRadio(); // Recargar la vista
        });

        botonesBox.getChildren().addAll(playBtn, favoritoBtn, radioBtn); // ⭐ MODIFICADO - Agregado playBtn

        item.getChildren().addAll(posLabel, icono, infoBox, spacer, botonesBox);

        // ⭐ NUEVO - Click en el item reproduce
        item.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                reproducirCancion(cancion);
            }
        });

        // Hover effect
        item.setOnMouseEntered(e ->
                item.setStyle("-fx-background-color: #282828; -fx-background-radius: 8; -fx-padding: 15;")
        );
        item.setOnMouseExited(e ->
                item.setStyle("-fx-background-color: #181818; -fx-background-radius: 8; -fx-padding: 15;")
        );

        return item;
    }

    // ⭐ NUEVO - Método para reproducir canción
    private void reproducirCancion(Cancion cancion) {
        if (musicPlayer != null) {
            musicPlayer.reproducir(cancion);
            mostrarAlerta("Reproduciendo en YouTube",
                    "🎵 " + cancion.getTitulo() + "\n" +
                            "🎤 " + cancion.getArtista() + "\n" +
                            "🎸 " + cancion.getGenero() + "\n\n" +
                            "Se abrirá YouTube en tu navegador");
        } else {
            mostrarAlerta("Error", "El reproductor no está disponible");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

