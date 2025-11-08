package co.edu.uniquindio.syncup.view.controllers;

import co.edu.uniquindio.syncup.Controller.PlaylistController;
import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import co.edu.uniquindio.syncup.Model.Entidades.Playlist;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import co.edu.uniquindio.syncup.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class RecommendationsViewController {

    @FXML private TableView<Cancion> recomendacionesTable;
    @FXML private TableColumn<Cancion, String> tituloColumn;
    @FXML private TableColumn<Cancion, String> artistaColumn;
    @FXML private TableColumn<Cancion, String> generoColumn;
    @FXML private Label infoLabel;

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
        generoColumn.setCellValueFactory(new PropertyValueFactory<>("genero"));

        generarDescubrimiento();
    }

    @FXML
    private void generarDescubrimiento() {
        recomendacionesTable.getItems().clear();

        // RF-005: Generar playlist usando Dijkstra en GrafoDeSimilitud
        Playlist descubrimiento = playlistController.generarDescubrimientoSemanal(usuarioActual);

        recomendacionesTable.getItems().addAll(descubrimiento.getCanciones());
        infoLabel.setText("Descubrimiento Semanal: " + descubrimiento.getCanciones().size() + " canciones recomendadas basadas en tus favoritos");
    }
}