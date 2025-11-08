package co.edu.uniquindio.syncup.Controller;

import co.edu.uniquindio.syncup.Model.Entidades.Administrador;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;


public class LoginController {
    @FXML private TextField usuarioField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private ComboBox<String> tipoUsuarioCombo;
    @FXML private Label errorLabel;

    private UsuarioController usuarioController;
    private AdministradorController administratorController;

    @FXML
    public void initialize() {
        tipoUsuarioCombo.getItems().addAll("Usuario", "Administrador");
        tipoUsuarioCombo.setValue("Usuario");
    }

    @FXML
    public void manejarLogin() {
        String username = usuarioField.getText();
        String password = passwordField.getText();
        String tipo = tipoUsuarioCombo.getValue();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Por favor completa todos los campos");
            return;
        }

        if ("Usuario".equals(tipo)) {
            Usuario usuario = usuarioController.autenticar(username, password);
            if (usuario != null) {
                errorLabel.setText("Login exitoso");
                // Aquí cargar la siguiente ventana
            } else {
                errorLabel.setText("Usuario o contraseña incorrectos");
            }
        } else {
            Administrador admin = administratorController.autenticar(username, password);
            if (admin != null) {
                errorLabel.setText("Login administrador exitoso");
                // Aquí cargar la siguiente ventana
            } else {
                errorLabel.setText("Administrador o contraseña incorrectos");
            }
        }
    }

    @FXML
    public void manejarRegistro() {
        String username = usuarioField.getText();
        String password = passwordField.getText();
        String tipo = tipoUsuarioCombo.getValue();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Por favor completa todos los campos");
            return;
        }

        if ("Usuario".equals(tipo)) {
            if (usuarioController.registrar(username, password, username)) {
                errorLabel.setText("Registro exitoso. Puedes ingresar ahora.");
                usuarioField.clear();
                passwordField.clear();
            } else {
                errorLabel.setText("El usuario ya existe");
            }
        } else {
            if (administratorController.registrar(username, password, username)) {
                errorLabel.setText("Administrador registrado exitosamente");
                usuarioField.clear();
                passwordField.clear();
            } else {
                errorLabel.setText("El administrador ya existe");
            }
        }
    }

    public void setControllers(UsuarioController usuarioController, AdministradorController administratorController) {
        this.usuarioController = usuarioController;
        this.administratorController = administratorController;
    }
}