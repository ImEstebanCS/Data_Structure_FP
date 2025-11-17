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

    // ✅ MÉTODO UNIFICADO - Solo un setControllers
    public void setControllers(UsuarioController usuarioController) {
        System.out.println("👥 [SocialViewController] Inicializando...");
        this.usuarioController = usuarioController;
        this.usuarioActual = SessionManager.getInstance().getUsuarioActual();

        if (usuarioActual != null) {
            System.out.println("✅ Usuario cargado: " + usuarioActual.getNombre());
            inicializar();
        } else {
            System.out.println("⚠️ No hay usuario en sesión");
        }
    }

    private void inicializar() {
        try {
            // Actualizar labels
            int cantidadSeguidores = usuarioController.obtenerSeguidores(usuarioActual).size();
            int cantidadSiguiendo = usuarioController.obtenerSiguiendo(usuarioActual).size();

            if (seguidoresLabel != null) {
                seguidoresLabel.setText(String.valueOf(cantidadSeguidores));
            }
            if (siguiendoLabel != null) {
                siguiendoLabel.setText(String.valueOf(cantidadSiguiendo));
            }

            System.out.println("✅ Seguidores: " + cantidadSeguidores + ", Siguiendo: " + cantidadSiguiendo);

            cargarSugerencias();
            cargarSiguiendo();

        } catch (Exception e) {
            System.err.println("❌ Error al inicializar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cargarSugerencias() {
        if (sugerenciasContainer == null) {
            System.out.println("⚠️ sugerenciasContainer es null");
            return;
        }

        sugerenciasContainer.getChildren().clear();
        System.out.println("👥 Cargando sugerencias de usuarios...");

        try {
            List<Usuario> sugerencias = usuarioController.obtenerSugerenciasDeUsuarios(usuarioActual, 10);

            if (sugerencias == null || sugerencias.isEmpty()) {
                Label mensaje = new Label("No hay sugerencias disponibles");
                mensaje.setStyle("-fx-text-fill: #B3B3B3; -fx-font-size: 14px;");
                sugerenciasContainer.getChildren().add(mensaje);
                System.out.println("⚠️ No hay sugerencias");
                return;
            }

            System.out.println("✅ " + sugerencias.size() + " usuarios sugeridos");

            for (Usuario u : sugerencias) {
                sugerenciasContainer.getChildren().add(crearUserCard(u, true));
            }
        } catch (Exception e) {
            System.err.println("❌ Error al cargar sugerencias: " + e.getMessage());
        }
    }

    private void cargarSiguiendo() {
        if (siguiendoContainer == null) {
            System.out.println("⚠️ siguiendoContainer es null");
            return;
        }

        siguiendoContainer.getChildren().clear();
        System.out.println("👥 Cargando usuarios que sigo...");

        try {
            List<Usuario> siguiendo = usuarioController.obtenerSiguiendo(usuarioActual);

            if (siguiendo == null || siguiendo.isEmpty()) {
                Label mensaje = new Label("No sigues a ningún usuario");
                mensaje.setStyle("-fx-text-fill: #B3B3B3; -fx-font-size: 14px;");
                siguiendoContainer.getChildren().add(mensaje);
                System.out.println("⚠️ No sigue a nadie");
                return;
            }

            System.out.println("✅ Cargando " + siguiendo.size() + " usuarios que sigo");

            for (Usuario u : siguiendo) {
                siguiendoContainer.getChildren().add(crearUserCard(u, false));
            }
        } catch (Exception e) {
            System.err.println("❌ Error al cargar siguiendo: " + e.getMessage());
        }
    }

    private HBox crearUserCard(Usuario usuario, boolean esSugerencia) {
        HBox card = new HBox(15);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-background-color: rgba(24, 24, 24, 0.95); -fx-background-radius: 10; -fx-padding: 15;");
        card.setPrefHeight(80);

        // Icono circular de usuario
        VBox circulo = new VBox();
        circulo.setAlignment(Pos.CENTER);
        circulo.setPrefSize(50, 50);
        circulo.setStyle("-fx-background-color: #1DB954; -fx-background-radius: 25;");
        Label icon = new Label("👤");
        icon.setStyle("-fx-font-size: 26px;");
        circulo.getChildren().add(icon);

        // Información del usuario
        VBox infoBox = new VBox(3);
        Label nombre = new Label(usuario.getNombre());
        nombre.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 15px; -fx-font-weight: bold;");
        Label username = new Label("@" + usuario.getUsername());
        username.setStyle("-fx-text-fill: #B3B3B3; -fx-font-size: 13px;");
        infoBox.getChildren().addAll(nombre, username);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        // Botón de acción
        Button actionBtn = new Button(esSugerencia ? "Seguir" : "Dejar de seguir");
        actionBtn.setStyle(esSugerencia
                ? "-fx-background-color: #1DB954; -fx-text-fill: white; -fx-background-radius: 20; -fx-cursor: hand; -fx-pref-width: 120; -fx-pref-height: 35; -fx-font-size: 12px; -fx-font-weight: bold;"
                : "-fx-background-color: transparent; -fx-text-fill: #B3B3B3; -fx-border-color: #B3B3B3; -fx-border-radius: 20; -fx-background-radius: 20; -fx-cursor: hand; -fx-pref-width: 120; -fx-pref-height: 35; -fx-font-size: 12px;");

        actionBtn.setOnAction(e -> {
            try {
                if (esSugerencia) {
                    usuarioController.seguir(usuarioActual, usuario);
                    UIComponents.mostrarAlertaPersonalizada(
                            "Éxito",
                            "Ahora sigues a " + usuario.getNombre(),
                            "✅"
                    );
                    System.out.println("✅ Ahora siguiendo a: " + usuario.getNombre());
                } else {
                    usuarioController.dejarDeSeguir(usuarioActual, usuario);
                    UIComponents.mostrarAlertaPersonalizada(
                            "Dejaste de seguir",
                            "Ya no sigues a " + usuario.getNombre(),
                            "👋"
                    );
                    System.out.println("👋 Dejó de seguir a: " + usuario.getNombre());
                }
                inicializar();
            } catch (Exception ex) {
                System.err.println("❌ Error al seguir/dejar de seguir: " + ex.getMessage());
                UIComponents.mostrarAlertaPersonalizada("Error", "No se pudo realizar la acción", "❌");
            }
        });

        card.getChildren().addAll(circulo, infoBox, actionBtn);
        return card;
    }
}