package co.edu.uniquindio.syncup.viewController;



import co.edu.uniquindio.syncup.Controller.AdministradorController;
import co.edu.uniquindio.syncup.Controller.UsuarioController;

import co.edu.uniquindio.syncup.Model.Entidades.Administrador;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;



/**
 * VIEW CONTROLLER - Conecta Login.fxml con la lógica de negocio
 *
 * Responsabilidades:
 * - Lee datos del FXML (campos de texto, botones)
 * - Llama a los controllers de lógica de negocio
 * - Actualiza la interfaz con los resultados
 */
public class LoginViewController {

    // ==================== COMPONENTES FXML ====================
    @FXML private TextField usuarioField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> tipoUsuarioCombo;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Label errorLabel;

    // ==================== CONTROLLERS DE LÓGICA ====================
    private UsuarioController usuarioController;
    private AdministradorController administratorController;

    // ==================== INICIALIZACIÓN ====================

    @FXML
    public void initialize() {
        // Configurar opciones del ComboBox
        tipoUsuarioCombo.getItems().addAll("Usuario", "Administrador");
        tipoUsuarioCombo.setValue("Usuario");

        // Limpiar mensaje de error
        errorLabel.setText("");
    }

    // ==================== MANEJO DE EVENTOS ====================

    /**
     * Maneja el evento de iniciar sesión
     * Lee datos del FXML y llama a la lógica de negocio
     */
    @FXML
    public void manejarLogin() {
        // PASO 1: OBTENER DATOS DEL FXML
        String username = usuarioField.getText();
        String password = passwordField.getText();
        String tipo = tipoUsuarioCombo.getValue();

        // PASO 2: VALIDAR BÁSICAMENTE
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Por favor completa todos los campos");
            return;
        }

        // PASO 3: LLAMAR LÓGICA DE NEGOCIO
        if ("Usuario".equals(tipo)) {
            // Autenticar como usuario normal
            Usuario usuario = usuarioController.autenticar(username, password);

            if (usuario != null) {
                // Login exitoso
                errorLabel.setText("✓ Login exitoso");
                errorLabel.setStyle("-fx-text-fill: green;");

                // Aquí abrirías la siguiente ventana (UsuarioPanel)
                abrirPanelUsuario(usuario);
            } else {
                // Credenciales incorrectas
                errorLabel.setText("✗ Usuario o contraseña incorrectos");
                errorLabel.setStyle("-fx-text-fill: red;");
            }
        } else {
            // Autenticar como administrador
            Administrador admin = administratorController.autenticar(username, password);

            if (admin != null) {
                // Login exitoso como admin
                errorLabel.setText("✓ Login administrador exitoso");
                errorLabel.setStyle("-fx-text-fill: green;");

                // Aquí abrirías el panel de administrador
                abrirPanelAdministrador(admin);
            } else {
                // Credenciales incorrectas
                errorLabel.setText("✗ Administrador o contraseña incorrectos");
                errorLabel.setStyle("-fx-text-fill: red;");
            }
        }
    }

    /**
     * Maneja el evento de registro
     * Permite crear nuevas cuentas
     */
    @FXML
    public void manejarRegistro() {
        // PASO 1: OBTENER DATOS
        String username = usuarioField.getText();
        String password = passwordField.getText();
        String tipo = tipoUsuarioCombo.getValue();

        // PASO 2: VALIDAR
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Por favor completa todos los campos");
            errorLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        // PASO 3: REGISTRAR
        if ("Usuario".equals(tipo)) {
            if (usuarioController.registrar(username, password, username)) {
                errorLabel.setText("✓ Registro exitoso. Puedes iniciar sesión ahora.");
                errorLabel.setStyle("-fx-text-fill: green;");

                // Limpiar campos
                usuarioField.clear();
                passwordField.clear();
            } else {
                errorLabel.setText("✗ El usuario ya existe");
                errorLabel.setStyle("-fx-text-fill: red;");
            }
        } else {
            if (administratorController.registrar(username, password, username)) {
                errorLabel.setText("✓ Administrador registrado exitosamente");
                errorLabel.setStyle("-fx-text-fill: green;");

                // Limpiar campos
                usuarioField.clear();
                passwordField.clear();
            } else {
                errorLabel.setText("✗ El administrador ya existe");
                errorLabel.setStyle("-fx-text-fill: red;");
            }
        }
    }

    // ==================== MÉTODOS PARA ABRIR OTRAS VENTANAS ====================

    /**
     * Abre el panel de usuario
     * @param usuario usuario autenticado
     */
    private void abrirPanelUsuario(Usuario usuario) {
        try {
            // Cargar FXML del panel de usuario
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("../fxml/UsuarioPanel.fxml")
            );
            Parent root = loader.load();

            // Obtener el controller del panel
            UsuarioPanelViewController controller = loader.getController();
            controller.setUsuarioActual(usuario);
            controller.setControllers(null, null); // Aquí inyectarías los controllers

            // Crear nueva ventana
            Stage stage = new Stage();
            stage.setTitle("SyncUp - Panel de Usuario");
            stage.setScene(new Scene(root, 1000, 700));
            stage.show();

            // Cerrar ventana de login
            Stage loginStage = (Stage) loginButton.getScene().getWindow();
            loginStage.close();

        } catch (Exception e) {
            errorLabel.setText("Error al abrir panel de usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Abre el panel de administrador
     * @param admin administrador autenticado
     */
    private void abrirPanelAdministrador(Administrador admin) {
        try {
            // Cargar FXML del panel de administrador
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("../fxml/AdministratorPanel.fxml")
            );
            Parent root = loader.load();

            // Obtener el controller del panel
            co.edu.uniquindio.syncup.Controller.AdministradorPanelController controller = loader.getController();
            controller.setControllers(null, null); // Aquí inyectarías los controllers

            // Crear nueva ventana
            Stage stage = new Stage();
            stage.setTitle("SyncUp - Panel de Administrador");
            stage.setScene(new Scene(root, 1000, 700));
            stage.show();

            // Cerrar ventana de login
            Stage loginStage = (Stage) loginButton.getScene().getWindow();
            loginStage.close();

        } catch (Exception e) {
            errorLabel.setText("Error al abrir panel de administrador: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ==================== INYECCIÓN DE DEPENDENCIAS ====================

    /**
     * Inyecta los controllers de lógica de negocio
     * Este método es llamado por SyncUpApplication
     * @param usuarioController controller de usuarios
     * @param administratorController controller de administradores
     */
    public void setControllers(UsuarioController usuarioController,
                               AdministradorController administratorController) {
        this.usuarioController = usuarioController;
        this.administratorController = administratorController;
    }
}