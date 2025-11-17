package co.edu.uniquindio.syncup.utils;

import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.StageStyle;

public class UIComponents {

    public static final String ICON_SUCCESS = "[OK]";
    public static final String ICON_ERROR = "[ERROR]";
    public static final String ICON_WARNING = "[WARNING]";
    public static final String ICON_INFO = "[INFO]";
    private static final String PRIMARY_GREEN = "#1DB954";
    private static final String PRIMARY_DARK = "#191414";
    private static final String SECONDARY_DARK = "#282828";
    private static final String ACCENT_DARK = "#181818";
    private static final String TEXT_WHITE = "#FFFFFF";
    private static final String TEXT_GRAY = "#B3B3B3";

    public static VBox crearCancionCard(Cancion cancion, Runnable onPlay, Runnable onLike, Runnable onRadio) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: rgba(24, 24, 24, 0.95); -fx-background-radius: 12; -fx-padding: 15; -fx-cursor: hand;");
        card.setPrefWidth(160);

        VBox imagePlaceholder = new VBox();
        imagePlaceholder.setAlignment(Pos.CENTER);
        imagePlaceholder.setPrefSize(140, 140);
        imagePlaceholder.setStyle("-fx-background-color: #282828; -fx-background-radius: 8;");
        Label iconLabel = new Label("🎵");
        iconLabel.setStyle("-fx-font-size: 50px;");
        imagePlaceholder.getChildren().add(iconLabel);

        Label titulo = new Label(cancion.getTitulo());
        titulo.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-font-weight: bold;");
        titulo.setWrapText(true);
        titulo.setAlignment(Pos.CENTER);
        titulo.setMaxWidth(140);

        Label artista = new Label(cancion.getArtista());
        artista.setStyle("-fx-text-fill: #B3B3B3; -fx-font-size: 12px;");
        artista.setWrapText(true);
        artista.setAlignment(Pos.CENTER);
        artista.setMaxWidth(140);

        HBox botonesBox = new HBox(5);
        botonesBox.setAlignment(Pos.CENTER);

        Button playBtn = new Button("▶");
        playBtn.setStyle("-fx-background-color: #1DB954; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 20; -fx-cursor: hand; -fx-pref-width: 40; -fx-pref-height: 40;");
        playBtn.setOnAction(e -> onPlay.run());

        Button likeBtn = new Button("❤");
        likeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #E91429; -fx-font-size: 16px; -fx-background-radius: 20; -fx-cursor: hand; -fx-pref-width: 40; -fx-pref-height: 40; -fx-border-color: #E91429; -fx-border-radius: 20;");
        likeBtn.setOnAction(e -> onLike.run());

        Button radioBtn = new Button("📻");
        radioBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #FFA500; -fx-font-size: 16px; -fx-background-radius: 20; -fx-cursor: hand; -fx-pref-width: 40; -fx-pref-height: 40; -fx-border-color: #FFA500; -fx-border-radius: 20;");
        radioBtn.setOnAction(e -> onRadio.run());

        botonesBox.getChildren().addAll(playBtn, likeBtn, radioBtn);
        card.getChildren().addAll(imagePlaceholder, titulo, artista, botonesBox);

        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: rgba(40, 40, 40, 0.95); -fx-background-radius: 12; -fx-padding: 15; -fx-cursor: hand; -fx-scale-x: 1.05; -fx-scale-y: 1.05;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: rgba(24, 24, 24, 0.95); -fx-background-radius: 12; -fx-padding: 15; -fx-cursor: hand;"));

        return card;
    }

    public static Button crearBotonCircular(String texto, String color, Runnable accion, int size) {
        Button btn = new Button(texto);
        btn.setPrefSize(size, size);
        btn.setStyle(
                "-fx-background-color: " + color + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 50%; " +
                        "-fx-cursor: hand; " +
                        "-fx-font-size: " + (size / 2.5) + "px;"
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
                        "-fx-scale-x: 1.1; -fx-scale-y: 1.1;"
        ));

        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: " + color + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 50%; " +
                        "-fx-cursor: hand; " +
                        "-fx-font-size: " + (size / 2.5) + "px; " +
                        "-fx-scale-x: 1.0; -fx-scale-y: 1.0;"
        ));

        return btn;
    }

    public static Button crearBotonCircular(String texto, String color, Runnable accion) {
        return crearBotonCircular(texto, color, accion, 40);
    }

    public static void mostrarAlertaPersonalizada(String titulo, String mensaje, String tipo) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);

        String icono = "";
        switch (tipo.toUpperCase()) {
            case "EXITO":
            case "SUCCESS":
            case "OK":
                icono = ICON_SUCCESS;
                break;
            case "ERROR":
                icono = ICON_ERROR;
                alert.setAlertType(Alert.AlertType.ERROR);
                break;
            case "ADVERTENCIA":
            case "WARNING":
                icono = ICON_WARNING;
                alert.setAlertType(Alert.AlertType.WARNING);
                break;
            default:
                icono = ICON_INFO;
        }

        alert.setContentText(icono + " " + mensaje);
        alert.showAndWait();
    }

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