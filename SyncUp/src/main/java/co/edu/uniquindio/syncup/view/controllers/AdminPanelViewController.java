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
    void removeUser(ActionEvent event) {

    }

    @FXML
    void updateSONG(ActionEvent event) {

    }

    @FXML
    void updateUser(ActionEvent event) {

    }

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

        cargarDatos();
    }

    private void cargarDatos() {
        cancionesTable.getItems().setAll(cancionController.obtenerTodas());
        usuariosTable.getItems().setAll(usuarioController.listarUsuarios());
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
            mostrarAlerta("Error", "Datos inválidos");
        }
    }

    @FXML
    private void eliminarCancion() {
        Cancion seleccionada = cancionesTable.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            cancionController.eliminarCancion(seleccionada.getId());
            cargarDatos();
            mostrarAlerta("Éxito", "Canción eliminada");
        }
    }

    @FXML
    private void eliminarUsuario() {
        Usuario seleccionado = usuariosTable.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            usuarioController.eliminar(seleccionado.getUsername());
            cargarDatos();
            mostrarAlerta("Éxito", "Usuario eliminado");
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

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}