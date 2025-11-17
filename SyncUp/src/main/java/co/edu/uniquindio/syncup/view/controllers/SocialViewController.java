package co.edu.uniquindio.syncup.view.controllers;

import co.edu.uniquindio.syncup.Controller.UsuarioController;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import co.edu.uniquindio.syncup.utils.SessionManager;
import co.edu.uniquindio.syncup.utils.UIComponents;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class SocialViewController {

    @FXML private Label seguidoresLabel;
    @FXML private Label siguiendoLabel;
    @FXML private VBox sugerenciasContainer;
    @FXML private VBox siguiendoContainer;

    private UsuarioController usuarioController;
    private Usuario usuarioActual;

    public void setControllers(UsuarioController usuarioController) {
        this.usuarioController = usuarioController;
        this.usuarioActual = SessionManager.getInstance().getUsuarioActual();
        inicializar();
    }

    private void inicializar() {
        seguidoresLabel.setText(String.valueOf(usuarioController.obtenerSiguiendo(usuarioActual).size()));
        siguiendoLabel.setText(String.valueOf(usuarioController.obtenerSiguiendo(usuarioActual).size()));

        cargarSugerencias();
        cargarSiguiendo();
    }

    private void cargarSugerencias() {
        sugerenciasContainer.getChildren().clear();
        List<Usuario> sugerencias = usuarioController.obtenerSugerencias(usuarioActual, 10);

        for (Usuario u : sugerencias) {
            sugerenciasContainer.getChildren().add(crearUserCard(u, true));
        }
    }

    private void cargarSiguiendo() {
        siguiendoContainer.getChildren().clear();
        for (Usuario u : usuarioController.obtenerSiguiendo(usuarioActual)) {
            siguiendoContainer.getChildren().add(crearUserCard(u, false));
        }
    }

    private HBox crearUserCard(Usuario usuario, boolean esSugerencia) {
        HBox card = new HBox(15);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-background-color: rgba(24, 24, 24, 0.95); -fx-background-radius: 10; -fx-padding: 15;");
        card.setPrefHeight(80);

        VBox circulo = new VBox();
        circulo.setAlignment(Pos.CENTER);
        circulo.setPrefSize(50, 50);
        circulo.setStyle("-fx-background-color: #1DB954; -fx-background-radius: 25;");
        Label icon = new Label("👤");
        icon.setStyle("-fx-font-size: 26px;");
        circulo.getChildren().add(icon);

        VBox infoBox = new VBox(3);
        Label nombre = new Label(usuario.getNombre());
        nombre.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 15px; -fx-font-weight: bold;");
        Label username = new Label("@" + usuario.getUsername());
        username.setStyle("-fx-text-fill: #B3B3B3; -fx-font-size: 13px;");
        infoBox.getChildren().addAll(nombre, username);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        Button actionBtn = new Button(esSugerencia ? "Seguir" : "Dejar de seguir");
        actionBtn.setStyle(esSugerencia
                ? "-fx-background-color: #1DB954; -fx-text-fill: white; -fx-background-radius: 20; -fx-cursor: hand; -fx-pref-width: 120; -fx-pref-height: 35; -fx-font-size: 12px; -fx-font-weight: bold;"
                : "-fx-background-color: transparent; -fx-text-fill: #B3B3B3; -fx-border-color: #B3B3B3; -fx-border-radius: 20; -fx-background-radius: 20; -fx-cursor: hand; -fx-pref-width: 120; -fx-pref-height: 35; -fx-font-size: 12px;");

        actionBtn.setOnAction(e -> {
            if (esSugerencia) {
                usuarioController.seguir(usuarioActual, usuario);
            } else {
                usuarioController.dejarDeSeguir(usuarioActual, usuario);
            }
            inicializar();
        });

        card.getChildren().addAll(circulo, infoBox, actionBtn);
        return card;
    }
}