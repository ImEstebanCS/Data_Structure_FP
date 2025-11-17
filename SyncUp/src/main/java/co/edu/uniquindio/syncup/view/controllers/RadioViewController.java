package co.edu.uniquindio.syncup.view.controllers;

import co.edu.uniquindio.syncup.Controller.PlaylistController;
import co.edu.uniquindio.syncup.Controller.RadioController;
import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import co.edu.uniquindio.syncup.Model.Entidades.Radio;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import co.edu.uniquindio.syncup.Service.MusicPlayer;
import co.edu.uniquindio.syncup.SyncUpApp;
import co.edu.uniquindio.syncup.utils.SessionManager;
import co.edu.uniquindio.syncup.utils.UIComponents;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.Queue;

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
    private MusicPlayer musicPlayer;

    public void setControllers(RadioController radioController, PlaylistController playlistController) {
        this.radioController = radioController;
        this.playlistController = playlistController;
        this.musicPlayer = SyncUpApp.getMusicPlayer();
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
            radioNombreLabel.setText("Mi Radio");
            infoLabel.setText("Inicia una radio desde cualquier canción usando el botón 📻");
            semillaTituloLabel.setText("No hay radio activa");
            semillaArtistaLabel.setText("Selecciona una canción para comenzar");
            colaCountLabel.setText("0 canciones en cola");
            colaPane.getChildren().clear();
            return;
        }

        radioNombreLabel.setText(radio.getNombre());
        Cancion semilla = radio.getCancionSemilla();
        semillaTituloLabel.setText(semilla.getTitulo());
        semillaArtistaLabel.setText(semilla.getArtista());
        infoLabel.setText("Radio basada en: " + semilla.getTitulo() + " de " + semilla.getArtista());

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
        item.setAlignment(Pos.CENTER_LEFT);
        item.getStyleClass().add("list-item");
        item.setPadding(new Insets(15));
        item.setPrefHeight(80);

        Label posLabel = new Label(String.valueOf(posicion));
        posLabel.setStyle("-fx-text-fill: #B3B3B3; -fx-font-size: 18px; -fx-font-weight: bold;");
        posLabel.setMinWidth(40);
        posLabel.setAlignment(Pos.CENTER);

        Label icono = new Label("🎵");
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
        Button favoritoBtn = UIComponents.crearBotonCircular("❤", "#E91429", () -> agregarAFavoritos(cancion), 40);
        Button radioBtn = UIComponents.crearBotonCircular("📻", "#FFA500", () -> reiniciarRadio(cancion), 40);

        botonesBox.getChildren().addAll(playBtn, favoritoBtn, radioBtn);

        item.getChildren().addAll(posLabel, icono, infoBox, spacer, botonesBox);

        item.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                reproducirCancion(cancion);
            }
        });

        return item;
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
        } else {
            UIComponents.mostrarAlertaPersonalizada("Error", "El reproductor no está disponible", "❌");
        }
    }

    private void agregarAFavoritos(Cancion cancion) {
        playlistController.agregarFavorito(usuarioActual, cancion);
        UIComponents.mostrarAlertaPersonalizada(
                "Favorito",
                cancion.getTitulo() + " agregada a favoritos",
                "❤️"
        );
    }

    private void reiniciarRadio(Cancion cancion) {
        radioController.iniciarRadio(usuarioActual, cancion);
        UIComponents.mostrarAlertaPersonalizada(
                "Radio Reiniciada",
                "Radio reiniciada desde:\n" + cancion.getTitulo(),
                "📻"
        );
        cargarRadio();
    }
}