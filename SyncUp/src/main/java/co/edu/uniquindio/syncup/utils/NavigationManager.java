package co.edu.uniquindio.syncup.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * NavigationManager
 * Gestiona la navegación entre vistas
 */
public class NavigationManager {
    private static NavigationManager instance;
    private Stage primaryStage;

    private NavigationManager() {
    }

    public static NavigationManager getInstance() {
        if (instance == null) {
            instance = new NavigationManager();
        }
        return instance;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void navigateTo(String fxmlPath, String title, int width, int height) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Scene scene = new Scene(root, width, height);

            if (primaryStage != null) {
                primaryStage.setTitle(title);
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        } catch (IOException e) {
            System.err.println("Error al navegar a: " + fxmlPath);
            e.printStackTrace();
        }
    }

    public FXMLLoader loadFXML(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        loader.load();
        return loader;
    }
}