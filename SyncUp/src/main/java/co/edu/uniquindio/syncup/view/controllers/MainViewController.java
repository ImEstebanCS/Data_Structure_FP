package co.edu.uniquindio.syncup.view.controllers;

import co.edu.uniquindio.syncup.Controller.*;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import co.edu.uniquindio.syncup.SyncUpApp;
import co.edu.uniquindio.syncup.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.io.IOException;

public class MainViewController {

    @FXML private VBox sidebar;
    @FXML private BorderPane contentArea;
    @FXML private HBox musicPlayerBar;
    @FXML private Label usernameLabel;
    @FXML private Label currentSongLabel;
    @FXML private Label currentArtistLabel;
    @FXML private Label playerIconLabel;

    @FXML private Button homeBtn;
    @FXML private Button searchBtn;
    @FXML private Button libraryBtn;
    @FXML private Button favoritesBtn;
    @FXML private Button socialBtn;
    @FXML private Button recommendationsBtn;
    @FXML private Button radioBtn;
    @FXML private Button profileBtn;
    @FXML private Button playPauseBtn;

    private UsuarioController usuarioController;
    private CancionController cancionController;
    private PlaylistController playlistController;
    private RadioController radioController;
    private Usuario usuarioActual;

    @FXML
    public void initialize() {
        System.out.println("✅ MainViewController inicializando...");

        this.usuarioController = SyncUpApp.getUsuarioController();
        this.cancionController = SyncUpApp.getCancionController();
        this.playlistController = SyncUpApp.getPlaylistController();
        this.radioController = SyncUpApp.getRadioController();

        usuarioActual = SessionManager.getInstance().getUsuarioActual();

        if (usuarioActual != null) {
            usernameLabel.setText(usuarioActual.getNombre());
            System.out.println("✅ Usuario activo: " + usuarioActual.getNombre());
        } else {
            System.out.println("⚠️ No hay usuario activo en la sesión");
        }

        // Cargar la vista de inicio automáticamente
        cargarHome();
    }

    // ✅ MÉTODO AGREGADO - Este era el método faltante que causaba el error
    private void cargarHome() {
        System.out.println("🏠 Cargando vista de inicio...");
        cargarVista("/fxml/views/Home.fxml", homeBtn);
    }

    @FXML
    private void mostrarHome() {
        System.out.println("🏠 Mostrando Home");
        cargarVista("/fxml/views/Home.fxml", homeBtn);
    }

    @FXML
    private void mostrarBuscar() {
        System.out.println("🔍 Mostrando Búsqueda");
        cargarVista("/fxml/views/Search.fxml", searchBtn);
    }

    @FXML
    private void mostrarBiblioteca() {
        System.out.println("📚 Mostrando Biblioteca");
        cargarVista("/fxml/views/Library.fxml", libraryBtn);
    }

    @FXML
    private void mostrarFavoritos() {
        System.out.println("❤️ Mostrando Favoritos");
        cargarVista("/fxml/views/Favorites.fxml", favoritesBtn);
    }

    @FXML
    private void mostrarRedSocial() {
        System.out.println("👥 Mostrando Red Social");
        cargarVista("/fxml/views/Social.fxml", socialBtn);
    }

    @FXML
    private void mostrarDescubrimiento() {
        System.out.println("🎯 Mostrando Descubrimiento");
        cargarVista("/fxml/views/Recommendations.fxml", recommendationsBtn);
    }

    @FXML
    private void mostrarRadio() {
        System.out.println("📻 Mostrando Radio");
        cargarVista("/fxml/views/Radio.fxml", radioBtn);
    }

    @FXML
    private void mostrarPerfil() {
        System.out.println("👤 Mostrando Perfil");
        cargarVista("/fxml/views/Profile.fxml", profileBtn);
    }

    // ✅ MÉTODO CORREGIDO - Ahora funciona correctamente con BorderPane
    private void cargarVista(String fxmlPath, Button botonActivo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Pane vista = loader.load();

            Object controller = loader.getController();

            // ✅ INYECCIÓN DE DEPENDENCIAS CORREGIDA
            if (controller instanceof HomeViewController) {
                // HomeViewController ya tiene su constructor que inicializa los controladores
                HomeViewController homeController = (HomeViewController) controller;
                System.out.println("✅ HomeViewController cargado");
            } else if (controller instanceof SearchViewController) {
                SearchViewController searchController = (SearchViewController) controller;
                searchController.setControllers(cancionController, playlistController, usuarioController);
                System.out.println("✅ SearchViewController cargado");
            } else if (controller instanceof LibraryViewController) {
                LibraryViewController libraryController = (LibraryViewController) controller;
                libraryController.setControllers(usuarioController, cancionController, playlistController, radioController);
                System.out.println("✅ LibraryViewController cargado");
            } else if (controller instanceof FavoritesViewController) {
                FavoritesViewController favController = (FavoritesViewController) controller;
                favController.setControllers(playlistController);
                System.out.println("✅ FavoritesViewController cargado");
            } else if (controller instanceof ProfileViewController) {
                ProfileViewController profileController = (ProfileViewController) controller;
                profileController.setControllers(usuarioController);
                System.out.println("✅ ProfileViewController cargado");
            } else if (controller instanceof SocialViewController) {
                SocialViewController socialController = (SocialViewController) controller;
                socialController.setControllers(usuarioController);
                System.out.println("✅ SocialViewController cargado");
            } else if (controller instanceof RadioViewController) {
                RadioViewController radioViewController = (RadioViewController) controller;
                radioViewController.setControllers(radioController, playlistController);
                System.out.println("✅ RadioViewController cargado");
            } else if (controller instanceof RecommendationsViewController) {
                RecommendationsViewController recController = (RecommendationsViewController) controller;
                recController.setControllers(playlistController);
                System.out.println("✅ RecommendationsViewController cargado");
            }

            // ✅ CORREGIDO: Usar setCenter en lugar de getChildren()
            contentArea.setCenter(vista);

            resetearEstilosBotones();
            if (botonActivo != null) {
                botonActivo.setStyle(
                        "-fx-background-color: #1DB954; " +
                                "-fx-text-fill: #FFFFFF; " +
                                "-fx-font-size: 14px; " +
                                "-fx-font-weight: bold; " +
                                "-fx-alignment: center-left; " +
                                "-fx-padding: 15 20; " +
                                "-fx-background-radius: 8; " +
                                "-fx-cursor: hand;"
                );
            }

        } catch (IOException e) {
            System.err.println("❌ Error al cargar vista: " + fxmlPath);
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void resetearEstilosBotones() {
        String estiloInactivo =
                "-fx-background-color: transparent; " +
                        "-fx-text-fill: #B3B3B3; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-alignment: center-left; " +
                        "-fx-padding: 15 20; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand;";

        homeBtn.setStyle(estiloInactivo);
        searchBtn.setStyle(estiloInactivo);
        libraryBtn.setStyle(estiloInactivo);
        favoritesBtn.setStyle(estiloInactivo);
        socialBtn.setStyle(estiloInactivo);
        recommendationsBtn.setStyle(estiloInactivo);
        radioBtn.setStyle(estiloInactivo);
        profileBtn.setStyle(estiloInactivo);
    }

    @FXML
    private void playPause() {
        System.out.println("▶️ Play/Pause");
    }

    @FXML
    private void nextSong() {
        System.out.println("⏭️ Next Song");
    }

    @FXML
    private void previousSong() {
        System.out.println("⏮️ Previous Song");
    }

    @FXML
    private void toggleShuffle() {
        System.out.println("🔀 Toggle Shuffle");
    }

    @FXML
    private void toggleRepeat() {
        System.out.println("🔁 Toggle Repeat");
    }

    @FXML
    private void toggleMute() {
        System.out.println("🔇 Toggle Mute");
    }

    @FXML
    private void cerrarSesion() {
        try {
            System.out.println("🚪 Cerrando sesión...");

            // Cerrar sesión en SessionManager
            SessionManager.getInstance().cerrarSesion();

            // Cargar la vista de Login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            javafx.scene.Parent loginView = loader.load();

            // Cambiar la escena actual a la de login
            usernameLabel.getScene().setRoot(loginView);

            System.out.println("✅ Sesión cerrada correctamente");

        } catch (IOException e) {
            System.err.println("❌ Error al cerrar sesión: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ✅ MÉTODO PÚBLICO para actualizar información de reproducción
    public void actualizarReproduccion(String titulo, String artista) {
        if (currentSongLabel != null) {
            currentSongLabel.setText(titulo);
        }
        if (currentArtistLabel != null) {
            currentArtistLabel.setText(artista);
        }
        if (playerIconLabel != null) {
            playerIconLabel.setText("▶️");
        }
    }
}