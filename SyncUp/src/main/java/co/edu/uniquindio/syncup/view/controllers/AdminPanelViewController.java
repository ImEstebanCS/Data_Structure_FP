package co.edu.uniquindio.syncup.view.controllers;

import co.edu.uniquindio.syncup.SyncUpApp;
import co.edu.uniquindio.syncup.Controller.CancionController;
import co.edu.uniquindio.syncup.Controller.UsuarioController;
import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import co.edu.uniquindio.syncup.utils.SessionManager;
import co.edu.uniquindio.syncup.utils.UIComponents;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class AdminPanelViewController {

<<<<<<< Updated upstream
=======
    @FXML private StackPane contentArea;
    @FXML private VBox dashboardView;
    @FXML private VBox cancionesView;
    @FXML private VBox usuariosView;
    @FXML private VBox estadisticasView;
    @FXML private VBox estadisticasContainer;

    @FXML private Button dashboardBtn;
    @FXML private Button cancionesBtn;
    @FXML private Button usuariosBtn;
    @FXML private Button estadisticasBtn;

    @FXML private Label totalCancionesLabel;
    @FXML private Label totalUsuariosLabel;
    @FXML private Label generosLabel;

>>>>>>> Stashed changes
    @FXML private TableView<Cancion> cancionesTable;
    @FXML private TableColumn<Cancion, Integer> idColumn;
    @FXML private TableColumn<Cancion, String> tituloColumn;
    @FXML private TableColumn<Cancion, String> artistaColumn;

    @FXML private TableView<Usuario> usuariosTable;
    @FXML private TableColumn<Usuario, String> usernameColumn;
    @FXML private TableColumn<Usuario, String> nombreColumn;

    @FXML private TextField idField;
    @FXML private TextField tituloField;
    @FXML private TextField artistaField;
    @FXML private TextField generoField;
    @FXML private TextField añoField;
    @FXML private TextField duracionField;

<<<<<<< Updated upstream
=======
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField nombreUsuarioField;

>>>>>>> Stashed changes
    private CancionController cancionController;
    private UsuarioController usuarioController;

    @FXML
    public void initialize() {
        cancionController = SyncUpApp.getCancionController();
        usuarioController = SyncUpApp.getUsuarioController();

        configurarTablas();
        configurarListeners();
        cargarDatos();
        actualizarDashboard();

        mostrarDashboard();
    }
    @FXML
    private void configurarTablas() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        tituloColumn.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        artistaColumn.setCellValueFactory(new PropertyValueFactory<>("artista"));

        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));

<<<<<<< Updated upstream
        cargarDatos();
=======
        cancionesTable.getStyleClass().add("styled-table");
        usuariosTable.getStyleClass().add("styled-table");
    }
    @FXML
    private void configurarListeners() {
        cancionesTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> cargarCancionEnCampos(newValue)
        );

        usuariosTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> cargarUsuarioEnCampos(newValue)
        );
>>>>>>> Stashed changes
    }
    @FXML
    private void cargarDatos() {
        cancionesTable.getItems().setAll(cancionController.obtenerTodas());
        usuariosTable.getItems().setAll(usuarioController.listarUsuarios());
    }
    @FXML
    private void actualizarDashboard() {
        int totalCanciones = cancionController.obtenerTotal();
        int totalUsuarios = usuarioController.getCantidadUsuarios();

<<<<<<< Updated upstream
=======
        Set<String> generos = new HashSet<>();
        for (Cancion cancion : cancionController.obtenerTodas()) {
            generos.add(cancion.getGenero());
        }

        if (totalCancionesLabel != null) totalCancionesLabel.setText(String.valueOf(totalCanciones));
        if (totalUsuariosLabel != null) totalUsuariosLabel.setText(String.valueOf(totalUsuarios));
        if (generosLabel != null) generosLabel.setText(String.valueOf(generos.size()));
    }

    @FXML
    private void mostrarDashboard() {
        cambiarVista(dashboardView, dashboardBtn);
        actualizarDashboard();
    }

    @FXML
    private void mostrarCanciones() {
        cambiarVista(cancionesView, cancionesBtn);
    }

    @FXML
    private void mostrarUsuarios() {
        cambiarVista(usuariosView, usuariosBtn);
    }

    @FXML
    private void mostrarEstadisticas() {
        cambiarVista(estadisticasView, estadisticasBtn);
        cargarEstadisticas();
    }
    @FXML
    private void cambiarVista(VBox vistaActiva, Button botonActivo) {
        dashboardView.setVisible(false);
        dashboardView.setManaged(false);
        cancionesView.setVisible(false);
        cancionesView.setManaged(false);
        usuariosView.setVisible(false);
        usuariosView.setManaged(false);
        estadisticasView.setVisible(false);
        estadisticasView.setManaged(false);

        vistaActiva.setVisible(true);
        vistaActiva.setManaged(true);

        resetearEstilosBotones();
        botonActivo.setStyle(
                "-fx-background-color: #282828; " +
                        "-fx-text-fill: #1DB954; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-alignment: center-left; " +
                        "-fx-padding: 15 20; " +
                        "-fx-background-radius: 8;"
        );
    }
    @FXML
    private void resetearEstilosBotones() {
        String estiloNormal = "-fx-background-color: transparent; " +
                "-fx-text-fill: #B3B3B3; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold; " +
                "-fx-alignment: center-left; " +
                "-fx-padding: 15 20; " +
                "-fx-background-radius: 8; " +
                "-fx-cursor: hand;";

        if (dashboardBtn != null) dashboardBtn.setStyle(estiloNormal);
        if (cancionesBtn != null) cancionesBtn.setStyle(estiloNormal);
        if (usuariosBtn != null) usuariosBtn.setStyle(estiloNormal);
        if (estadisticasBtn != null) estadisticasBtn.setStyle(estiloNormal);
    }
    @FXML
    private void cargarEstadisticas() {
        estadisticasContainer.getChildren().clear();

        int totalCanciones = cancionController.obtenerTotal();
        int totalUsuarios = usuarioController.getCantidadUsuarios();

        Set<String> generos = new HashSet<>();
        Set<String> artistas = new HashSet<>();
        for (Cancion cancion : cancionController.obtenerTodas()) {
            generos.add(cancion.getGenero());
            artistas.add(cancion.getArtista());
        }

        HBox statsRow1 = new HBox(20);
        statsRow1.setPadding(new Insets(10));
        statsRow1.getChildren().addAll(
                UIComponents.crearStatCard(String.valueOf(totalCanciones), "Total Canciones", "🎵"),
                UIComponents.crearStatCard(String.valueOf(totalUsuarios), "Total Usuarios", "👥"),
                UIComponents.crearStatCard(String.valueOf(generos.size()), "Géneros Musicales", "🎸"),
                UIComponents.crearStatCard(String.valueOf(artistas.size()), "Artistas Únicos", "🎤")
        );

        estadisticasContainer.getChildren().add(statsRow1);
    }
    @FXML
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

>>>>>>> Stashed changes
    @FXML
    private void agregarCancion() {
        try {
            int id = Integer.parseInt(idField.getText());
            String titulo = tituloField.getText();
            String artista = artistaField.getText();
            String genero = generoField.getText();
            int año = Integer.parseInt(añoField.getText());
            double duracion = Double.parseDouble(duracionField.getText());

<<<<<<< Updated upstream
            cancionController.agregarCancion(id, titulo, artista, genero, año, duracion);
            cargarDatos();
            limpiarCampos();
            mostrarAlerta("Éxito", "Canción agregada correctamente");
        } catch (Exception e) {
            mostrarAlerta("Error", "Datos inválidos");
=======
            if (titulo.isEmpty() || artista.isEmpty() || genero.isEmpty()) {
                mostrarAlerta("Error", "Todos los campos deben estar llenos", "❌");
                return;
            }

            cancionController.agregarCancion(id, titulo, artista, genero, año, duracion);
            cargarDatos();
            limpiarCamposCanciones();
            actualizarDashboard();
            UIComponents.mostrarAlertaPersonalizada("Éxito", "Canción agregada correctamente", "✅");
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Los campos ID, Año y Duración deben ser números válidos", "❌");
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al agregar: " + e.getMessage(), "❌");
        }
    }

    @FXML
    private void actualizarCancion() {
        Cancion seleccionada = cancionesTable.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarAlerta("Advertencia", "Debe seleccionar una canción de la tabla para actualizar", "⚠️");
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
                mostrarAlerta("Error", "Todos los campos deben estar llenos", "❌");
                return;
            }

            cancionController.actualizarCancion(id, titulo, artista, genero, año, duracion);
            cargarDatos();
            limpiarCamposCanciones();
            actualizarDashboard();
            UIComponents.mostrarAlertaPersonalizada("Éxito", "Canción actualizada correctamente", "✅");
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Los campos ID, Año y Duración deben ser números válidos", "❌");
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al actualizar: " + e.getMessage(), "❌");
>>>>>>> Stashed changes
        }
    }

    @FXML
    private void eliminarCancion() {
        Cancion seleccionada = cancionesTable.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
<<<<<<< Updated upstream
            cancionController.eliminarCancion(seleccionada.getId());
            cargarDatos();
            mostrarAlerta("Éxito", "Canción eliminada");
=======
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText(null);
            confirmacion.setContentText("¿Está seguro de eliminar la canción: " + seleccionada.getTitulo() + "?");

            if (confirmacion.showAndWait().get() == ButtonType.OK) {
                cancionController.eliminarCancion(seleccionada.getId());
                cargarDatos();
                limpiarCamposCanciones();
                actualizarDashboard();
                UIComponents.mostrarAlertaPersonalizada("Éxito", "Canción eliminada", "✅");
            }
        } else {
            mostrarAlerta("Advertencia", "Debe seleccionar una canción para eliminar", "⚠️");
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

    @FXML
    private void cargaMasiva() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo de canciones");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de texto", "*.txt"),
                new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
        );

        File archivo = fileChooser.showOpenDialog(null);

        if (archivo != null) {
            UIComponents.mostrarAlertaPersonalizada("Carga Masiva",
                    "Funcionalidad de carga masiva\nArchivo seleccionado: " + archivo.getName() +
                            "\n\nEsta funcionalidad será implementada próximamente", "📥");
        }
    }
    @FXML
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
                mostrarAlerta("Error", "Todos los campos deben estar llenos", "❌");
                return;
            }

            if (password.length() < 4) {
                mostrarAlerta("Error", "La contraseña debe tener al menos 4 caracteres", "❌");
                return;
            }

            boolean exito = usuarioController.registrar(username, password, nombre);

            if (exito) {
                cargarDatos();
                limpiarCamposUsuarios();
                actualizarDashboard();
                UIComponents.mostrarAlertaPersonalizada("Éxito", "Usuario agregado correctamente", "✅");
            } else {
                mostrarAlerta("Error", "El username ya existe o los datos son inválidos", "❌");
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al agregar usuario: " + e.getMessage(), "❌");
        }
    }

    @FXML
    private void actualizarUsuario() {
        Usuario seleccionado = usuariosTable.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta("Advertencia", "Debe seleccionar un usuario de la tabla para actualizar", "⚠️");
            return;
        }

        try {
            String usernameNuevo = usernameField.getText().trim();
            String password = passwordField.getText();
            String nombre = nombreUsuarioField.getText().trim();

            if (usernameNuevo.isEmpty() || password.isEmpty() || nombre.isEmpty()) {
                mostrarAlerta("Error", "Todos los campos deben estar llenos", "❌");
                return;
            }

            if (password.length() < 4) {
                mostrarAlerta("Error", "La contraseña debe tener al menos 4 caracteres", "❌");
                return;
            }

            usuarioController.actualizar(usernameOriginal, usernameNuevo, password, nombre);
            cargarDatos();
            limpiarCamposUsuarios();
            actualizarDashboard();
            UIComponents.mostrarAlertaPersonalizada("Éxito", "Usuario actualizado correctamente", "✅");
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al actualizar usuario: " + e.getMessage(), "❌");
>>>>>>> Stashed changes
        }
    }

    @FXML
    private void eliminarUsuario() {
        Usuario seleccionado = usuariosTable.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
<<<<<<< Updated upstream
            usuarioController.eliminar(seleccionado.getUsername());
            cargarDatos();
            mostrarAlerta("Éxito", "Usuario eliminado");
=======
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText(null);
            confirmacion.setContentText("¿Está seguro de eliminar el usuario: " + seleccionado.getUsername() + "?");

            if (confirmacion.showAndWait().get() == ButtonType.OK) {
                usuarioController.eliminar(seleccionado.getUsername());
                cargarDatos();
                limpiarCamposUsuarios();
                actualizarDashboard();
                UIComponents.mostrarAlertaPersonalizada("Éxito", "Usuario eliminado", "✅");
            }
        } else {
            mostrarAlerta("Advertencia", "Debe seleccionar un usuario para eliminar", "⚠️");
>>>>>>> Stashed changes
        }
    }

    private void limpiarCampos() {
        idField.clear();
        tituloField.clear();
        artistaField.clear();
        generoField.clear();
        añoField.clear();
        duracionField.clear();
    }

<<<<<<< Updated upstream
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
=======
    @FXML
    private void cerrarSesion() {
        SessionManager.getInstance().cerrarSesion();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent loginView = loader.load();

            usernameField.getScene().setRoot(loginView);

        } catch (IOException e) {
            System.err.println("Error al cargar Login");
            e.printStackTrace();
            mostrarAlerta("Error", "Error al cerrar sesión: " + e.getMessage(), "❌");
        }
    }
    @FXML
    private void mostrarAlerta(String titulo, String mensaje, String icono) {
        UIComponents.mostrarAlertaPersonalizada(titulo, mensaje, icono);
    }

    // ==================== EFECTOS HOVER ====================

    // Logout Button
    @FXML
    public void logoutHoverEnter(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #FF1744; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand; " +
                        "-fx-scale-x: 1.02; " +
                        "-fx-scale-y: 1.02;"
        );
    }

    @FXML
    public void logoutHoverExit(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #E91429; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand;"
        );
    }

    // Carga Masiva Button
    @FXML
    public void cargaHoverEnter(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #FFB730; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand; " +
                        "-fx-scale-x: 1.02; " +
                        "-fx-scale-y: 1.02;"
        );
    }

    @FXML
    public void cargaHoverExit(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #FFA500; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand;"
        );
    }

    // Agregar Canción Button
    @FXML
    public void agregarHoverEnter(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #1ed760; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand; " +
                        "-fx-scale-x: 1.02; " +
                        "-fx-scale-y: 1.02;"
        );
    }

    @FXML
    public void agregarHoverExit(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #1DB954; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand;"
        );
    }

    // Actualizar Canción Button
    @FXML
    public void actualizarHoverEnter(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #FFB730; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand; " +
                        "-fx-scale-x: 1.02; " +
                        "-fx-scale-y: 1.02;"
        );
    }

    @FXML
    public void actualizarHoverExit(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #FFA500; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand;"
        );
    }

    // Eliminar Canción Button
    @FXML
    public void eliminarHoverEnter(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #FF1744; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand; " +
                        "-fx-scale-x: 1.02; " +
                        "-fx-scale-y: 1.02;"
        );
    }

    @FXML
    public void eliminarHoverExit(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #E91429; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand;"
        );
    }

    // Limpiar Campos Button
    @FXML
    public void limpiarHoverEnter(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #505050; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand; " +
                        "-fx-scale-x: 1.02; " +
                        "-fx-scale-y: 1.02;"
        );
    }

    @FXML
    public void limpiarHoverExit(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #404040; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand;"
        );
    }

    // Agregar Usuario Button
    @FXML
    public void agregarUsuarioHoverEnter(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #1ed760; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand; " +
                        "-fx-scale-x: 1.02; " +
                        "-fx-scale-y: 1.02;"
        );
    }

    @FXML
    public void agregarUsuarioHoverExit(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #1DB954; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand;"
        );
    }

    // Actualizar Usuario Button
    @FXML
    public void actualizarUsuarioHoverEnter(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #FFB730; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand; " +
                        "-fx-scale-x: 1.02; " +
                        "-fx-scale-y: 1.02;"
        );
    }

    @FXML
    public void actualizarUsuarioHoverExit(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #FFA500; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand;"
        );
    }

    // Eliminar Usuario Button
    @FXML
    public void eliminarUsuarioHoverEnter(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #FF1744; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand; " +
                        "-fx-scale-x: 1.02; " +
                        "-fx-scale-y: 1.02;"
        );
    }

    @FXML
    public void eliminarUsuarioHoverExit(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #E91429; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand;"
        );
    }

    // Limpiar Usuario Button
    @FXML
    public void limpiarUsuarioHoverEnter(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #505050; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand; " +
                        "-fx-scale-x: 1.02; " +
                        "-fx-scale-y: 1.02;"
        );
    }

    @FXML
    public void limpiarUsuarioHoverExit(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #404040; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand;"
        );
>>>>>>> Stashed changes
    }
}