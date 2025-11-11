package co.edu.uniquindio.syncup.view.controllers;

import co.edu.uniquindio.syncup.Controller.UsuarioController;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import co.edu.uniquindio.syncup.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ProfileViewController {

    @FXML private Label usernameLabel;
    @FXML private Label nombreLabel;
    @FXML private Label favoritosLabel;
    @FXML private Label seguidoresLabel;
    @FXML private Label siguiendoLabel;

    private Usuario usuarioActual;

    public void setControllers(UsuarioController usuarioController) {
        // Campo no usado actualmente, pero mantenido para futuras funcionalidades
        inicializar();
    }

    private void inicializar() {
        usuarioActual = SessionManager.getInstance().getUsuarioActual();

        if (usuarioActual != null) {
            usernameLabel.setText("@" + usuarioActual.getUsername());
            nombreLabel.setText(usuarioActual.getNombre());
            favoritosLabel.setText(usuarioActual.getListaFavoritos().size() + " canciones favoritas");
            seguidoresLabel.setText(usuarioActual.getSeguidores().size() + " seguidores");
            siguiendoLabel.setText(usuarioActual.getSiguiendo().size() + " siguiendo");
        }
    }
}