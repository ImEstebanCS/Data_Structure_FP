package co.edu.uniquindio.syncup.view.controllers;

import co.edu.uniquindio.syncup.SyncUpApp;
import co.edu.uniquindio.syncup.Controller.AdministradorController;
import co.edu.uniquindio.syncup.Controller.UsuarioController;
import co.edu.uniquindio.syncup.Model.Entidades.Administrador;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import co.edu.uniquindio.syncup.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class LoginViewController {

    @FXML private TextField usuarioField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> tipoUsuarioCombo;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Label errorLabel;

    private UsuarioController usuarioController;
    private AdministradorController administradorController;

    @FXML
    public void initialize() {
        tipoUsuarioCombo.getItems().addAll("Usuario", "Administrador");
        tipoUsuarioCombo.setValue("Usuario");

        errorLabel.setText("");

        usuarioController = SyncUpApp.getUsuarioController();
        administradorController = SyncUpApp.getAdministradorController();

        passwordField.setOnAction(event -> manejarLogin());
    }

    @FXML
    public void manejarLogin() {
        String username = usuarioField.getText();
        String password = passwordField.getText();
        String tipo = tipoUsuarioCombo.getValue();

        if (username.trim().isEmpty() || password.isEmpty()) {
            mostrarError("Por favor completa todos los campos");
            return;
        }

        if ("Usuario".equals(tipo)) {
            Usuario usuario = usuarioController.autenticar(username, password);

            if (usuario != null) {
                SessionManager.getInstance().setUsuarioActual(usuario);
                System.out.println("✓ Login exitoso: " + usuario.getNombre());
                navegarAMain();
            } else {
                mostrarError("Usuario o contraseña incorrectos");
            }

        } else {
            Administrador admin = administradorController.autenticar(username, password);

            if (admin != null) {
                SessionManager.getInstance().setAdministradorActual(admin);
                System.out.println("✓ Login admin exitoso: " + admin.getNombre());
                navegarAAdminPanel();
            } else {
                mostrarError("Administrador o contraseña incorrectos");
            }
        }
    }

    private void navegarAMain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
            BorderPane mainView = loader.load();

            Scene scene = usuarioField.getScene();
            scene.setRoot(mainView);

            Stage stage = (Stage) scene.getWindow();
            stage.setWidth(1366);
            stage.setHeight(768);
            stage.setMinWidth(1280);
            stage.setMinHeight(720);
            stage.centerOnScreen();

        } catch (Exception e) {
            System.err.println("Error al cargar Main.fxml: " + e.getMessage());
            e.printStackTrace();
            mostrarError("Error al cargar la vista principal");
        }
    }

    private void navegarAAdminPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/AdminPanel.fxml"));
            BorderPane adminView = loader.load();

            Scene scene = usuarioField.getScene();
            scene.setRoot(adminView);

            Stage stage = (Stage) scene.getWindow();
            stage.setWidth(1366);
            stage.setHeight(768);
            stage.setMinWidth(1280);
            stage.setMinHeight(720);
            stage.centerOnScreen();

        } catch (Exception e) {
            System.err.println("Error al cargar AdminPanel.fxml: " + e.getMessage());
            e.printStackTrace();
            mostrarError("Error al cargar panel de administrador");
        }
    }

    @FXML
    public void manejarRegistro() {
        String username = usuarioField.getText();
        String password = passwordField.getText();
        String tipo = tipoUsuarioCombo.getValue();

        if (username.trim().isEmpty() || password.isEmpty()) {
            mostrarError("Por favor completa todos los campos");
            return;
        }

        if (username.length() < 3) {
            mostrarError("El usuario debe tener al menos 3 caracteres");
            return;
        }

        if (password.length() < 4) {
            mostrarError("La contraseña debe tener al menos 4 caracteres");
            return;
        }

        if ("Usuario".equals(tipo)) {
            if (usuarioController.registrar(username, password, username)) {
                mostrarExito("¡Registro exitoso! Ahora puedes iniciar sesión");
                limpiarCampos();
            } else {
                mostrarError("El usuario ya existe");
            }

        } else {
            if (administradorController.registrar(username, password, username)) {
                mostrarExito("¡Administrador registrado! Ahora puedes iniciar sesión");
                limpiarCampos();
            } else {
                mostrarError("El administrador ya existe");
            }
        }
    }

    @FXML
    public void buttonHoverEnter(MouseEvent event) {
        loginButton.setStyle(
                "-fx-background-color: #1ed760; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 25; " +
                        "-fx-cursor: hand; " +
                        "-fx-scale-x: 1.02; " +
                        "-fx-scale-y: 1.02;"
        );
    }

    @FXML
    public void buttonHoverExit(MouseEvent event) {
        loginButton.setStyle(
                "-fx-background-color: #1DB954; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 25; " +
                        "-fx-cursor: hand;"
        );
    }

    @FXML
    public void buttonRegisterHoverEnter(MouseEvent event) {
        registerButton.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.1); " +
                        "-fx-text-fill: #1DB954; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-border-color: #1DB954; " +
                        "-fx-border-width: 2; " +
                        "-fx-background-radius: 25; " +
                        "-fx-border-radius: 25; " +
                        "-fx-cursor: hand; " +
                        "-fx-scale-x: 1.02; " +
                        "-fx-scale-y: 1.02;"
        );
    }

    @FXML
    public void buttonRegisterHoverExit(MouseEvent event) {
        registerButton.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-text-fill: #FFFFFF; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-border-color: #FFFFFF; " +
                        "-fx-border-width: 2; " +
                        "-fx-background-radius: 25; " +
                        "-fx-border-radius: 25; " +
                        "-fx-cursor: hand;"
        );
    }

    private void mostrarError(String mensaje) {
        errorLabel.setText("✗ " + mensaje);
        errorLabel.setStyle("-fx-text-fill: #FF4444; -fx-font-size: 13px;");
    }

    private void mostrarExito(String mensaje) {
        errorLabel.setText("✓ " + mensaje);
        errorLabel.setStyle("-fx-text-fill: #1DB954; -fx-font-size: 13px;");
    }

    private void limpiarCampos() {
        usuarioField.clear();
        passwordField.clear();
    }
}