package co.edu.uniquindio.syncup.view.controllers;

import co.edu.uniquindio.syncup.Controller.CancionController;
import co.edu.uniquindio.syncup.Controller.PlaylistController;
import co.edu.uniquindio.syncup.Controller.RadioController;
import co.edu.uniquindio.syncup.Controller.UsuarioController;
import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import co.edu.uniquindio.syncup.Model.Entidades.Playlist;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import co.edu.uniquindio.syncup.SyncUpApp;
import co.edu.uniquindio.syncup.utils.SessionManager;
import co.edu.uniquindio.syncup.utils.UIComponents;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeViewController {

    @FXML private Label bienvenidaLabel;
    @FXML private FlowPane recentSongsPane;
    @FXML private FlowPane forYouPane;
    @FXML private VBox sugerenciasContainer;

    private CancionController cancionController;
    private PlaylistController playlistController;
    private RadioController radioController;
    private UsuarioController usuarioController;
    private Usuario usuarioActual;
    private List<Cancion> historialReproduccion;

    public HomeViewController() {
        this.cancionController = SyncUpApp.getCancionController();
        this.playlistController = SyncUpApp.getPlaylistController();
        this.radioController = SyncUpApp.getRadioController();
        this.usuarioController = SyncUpApp.getUsuarioController();
        this.historialReproduccion = new ArrayList<>();
    }

    @FXML
    public void initialize() {
        System.out.println("🏠 [HomeViewController] Inicializando...");

        usuarioActual = SessionManager.getInstance().getUsuarioActual();

        if (usuarioActual != null) {
            bienvenidaLabel.setText("Bienvenido, " + usuarioActual.getNombre());
            System.out.println("✅ Usuario cargado: " + usuarioActual.getNombre());
        } else {
            bienvenidaLabel.setText("Bienvenido");
            System.out.println("⚠️ No hay usuario en sesión");
        }

        // ✅ CARGAR CONTENIDO INMEDIATAMENTE
        cargarCancionesRecientes();
        cargarDescubrimientoSemanal();

        if (sugerenciasContainer != null) {
            cargarSugerenciasUsuarios();
        }

        System.out.println("✅ [HomeViewController] Inicialización completa");
    }

    private void cargarCancionesRecientes() {
        if (recentSongsPane == null) {
            System.out.println("⚠️ recentSongsPane es null");
            return;
        }

        recentSongsPane.getChildren().clear();
        System.out.println("🎵 Cargando canciones recientes...");

        List<Cancion> recientes = new ArrayList<>();

        // Si hay historial, usarlo
        if (historialReproduccion.size() > 10) {
            recientes = historialReproduccion.subList(
                    Math.max(historialReproduccion.size() - 10, 0),
                    historialReproduccion.size()
            );
        } else {
            recientes = new ArrayList<>(historialReproduccion);
        }

        Collections.reverse(recientes);

        // ✅ Si no hay historial, mostrar canciones del catálogo
        if (recientes.isEmpty()) {
            System.out.println("📚 No hay historial, cargando del catálogo...");
            List<Cancion> todasLasCanciones = cancionController.obtenerTodas();

            if (todasLasCanciones != null && !todasLasCanciones.isEmpty()) {
                int limite = Math.min(10, todasLasCanciones.size());
                recientes = todasLasCanciones.subList(0, limite);
                System.out.println("✅ Cargadas " + recientes.size() + " canciones del catálogo");
            } else {
                System.out.println("❌ El catálogo está vacío");
            }
        }

        // ✅ CREAR CARDS DE CANCIONES
        for (Cancion cancion : recientes) {
            try {
                VBox card = UIComponents.crearCancionCard(
                        cancion,
                        () -> reproducirCancion(cancion),
                        () -> agregarAFavoritos(cancion),
                        () -> iniciarRadio(cancion)
                );
                recentSongsPane.getChildren().add(card);
            } catch (Exception e) {
                System.err.println("❌ Error al crear card para: " + cancion.getTitulo());
                e.printStackTrace();
            }
        }

        System.out.println("✅ Cards de recientes creados: " + recentSongsPane.getChildren().size());
    }

    private void cargarDescubrimientoSemanal() {
        if (forYouPane == null) {
            System.out.println("⚠️ forYouPane es null");
            return;
        }

        forYouPane.getChildren().clear();
        System.out.println("🎯 Cargando descubrimiento semanal...");

        try {
            Playlist descubrimiento = playlistController.generarDescubrimientoSemanal(usuarioActual);

            if (descubrimiento != null && descubrimiento.getCanciones() != null) {
                System.out.println("✅ Descubrimiento generado con " + descubrimiento.getCanciones().size() + " canciones");

                for (Cancion cancion : descubrimiento.getCanciones()) {
                    try {
                        VBox card = UIComponents.crearCancionCard(
                                cancion,
                                () -> reproducirCancion(cancion),
                                () -> agregarAFavoritos(cancion),
                                () -> iniciarRadio(cancion)
                        );
                        forYouPane.getChildren().add(card);
                    } catch (Exception e) {
                        System.err.println("❌ Error al crear card para: " + cancion.getTitulo());
                        e.printStackTrace();
                    }
                }

                System.out.println("✅ Cards de descubrimiento creados: " + forYouPane.getChildren().size());
            } else {
                System.out.println("⚠️ No se pudo generar descubrimiento semanal");
            }
        } catch (Exception e) {
            System.err.println("❌ Error al cargar descubrimiento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cargarSugerenciasUsuarios() {
        if (sugerenciasContainer == null) {
            System.out.println("⚠️ sugerenciasContainer es null");
            return;
        }

        sugerenciasContainer.getChildren().clear();
        System.out.println("👥 Cargando sugerencias de usuarios...");

        try {
            List<Usuario> sugerencias = usuarioController.obtenerSugerenciasDeUsuarios(usuarioActual, 5);

            if (sugerencias != null && !sugerencias.isEmpty()) {
                System.out.println("✅ " + sugerencias.size() + " usuarios sugeridos");

                for (Usuario usuario : sugerencias) {
                    HBox userCard = crearTarjetaUsuario(usuario);
                    sugerenciasContainer.getChildren().add(userCard);
                }
            } else {
                System.out.println("⚠️ No hay sugerencias de usuarios");
            }
        } catch (Exception e) {
            System.err.println("❌ Error al cargar sugerencias: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private HBox crearTarjetaUsuario(Usuario usuario) {
        HBox card = new HBox(10);
        card.setStyle("-fx-background-color: #181818; -fx-padding: 10; -fx-background-radius: 8;");
        card.setAlignment(Pos.CENTER_LEFT);

        // Icono de usuario
        VBox iconoContainer = new VBox();
        iconoContainer.setAlignment(Pos.CENTER);
        iconoContainer.setPrefSize(40, 40);
        iconoContainer.setStyle("-fx-background-color: #1DB954; -fx-background-radius: 20;");
        Label icono = new Label("👤");
        icono.setStyle("-fx-font-size: 20px;");
        iconoContainer.getChildren().add(icono);

        // Info del usuario
        VBox info = new VBox(2);
        Label nombre = new Label(usuario.getNombre());
        nombre.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 12px;");
        Label username = new Label("@" + usuario.getUsername());
        username.setStyle("-fx-text-fill: #B3B3B3; -fx-font-size: 10px;");
        info.getChildren().addAll(nombre, username);
        HBox.setHgrow(info, Priority.ALWAYS);

        // Botón seguir
        Button seguirBtn = new Button("Seguir");
        seguirBtn.setStyle(
                "-fx-background-color: #1DB954; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 10px; " +
                        "-fx-padding: 5 10; " +
                        "-fx-background-radius: 15; " +
                        "-fx-cursor: hand;"
        );
        seguirBtn.setOnAction(e -> {
            usuarioController.seguir(usuarioActual, usuario);
            UIComponents.mostrarAlertaPersonalizada("Éxito", "Ahora sigues a " + usuario.getNombre(), "✅");
            cargarSugerenciasUsuarios();
        });

        card.getChildren().addAll(iconoContainer, info, seguirBtn);

        return card;
    }

    private void reproducirCancion(Cancion cancion) {
        if (!historialReproduccion.contains(cancion)) {
            historialReproduccion.add(cancion);
        }

        SyncUpApp.getMusicPlayer().reproducir(cancion);

        UIComponents.mostrarAlertaPersonalizada(
                "Reproduciendo en YouTube",
                "🎵 " + cancion.getTitulo() + "\n" +
                        "🎤 " + cancion.getArtista() + "\n" +
                        "🎸 " + cancion.getGenero() + "\n\n" +
                        "Se abrirá YouTube en tu navegador",
                "▶️"
        );

        System.out.println("▶️ Reproduciendo: " + cancion.getTitulo());
    }

    private void agregarAFavoritos(Cancion cancion) {
        usuarioController.agregarFavorito(usuarioActual, cancion);
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