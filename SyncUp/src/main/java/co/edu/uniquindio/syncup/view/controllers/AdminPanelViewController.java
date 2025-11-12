package co.edu.uniquindio.syncup.view.controllers;

import co.edu.uniquindio.syncup.SyncUpApp;
import co.edu.uniquindio.syncup.Controller.CancionController;
import co.edu.uniquindio.syncup.Controller.UsuarioController;
import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminPanelViewController {

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

        cargarDatos();
    }

    private void cargarDatos() {
        cancionesTable.getItems().setAll(cancionController.obtenerTodas());
        usuariosTable.getItems().setAll(usuarioController.listarUsuarios());
    }

    /**
     * Carga los datos de la canción seleccionada en los campos de texto
     */
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

            cancionController.agregarCancion(id, titulo, artista, genero, año, duracion);
            cargarDatos();
            limpiarCampos();
            mostrarAlerta("Éxito", "Canción agregada correctamente");
        } catch (Exception e) {
            mostrarAlerta("Error", "Datos inválidos: " + e.getMessage());
        }
    }

    /**
     * Actualiza la canción seleccionada con los datos de los campos
     */
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

            // Validar que los campos no estén vacíos
            if (titulo.isEmpty() || artista.isEmpty() || genero.isEmpty()) {
                mostrarAlerta("Error", "Todos los campos deben estar llenos");
                return;
            }

            // Actualizar la canción
            cancionController.actualizarCancion(id, titulo, artista, genero, año, duracion);
            cargarDatos();
            limpiarCampos();
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
                limpiarCampos();
                mostrarAlerta("Éxito", "Canción eliminada");
            }
        } else {
            mostrarAlerta("Advertencia", "Debe seleccionar una canción para eliminar");
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
                mostrarAlerta("Éxito", "Usuario eliminado");
            }
        } else {
            mostrarAlerta("Advertencia", "Debe seleccionar un usuario para eliminar");
        }
    }

    @FXML
    private void limpiarCampos() {
        idField.clear();
        tituloField.clear();
        artistaField.clear();
        generoField.clear();
        añoField.clear();
        duracionField.clear();
        cancionesTable.getSelectionModel().clearSelection();
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

    // Métodos que puedes eliminar si no los usas
    @FXML
    void removeUser(ActionEvent event) {
        eliminarUsuario();
    }

    @FXML
    void updateSONG(ActionEvent event) {
        actualizarCancion();
    }

    @FXML
    void updateUser(ActionEvent event) {
        // Implementar si necesitas actualizar usuarios
    }
}