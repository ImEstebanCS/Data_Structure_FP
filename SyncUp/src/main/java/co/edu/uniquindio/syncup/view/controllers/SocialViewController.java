package co.edu.uniquindio.syncup.view.controllers;

import co.edu.uniquindio.syncup.Controller.UsuarioController;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import co.edu.uniquindio.syncup.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class SocialViewController {

    @FXML private ListView<String> seguidoresListView;
    @FXML private ListView<String> siguiendoListView;
    @FXML private ListView<String> sugerenciasListView;
    @FXML private Label seguidoresLabel;
    @FXML private Label siguiendoLabel;

    private UsuarioController usuarioController;
    private Usuario usuarioActual;

    public void setControllers(UsuarioController usuarioController) {
        this.usuarioController = usuarioController;
        inicializar();
    }

    private void inicializar() {
        usuarioActual = SessionManager.getInstance().getUsuarioActual();
        cargarDatos();
    }

    private void cargarDatos() {
        // Seguidores
        List<Usuario> seguidores = usuarioController.obtenerSeguidores(usuarioActual);
        seguidoresListView.getItems().clear();
        seguidores.forEach(u -> seguidoresListView.getItems().add(u.toString()));
        seguidoresLabel.setText(seguidores.size() + " seguidores");

        // Siguiendo
        List<Usuario> siguiendo = usuarioController.obtenerSiguiendo(usuarioActual);
        siguiendoListView.getItems().clear();
        siguiendo.forEach(u -> siguiendoListView.getItems().add(u.toString()));
        siguiendoLabel.setText(siguiendo.size() + " siguiendo");

        // RF-008: Sugerencias usando BFS
        List<Usuario> sugerencias = usuarioController.obtenerSugerencias(usuarioActual, 5);
        sugerenciasListView.getItems().clear();
        sugerencias.forEach(u -> sugerenciasListView.getItems().add(u.toString() + " (amigo de amigo)"));
    }
}