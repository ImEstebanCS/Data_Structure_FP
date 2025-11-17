package co.edu.uniquindio.syncup;

import co.edu.uniquindio.syncup.Controller.*;
import co.edu.uniquindio.syncup.Service.MusicPlayer;
import co.edu.uniquindio.syncup.Service.SyncUpService;
import co.edu.uniquindio.syncup.utils.NavigationManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SyncUpApp extends Application {
    private static SyncUpService syncUpService;
    private static UsuarioController usuarioController;
    private static AdministradorController administradorController;
    private static CancionController cancionController;
    private static PlaylistController playlistController;
    private static RadioController radioController;
    private static MusicPlayer musicPlayer;

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("═══════════════════════════════════════");
        System.out.println("   🎵 SYNCUP - Sistema Inicializando");
        System.out.println("═══════════════════════════════════════");

        syncUpService = new SyncUpService();

        usuarioController = new UsuarioController(syncUpService);
        administradorController = new AdministradorController(syncUpService);
        cancionController = new CancionController(syncUpService);
        playlistController = new PlaylistController(syncUpService);
        radioController = new RadioController(syncUpService);

        musicPlayer = new MusicPlayer();

        System.out.println("✓ Controladores inicializados");
        System.out.println("✓ Reproductor de YouTube inicializado");
        System.out.println("✓ Catálogo: " + syncUpService.getCantidadCanciones() + " canciones");
        System.out.println("✓ Usuario admin: admin / admin123");
        System.out.println("═══════════════════════════════════════\n");

        NavigationManager.getInstance().setPrimaryStage(primaryStage);

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/Login.fxml"));

            if (loader.getLocation() == null) {
                System.err.println("ERROR: No se puede encontrar /fxml/Login.fxml");
                System.err.println("Verifica que el archivo existe en src/main/resources/fxml/");
                return;
            }

            Parent root = loader.load();

            // ✅ DIMENSIONES CORREGIDAS
            Scene scene = new Scene(root);

            // ✅ AGREGAR ESTILOS CSS
            String css = getClass().getResource("/css/styles.css").toExternalForm();
            scene.getStylesheets().add(css);

            primaryStage.setTitle("SyncUp - Plataforma de Música Social");
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.setMinWidth(1280);
            primaryStage.setMinHeight(720);
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("ERROR al cargar Login.fxml: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("✓ Aplicación iniciada correctamente\n");
    }

    @Override
    public void stop() {
        System.out.println("\n═══════════════════════════════════════");
        System.out.println("   🎵 SYNCUP - Cerrando Sistema");
        System.out.println("═══════════════════════════════════════");

        if (musicPlayer != null) {
            musicPlayer.detener();
            System.out.println("✓ Reproductor detenido");
        }

        System.out.println("✓ Sesión cerrada");
        System.out.println("✓ Hasta pronto!\n");
    }

    public static SyncUpService getSyncUpService() {
        return syncUpService;
    }

    public static UsuarioController getUsuarioController() {
        return usuarioController;
    }

    public static AdministradorController getAdministradorController() {
        return administradorController;
    }

    public static CancionController getCancionController() {
        return cancionController;
    }

    public static PlaylistController getPlaylistController() {
        return playlistController;
    }

    public static RadioController getRadioController() {
        return radioController;
    }

    public static MusicPlayer getMusicPlayer() {
        return musicPlayer;
    }

    public static void main(String[] args) {
        launch(args);
    }
}