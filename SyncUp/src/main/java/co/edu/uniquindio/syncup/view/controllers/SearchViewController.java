package co.edu.uniquindio.syncup.view.controllers;

import co.edu.uniquindio.syncup.Controller.CancionController;
import co.edu.uniquindio.syncup.Controller.PlaylistController;
import co.edu.uniquindio.syncup.Controller.RadioController;
import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import co.edu.uniquindio.syncup.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
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
    
    // RF-004: Búsqueda avanzada
    @FXML private TextField artistaField;
    @FXML private TextField generoField;
    @FXML private TextField añoField;

    private CancionController cancionController;
    private PlaylistController playlistController;
    private RadioController radioController;
    private Usuario usuarioActual;

    public void setControllers(CancionController cancionController, PlaylistController playlistController) {
        this.cancionController = cancionController;
        this.playlistController = playlistController;
        this.radioController = co.edu.uniquindio.syncup.SyncUpApp.getRadioController();

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
        card.setPrefSize(150, 220);
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

        // Duración
        Label duracion = new Label(cancion.getDuracionFormateada());
        duracion.setStyle("-fx-text-fill: #B3B3B3; -fx-font-size: 11px;");

        // Botones de acción - RF-006
        HBox botonesBox = new HBox(5);
        botonesBox.setAlignment(javafx.geometry.Pos.CENTER);
        
        Button favoritoBtn = new Button("❤️");
        favoritoBtn.setStyle("-fx-background-color: #1DB954; -fx-text-fill: #FFFFFF; -fx-background-radius: 15; -fx-cursor: hand;");
        favoritoBtn.setPrefSize(30, 30);
        favoritoBtn.setOnAction(e -> {
            playlistController.agregarFavorito(usuarioActual, cancion);
            mostrarAlerta("Agregado", cancion.getTitulo() + " agregada a favoritos");
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

        card.getChildren().addAll(imagePlaceholder, titulo, artista, duracion, botonesBox);

        // Hover effect
        card.setOnMouseEntered(e ->
                card.setStyle("-fx-background-color: #282828; -fx-background-radius: 8;")
        );
        card.setOnMouseExited(e ->
                card.setStyle("-fx-background-color: #181818; -fx-background-radius: 8;")
        );

        return card;
    }

    /**
     * RF-004: Búsqueda avanzada con lógica automática
     * Usa AND si hay múltiples criterios, OR si solo hay uno
     */
    @FXML
    private void buscarAvanzada() {
        String artista = artistaField.getText().trim();
        String genero = generoField.getText().trim();
        String añoTexto = añoField.getText().trim();
        int año = 0;

        // Validar y convertir año
        if (!añoTexto.isEmpty()) {
            try {
                año = Integer.parseInt(añoTexto);
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "El año debe ser un número válido");
                return;
            }
        }

        // Determinar lógica automáticamente
        // Si hay múltiples criterios, usar AND; si solo hay uno, usar OR
        boolean tieneArtista = !artista.isEmpty();
        boolean tieneGenero = !genero.isEmpty();
        boolean tieneAño = año > 0;
        
        int criteriosCount = (tieneArtista ? 1 : 0) + (tieneGenero ? 1 : 0) + (tieneAño ? 1 : 0);
        boolean usarOR = criteriosCount <= 1; // Si hay 1 o menos criterios, usar OR; si hay más, usar AND

        // Realizar búsqueda avanzada
        List<Cancion> resultados = cancionController.buscarAvanzada(
                artista.isEmpty() ? null : artista,
                genero.isEmpty() ? null : genero,
                año,
                usarOR
        );

        mostrarResultados(resultados);
        
        // Mostrar información de la búsqueda
        String logicaTexto = usarOR ? "OR" : "AND";
        resultadosLabel.setText(resultados.size() + " resultados encontrados (Lógica automática: " + logicaTexto + ")");
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}