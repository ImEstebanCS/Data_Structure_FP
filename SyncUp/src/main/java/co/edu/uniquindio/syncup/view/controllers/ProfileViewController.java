package co.edu.uniquindio.syncup.view.controllers;

import co.edu.uniquindio.syncup.Controller.UsuarioController;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import co.edu.uniquindio.syncup.utils.SessionManager;
import co.edu.uniquindio.syncup.utils.UIComponents;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class ProfileViewController {

    @FXML private Label usernameLabel;
    @FXML private Label favoritosLabel;
    @FXML private Label seguidoresLabel;
    @FXML private Label siguiendoLabel;

<<<<<<< Updated upstream
=======
    @FXML private TextField nombreField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label mensajeLabel;

    @FXML private VBox seguidoresContainer;
    @FXML private VBox siguiendoContainer;
    @FXML private Label seguidoresTituloLabel;
    @FXML private Label siguiendoTituloLabel;

>>>>>>> Stashed changes
    private UsuarioController usuarioController;
    private Usuario usuarioActual;

    public void setControllers(UsuarioController usuarioController) {
        this.usuarioController = usuarioController;
        inicializar();
    }

    private void inicializar() {
        usuarioActual = SessionManager.getInstance().getUsuarioActual();

        if (usuarioActual != null) {
            usernameLabel.setText("@" + usuarioActual.getUsername());
            nombreField.setText(usuarioActual.getNombre());
            passwordField.clear();
            confirmPasswordField.clear();

            actualizarEstadisticas();
            cargarSeguidores();
            cargarSiguiendo();
        }
    }

    private void actualizarEstadisticas() {
        favoritosLabel.setText(usuarioActual.getListaFavoritos().size() + " canciones favoritas");
        seguidoresLabel.setText(usuarioActual.getSeguidores().size() + " seguidores");
        siguiendoLabel.setText(usuarioActual.getSiguiendo().size() + " siguiendo");
    }

    private void cargarSeguidores() {
        // ✅ VALIDACIÓN AGREGADA
        if (seguidoresContainer == null) {
            System.out.println("⚠️ seguidoresContainer no está vinculado en el FXML");
            return;
        }

        seguidoresContainer.getChildren().clear();

        List<Usuario> seguidores = usuarioController.obtenerSeguidores(usuarioActual); // ✅ CORREGIDO

        // ✅ VALIDACIÓN AGREGADA
        if (seguidoresTituloLabel != null) {
            seguidoresTituloLabel.setText("Seguidores (" + seguidores.size() + ")");
        }

        if (seguidores.isEmpty()) {
            Label mensajeVacio = new Label("Aún no tienes seguidores");
            mensajeVacio.setStyle("-fx-text-fill: #B3B3B3; -fx-font-size: 14px;");
            seguidoresContainer.getChildren().add(mensajeVacio);
            return;
        }

        for (Usuario seguidor : seguidores) {
            HBox userItem = crearUsuarioItem(seguidor);
            seguidoresContainer.getChildren().add(userItem);
        }
    }

    private void cargarSiguiendo() {
        // ✅ VALIDACIÓN AGREGADA
        if (siguiendoContainer == null) {
            System.out.println("⚠️ siguiendoContainer no está vinculado en el FXML");
            return;
        }

        siguiendoContainer.getChildren().clear();

        List<Usuario> siguiendo = usuarioController.obtenerSiguiendo(usuarioActual);

        // ✅ VALIDACIÓN AGREGADA
        if (siguiendoTituloLabel != null) {
            siguiendoTituloLabel.setText("Siguiendo (" + siguiendo.size() + ")");
        }

        if (siguiendo.isEmpty()) {
            Label mensajeVacio = new Label("No sigues a ningún usuario");
            mensajeVacio.setStyle("-fx-text-fill: #B3B3B3; -fx-font-size: 14px;");
            siguiendoContainer.getChildren().add(mensajeVacio);
            return;
        }

        for (Usuario usuario : siguiendo) {
            HBox userItem = crearUsuarioItem(usuario);
            siguiendoContainer.getChildren().add(userItem);
        }
    }

    private HBox crearUsuarioItem(Usuario usuario) {
        HBox item = new HBox(15);
        item.setAlignment(Pos.CENTER_LEFT);
        item.getStyleClass().add("list-item");
        item.setPadding(new Insets(15));

        Label iconoUsuario = new Label("👤");
        iconoUsuario.setStyle("-fx-font-size: 28px;");

        VBox infoBox = new VBox(5);
        Label nombreLabel = new Label(usuario.getNombre());
        nombreLabel.getStyleClass().add("list-item-title");
        nombreLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-font-weight: bold;");

        Label usernameLabel = new Label("@" + usuario.getUsername());
        usernameLabel.getStyleClass().add("list-item-subtitle");
        usernameLabel.setStyle("-fx-text-fill: #B3B3B3; -fx-font-size: 12px;");

        infoBox.getChildren().addAll(nombreLabel, usernameLabel);

        item.getChildren().addAll(iconoUsuario, infoBox);

        return item;
    }

    @FXML
    private void guardarCambios() {
        if (usuarioActual == null) {
            mostrarMensaje("Error: No hay usuario activo", true);
            return;
        }

        String nuevoNombre = nombreField.getText().trim();
        String nuevaPassword = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (nuevoNombre.isEmpty()) {
            mostrarMensaje("El nombre no puede estar vacío", true);
            return;
        }

        if (!nuevaPassword.isEmpty()) {
            if (nuevaPassword.length() < 4) {
                mostrarMensaje("La contraseña debe tener al menos 4 caracteres", true);
                return;
            }

            if (!nuevaPassword.equals(confirmPassword)) {
                mostrarMensaje("Las contraseñas no coinciden", true);
                return;
            }
        }

        try {
            String usernameActual = usuarioActual.getUsername();
            String passwordFinal = nuevaPassword.isEmpty() ? usuarioActual.getPassword() : nuevaPassword;

            usuarioController.actualizar(
                    usernameActual,
                    usernameActual,
                    passwordFinal,
                    nuevoNombre
            );

            Usuario usuarioActualizado = usuarioController.obtenerUsuarioPorUsername(usernameActual);

            if (usuarioActualizado != null) {
                SessionManager.getInstance().setUsuarioActual(usuarioActualizado);
                usuarioActual = usuarioActualizado;
                nombreField.setText(usuarioActual.getNombre());
            }

            actualizarEstadisticas();
            passwordField.clear();
            confirmPasswordField.clear();

            UIComponents.mostrarAlertaPersonalizada(
                    "Perfil Actualizado",
                    "Tu perfil se ha actualizado correctamente",
                    "✅"
            );

            mostrarMensaje("✓ Perfil actualizado correctamente", false);
        } catch (Exception e) {
            mostrarMensaje("Error al actualizar perfil: " + e.getMessage(), true);
        }
    }

    private void mostrarMensaje(String mensaje, boolean esError) {
        mensajeLabel.setText(mensaje);
        mensajeLabel.setStyle("-fx-text-fill: " + (esError ? "#FF4444" : "#1DB954") + "; -fx-font-size: 12px;");
    }
}