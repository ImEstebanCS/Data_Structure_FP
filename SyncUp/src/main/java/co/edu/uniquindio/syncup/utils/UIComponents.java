package co.edu.uniquindio.syncup.utils;

import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class UIComponents {

    public static final String ICON_SUCCESS = "✅";
    public static final String ICON_ERROR = "❌";
    public static final String ICON_WARNING = "⚠️";
    public static final String ICON_INFO = "ℹ️";
    private static final String PRIMARY_GREEN = "#1DB954";
    private static final String PRIMARY_DARK = "#191414";
    private static final String SECONDARY_DARK = "#282828";
    private static final String ACCENT_DARK = "#181818";
    private static final String TEXT_WHITE = "#FFFFFF";
    private static final String TEXT_GRAY = "#B3B3B3";

    // ==================== CARDS DE CANCIONES ====================

    public static VBox crearCancionCard(Cancion cancion, Runnable onPlay, Runnable onLike, Runnable onRadio) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: rgba(24, 24, 24, 0.95); -fx-background-radius: 12; -fx-padding: 15; -fx-cursor: hand;");
        card.setPrefWidth(160);

        // Imagen placeholder
        VBox imagePlaceholder = new VBox();
        imagePlaceholder.setAlignment(Pos.CENTER);
        imagePlaceholder.setPrefSize(140, 140);
        imagePlaceholder.setStyle("-fx-background-color: #282828; -fx-background-radius: 8;");
        Label iconLabel = new Label("🎵");
        iconLabel.setStyle("-fx-font-size: 50px;");
        imagePlaceholder.getChildren().add(iconLabel);

        // Título
        Label titulo = new Label(cancion.getTitulo());
        titulo.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-font-weight: bold;");
        titulo.setWrapText(true);
        titulo.setAlignment(Pos.CENTER);
        titulo.setMaxWidth(140);

        // Artista
        Label artista = new Label(cancion.getArtista());
        artista.setStyle("-fx-text-fill: #B3B3B3; -fx-font-size: 12px;");
        artista.setWrapText(true);
        artista.setAlignment(Pos.CENTER);
        artista.setMaxWidth(140);

        // Botones de acción
        HBox botonesBox = new HBox(5);
        botonesBox.setAlignment(Pos.CENTER);

        // ✅ BOTÓN PLAY (Verde) con efectos mejorados
        Button playBtn = new Button("▶");
        playBtn.setStyle(
                "-fx-background-color: #1DB954; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-background-radius: 20; " +
                        "-fx-cursor: hand; " +
                        "-fx-pref-width: 40; " +
                        "-fx-pref-height: 40; " +
                        "-fx-effect: dropshadow(gaussian, rgba(29, 185, 84, 0.6), 8, 0.5, 0, 0);"
        );
        playBtn.setOnAction(e -> onPlay.run());

        // Hover effects para Play
        playBtn.setOnMouseEntered(e -> playBtn.setStyle(
                "-fx-background-color: #1ed760; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 18px; " +
                        "-fx-background-radius: 20; " +
                        "-fx-cursor: hand; " +
                        "-fx-pref-width: 40; " +
                        "-fx-pref-height: 40; " +
                        "-fx-effect: dropshadow(gaussian, rgba(29, 185, 84, 0.9), 12, 0.7, 0, 0); " +
                        "-fx-scale-x: 1.1; -fx-scale-y: 1.1;"
        ));

        playBtn.setOnMouseExited(e -> playBtn.setStyle(
                "-fx-background-color: #1DB954; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-background-radius: 20; " +
                        "-fx-cursor: hand; " +
                        "-fx-pref-width: 40; " +
                        "-fx-pref-height: 40; " +
                        "-fx-effect: dropshadow(gaussian, rgba(29, 185, 84, 0.6), 8, 0.5, 0, 0);"
        ));

        // ✅ BOTÓN LIKE (Rojo) con efectos mejorados
        Button likeBtn = new Button("♥");
        likeBtn.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-text-fill: #E91429; " +
                        "-fx-font-size: 16px; " +
                        "-fx-background-radius: 20; " +
                        "-fx-cursor: hand; " +
                        "-fx-pref-width: 40; " +
                        "-fx-pref-height: 40; " +
                        "-fx-border-color: #E91429; " +
                        "-fx-border-radius: 20; " +
                        "-fx-border-width: 2; " +
                        "-fx-effect: dropshadow(gaussian, rgba(233, 20, 41, 0.5), 6, 0.4, 0, 0);"
        );
        likeBtn.setOnAction(e -> onLike.run());

        // Hover effects para Like
        likeBtn.setOnMouseEntered(e -> likeBtn.setStyle(
                "-fx-background-color: #E91429; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 18px; " +
                        "-fx-background-radius: 20; " +
                        "-fx-cursor: hand; " +
                        "-fx-pref-width: 40; " +
                        "-fx-pref-height: 40; " +
                        "-fx-border-color: #E91429; " +
                        "-fx-border-radius: 20; " +
                        "-fx-border-width: 2; " +
                        "-fx-effect: dropshadow(gaussian, rgba(233, 20, 41, 0.8), 10, 0.6, 0, 0); " +
                        "-fx-scale-x: 1.1; -fx-scale-y: 1.1;"
        ));

        likeBtn.setOnMouseExited(e -> likeBtn.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-text-fill: #E91429; " +
                        "-fx-font-size: 16px; " +
                        "-fx-background-radius: 20; " +
                        "-fx-cursor: hand; " +
                        "-fx-pref-width: 40; " +
                        "-fx-pref-height: 40; " +
                        "-fx-border-color: #E91429; " +
                        "-fx-border-radius: 20; " +
                        "-fx-border-width: 2; " +
                        "-fx-effect: dropshadow(gaussian, rgba(233, 20, 41, 0.5), 6, 0.4, 0, 0);"
        ));

        // ✅ BOTÓN RADIO (Naranja) con efectos mejorados
        Button radioBtn = new Button("⊙");
        radioBtn.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-text-fill: #FFA500; " +
                        "-fx-font-size: 16px; " +
                        "-fx-background-radius: 20; " +
                        "-fx-cursor: hand; " +
                        "-fx-pref-width: 40; " +
                        "-fx-pref-height: 40; " +
                        "-fx-border-color: #FFA500; " +
                        "-fx-border-radius: 20; " +
                        "-fx-border-width: 2; " +
                        "-fx-effect: dropshadow(gaussian, rgba(255, 165, 0, 0.5), 6, 0.4, 0, 0);"
        );
        radioBtn.setOnAction(e -> onRadio.run());

        // Hover effects para Radio
        radioBtn.setOnMouseEntered(e -> radioBtn.setStyle(
                "-fx-background-color: #FFA500; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 18px; " +
                        "-fx-background-radius: 20; " +
                        "-fx-cursor: hand; " +
                        "-fx-pref-width: 40; " +
                        "-fx-pref-height: 40; " +
                        "-fx-border-color: #FFA500; " +
                        "-fx-border-radius: 20; " +
                        "-fx-border-width: 2; " +
                        "-fx-effect: dropshadow(gaussian, rgba(255, 165, 0, 0.8), 10, 0.6, 0, 0); " +
                        "-fx-scale-x: 1.1; -fx-scale-y: 1.1;"
        ));

        radioBtn.setOnMouseExited(e -> radioBtn.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-text-fill: #FFA500; " +
                        "-fx-font-size: 16px; " +
                        "-fx-background-radius: 20; " +
                        "-fx-cursor: hand; " +
                        "-fx-pref-width: 40; " +
                        "-fx-pref-height: 40; " +
                        "-fx-border-color: #FFA500; " +
                        "-fx-border-radius: 20; " +
                        "-fx-border-width: 2; " +
                        "-fx-effect: dropshadow(gaussian, rgba(255, 165, 0, 0.5), 6, 0.4, 0, 0);"
        ));

        botonesBox.getChildren().addAll(playBtn, likeBtn, radioBtn);
        card.getChildren().addAll(imagePlaceholder, titulo, artista, botonesBox);

        // Hover effect del card completo
        card.setOnMouseEntered(e -> card.setStyle(
                "-fx-background-color: rgba(40, 40, 40, 0.95); " +
                        "-fx-background-radius: 12; " +
                        "-fx-padding: 15; " +
                        "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(gaussian, rgba(29, 185, 84, 0.3), 12, 0.5, 0, 0); " +
                        "-fx-scale-x: 1.03; -fx-scale-y: 1.03;"
        ));

        card.setOnMouseExited(e -> card.setStyle(
                "-fx-background-color: rgba(24, 24, 24, 0.95); " +
                        "-fx-background-radius: 12; " +
                        "-fx-padding: 15; " +
                        "-fx-cursor: hand;"
        ));

        return card;
    }

    // ==================== BOTONES CIRCULARES ====================

    public static Button crearBotonCircular(String texto, String color, Runnable accion, int size) {
        Button btn = new Button(texto);
        btn.setPrefSize(size, size);
        btn.setStyle(
                "-fx-background-color: " + color + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 50%; " +
                        "-fx-cursor: hand; " +
                        "-fx-font-size: " + (size / 2.5) + "px; " +
                        "-fx-effect: dropshadow(gaussian, rgba(29, 185, 84, 0.5), 8, 0.4, 0, 0);"
        );

        if (accion != null) {
            btn.setOnAction(e -> accion.run());
        }

        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: derive(" + color + ", 20%); " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 50%; " +
                        "-fx-cursor: hand; " +
                        "-fx-font-size: " + (size / 2.5) + "px; " +
                        "-fx-effect: dropshadow(gaussian, rgba(29, 185, 84, 0.8), 12, 0.6, 0, 0); " +
                        "-fx-scale-x: 1.1; -fx-scale-y: 1.1;"
        ));

        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: " + color + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 50%; " +
                        "-fx-cursor: hand; " +
                        "-fx-font-size: " + (size / 2.5) + "px; " +
                        "-fx-effect: dropshadow(gaussian, rgba(29, 185, 84, 0.5), 8, 0.4, 0, 0); " +
                        "-fx-scale-x: 1.0; -fx-scale-y: 1.0;"
        ));

        return btn;
    }

    public static Button crearBotonCircular(String texto, String color, Runnable accion) {
        return crearBotonCircular(texto, color, accion, 40);
    }

    // ==================== ALERTAS ANIMADAS Y ESTILIZADAS ====================

    public static void mostrarAlertaPersonalizada(String titulo, String mensaje, String tipo) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);

        String icono = "";
        String borderColor = PRIMARY_GREEN;

        switch (tipo.toUpperCase()) {
            case "EXITO":
            case "SUCCESS":
            case "OK":
            case "✅":
                icono = ICON_SUCCESS;
                borderColor = PRIMARY_GREEN;
                break;
            case "ERROR":
            case "❌":
                icono = ICON_ERROR;
                borderColor = "#E91429";
                alert.setAlertType(Alert.AlertType.ERROR);
                break;
            case "ADVERTENCIA":
            case "WARNING":
            case "⚠️":
                icono = ICON_WARNING;
                borderColor = "#FFA500";
                alert.setAlertType(Alert.AlertType.WARNING);
                break;
            default:
                icono = tipo.length() <= 2 ? tipo : ICON_INFO;
                borderColor = PRIMARY_GREEN;
        }

        alert.setContentText(icono + " " + mensaje);

        // ✅ ESTILIZAR EL DIÁLOGO
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle(
                "-fx-background-color: #181818; " +
                        "-fx-border-color: " + borderColor + "; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 10; " +
                        "-fx-background-radius: 10; " +
                        "-fx-effect: dropshadow(gaussian, " + borderColor + ", 20, 0.6, 0, 0);"
        );

        // Estilizar texto
        dialogPane.lookup(".content.label").setStyle(
                "-fx-text-fill: #FFFFFF; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold;"
        );

        // Estilizar botón OK
        dialogPane.lookupButton(ButtonType.OK).setStyle(
                "-fx-background-color: " + borderColor + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 8 20;"
        );

        // ✅ ANIMACIÓN DE ENTRADA (Fade + Scale)
        Stage stage = (Stage) dialogPane.getScene().getWindow();

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), dialogPane);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(300), dialogPane);
        scaleIn.setFromX(0.7);
        scaleIn.setFromY(0.7);
        scaleIn.setToX(1.0);
        scaleIn.setToY(1.0);

        ParallelTransition animation = new ParallelTransition(fadeIn, scaleIn);
        animation.play();

        alert.showAndWait();
    }

    // ==================== STAT CARDS ====================

    public static VBox crearStatCard(String numero, String label, String icono) {
        VBox card = new VBox(15);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(30));
        card.getStyleClass().add("stat-card");
        card.setPrefWidth(220);

        Label iconoLabel = new Label(icono);
        iconoLabel.setStyle("-fx-font-size: 40px;");

        Label numeroLabel = new Label(numero);
        numeroLabel.getStyleClass().add("stat-number");

        Label textoLabel = new Label(label);
        textoLabel.getStyleClass().add("stat-label");

        card.getChildren().addAll(iconoLabel, numeroLabel, textoLabel);

        return card;
    }

    // ==================== LIST ITEMS ====================

    public static HBox crearListItem(String titulo, String subtitulo, String icono, Runnable... acciones) {
        HBox item = new HBox(15);
        item.setAlignment(Pos.CENTER_LEFT);
        item.getStyleClass().add("list-item");
        item.setPadding(new Insets(15));

        Label iconLabel = new Label(icono);
        iconLabel.setStyle("-fx-font-size: 28px;");

        VBox infoBox = new VBox(5);
        Label tituloLabel = new Label(titulo);
        tituloLabel.getStyleClass().add("list-item-title");

        Label subtituloLabel = new Label(subtitulo);
        subtituloLabel.getStyleClass().add("list-item-subtitle");

        infoBox.getChildren().addAll(tituloLabel, subtituloLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox botonesBox = new HBox(10);
        for (Runnable accion : acciones) {
            if (accion != null) {
                // Se pueden agregar botones personalizados aquí
            }
        }

        item.getChildren().addAll(iconLabel, infoBox, spacer, botonesBox);

        return item;
    }
}