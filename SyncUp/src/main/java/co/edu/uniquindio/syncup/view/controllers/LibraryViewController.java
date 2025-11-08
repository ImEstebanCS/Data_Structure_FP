package co.edu.uniquindio.syncup.view.controllers;

import co.edu.uniquindio.syncup.Controller.CancionController;
import co.edu.uniquindio.syncup.Controller.PlaylistController;
import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class LibraryViewController {

    @FXML private TableView<Cancion> cancionesTable;
    @FXML private TableColumn<Cancion, String> tituloColumn;
    @FXML private TableColumn<Cancion, String> artistaColumn;
    @FXML private TableColumn<Cancion, String> generoColumn;
    @FXML private TableColumn<Cancion, Integer> añoColumn;
    @FXML private Label totalLabel;

    private CancionController cancionController;
    private PlaylistController playlistController;

    public void setControllers(CancionController cancionController, PlaylistController playlistController) {
        this.cancionController = cancionController;
        this.playlistController = playlistController;
        inicializar();
    }

    private void inicializar() {
        // Configurar columnas
        tituloColumn.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        artistaColumn.setCellValueFactory(new PropertyValueFactory<>("artista"));
        generoColumn.setCellValueFactory(new PropertyValueFactory<>("genero"));
        añoColumn.setCellValueFactory(new PropertyValueFactory<>("año"));

        // Cargar canciones
        List<Cancion> canciones = cancionController.obtenerTodas();
        cancionesTable.getItems().addAll(canciones);
        totalLabel.setText(canciones.size() + " canciones en biblioteca");
    }
}