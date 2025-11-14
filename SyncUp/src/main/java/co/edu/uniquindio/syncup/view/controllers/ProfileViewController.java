package co.edu.uniquindio.syncup.view.controllers;

import co.edu.uniquindio.syncup.Controller.UsuarioController;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import co.edu.uniquindio.syncup.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ProfileViewController {

    @FXML private Label usernameLabel;
    @FXML private Label favoritosLabel;
    @FXML private Label seguidoresLabel;
    @FXML private Label siguiendoLabel;
    
    // RF-002: Campos de edición
    @FXML private TextField nombreField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label mensajeLabel;

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
            
            // RF-002: Cargar datos actuales en los campos
            nombreField.setText(usuarioActual.getNombre());
            passwordField.clear();
            confirmPasswordField.clear();
            
            favoritosLabel.setText(usuarioActual.getListaFavoritos().size() + " canciones favoritas");
            seguidoresLabel.setText(usuarioActual.getSeguidores().size() + " seguidores");
            siguiendoLabel.setText(usuarioActual.getSiguiendo().size() + " siguiendo");
        }
    }

    /**
     * RF-002: Guardar cambios en el perfil (nombre y contraseña)
     */
    @FXML
    private void guardarCambios() {
        if (usuarioActual == null) {
            mostrarMensaje("Error: No hay usuario activo", true);
            return;
        }

        String nuevoNombre = nombreField.getText().trim();
        String nuevaPassword = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validar nombre
        if (nuevoNombre.isEmpty()) {
            mostrarMensaje("El nombre no puede estar vacío", true);
            return;
        }

        // Validar contraseña si se proporcionó
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
            // Si no se cambió la contraseña, usar la actual
            String passwordFinal = nuevaPassword.isEmpty() ? usuarioActual.getPassword() : nuevaPassword;

            // Actualizar usuario
            usuarioController.actualizar(
                    usuarioActual.getUsername(),  // username no cambia
                    usuarioActual.getUsername(),  // mantener el mismo username
                    passwordFinal,
                    nuevoNombre
            );

            // Actualizar usuario en sesión
            usuarioActual.setNombre(nuevoNombre);
            usuarioActual.setPassword(passwordFinal);
            SessionManager.getInstance().setUsuarioActual(usuarioActual);

            // Actualizar etiquetas
            favoritosLabel.setText(usuarioActual.getListaFavoritos().size() + " canciones favoritas");
            seguidoresLabel.setText(usuarioActual.getSeguidores().size() + " seguidores");
            siguiendoLabel.setText(usuarioActual.getSiguiendo().size() + " siguiendo");

            // Limpiar campos de contraseña
            passwordField.clear();
            confirmPasswordField.clear();

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