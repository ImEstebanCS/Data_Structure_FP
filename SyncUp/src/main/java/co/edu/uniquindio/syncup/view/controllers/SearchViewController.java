package co.edu.uniquindio.syncup.view.controllers;

import co.edu.uniquindio.syncup.Controller.CancionController;
import co.edu.uniquindio.syncup.Controller.PlaylistController;
import co.edu.uniquindio.syncup.Controller.RadioController;
import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import co.edu.uniquindio.syncup.Service.MusicPlayer;
import co.edu.uniquindio.syncup.SyncUpApp;
import co.edu.uniquindio.syncup.utils.SessionManager;
import co.edu.uniquindio.syncup.utils.UIComponents;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class SearchViewController {

    @FXML private TextField searchField;
    @FXML private FlowPane resultadosPane;
    @FXML private Label resultadosLabel;
    @FXML private ListView<String> sugerenciasListView;

    @FXML private TextField artistaField;
    @FXML private TextField generoField;
    @FXML private TextField añoField;

    private CancionController cancionController;
    private PlaylistController playlistController;
    private RadioController radioController;
    private MusicPlayer musicPlayer;
    private Usuario usuarioActual;

    public void setControllers(CancionController cancionController, PlaylistController playlistController) {
        this.cancionController = cancionController;
        this.playlistController = playlistController;
        this.radioController = SyncUpApp.getRadioController();
        this.musicPlayer = SyncUpApp.getMusicPlayer();

        inicializar();
    }

    private void inicializar() {
        usuarioActual = SessionManager.getInstance().getUsuarioActual();

        searchField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue.length() >= 2) {
                buscarConAutocompletado(newValue);
            } else {
                sugerenciasListView.getItems().clear();
                resultadosPane.getChildren().clear();
                resultadosLabel.setText("");
            }
        });

        sugerenciasListView.setOnMouseClicked(event -> {
            String seleccionada = sugerenciasListView.getSelectionModel().getSelectedItem();
            if (seleccionada != null) {
                searchField.setText(seleccionada);
                buscarCancion(seleccionada);
            }
        });
    }

    private void buscarConAutocompletado(String prefijo) {
        List<Cancion> sugerencias = cancionController.autocompletar(prefijo);

        sugerenciasListView.getItems().clear();
        sugerencias.stream()
                .limit(5)
                .forEach(c -> sugerenciasListView.getItems().add(c.getTitulo()));

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
            VBox card = UIComponents.crearCancionCard(
                    cancion,
                    () -> reproducirCancion(cancion),
                    () -> agregarAFavoritos(cancion),
                    () -> iniciarRadio(cancion)
            );
            resultadosPane.getChildren().add(card);
        }
    }

    @FXML
    private void buscarAvanzada() {
        String artista = artistaField.getText().trim();
        String genero = generoField.getText().trim();
        String añoTexto = añoField.getText().trim();
        int año = 0;

        if (!añoTexto.isEmpty()) {
            try {
                año = Integer.parseInt(añoTexto);
            } catch (NumberFormatException e) {
                UIComponents.mostrarAlertaPersonalizada("Error", "El año debe ser un número válido", "❌");
                return;
            }
        }

        boolean tieneArtista = !artista.isEmpty();
        boolean tieneGenero = !genero.isEmpty();
        boolean tieneAño = año > 0;

        int criteriosCount = (tieneArtista ? 1 : 0) + (tieneGenero ? 1 : 0) + (tieneAño ? 1 : 0);
        boolean usarOR = criteriosCount <= 1;

        List<Cancion> resultados = cancionController.buscarAvanzada(
                artista.isEmpty() ? null : artista,
                genero.isEmpty() ? null : genero,
                año,
                usarOR
        );

        mostrarResultados(resultados);

        String logicaTexto = usarOR ? "OR" : "AND";
        resultadosLabel.setText(resultados.size() + " resultados encontrados (Lógica automática: " + logicaTexto + ")");
    }

    private void reproducirCancion(Cancion cancion) {
        if (musicPlayer != null) {
            musicPlayer.reproducir(cancion);
            UIComponents.mostrarAlertaPersonalizada(
                    "Reproduciendo en YouTube",
                    "🎵 " + cancion.getTitulo() + "\n" +
                            "🎤 " + cancion.getArtista() + "\n" +
                            "🎸 " + cancion.getGenero() + "\n\n" +
                            "Se abrirá YouTube en tu navegador",
                    "▶️"
            );
        } else {
            UIComponents.mostrarAlertaPersonalizada("Error", "El reproductor no está disponible", "❌");
        }
    }

    private void agregarAFavoritos(Cancion cancion) {
        playlistController.agregarFavorito(usuarioActual, cancion);
        UIComponents.mostrarAlertaPersonalizada(
                "Favorito",
                "Agregado a favoritos:\n" + cancion.getTitulo(),
                "❤️"
        );
    }

    private void iniciarRadio(Cancion cancion) {
        radioController.iniciarRadio(usuarioActual, cancion);
        UIComponents.mostrarAlertaPersonalizada(
                "Radio Iniciada",
                "Radio iniciada desde:\n" + cancion.getTitulo() + "\n\n" +
                        "Se generó una cola de reproducción con canciones similares",
                "📻"
        );
    }
}