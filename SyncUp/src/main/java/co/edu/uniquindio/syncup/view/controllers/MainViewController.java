package co.edu.uniquindio.syncup.view.controllers;

import co.edu.uniquindio.syncup.SyncUpApp;
import co.edu.uniquindio.syncup.Controller.*;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import co.edu.uniquindio.syncup.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * MainViewController
 * Controlador principal que gestiona la navegación entre vistas
 */
public class MainViewController {

    @FXML private BorderPane mainContainer;
    @FXML private VBox sidebar;
    @FXML private BorderPane contentArea;
    @FXML private Label usernameLabel;
    @FXML private Label currentSongLabel;

    // Controladores
    private UsuarioController usuarioController;
    private CancionController cancionController;
    private PlaylistController playlistController;
    private RadioController radioController;

    private Usuario usuarioActual;

    @FXML
    public void initialize() {
        // Obtener controladores
        usuarioController = SyncUpApp.getUsuarioController();
        cancionController = SyncUpApp.getCancionController();
        playlistController = SyncUpApp.getPlaylistController();
        radioController = SyncUpApp.getRadioController();

        // Obtener usuario actual
        usuarioActual = SessionManager.getInstance().getUsuarioActual();

        if (usuarioActual != null) {
            usernameLabel.setText(usuarioActual.getNombre());
        }

        // Cargar vista Home por defecto
        cargarHome();
    }

    /**
     * Navegación a Home
     */
    @FXML
    public void cargarHome() {
        cargarVista("/fxml/views/Home.fxml");
    }

    /**
     * Navegación a Search
     */
    @FXML
    public void cargarSearch() {
        cargarVista("/fxml/views/Search.fxml");
    }

    /**
     * Navegación a Library
     */
    @FXML
    public void cargarLibrary() {
        cargarVista("/fxml/views/Library.fxml");
    }

    /**
     * Navegación a Favorites
     */
    @FXML
    public void cargarFavorites() {
        cargarVista("/fxml/views/Favorites.fxml");
    }

    /**
     * Navegación a Social
     */
    @FXML
    public void cargarSocial() {
        cargarVista("/fxml/views/Social.fxml");
    }

    /**
     * Navegación a Recommendations
     */
    @FXML
    public void cargarRecommendations() {
        cargarVista("/fxml/views/Recommendations.fxml");
    }

    /**
     * Navegación a Radio
     */
    @FXML
    public void cargarRadio() {
        cargarVista("/fxml/views/Radio.fxml");
    }

    /**
     * Navegación a Profile
     */
    @FXML
    public void cargarProfile() {
        cargarVista("/fxml/views/Profile.fxml");
    }

    /**
     * Método genérico para cargar vistas
     */
    private void cargarVista(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node vista = loader.load();

            // Obtener el controller de la vista cargada e inyectar dependencias
            Object controller = loader.getController();

            if (controller instanceof HomeViewController) {
                ((HomeViewController) controller).setControllers(
                        usuarioController, cancionController, playlistController, radioController
                );
            } else if (controller instanceof SearchViewController) {
                ((SearchViewController) controller).setControllers(
                        cancionController, playlistController
                );
            } else if (controller instanceof LibraryViewController) {
                ((LibraryViewController) controller).setControllers(
                        cancionController, playlistController
                );
            } else if (controller instanceof FavoritesViewController) {
                ((FavoritesViewController) controller).setControllers(
                        playlistController
                );
            } else if (controller instanceof SocialViewController) {
                ((SocialViewController) controller).setControllers(
                        usuarioController
                );
            } else if (controller instanceof RecommendationsViewController) {
                ((RecommendationsViewController) controller).setControllers(
                        playlistController
                );
            } else if (controller instanceof ProfileViewController) {
                ((ProfileViewController) controller).setControllers(
                        usuarioController
                );
            } else if (controller instanceof RadioViewController) {
                ((RadioViewController) controller).setControllers(
                        radioController, playlistController
                );
            }

            // Cargar en el área de contenido
            contentArea.setCenter(vista);

        } catch (IOException e) {
            System.err.println("Error al cargar vista: " + fxmlPath);
            e.printStackTrace();
        }
    }

    /**
     * Cerrar sesión
     */
    @FXML
    public void cerrarSesion() {
        SessionManager.getInstance().cerrarSesion();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent loginView = loader.load();

            mainContainer.getScene().setRoot(loginView);

        } catch (IOException e) {
            System.err.println("Error al cargar Login");
            e.printStackTrace();
        }
    }

    public void actualizarCancionActual(String nombreCancion) {
        if (currentSongLabel != null) {
            currentSongLabel.setText("♫ " + nombreCancion);
        }
    }
}