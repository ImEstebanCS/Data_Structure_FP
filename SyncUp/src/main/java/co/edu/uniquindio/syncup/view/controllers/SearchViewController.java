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

    // ✅ MÉTODO UNIFICADO - Solo un setControllers
    public void setControllers(CancionController cancionController, PlaylistController playlistController, co.edu.uniquindio.syncup.Controller.UsuarioController usuarioController) {
        System.out.println("🔍 [SearchViewController] Inicializando...");
        this.cancionController = cancionController;
        this.playlistController = playlistController;
        this.radioController = SyncUpApp.getRadioController();
        this.musicPlayer = SyncUpApp.getMusicPlayer();

        inicializar();
    }

    private void inicializar() {
        usuarioActual = SessionManager.getInstance().getUsuarioActual();

        if (usuarioActual != null) {
            System.out.println("✅ Usuario cargado: " + usuarioActual.getNombre());
        } else {
            System.out.println("⚠️ No hay usuario en sesión");
        }

        // Autocompletado en tiempo real
        if (searchField != null) {
            searchField.textProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue.length() >= 2) {
                    buscarConAutocompletado(newValue);
                } else {
                    if (sugerenciasListView != null) {
                        sugerenciasListView.getItems().clear();
                    }
                    if (resultadosPane != null) {
                        resultadosPane.getChildren().clear();
                    }
                    if (resultadosLabel != null) {
                        resultadosLabel.setText("");
                    }
                }
            });
        }

        // Click en sugerencia
        if (sugerenciasListView != null) {
            sugerenciasListView.setOnMouseClicked(event -> {
                String seleccionada = sugerenciasListView.getSelectionModel().getSelectedItem();
                if (seleccionada != null && searchField != null) {
                    searchField.setText(seleccionada);
                    buscarCancion(seleccionada);
                }
            });
        }

        System.out.println("✅ SearchViewController inicializado");
    }

    private void buscarConAutocompletado(String prefijo) {
        try {
            List<Cancion> sugerencias = cancionController.autocompletar(prefijo);

            if (sugerenciasListView != null) {
                sugerenciasListView.getItems().clear();
                sugerencias.stream()
                        .limit(5)
                        .forEach(c -> sugerenciasListView.getItems().add(c.getTitulo()));
            }

            mostrarResultados(sugerencias);
            System.out.println("✅ Autocompletado: " + sugerencias.size() + " resultados");

        } catch (Exception e) {
            System.err.println("❌ Error en autocompletado: " + e.getMessage());
        }
    }

    @FXML
    private void buscarCancion() {
        if (searchField == null || searchField.getText().trim().isEmpty()) {
            return;
        }

        String query = searchField.getText();
        buscarCancion(query);
    }

    private void buscarCancion(String query) {
        try {
            List<Cancion> resultados = cancionController.buscarPorTitulo(query);
            mostrarResultados(resultados);
            System.out.println("🔍 Búsqueda: " + resultados.size() + " resultados para '" + query + "'");
        } catch (Exception e) {
            System.err.println("❌ Error en búsqueda: " + e.getMessage());
        }
    }

    private void mostrarResultados(List<Cancion> canciones) {
        if (resultadosPane == null) {
            System.out.println("⚠️ resultadosPane es null");
            return;
        }

        resultadosPane.getChildren().clear();

        if (canciones == null || canciones.isEmpty()) {
            if (resultadosLabel != null) {
                resultadosLabel.setText("No se encontraron resultados");
            }
            return;
        }

        if (resultadosLabel != null) {
            resultadosLabel.setText(canciones.size() + " resultados encontrados");
        }

        for (Cancion cancion : canciones) {
            try {
                VBox card = UIComponents.crearCancionCard(
                        cancion,
                        () -> reproducirCancion(cancion),
                        () -> agregarAFavoritos(cancion),
                        () -> iniciarRadio(cancion)
                );
                resultadosPane.getChildren().add(card);
            } catch (Exception e) {
                System.err.println("❌ Error al crear card: " + e.getMessage());
            }
        }
    }

    @FXML
    private void buscarAvanzada() {
        System.out.println("🔍 Búsqueda avanzada iniciada");

        String artista = artistaField != null ? artistaField.getText().trim() : "";
        String genero = generoField != null ? generoField.getText().trim() : "";
        String añoTexto = añoField != null ? añoField.getText().trim() : "";
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

        try {
            List<Cancion> resultados = cancionController.buscarAvanzada(
                    artista.isEmpty() ? null : artista,
                    genero.isEmpty() ? null : genero,
                    año,
                    usarOR
            );

            mostrarResultados(resultados);

            String logicaTexto = usarOR ? "OR" : "AND";
            if (resultadosLabel != null) {
                resultadosLabel.setText(resultados.size() + " resultados (Lógica: " + logicaTexto + ")");
            }

            System.out.println("✅ Búsqueda avanzada: " + resultados.size() + " resultados con lógica " + logicaTexto);

        } catch (Exception e) {
            System.err.println("❌ Error en búsqueda avanzada: " + e.getMessage());
            e.printStackTrace();
        }
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
            System.out.println("▶️ Reproduciendo: " + cancion.getTitulo());
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
        System.out.println("❤️ Agregado a favoritos: " + cancion.getTitulo());
    }

    private void iniciarRadio(Cancion cancion) {
        radioController.iniciarRadio(usuarioActual, cancion);
        UIComponents.mostrarAlertaPersonalizada(
                "Radio Iniciada",
                "Radio iniciada desde:\n" + cancion.getTitulo() + "\n\n" +
                        "Se generó una cola de reproducción con canciones similares",
                "📻"
        );
        System.out.println("📻 Radio iniciada desde: " + cancion.getTitulo());
    }
}