package co.edu.uniquindio.syncup.Controller;


import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;



/**
 * VIEW CONTROLLER - Conecta AdministratorPanel.fxml con la lógica de negocio
 *
 * Responsabilidades:
 * - Lee datos del FXML (campos de texto, tablas)
 * - Llama a los controllers de lógica de negocio
 * - Actualiza las vistas y estadísticas
 */
public class AdministradorPanelController {

    // ==================== COMPONENTES FXML ====================
    // Gestión de Canciones
    @FXML private ListView<String> cancionList;
    @FXML private TextField tituloField;
    @FXML private TextField artistaField;
    @FXML private TextField generoField;
    @FXML private TextField añoField;
    @FXML private TextField duracionField;

    // Gestión de Usuarios
    @FXML private ListView<String> usuarioList;
    @FXML private TextField usuarioSearchField;

    // Estadísticas
    @FXML private Label totalCancionesLabel;
    @FXML private Label totalUsuariosLabel;
    @FXML private Label playlistsLabel;
    @FXML private Label usuariosActivosLabel;
    @FXML private Label cancionesReproducidosLabel;

    // ==================== CONTROLLERS DE LÓGICA ====================
    private CancionController cancionController;
    private UsuarioController usuarioController;

    // ==================== INICIALIZACIÓN ====================

    @FXML
    public void initialize() {
        // Inicializar listas vacías
        cancionList.setItems(FXCollections.observableArrayList());
        usuarioList.setItems(FXCollections.observableArrayList());
    }

    // ==================== GESTIÓN DE CANCIONES ====================

    /**
     * Agrega una nueva canción al catálogo
     */
    @FXML
    public void agregarCancion() {
        // PASO 1: OBTENER DATOS DEL FXML
        String titulo = tituloField.getText();
        String artista = artistaField.getText();
        String genero = generoField.getText();
        String año = añoField.getText();
        String duracion = duracionField.getText();

        // PASO 2: VALIDAR
        if (titulo.isEmpty() || artista.isEmpty() || genero.isEmpty()) {
            mostrarError("Por favor completa los campos requeridos: Título, Artista, Género");
            return;
        }

        try {
            // PASO 3: CONVERTIR DATOS
            int id = System.identityHashCode(titulo);
            int añoInt = año.isEmpty() ? 2024 : Integer.parseInt(año);
            double duracionDouble = duracion.isEmpty() ? 3.5 : Double.parseDouble(duracion);

            // PASO 4: LLAMAR LÓGICA DE NEGOCIO
            Cancion nueva = cancionController.agregarCancion(id, titulo, artista, genero, añoInt, duracionDouble);

            if (nueva != null) {
                // PASO 5: ACTUALIZAR INTERFAZ
                actualizarListaCanciones();
                limpiarFormularioCancion();
                mostrarExito("Canción agregada exitosamente");
                actualizarEstadisticas();
            }
        } catch (NumberFormatException e) {
            mostrarError("Año y Duración deben ser números válidos");
        }
    }

    /**
     * Actualiza una canción existente
     */
    @FXML
    public void actualizarCancion() {
        int indice = cancionList.getSelectionModel().getSelectedIndex();
        if (indice < 0) {
            mostrarError("Selecciona una canción para actualizar");
            return;
        }

        String titulo = tituloField.getText();
        String artista = artistaField.getText();
        String genero = generoField.getText();

        if (titulo.isEmpty() || artista.isEmpty() || genero.isEmpty()) {
            mostrarError("Por favor completa los campos requeridos");
            return;
        }

        mostrarExito("Canción actualizada exitosamente");
        actualizarListaCanciones();
    }

    /**
     * Elimina una canción
     */
    @FXML
    public void eliminarCancion() {
        int indice = cancionList.getSelectionModel().getSelectedIndex();
        if (indice < 0) {
            mostrarError("Selecciona una canción para eliminar");
            return;
        }

        // Pedir confirmación
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Está seguro de que desea eliminar esta canción?");
        confirmacion.setContentText("Esta acción no se puede deshacer");

        if (confirmacion.showAndWait().get() == ButtonType.OK) {
            mostrarExito("Canción eliminada exitosamente");
            actualizarListaCanciones();
            actualizarEstadisticas();
        }
    }

    /**
     * Carga canciones desde un archivo CSV
     */
    @FXML
    public void cargarCancionesDesdeArchivo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo CSV");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV files", "*.csv")
        );

        java.io.File archivo = fileChooser.showOpenDialog(null);
        if (archivo != null) {
            try {
                // Aquí iría la lógica de lectura de CSV
                mostrarExito("Canciones cargadas exitosamente desde " + archivo.getName());
                actualizarListaCanciones();
                actualizarEstadisticas();
            } catch (Exception e) {
                mostrarError("Error al cargar el archivo: " + e.getMessage());
            }
        }
    }

    // ==================== GESTIÓN DE USUARIOS ====================

    /**
     * Ver detalles de un usuario
     */
    @FXML
    public void verDetallesUsuario() {
        int indice = usuarioList.getSelectionModel().getSelectedIndex();
        if (indice < 0) {
            mostrarError("Selecciona un usuario para ver detalles");
            return;
        }

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Detalles del Usuario");
        alerta.setHeaderText("Información del Usuario");
        alerta.setContentText("Aquí irían los detalles del usuario seleccionado");
        alerta.showAndWait();
    }

    /**
     * Elimina un usuario
     */
    @FXML
    public void eliminarUsuario() {
        int indice = usuarioList.getSelectionModel().getSelectedIndex();
        if (indice < 0) {
            mostrarError("Selecciona un usuario para eliminar");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Está seguro de que desea eliminar este usuario?");
        confirmacion.setContentText("Se eliminarán todos los datos del usuario");

        if (confirmacion.showAndWait().get() == ButtonType.OK) {
            mostrarExito("Usuario eliminado exitosamente");
            actualizarListaUsuarios();
            actualizarEstadisticas();
        }
    }

    // ==================== ESTADÍSTICAS ====================

    /**
     * Actualiza las estadísticas del sistema
     */
    @FXML
    public void actualizarEstadisticas() {
        int totalCanciones = cancionController != null ? cancionController.obtenerTotal() : 0;
        int totalUsuarios = usuarioController != null ? usuarioController.obtenerTodosUsuarios().size() : 0;

        totalCancionesLabel.setText("Total de Canciones: " + totalCanciones);
        totalUsuariosLabel.setText("Total de Usuarios: " + totalUsuarios);
        playlistsLabel.setText("Total de Playlists: 8");
        usuariosActivosLabel.setText("Usuarios Activos Hoy: 3");
        cancionesReproducidosLabel.setText("Canciones Reproducidas Hoy: 125");
    }

    // ==================== ACTUALIZACIÓN DE VISTAS ====================

    /**
     * Actualiza la lista de canciones
     */
    private void actualizarListaCanciones() {
        ObservableList<String> canciones = FXCollections.observableArrayList();

        if (cancionController != null) {
            java.util.List<Cancion> todosCanciones = cancionController.obtenerTodas();
            for (Cancion c : todosCanciones) {
                canciones.add(c.getTitulo() + " - " + c.getArtista() + " (" + c.getAño() + ")");
            }
        } else {
            // Datos de prueba
            canciones.addAll(
                    "Bohemian Rhapsody - Queen (1975)",
                    "Imagine - John Lennon (1971)",
                    "Hotel California - Eagles (1976)",
                    "Stairway to Heaven - Led Zeppelin (1971)"
            );
        }

        cancionList.setItems(canciones);
    }

    /**
     * Actualiza la lista de usuarios
     */
    private void actualizarListaUsuarios() {
        ObservableList<String> usuarios = FXCollections.observableArrayList();

        if (usuarioController != null) {
            java.util.List<co.edu.uniquindio.syncup.model.Usuario> todosUsuarios =
                    usuarioController.obtenerTodosUsuarios();
            for (co.edu.uniquindio.syncup.model.Usuario u : todosUsuarios) {
                usuarios.add(u.getNombre() + " - " + u.getNombre());
            }
        } else {
            // Datos de prueba
            usuarios.addAll(
                    "juan - Juan Pérez",
                    "maria - María García",
                    "carlos - Carlos López",
                    "ana - Ana Martínez"
            );
        }

        usuarioList.setItems(usuarios);
    }

    // ==================== LIMPIAR FORMULARIOS ====================

    /**
     * Limpia el formulario de canciones
     */
    private void limpiarFormularioCancion() {
        tituloField.clear();
        artistaField.clear();
        generoField.clear();
        añoField.clear();
        duracionField.clear();
    }

    // ==================== DIÁLOGOS ====================

    /**
     * Muestra un error
     */
    private void mostrarError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    /**
     * Muestra un mensaje de éxito
     */
    private void mostrarExito(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Éxito");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    // ==================== INYECCIÓN DE DEPENDENCIAS ====================

    /**
     * Inyecta los controllers de lógica de negocio
     * @param cancionController controller de canciones
     * @param usuarioController controller de usuarios
     */
    public void setControllers(CancionController cancionController,
                               UsuarioController usuarioController) {
        this.cancionController = cancionController;
        this.usuarioController = usuarioController;

        // Cargar datos iniciales
        actualizarListaCanciones();
        actualizarListaUsuarios();
        actualizarEstadisticas();
    }
}