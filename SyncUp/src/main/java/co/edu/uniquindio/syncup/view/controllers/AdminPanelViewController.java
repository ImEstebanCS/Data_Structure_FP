package co.edu.uniquindio.syncup.view.controllers;

import co.edu.uniquindio.syncup.SyncUpApp;
import co.edu.uniquindio.syncup.Controller.CancionController;
import co.edu.uniquindio.syncup.Controller.UsuarioController;
import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import co.edu.uniquindio.syncup.utils.SessionManager;
import co.edu.uniquindio.syncup.utils.UIComponents;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.io.BufferedReader;
import java.io.FileReader;

public class AdminPanelViewController {

    @FXML private StackPane contentArea;
    @FXML private VBox dashboardView;
    @FXML private VBox cancionesView;
    @FXML private VBox usuariosView;
    @FXML private VBox estadisticasView;
    @FXML private VBox estadisticasContainer;

    @FXML private Button dashboardBtn;
    @FXML private Button cancionesBtn;
    @FXML private Button usuariosBtn;
    @FXML private Button estadisticasBtn;

    @FXML private Label totalCancionesLabel;
    @FXML private Label totalUsuariosLabel;
    @FXML private Label generosLabel;

    @FXML private TableView<Cancion> cancionesTable;
    @FXML private TableColumn<Cancion, Integer> idColumn;
    @FXML private TableColumn<Cancion, String> tituloColumn;
    @FXML private TableColumn<Cancion, String> artistaColumn;

    @FXML private TableView<Usuario> usuariosTable;
    @FXML private TableColumn<Usuario, String> usernameColumn;
    @FXML private TableColumn<Usuario, String> nombreColumn;

    @FXML private TextField idField;
    @FXML private TextField tituloField;
    @FXML private TextField artistaField;
    @FXML private TextField generoField;
    @FXML private TextField añoField;
    @FXML private TextField duracionField;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField nombreUsuarioField;

    private CancionController cancionController;
    private UsuarioController usuarioController;
    private String usernameOriginal;

    @FXML
    public void initialize() {
        cancionController = SyncUpApp.getCancionController();
        usuarioController = SyncUpApp.getUsuarioController();

        configurarTablas();
        configurarListeners();
        cargarDatos();
        actualizarDashboard();

        mostrarDashboard();
    }

    private void configurarTablas() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        tituloColumn.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        artistaColumn.setCellValueFactory(new PropertyValueFactory<>("artista"));

        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        cancionesTable.getStyleClass().add("styled-table");
        usuariosTable.getStyleClass().add("styled-table");
    }

    private void configurarListeners() {
        cancionesTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> cargarCancionEnCampos(newValue)
        );

        usuariosTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> cargarUsuarioEnCampos(newValue)
        );
    }

    private void cargarDatos() {
        cancionesTable.getItems().setAll(cancionController.obtenerTodas());
        usuariosTable.getItems().setAll(usuarioController.listarUsuarios());
    }

    private void actualizarDashboard() {
        int totalCanciones = cancionController.obtenerTotal();
        int totalUsuarios = usuarioController.getCantidadUsuarios();

        Set<String> generos = new HashSet<>();
        for (Cancion cancion : cancionController.obtenerTodas()) {
            generos.add(cancion.getGenero());
        }

        if (totalCancionesLabel != null) totalCancionesLabel.setText(String.valueOf(totalCanciones));
        if (totalUsuariosLabel != null) totalUsuariosLabel.setText(String.valueOf(totalUsuarios));
        if (generosLabel != null) generosLabel.setText(String.valueOf(generos.size()));
    }

    @FXML
    private void mostrarDashboard() {
        cambiarVista(dashboardView, dashboardBtn);
        actualizarDashboard();
    }

    @FXML
    private void mostrarCanciones() {
        cambiarVista(cancionesView, cancionesBtn);
    }

    @FXML
    private void mostrarUsuarios() {
        cambiarVista(usuariosView, usuariosBtn);
    }

    @FXML
    private void mostrarEstadisticas() {
        cambiarVista(estadisticasView, estadisticasBtn);
        cargarEstadisticas();
    }

    private void cambiarVista(VBox vistaActiva, Button botonActivo) {
        dashboardView.setVisible(false);
        dashboardView.setManaged(false);
        cancionesView.setVisible(false);
        cancionesView.setManaged(false);
        usuariosView.setVisible(false);
        usuariosView.setManaged(false);
        estadisticasView.setVisible(false);
        estadisticasView.setManaged(false);

        vistaActiva.setVisible(true);
        vistaActiva.setManaged(true);

        resetearEstilosBotones();
        botonActivo.setStyle(
                "-fx-background-color: #282828; " +
                        "-fx-text-fill: #1DB954; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-alignment: center-left; " +
                        "-fx-padding: 15 20; " +
                        "-fx-background-radius: 8;"
        );
    }

    private void resetearEstilosBotones() {
        String estiloNormal = "-fx-background-color: transparent; " +
                "-fx-text-fill: #B3B3B3; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold; " +
                "-fx-alignment: center-left; " +
                "-fx-padding: 15 20; " +
                "-fx-background-radius: 8; " +
                "-fx-cursor: hand;";

        if (dashboardBtn != null) dashboardBtn.setStyle(estiloNormal);
        if (cancionesBtn != null) cancionesBtn.setStyle(estiloNormal);
        if (usuariosBtn != null) usuariosBtn.setStyle(estiloNormal);
        if (estadisticasBtn != null) estadisticasBtn.setStyle(estiloNormal);
    }

    private void cargarEstadisticas() {
        estadisticasContainer.getChildren().clear();

        int totalCanciones = cancionController.obtenerTotal();
        int totalUsuarios = usuarioController.getCantidadUsuarios();

        Set<String> generos = new HashSet<>();
        Set<String> artistas = new HashSet<>();
        for (Cancion cancion : cancionController.obtenerTodas()) {
            generos.add(cancion.getGenero());
            artistas.add(cancion.getArtista());
        }

        HBox statsRow1 = new HBox(30);
        statsRow1.setPadding(new Insets(10));
        statsRow1.setAlignment(javafx.geometry.Pos.CENTER);

        statsRow1.getChildren().addAll(
                crearStatCardMejorado(String.valueOf(totalCanciones), "Total Canciones", "🎵"),
                crearStatCardMejorado(String.valueOf(totalUsuarios), "Total Usuarios", "👥"),
                crearStatCardMejorado(String.valueOf(generos.size()), "Géneros Musicales", "🎸"),
                crearStatCardMejorado(String.valueOf(artistas.size()), "Artistas Únicos", "🎤")
        );

        estadisticasContainer.getChildren().add(statsRow1);
    }

    private VBox crearStatCardMejorado(String valor, String titulo, String icono) {
        VBox card = new VBox(15);
        card.setAlignment(javafx.geometry.Pos.CENTER);
        card.setPadding(new Insets(40));
        card.setPrefSize(250, 220);
        card.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #282828, #181818); " +
                        "-fx-background-radius: 20; " +
                        "-fx-effect: dropshadow(gaussian, rgba(29, 185, 84, 0.4), 15, 0, 0, 0);"
        );

        // Icono en verde
        Label iconLabel = new Label(icono);
        iconLabel.setStyle("-fx-font-size: 50px; -fx-text-fill: #1DB954;");

        // Valor en verde y grande
        Label valorLabel = new Label(valor);
        valorLabel.setStyle("-fx-text-fill: #1DB954; -fx-font-size: 48px; -fx-font-weight: bold;");

        // Título en blanco
        Label tituloLabel = new Label(titulo);
        tituloLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 16px; -fx-font-weight: bold;");

        card.getChildren().addAll(iconLabel, valorLabel, tituloLabel);
        return card;
    }

    private void cargarCancionEnCampos(Cancion cancion) {
        if (cancion != null) {
            idField.setText(String.valueOf(cancion.getId()));
            tituloField.setText(cancion.getTitulo());
            artistaField.setText(cancion.getArtista());
            generoField.setText(cancion.getGenero());
            añoField.setText(String.valueOf(cancion.getAño()));
            duracionField.setText(String.valueOf(cancion.getDuracion()));
        }
    }

    @FXML
    private void agregarCancion() {
        try {
            int id = Integer.parseInt(idField.getText());
            String titulo = tituloField.getText();
            String artista = artistaField.getText();
            String genero = generoField.getText();
            int año = Integer.parseInt(añoField.getText());
            double duracion = Double.parseDouble(duracionField.getText());

            if (titulo.isEmpty() || artista.isEmpty() || genero.isEmpty()) {
                UIComponents.mostrarAlertaPersonalizada("Error", "Todos los campos deben estar llenos", "❌");
                return;
            }

            cancionController.agregarCancion(id, titulo, artista, genero, año, duracion);
            cargarDatos();
            limpiarCamposCanciones();
            actualizarDashboard();
            UIComponents.mostrarAlertaPersonalizada("Éxito", "Canción agregada correctamente", "✅");
        } catch (NumberFormatException e) {
            UIComponents.mostrarAlertaPersonalizada("Error", "Los campos ID, Año y Duración deben ser números válidos", "❌");
        } catch (Exception e) {
            UIComponents.mostrarAlertaPersonalizada("Error", "Error al agregar: " + e.getMessage(), "❌");
        }
    }

    @FXML
    private void actualizarCancion() {
        Cancion seleccionada = cancionesTable.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            UIComponents.mostrarAlertaPersonalizada("Advertencia", "Debe seleccionar una canción de la tabla para actualizar", "⚠️");
            return;
        }

        try {
            int id = Integer.parseInt(idField.getText());
            String titulo = tituloField.getText();
            String artista = artistaField.getText();
            String genero = generoField.getText();
            int año = Integer.parseInt(añoField.getText());
            double duracion = Double.parseDouble(duracionField.getText());

            if (titulo.isEmpty() || artista.isEmpty() || genero.isEmpty()) {
                UIComponents.mostrarAlertaPersonalizada("Error", "Todos los campos deben estar llenos", "❌");
                return;
            }

            cancionController.actualizarCancion(id, titulo, artista, genero, año, duracion);
            cargarDatos();
            limpiarCamposCanciones();
            actualizarDashboard();
            UIComponents.mostrarAlertaPersonalizada("Éxito", "Canción actualizada correctamente", "✅");
        } catch (NumberFormatException e) {
            UIComponents.mostrarAlertaPersonalizada("Error", "Los campos ID, Año y Duración deben ser números válidos", "❌");
        } catch (Exception e) {
            UIComponents.mostrarAlertaPersonalizada("Error", "Error al actualizar: " + e.getMessage(), "❌");
        }
    }

    @FXML
    private void eliminarCancion() {
        Cancion seleccionada = cancionesTable.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText(null);
            confirmacion.setContentText("🗑️ ¿Está seguro de eliminar la canción:\n" + seleccionada.getTitulo() + "?");

            // ✅ ESTILIZAR EL DIÁLOGO DE CONFIRMACIÓN
            DialogPane dialogPane = confirmacion.getDialogPane();
            dialogPane.setStyle(
                    "-fx-background-color: #181818; " +
                            "-fx-border-color: #E91429; " +
                            "-fx-border-width: 2; " +
                            "-fx-border-radius: 10; " +
                            "-fx-background-radius: 10; " +
                            "-fx-effect: dropshadow(gaussian, #E91429, 15, 0.6, 0, 0);"
            );
            dialogPane.lookup(".content.label").setStyle(
                    "-fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-font-weight: bold;"
            );
            dialogPane.lookupButton(ButtonType.OK).setStyle(
                    "-fx-background-color: #E91429; -fx-text-fill: white; -fx-font-weight: bold; " +
                            "-fx-background-radius: 5; -fx-cursor: hand;"
            );
            dialogPane.lookupButton(ButtonType.CANCEL).setStyle(
                    "-fx-background-color: #404040; -fx-text-fill: white; -fx-font-weight: bold; " +
                            "-fx-background-radius: 5; -fx-cursor: hand;"
            );

            if (confirmacion.showAndWait().get() == ButtonType.OK) {
                cancionController.eliminarCancion(seleccionada.getId());
                cargarDatos();
                limpiarCamposCanciones();
                actualizarDashboard();
                UIComponents.mostrarAlertaPersonalizada("Éxito", "Canción eliminada", "✅");
            }
        } else {
            UIComponents.mostrarAlertaPersonalizada("Advertencia", "Debe seleccionar una canción para eliminar", "⚠️");
        }
    }

    @FXML
    private void limpiarCamposCanciones() {
        idField.clear();
        tituloField.clear();
        artistaField.clear();
        generoField.clear();
        añoField.clear();
        duracionField.clear();
        cancionesTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void cargaMasiva() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo de canciones");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de texto", "*.txt"),
                new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
        );

        File archivo = fileChooser.showOpenDialog(null);

        if (archivo != null) {
            try {
                int cancionesImportadas = procesarCargaMasiva(archivo);

                if (cancionesImportadas > 0) {
                    cargarDatos();
                    actualizarDashboard();
                    UIComponents.mostrarAlertaPersonalizada(
                            "Carga Masiva Exitosa",
                            cancionesImportadas + " canciones importadas correctamente desde:\n" + archivo.getName(),
                            "✅"
                    );
                } else {
                    UIComponents.mostrarAlertaPersonalizada(
                            "Advertencia",
                            "No se importaron canciones. Verifique el formato del archivo.",
                            "⚠️"
                    );
                }
            } catch (Exception e) {
                UIComponents.mostrarAlertaPersonalizada(
                        "Error",
                        "Error al procesar el archivo: " + e.getMessage() +
                                "\n\nFormato esperado por línea:\nid|titulo|artista|genero|año|duracion",
                        "❌"
                );
            }
        }
    }

    private int procesarCargaMasiva(File archivo) throws Exception {
        int cancionesImportadas = 0;
        List<String> errores = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            int numeroLinea = 0;

            while ((linea = reader.readLine()) != null) {
                numeroLinea++;
                linea = linea.trim();

                if (linea.isEmpty() || linea.startsWith("#")) {
                    continue;
                }

                try {
                    String[] partes = linea.split("\\|");

                    if (partes.length != 6) {
                        errores.add("Línea " + numeroLinea + ": formato inválido (se esperan 6 campos)");
                        continue;
                    }

                    int id = Integer.parseInt(partes[0].trim());
                    String titulo = partes[1].trim();
                    String artista = partes[2].trim();
                    String genero = partes[3].trim();
                    int año = Integer.parseInt(partes[4].trim());
                    double duracion = Double.parseDouble(partes[5].trim());

                    if (titulo.isEmpty() || artista.isEmpty() || genero.isEmpty()) {
                        errores.add("Línea " + numeroLinea + ": campos vacíos");
                        continue;
                    }

                    cancionController.agregarCancion(id, titulo, artista, genero, año, duracion);
                    cancionesImportadas++;

                } catch (NumberFormatException e) {
                    errores.add("Línea " + numeroLinea + ": error en formato numérico");
                } catch (Exception e) {
                    errores.add("Línea " + numeroLinea + ": " + e.getMessage());
                }
            }
        }

        if (!errores.isEmpty() && errores.size() < 10) {
            System.out.println("Errores durante la carga masiva:");
            errores.forEach(System.out::println);
        }

        return cancionesImportadas;
    }

    private void cargarUsuarioEnCampos(Usuario usuario) {
        if (usuario != null) {
            usernameOriginal = usuario.getUsername();
            usernameField.setText(usuario.getUsername());
            usernameField.setEditable(true);
            passwordField.setText(usuario.getPassword());
            nombreUsuarioField.setText(usuario.getNombre());
        } else {
            usernameOriginal = null;
            usernameField.setEditable(true);
        }
    }

    @FXML
    private void agregarUsuario() {
        try {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();
            String nombre = nombreUsuarioField.getText().trim();

            if (username.isEmpty() || password.isEmpty() || nombre.isEmpty()) {
                UIComponents.mostrarAlertaPersonalizada("Error", "Todos los campos deben estar llenos", "❌");
                return;
            }

            if (password.length() < 4) {
                UIComponents.mostrarAlertaPersonalizada("Error", "La contraseña debe tener al menos 4 caracteres", "❌");
                return;
            }

            boolean exito = usuarioController.registrar(username, password, nombre);

            if (exito) {
                cargarDatos();
                limpiarCamposUsuarios();
                actualizarDashboard();
                UIComponents.mostrarAlertaPersonalizada("Éxito", "Usuario agregado correctamente", "✅");
            } else {
                UIComponents.mostrarAlertaPersonalizada("Error", "El username ya existe o los datos son inválidos", "❌");
            }
        } catch (Exception e) {
            UIComponents.mostrarAlertaPersonalizada("Error", "Error al agregar usuario: " + e.getMessage(), "❌");
        }
    }

    @FXML
    private void actualizarUsuario() {
        Usuario seleccionado = usuariosTable.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            UIComponents.mostrarAlertaPersonalizada("Advertencia", "Debe seleccionar un usuario de la tabla para actualizar", "⚠️");
            return;
        }

        try {
            String usernameNuevo = usernameField.getText().trim();
            String password = passwordField.getText();
            String nombre = nombreUsuarioField.getText().trim();

            if (usernameNuevo.isEmpty() || password.isEmpty() || nombre.isEmpty()) {
                UIComponents.mostrarAlertaPersonalizada("Error", "Todos los campos deben estar llenos", "❌");
                return;
            }

            if (password.length() < 4) {
                UIComponents.mostrarAlertaPersonalizada("Error", "La contraseña debe tener al menos 4 caracteres", "❌");
                return;
            }

            usuarioController.actualizar(usernameOriginal, usernameNuevo, password, nombre);
            cargarDatos();
            limpiarCamposUsuarios();
            actualizarDashboard();
            UIComponents.mostrarAlertaPersonalizada("Éxito", "Usuario actualizado correctamente", "✅");
        } catch (Exception e) {
            UIComponents.mostrarAlertaPersonalizada("Error", "Error al actualizar usuario: " + e.getMessage(), "❌");
        }
    }

    @FXML
    private void eliminarUsuario() {
        Usuario seleccionado = usuariosTable.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText(null);
            confirmacion.setContentText("🗑️ ¿Está seguro de eliminar el usuario:\n" + seleccionado.getUsername() + "?");

            // ✅ ESTILIZAR EL DIÁLOGO DE CONFIRMACIÓN
            DialogPane dialogPane = confirmacion.getDialogPane();
            dialogPane.setStyle(
                    "-fx-background-color: #181818; " +
                            "-fx-border-color: #E91429; " +
                            "-fx-border-width: 2; " +
                            "-fx-border-radius: 10; " +
                            "-fx-background-radius: 10; " +
                            "-fx-effect: dropshadow(gaussian, #E91429, 15, 0.6, 0, 0);"
            );
            dialogPane.lookup(".content.label").setStyle(
                    "-fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-font-weight: bold;"
            );
            dialogPane.lookupButton(ButtonType.OK).setStyle(
                    "-fx-background-color: #E91429; -fx-text-fill: white; -fx-font-weight: bold; " +
                            "-fx-background-radius: 5; -fx-cursor: hand;"
            );
            dialogPane.lookupButton(ButtonType.CANCEL).setStyle(
                    "-fx-background-color: #404040; -fx-text-fill: white; -fx-font-weight: bold; " +
                            "-fx-background-radius: 5; -fx-cursor: hand;"
            );

            if (confirmacion.showAndWait().get() == ButtonType.OK) {
                usuarioController.eliminar(seleccionado.getUsername());
                cargarDatos();
                limpiarCamposUsuarios();
                actualizarDashboard();
                UIComponents.mostrarAlertaPersonalizada("Éxito", "Usuario eliminado", "✅");
            }
        } else {
            UIComponents.mostrarAlertaPersonalizada("Advertencia", "Debe seleccionar un usuario para eliminar", "⚠️");
        }
    }

    @FXML
    private void limpiarCamposUsuarios() {
        usernameField.clear();
        passwordField.clear();
        nombreUsuarioField.clear();
        usuariosTable.getSelectionModel().clearSelection();
        usernameOriginal = null;
    }

    @FXML
    private void cerrarSesion() {
        SessionManager.getInstance().cerrarSesion();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent loginView = loader.load();
            usernameField.getScene().setRoot(loginView);
        } catch (IOException e) {
            System.err.println("Error al cargar Login");
            e.printStackTrace();
            UIComponents.mostrarAlertaPersonalizada("Error", "Error al cerrar sesión: " + e.getMessage(), "❌");
        }
    }

    // ==================== EFECTOS HOVER ====================

    @FXML
    public void logoutHoverEnter(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #FF1744; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand; " +
                        "-fx-scale-x: 1.02; " +
                        "-fx-scale-y: 1.02;"
        );
    }

    @FXML
    public void logoutHoverExit(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #E91429; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand;"
        );
    }

    @FXML
    public void cargaHoverEnter(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #FFB730; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand; " +
                        "-fx-scale-x: 1.02; " +
                        "-fx-scale-y: 1.02;"
        );
    }

    @FXML
    public void cargaHoverExit(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #FFA500; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand;"
        );
    }

    @FXML
    public void agregarHoverEnter(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #1ed760; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand; " +
                        "-fx-scale-x: 1.02; " +
                        "-fx-scale-y: 1.02;"
        );
    }

    @FXML
    public void agregarHoverExit(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #1DB954; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand;"
        );
    }

    @FXML
    public void actualizarHoverEnter(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #FFB730; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand; " +
                        "-fx-scale-x: 1.02; " +
                        "-fx-scale-y: 1.02;"
        );
    }

    @FXML
    public void actualizarHoverExit(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #FFA500; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand;"
        );
    }

    @FXML
    public void eliminarHoverEnter(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #FF1744; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand; " +
                        "-fx-scale-x: 1.02; " +
                        "-fx-scale-y: 1.02;"
        );
    }

    @FXML
    public void eliminarHoverExit(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #E91429; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand;"
        );
    }

    @FXML
    public void limpiarHoverEnter(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #505050; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand; " +
                        "-fx-scale-x: 1.02; " +
                        "-fx-scale-y: 1.02;"
        );
    }

    @FXML
    public void limpiarHoverExit(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #404040; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand;"
        );
    }

    @FXML
    public void agregarUsuarioHoverEnter(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #1ed760; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand; " +
                        "-fx-scale-x: 1.02; " +
                        "-fx-scale-y: 1.02;"
        );
    }

    @FXML
    public void agregarUsuarioHoverExit(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #1DB954; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand;"
        );
    }

    @FXML
    public void actualizarUsuarioHoverEnter(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #FFB730; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand; " +
                        "-fx-scale-x: 1.02; " +
                        "-fx-scale-y: 1.02;"
        );
    }

    @FXML
    public void actualizarUsuarioHoverExit(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #FFA500; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand;"
        );
    }

    @FXML
    public void eliminarUsuarioHoverEnter(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #FF1744; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand; " +
                        "-fx-scale-x: 1.02; " +
                        "-fx-scale-y: 1.02;"
        );
    }

    @FXML
    public void eliminarUsuarioHoverExit(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #E91429; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand;"
        );
    }

    @FXML
    public void limpiarUsuarioHoverEnter(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #505050; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand; " +
                        "-fx-scale-x: 1.02; " +
                        "-fx-scale-y: 1.02;"
        );
    }

    @FXML
    public void limpiarUsuarioHoverExit(MouseEvent event) {
        ((Button) event.getSource()).setStyle(
                "-fx-background-color: #404040; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand;"
        );
    }
}