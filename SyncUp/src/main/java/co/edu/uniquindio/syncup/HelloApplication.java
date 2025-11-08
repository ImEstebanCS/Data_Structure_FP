package co.edu.uniquindio.syncup;

import co.edu.uniquindio.syncup.Controller.CancionController;
import co.edu.uniquindio.syncup.Controller.PlaylistController;
import co.edu.uniquindio.syncup.Service.SyncUpService;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import co.edu.uniquindio.syncup.viewController.UsuarioPanelViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private static SyncUpService syncUpService;
    
    @Override
    public void start(Stage stage) throws IOException {
        // Inicializar el servicio principal
        syncUpService = new SyncUpService();
        
        // Agregar algunas canciones de prueba
        CancionController cancionController = new CancionController(syncUpService);
        cancionController.agregarCancion(1, "Bohemian Rhapsody", "Queen", "Rock", 1975, 5.55);
        cancionController.agregarCancion(2, "Imagine", "John Lennon", "Rock", 1971, 3.07);
        cancionController.agregarCancion(3, "Hotel California", "Eagles", "Rock", 1976, 6.30);
        cancionController.agregarCancion(4, "Stairway to Heaven", "Led Zeppelin", "Rock", 1971, 8.02);
        cancionController.agregarCancion(5, "Billie Jean", "Michael Jackson", "Pop", 1982, 4.54);
        cancionController.agregarCancion(6, "Like a Rolling Stone", "Bob Dylan", "Rock", 1965, 6.13);
        
        // Cargar el FXML
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("fxml/UsuarioPanel.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        
        // Obtener el controller y configurarlo
        UsuarioPanelViewController controller = fxmlLoader.getController();
        
        // Crear controllers de lógica de negocio
        PlaylistController playlistController = new PlaylistController(syncUpService);
        
        // Inyectar controllers
        controller.setControllers(cancionController, playlistController);
        
        // Crear un usuario de prueba para demostración
        Usuario usuarioPrueba = new Usuario("usuario1", "password123", "Usuario de Prueba");
        controller.setUsuarioActual(usuarioPrueba);
        
        stage.setTitle("SyncUp - Panel de Usuario");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}