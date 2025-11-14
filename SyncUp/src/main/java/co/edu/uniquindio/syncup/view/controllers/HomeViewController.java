package co.edu.uniquindio.syncup.view.controllers;

import co.edu.uniquindio.syncup.Controller.*;
import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import co.edu.uniquindio.syncup.Model.Entidades.Playlist;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import co.edu.uniquindio.syncup.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * HomeViewController
 * Vista de inicio con recomendaciones y contenido personalizado
 */
public class HomeViewController {

    @FXML private ScrollPane scrollPane;
    @FXML private VBox mainContent;
    @FXML private Label welcomeLabel;
    @FXML private FlowPane recentSongsPane;
    @FXML private FlowPane recommendationsPane;

    private CancionController cancionController;
    private PlaylistController playlistController;
    private RadioController radioController;
    private Usuario usuarioActual;

    public void setControllers(UsuarioController usuarioController, CancionController cancionController,
                               PlaylistController playlistController, RadioController radioController) {
        this.cancionController = cancionController;
        this.playlistController = playlistController;
        this.radioController = radioController;

        inicializar();
    }

    private void inicializar() {
        usuarioActual = SessionManager.getInstance().getUsuarioActual();

        if (usuarioActual != null) {
            welcomeLabel.setText("Bienvenido, " + usuarioActual.getNombre());
            cargarContenido();
        }
    }

    private void cargarContenido() {
        // Cargar canciones recientes (últimas 10)
        cargarCancionesRecientes();

        // Cargar recomendaciones
        cargarRecomendaciones();
    }

    private void cargarCancionesRecientes() {
        recentSongsPane.getChildren().clear();

        List<Cancion> canciones = cancionController.obtenerTodas();
        int limite = Math.min(10, canciones.size());

        for (int i = 0; i < limite; i++) {
            Cancion cancion = canciones.get(i);
            VBox cancionCard = crearCancionCard(cancion);
            recentSongsPane.getChildren().add(cancionCard);
        }
    }

    private void cargarRecomendaciones() {
        recommendationsPane.getChildren().clear();

        // Generar descubrimiento semanal
        Playlist descubrimiento = playlistController.generarDescubrimientoSemanal(usuarioActual);

        List<Cancion> recomendadas = descubrimiento.getCanciones();
        int limite = Math.min(8, recomendadas.size());

        for (int i = 0; i < limite; i++) {
            Cancion cancion = recomendadas.get(i);
            VBox cancionCard = crearCancionCard(cancion);
            recommendationsPane.getChildren().add(cancionCard);
        }
    }

    private VBox crearCancionCard(Cancion cancion) {
        VBox card = new VBox(8);
        card.setPrefSize(150, 200);
        card.setStyle("-fx-background-color: #181818; -fx-background-radius: 8;");
        card.setPadding(new Insets(10));

        // Imagen placeholder
        Label imagePlaceholder = new Label("🎵");
        imagePlaceholder.setStyle("-fx-font-size: 50px; -fx-text-fill: #1DB954; -fx-alignment: center;");
        imagePlaceholder.setMaxWidth(Double.MAX_VALUE);

        // Título
        Label titulo = new Label(cancion.getTitulo());
        titulo.setStyle("-fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-font-size: 13px;");
        titulo.setWrapText(true);
        titulo.setMaxWidth(130);

        // Artista
        Label artista = new Label(cancion.getArtista());
        artista.setStyle("-fx-text-fill: #B3B3B3; -fx-font-size: 12px;");
        artista.setWrapText(true);
        artista.setMaxWidth(130);

        // Botones de acción - RF-006
        HBox botonesBox = new HBox(5);
        botonesBox.setAlignment(javafx.geometry.Pos.CENTER);
        
        Button favoritoBtn = new Button("❤️");
        favoritoBtn.setStyle("-fx-background-color: #1DB954; -fx-text-fill: #FFFFFF; -fx-background-radius: 15; -fx-cursor: hand;");
        favoritoBtn.setPrefSize(30, 30);
        favoritoBtn.setOnAction(e -> {
            playlistController.agregarFavorito(usuarioActual, cancion);
        });

        Button radioBtn = new Button("📻");
        radioBtn.setStyle("-fx-background-color: #FFA500; -fx-text-fill: #FFFFFF; -fx-background-radius: 15; -fx-cursor: hand;");
        radioBtn.setPrefSize(30, 30);
        radioBtn.setOnAction(e -> {
            // RF-006: Iniciar Radio desde canción
            radioController.iniciarRadio(usuarioActual, cancion);
            mostrarAlerta("Radio Iniciada", "Radio iniciada desde: " + cancion.getTitulo() + "\n" +
                    "Se generó una cola de reproducción con canciones similares");
        });

        botonesBox.getChildren().addAll(favoritoBtn, radioBtn);

        card.getChildren().addAll(imagePlaceholder, titulo, artista, botonesBox);

        // Hover effect
        card.setOnMouseEntered(e ->
                card.setStyle("-fx-background-color: #282828; -fx-background-radius: 8;")
        );
        card.setOnMouseExited(e ->
                card.setStyle("-fx-background-color: #181818; -fx-background-radius: 8;")
        );

        return card;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}