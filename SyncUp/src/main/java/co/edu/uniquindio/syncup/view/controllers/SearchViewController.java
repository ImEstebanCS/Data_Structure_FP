package co.edu.uniquindio.syncup.view.controllers;

import co.edu.uniquindio.syncup.Controller.CancionController;
import co.edu.uniquindio.syncup.Controller.PlaylistController;
import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import co.edu.uniquindio.syncup.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * SearchViewController
 * Vista de búsqueda con autocompletado en tiempo real usando Trie
 */
public class SearchViewController {

    @FXML private TextField searchField;
    @FXML private FlowPane resultadosPane;
    @FXML private Label resultadosLabel;
    @FXML private ListView<String> sugerenciasListView;

    private CancionController cancionController;
    private PlaylistController playlistController;
    private Usuario usuarioActual;

    public void setControllers(CancionController cancionController, PlaylistController playlistController) {
        this.cancionController = cancionController;
        this.playlistController = playlistController;

        inicializar();
    }

    private void inicializar() {
        usuarioActual = SessionManager.getInstance().getUsuarioActual();

        // Configurar autocompletado en tiempo real - RF-003
        searchField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue.length() >= 2) {
                buscarConAutocompletado(newValue);
            } else {
                sugerenciasListView.getItems().clear();
                resultadosPane.getChildren().clear();
                resultadosLabel.setText("");
            }
        });

        // Seleccionar sugerencia al hacer click
        sugerenciasListView.setOnMouseClicked(event -> {
            String seleccionada = sugerenciasListView.getSelectionModel().getSelectedItem();
            if (seleccionada != null) {
                searchField.setText(seleccionada);
                buscarCancion(seleccionada);
            }
        });
    }

    /**
     * RF-003: Autocompletado usando Trie
     */
    private void buscarConAutocompletado(String prefijo) {
        // Buscar en Trie
        List<Cancion> sugerencias = cancionController.autocompletar(prefijo);

        // Actualizar ListView de sugerencias
        sugerenciasListView.getItems().clear();
        sugerencias.stream()
                .limit(5)
                .forEach(c -> sugerenciasListView.getItems().add(c.getTitulo()));

        // También mostrar resultados completos
        mostrarResultados(sugerencias);
    }

    @FXML
    private void buscarCancion() {
        String query = searchField.getText();
        if (query.trim().isEmpty()) {
            return;
        }

        buscarCancion(query);
    }

    private void buscarCancion(String query) {
        List<Cancion> resultados = cancionController.buscarPorTitulo(query);
        mostrarResultados(resultados);
    }

    private void mostrarResultados(List<Cancion> canciones) {
        resultadosPane.getChildren().clear();

        if (canciones.isEmpty()) {
            resultadosLabel.setText("No se encontraron resultados");
            return;
        }

        resultadosLabel.setText(canciones.size() + " resultados encontrados");

        for (Cancion cancion : canciones) {
            VBox card = crearCancionCard(cancion);
            resultadosPane.getChildren().add(card);
        }
    }

    private VBox crearCancionCard(Cancion cancion) {
        VBox card = new VBox(8);
        card.setPrefSize(150, 200);
        card.setStyle("-fx-background-color: #181818; -fx-background-radius: 8; -fx-cursor: hand;");
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

        // Duración
        Label duracion = new Label(cancion.getDuracionFormateada());
        duracion.setStyle("-fx-text-fill: #B3B3B3; -fx-font-size: 11px;");

        card.getChildren().addAll(imagePlaceholder, titulo, artista, duracion);

        // Evento de click - Agregar a favoritos
        card.setOnMouseClicked(e -> {
            playlistController.agregarFavorito(usuarioActual, cancion);
            mostrarAlerta("Agregado", cancion.getTitulo() + " agregada a favoritos");
        });

        // Hover effect
        card.setOnMouseEntered(e ->
                card.setStyle("-fx-background-color: #282828; -fx-background-radius: 8; -fx-cursor: hand;")
        );
        card.setOnMouseExited(e ->
                card.setStyle("-fx-background-color: #181818; -fx-background-radius: 8; -fx-cursor: hand;")
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