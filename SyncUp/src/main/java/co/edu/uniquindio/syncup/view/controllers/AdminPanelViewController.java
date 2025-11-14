package co.edu.uniquindio.syncup.view.controllers;

import co.edu.uniquindio.syncup.SyncUpApp;
import co.edu.uniquindio.syncup.Controller.CancionController;
import co.edu.uniquindio.syncup.Controller.UsuarioController;
import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import co.edu.uniquindio.syncup.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class AdminPanelViewController {
    private String usernameOriginal = null;

    // Tabla y columnas de canciones
    @FXML private TableView<Cancion> cancionesTable;
    @FXML private TableColumn<Cancion, Integer> idColumn;
    @FXML private TableColumn<Cancion, String> tituloColumn;
    @FXML private TableColumn<Cancion, String> artistaColumn;

    // Tabla y columnas de usuarios
    @FXML private TableView<Usuario> usuariosTable;
    @FXML private TableColumn<Usuario, String> usernameColumn;
    @FXML private TableColumn<Usuario, String> nombreColumn;

    // Campos para canciones
    @FXML private TextField idField;
    @FXML private TextField tituloField;
    @FXML private TextField artistaField;
    @FXML private TextField generoField;
    @FXML private TextField añoField;
    @FXML private TextField duracionField;

    // Campos para usuarios
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField nombreUsuarioField;

    private CancionController cancionController;
    private UsuarioController usuarioController;

    @FXML
    public void initialize() {
        cancionController = SyncUpApp.getCancionController();
        usuarioController = SyncUpApp.getUsuarioController();

        // Configurar columnas canciones
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        tituloColumn.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        artistaColumn.setCellValueFactory(new PropertyValueFactory<>("artista"));

        // Configurar columnas usuarios
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        // Listener para cargar datos al seleccionar una canción
        cancionesTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> cargarCancionEnCampos(newValue)
        );

        // Listener para cargar datos al seleccionar un usuario
        usuariosTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> cargarUsuarioEnCampos(newValue)
        );

        cargarDatos();
    }

    private void cargarDatos() {
        cancionesTable.getItems().setAll(cancionController.obtenerTodas());
        usuariosTable.getItems().setAll(usuarioController.listarUsuarios());
    }

    // ==================== GESTIÓN DE CANCIONES ====================

    private void cargarCancionEnCampos(Cancion cancion) {
        if (cancion != null) {
            idField.setText(String.valueOf(cancion.getId()));
            tituloField.setText(cancion.getTitulo());
            artistaField.setText(cancion.getArtista());
            generoField.setText(cancion.getGenero());
            añoField.setText(String.valueOf(cancion.getAño()));
            duracionField.setText(String.valueOf(cancion.getDuracion()));
        }
    }

    @FXML
    private void agregarCancion() {
        try {
            int id = Integer.parseInt(idField.getText());
            String titulo = tituloField.getText();
            String artista = artistaField.getText();
            String genero = generoField.getText();
            int año = Integer.parseInt(añoField.getText());
            double duracion = Double.parseDouble(duracionField.getText());

            if (titulo.isEmpty() || artista.isEmpty() || genero.isEmpty()) {
                mostrarAlerta("Error", "Todos los campos deben estar llenos");
                return;
            }

            cancionController.agregarCancion(id, titulo, artista, genero, año, duracion);
            cargarDatos();
            limpiarCamposCanciones();
            mostrarAlerta("Éxito", "Canción agregada correctamente");
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Los campos ID, Año y Duración deben ser números válidos");
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al agregar: " + e.getMessage());
        }
    }

    @FXML
    private void actualizarCancion() {
        Cancion seleccionada = cancionesTable.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarAlerta("Advertencia", "Debe seleccionar una canción de la tabla para actualizar");
            return;
        }

        try {
            int id = Integer.parseInt(idField.getText());
            String titulo = tituloField.getText();
            String artista = artistaField.getText();
            String genero = generoField.getText();
            int año = Integer.parseInt(añoField.getText());
            double duracion = Double.parseDouble(duracionField.getText());

            if (titulo.isEmpty() || artista.isEmpty() || genero.isEmpty()) {
                mostrarAlerta("Error", "Todos los campos deben estar llenos");
                return;
            }

            cancionController.actualizarCancion(id, titulo, artista, genero, año, duracion);
            cargarDatos();
            limpiarCamposCanciones();
            mostrarAlerta("Éxito", "Canción actualizada correctamente");
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Los campos ID, Año y Duración deben ser números válidos");
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al actualizar: " + e.getMessage());
        }
    }

    @FXML
    private void eliminarCancion() {
        Cancion seleccionada = cancionesTable.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText(null);
            confirmacion.setContentText("¿Está seguro de eliminar la canción: " + seleccionada.getTitulo() + "?");

            if (confirmacion.showAndWait().get() == ButtonType.OK) {
                cancionController.eliminarCancion(seleccionada.getId());
                cargarDatos();
                limpiarCamposCanciones();
                mostrarAlerta("Éxito", "Canción eliminada");
            }
        } else {
            mostrarAlerta("Advertencia", "Debe seleccionar una canción para eliminar");
        }
    }

    @FXML
    private void limpiarCamposCanciones() {
        idField.clear();
        tituloField.clear();
        artistaField.clear();
        generoField.clear();
        añoField.clear();
        duracionField.clear();
        cancionesTable.getSelectionModel().clearSelection();
    }

    // ==================== GESTIÓN DE USUARIOS ====================

    private void cargarUsuarioEnCampos(Usuario usuario) {
        if (usuario != null) {
            usernameOriginal = usuario.getUsername();
            usernameField.setText(usuario.getUsername());
            usernameField.setEditable(true);
            passwordField.setText(usuario.getPassword());
            nombreUsuarioField.setText(usuario.getNombre());
        } else {
            usernameOriginal = null;
            usernameField.setEditable(true);
        }
    }

    @FXML
    private void agregarUsuario() {
        try {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();
            String nombre = nombreUsuarioField.getText().trim();

            if (username.isEmpty() || password.isEmpty() || nombre.isEmpty()) {
                mostrarAlerta("Error", "Todos los campos deben estar llenos");
                return;
            }

            if (password.length() < 4) {
                mostrarAlerta("Error", "La contraseña debe tener al menos 4 caracteres");
                return;
            }

            boolean exito = usuarioController.registrar(username, password, nombre);

            if (exito) {
                cargarDatos();
                limpiarCamposUsuarios();
                mostrarAlerta("Éxito", "Usuario agregado correctamente");
            } else {
                mostrarAlerta("Error", "El username ya existe o los datos son inválidos");
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al agregar usuario: " + e.getMessage());
        }
    }

    @FXML
    private void actualizarUsuario() {
        Usuario seleccionado = usuariosTable.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta("Advertencia", "Debe seleccionar un usuario de la tabla para actualizar");
            return;
        }

        try {
            String usernameNuevo = usernameField.getText().trim();
            String password = passwordField.getText();
            String nombre = nombreUsuarioField.getText().trim();

            if (usernameNuevo.isEmpty() || password.isEmpty() || nombre.isEmpty()) {
                mostrarAlerta("Error", "Todos los campos deben estar llenos");
                return;
            }

            if (password.length() < 4) {
                mostrarAlerta("Error", "La contraseña debe tener al menos 4 caracteres");
                return;
            }

            usuarioController.actualizar(usernameOriginal, usernameNuevo, password, nombre);
            cargarDatos();
            limpiarCamposUsuarios();
            mostrarAlerta("Éxito", "Usuario actualizado correctamente");
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al actualizar usuario: " + e.getMessage());
        }
    }

    @FXML
    private void eliminarUsuario() {
        Usuario seleccionado = usuariosTable.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText(null);
            confirmacion.setContentText("¿Está seguro de eliminar el usuario: " + seleccionado.getUsername() + "?");

            if (confirmacion.showAndWait().get() == ButtonType.OK) {
                usuarioController.eliminar(seleccionado.getUsername());
                cargarDatos();
                limpiarCamposUsuarios();
                mostrarAlerta("Éxito", "Usuario eliminado");
            }
        } else {
            mostrarAlerta("Advertencia", "Debe seleccionar un usuario para eliminar");
        }
    }

    @FXML
    private void limpiarCamposUsuarios() {
        usernameOriginal = null;
        usernameField.clear();
        usernameField.setEditable(true);
        passwordField.clear();
        nombreUsuarioField.clear();
        usuariosTable.getSelectionModel().clearSelection();
    }

    // ==================== UTILIDADES ====================

    /**
     * Cerrar sesión del administrador
     */
    @FXML
    private void cerrarSesion() {
        // Cerrar sesión en SessionManager
        SessionManager.getInstance().cerrarSesion();

        try {
            // Cargar vista de Login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent loginView = loader.load();

            // Obtener la escena actual y cambiar la raíz
            usernameField.getScene().setRoot(loginView);

        } catch (IOException e) {
            System.err.println("Error al cargar Login");
            e.printStackTrace();
            mostrarAlerta("Error", "Error al cerrar sesión: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(
                titulo.equals("Error") ? Alert.AlertType.ERROR :
                        titulo.equals("Advertencia") ? Alert.AlertType.WARNING :
                                Alert.AlertType.INFORMATION
        );
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}