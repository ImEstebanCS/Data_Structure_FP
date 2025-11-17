package co.edu.uniquindio.syncup.view.controllers;

import co.edu.uniquindio.syncup.Controller.*;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import co.edu.uniquindio.syncup.SyncUpApp;
import co.edu.uniquindio.syncup.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.List;

public class MainViewController {

    @FXML private BorderPane contentArea;
    @FXML private Label usernameLabel;
    @FXML private VBox sugerenciasContainer;
    @FXML private Label currentSongLabel;
    @FXML private Label currentArtistLabel;
    @FXML private Label playerIconLabel;
    @FXML private Button playPauseBtn;

    // ✅ AGREGADOS - Botones del menú
    @FXML private Button homeBtn;
    @FXML private Button searchBtn;
    @FXML private Button libraryBtn;
    @FXML private Button favoritesBtn;
    @FXML private Button socialBtn;
    @FXML private Button recommendationsBtn;
    @FXML private Button radioBtn;
    @FXML private Button profileBtn;

    private UsuarioController usuarioController;
    private CancionController cancionController;
    private PlaylistController playlistController;
    private RadioController radioController;
    private Usuario usuarioActual;

    @FXML
    public void initialize() {
        usuarioController = SyncUpApp.getUsuarioController();
        cancionController = SyncUpApp.getCancionController();
        playlistController = SyncUpApp.getPlaylistController();
        radioController = SyncUpApp.getRadioController();

        usuarioActual = SessionManager.getInstance().getUsuarioActual();

        if (usuarioActual != null) {
            usernameLabel.setText(usuarioActual.getNombre());
            cargarSugerencias();
        }
        configurarBotonesMenu();
        cargarHome();
    }
    // ✅ MÉTODO NUEVO - Configurar hover effects
    private void configurarBotonesMenu() {
        configurarBotonHover(homeBtn);
        configurarBotonHover(searchBtn);
        configurarBotonHover(libraryBtn);
        configurarBotonHover(favoritesBtn);
        configurarBotonHover(socialBtn);
        configurarBotonHover(recommendationsBtn);
        configurarBotonHover(radioBtn);
        configurarBotonHover(profileBtn);
    }

    // ✅ MÉTODO NUEVO - Configurar hover individual
    private void configurarBotonHover(Button button) {
        if (button == null) return;

        String estilo = button.getStyle();

        button.setOnMouseEntered(e -> {
            if (!button.getStyleClass().contains("menu-button-active")) {
                button.setStyle(estilo + "-fx-background-color: #282828; -fx-text-fill: #1DB954;");
            }
        });

        button.setOnMouseExited(e -> {
            if (!button.getStyleClass().contains("menu-button-active")) {
                button.setStyle(estilo);
            }
        });
    }

    // ✅ MÉTODO AGREGADO - Para manejar estados activos de botones
    private void setActiveButton(Button activeButton) {
        // Remover clase activa de todos los botones
        if (homeBtn != null) homeBtn.getStyleClass().remove("menu-button-active");
        if (searchBtn != null) searchBtn.getStyleClass().remove("menu-button-active");
        if (libraryBtn != null) libraryBtn.getStyleClass().remove("menu-button-active");
        if (favoritesBtn != null) favoritesBtn.getStyleClass().remove("menu-button-active");
        if (socialBtn != null) socialBtn.getStyleClass().remove("menu-button-active");
        if (recommendationsBtn != null) recommendationsBtn.getStyleClass().remove("menu-button-active");
        if (radioBtn != null) radioBtn.getStyleClass().remove("menu-button-active");
        if (profileBtn != null) profileBtn.getStyleClass().remove("menu-button-active");

        // Agregar clase activa al botón seleccionado
        if (activeButton != null && !activeButton.getStyleClass().contains("menu-button-active")) {
            activeButton.getStyleClass().add("menu-button-active");
        }
    }

    private void cargarSugerencias() {
        if (sugerenciasContainer == null) return;

        sugerenciasContainer.getChildren().clear();

        List<Usuario> sugerencias = usuarioController.obtenerSugerencias(usuarioActual, 5);

        for (Usuario usuario : sugerencias) {
            VBox userCard = crearSugerenciaUsuario(usuario);
            sugerenciasContainer.getChildren().add(userCard);
        }
    }

    private VBox crearSugerenciaUsuario(Usuario usuario) {
        VBox card = new VBox(8);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: rgba(40, 40, 40, 0.8); -fx-background-radius: 12; -fx-padding: 15; -fx-cursor: hand;");

        VBox circulo = new VBox();
        circulo.setAlignment(Pos.CENTER);
        circulo.setPrefSize(60, 60);
        circulo.setStyle("-fx-background-color: #1DB954; -fx-background-radius: 30;");
        Label icono = new Label("👤");
        icono.setStyle("-fx-font-size: 30px;");
        circulo.getChildren().add(icono);

        Label nombre = new Label(usuario.getNombre());
        nombre.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 12px; -fx-font-weight: bold;");
        nombre.setWrapText(true);
        nombre.setAlignment(Pos.CENTER);
        nombre.setMaxWidth(200);

        Button seguirBtn = new Button("Seguir");
        seguirBtn.setStyle("-fx-background-color: #1DB954; -fx-text-fill: white; -fx-font-size: 11px; -fx-background-radius: 15; -fx-cursor: hand; -fx-pref-height: 30;");
        seguirBtn.setOnAction(e -> {
            usuarioController.seguir(usuarioActual, usuario);
            cargarSugerencias();
        });

        card.getChildren().addAll(circulo, nombre, seguirBtn);
        return card;
    }

    @FXML
    private void cargarHome() {
        setActiveButton(homeBtn);
        cargarVista("/fxml/views/Home.fxml", "setControllers");
    }

    @FXML
    private void cargarSearch() {
        setActiveButton(searchBtn);
        cargarVista("/fxml/views/Search.fxml", "setControllers");
    }

    @FXML
    private void cargarLibrary() {
        setActiveButton(libraryBtn);
        cargarVista("/fxml/views/Library.fxml", "setControllers");
    }

    @FXML
    private void cargarFavorites() {
        setActiveButton(favoritesBtn);
        cargarVista("/fxml/views/Favorites.fxml", "setControllers");
    }

    @FXML
    private void cargarSocial() {
        setActiveButton(socialBtn);
        cargarVista("/fxml/views/Social.fxml", "setControllers");
    }

    @FXML
    private void cargarRecommendations() {
        setActiveButton(recommendationsBtn);
        cargarVista("/fxml/views/Recommendations.fxml", "setControllers");
    }

    @FXML
    private void cargarRadio() {
        setActiveButton(radioBtn);
        cargarVista("/fxml/views/Radio.fxml", "setControllers");
    }

    @FXML
    private void cargarProfile() {
        setActiveButton(profileBtn);
        cargarVista("/fxml/views/Profile.fxml", "setControllers");
    }

    private void cargarVista(String fxmlPath, String initMethod) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent vista = loader.load();

            if (initMethod != null && initMethod.equals("setControllers")) {
                Object controller = loader.getController();
                if (controller instanceof HomeViewController) {
                    ((HomeViewController) controller).setControllers(usuarioController, cancionController, playlistController, radioController);
                } else if (controller instanceof LibraryViewController) {
                    ((LibraryViewController) controller).setControllers(usuarioController, cancionController, playlistController, radioController);
                } else if (controller instanceof FavoritesViewController) {
                    ((FavoritesViewController) controller).setControllers(playlistController);
                } else if (controller instanceof SocialViewController) {
                    ((SocialViewController) controller).setControllers(usuarioController);
                } else if (controller instanceof SearchViewController) {
                    ((SearchViewController) controller).setControllers(cancionController, playlistController);
                } else if (controller instanceof RecommendationsViewController) {
                    ((RecommendationsViewController) controller).setControllers(playlistController);
                } else if (controller instanceof RadioViewController) {
                    ((RadioViewController) controller).setControllers(radioController, playlistController);
                } else if (controller instanceof ProfileViewController) {
                    ((ProfileViewController) controller).setControllers(usuarioController);
                }
            }

            contentArea.setCenter(vista);

        } catch (IOException e) {
            System.err.println("Error al cargar vista: " + fxmlPath);
            e.printStackTrace();
        }
    }

    @FXML
    private void cerrarSesion() {
        SessionManager.getInstance().cerrarSesion();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent loginView = loader.load();
            usernameLabel.getScene().setRoot(loginView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}