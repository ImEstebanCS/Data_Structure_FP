package co.edu.uniquindio.syncup.view.controllers;

import co.edu.uniquindio.syncup.Controller.PlaylistController;
import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import co.edu.uniquindio.syncup.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class FavoritesViewController {

    @FXML private TableView<Cancion> favoritosTable;
    @FXML private TableColumn<Cancion, String> tituloColumn;
    @FXML private TableColumn<Cancion, String> artistaColumn;
    @FXML private TableColumn<Cancion, String> duracionColumn;
    @FXML private Label totalLabel;

    private PlaylistController playlistController;
    private Usuario usuarioActual;

    public void setControllers(PlaylistController playlistController) {
        this.playlistController = playlistController;
        inicializar();
    }

    private void inicializar() {
        usuarioActual = SessionManager.getInstance().getUsuarioActual();

        tituloColumn.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        artistaColumn.setCellValueFactory(new PropertyValueFactory<>("artista"));
        duracionColumn.setCellValueFactory(new PropertyValueFactory<>("duracionFormateada"));

        List<Cancion> favoritos = playlistController.obtenerFavoritos(usuarioActual);
        favoritosTable.getItems().addAll(favoritos);
        totalLabel.setText(favoritos.size() + " canciones favoritas");
    }

    @FXML
    private void eliminarFavorito() {
        Cancion seleccionada = favoritosTable.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            playlistController.removerFavorito(usuarioActual, seleccionada);
            favoritosTable.getItems().remove(seleccionada);
            totalLabel.setText(favoritosTable.getItems().size() + " canciones favoritas");
        }
    }
}