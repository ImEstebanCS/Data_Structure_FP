package co.edu.uniquindio.syncup.view.controllers;

import co.edu.uniquindio.syncup.SyncUpApp;
import co.edu.uniquindio.syncup.Controller.AdministradorController;
import co.edu.uniquindio.syncup.Controller.UsuarioController;
import co.edu.uniquindio.syncup.Model.Entidades.Administrador;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import co.edu.uniquindio.syncup.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

/**
 * LoginViewController - RF-001
 * Controla la vista de inicio de sesión y registro
 */
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
        // Configurar combo box
        tipoUsuarioCombo.getItems().addAll("Usuario", "Administrador");
        tipoUsuarioCombo.setValue("Usuario");

        // Limpiar mensaje de error
        errorLabel.setText("");

        // Obtener controladores
        usuarioController = SyncUpApp.getUsuarioController();
        administradorController = SyncUpApp.getAdministradorController();

        // Configurar eventos de Enter
        passwordField.setOnAction(event -> manejarLogin());
    }

    /**
     * RF-001: Maneja el inicio de sesión
     */
    @FXML
    public void manejarLogin() {
        String username = usuarioField.getText();
        String password = passwordField.getText();
        String tipo = tipoUsuarioCombo.getValue();

        // Validar campos
        if (username.trim().isEmpty() || password.isEmpty()) {
            mostrarError("Por favor completa todos los campos");
            return;
        }

        if ("Usuario".equals(tipo)) {
            // Autenticar como usuario
            Usuario usuario = usuarioController.autenticar(username, password);

            if (usuario != null) {
                SessionManager.getInstance().setUsuarioActual(usuario);
                System.out.println("✓ Login exitoso: " + usuario.getNombre());

                // Navegar a vista principal
                navegarAMain();

            } else {
                mostrarError("Usuario o contraseña incorrectos");
            }

        } else {
            // Autenticar como administrador
            Administrador admin = administradorController.autenticar(username, password);

            if (admin != null) {
                SessionManager.getInstance().setAdministradorActual(admin);
                System.out.println("✓ Login admin exitoso: " + admin.getNombre());

                // Navegar a panel de administrador
                navegarAAdminPanel();

            } else {
                mostrarError("Administrador o contraseña incorrectos");
            }
        }
    }

    /**
     * Navega a la vista principal de usuario
     */
    private void navegarAMain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
            BorderPane mainView = loader.load();

            // Reemplazar la escena actual
            usuarioField.getScene().setRoot(mainView);

        } catch (Exception e) {
            System.err.println("Error al cargar Main.fxml: " + e.getMessage());
            e.printStackTrace();
            mostrarError("Error al cargar la vista principal");
        }
    }

    /**
     * Navega al panel de administrador
     */
    private void navegarAAdminPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/AdminPanel.fxml"));
            BorderPane adminView = loader.load();

            // Reemplazar la escena actual
            usuarioField.getScene().setRoot(adminView);

        } catch (Exception e) {
            System.err.println("Error al cargar AdminPanel.fxml: " + e.getMessage());
            e.printStackTrace();
            mostrarError("Error al cargar panel de administrador");
        }
    }

    /**
     * RF-001: Maneja el registro de nuevos usuarios
     */
    @FXML
    public void manejarRegistro() {
        String username = usuarioField.getText();
        String password = passwordField.getText();
        String tipo = tipoUsuarioCombo.getValue();

        // Validar campos
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
            // Registrar usuario
            if (usuarioController.registrar(username, password, username)) {
                mostrarExito("¡Registro exitoso! Ahora puedes iniciar sesión");
                limpiarCampos();
            } else {
                mostrarError("El usuario ya existe");
            }

        } else {
            // Registrar administrador
            if (administradorController.registrar(username, password, username)) {
                mostrarExito("¡Administrador registrado! Ahora puedes iniciar sesión");
                limpiarCampos();
            } else {
                mostrarError("El administrador ya existe");
            }
        }
    }

    private void mostrarError(String mensaje) {
        errorLabel.setText("✗ " + mensaje);
        errorLabel.setStyle("-fx-text-fill: #FF4444;");
    }

    private void mostrarExito(String mensaje) {
        errorLabel.setText("✓ " + mensaje);
        errorLabel.setStyle("-fx-text-fill: #1DB954;");
    }

    private void limpiarCampos() {
        usuarioField.clear();
        passwordField.clear();
    }
}