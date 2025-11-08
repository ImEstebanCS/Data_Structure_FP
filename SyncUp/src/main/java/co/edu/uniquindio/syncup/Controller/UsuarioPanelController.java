package co.edu.uniquindio.syncup.Controller;



import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import co.edu.uniquindio.syncup.Model.Entidades.Playlist;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class UsuarioPanelController {
    @FXML private TextArea busquedaField;
    @FXML private ListView<String> resultadosList;
    @FXML private ListView<String> favoritosList;
    @FXML private ListView<String> playlistList;
    @FXML private ComboBox<String> tipoBusquedaCombo;
    @FXML private Button buscarButton;
    @FXML private Button agregarFavoritoButton;
    @FXML private Button crearPlaylistButton;
    @FXML private Label usuarioLabel;

    private CancionController cancionController;
    private PlaylistController playlistController;
    private Usuario usuarioActual;
    private ObservableList<String> resultados;
    private ObservableList<String> favoritos;

    @FXML
    public void initialize() {
        tipoBusquedaCombo.getItems().addAll("Título", "Artista", "Género");
        tipoBusquedaCombo.setValue("Título");
        resultados = FXCollections.observableArrayList();
        favoritos = FXCollections.observableArrayList();
        resultadosList.setItems(resultados);
        favoritosList.setItems(favoritos);
    }

    @FXML
    public void manejarBusqueda() {
        String criterio = busquedaField.getText();
        String tipo = tipoBusquedaCombo.getValue();

        if (criterio.isEmpty()) {
            return;
        }

        resultados.clear();

        java.util.List<Cancion> canciones = null;
        if ("Título".equals(tipo)) {
            canciones = cancionController.buscarPorTitulo(criterio);
        } else if ("Artista".equals(tipo)) {
            canciones = cancionController.buscarPorArtista(criterio);
        } else if ("Género".equals(tipo)) {
            canciones = cancionController.buscarPorGenero(criterio);
        }

        if (canciones != null) {
            for (Cancion cancion : canciones) {
                resultados.add(cancion.getTitulo() + " - " + cancion.getArtista());
            }
        }
    }

    @FXML
    public void manejarAutocompletar() {
        String prefijo = busquedaField.getText();
        if (prefijo.isEmpty()) {
            resultados.clear();
            return;
        }

        resultados.clear();
        java.util.List<Cancion> canciones = cancionController.autocompletar(prefijo);

        for (Cancion cancion : canciones) {
            resultados.add(cancion.getTitulo() + " - " + cancion.getArtista());
        }
    }

    @FXML
    public void agregarFavorito() {
        int indice = resultadosList.getSelectionModel().getSelectedIndex();
        if (indice >= 0) {
            String seleccionado = resultados.get(indice);
            favoritos.add(seleccionado);
        }
    }

    @FXML
    public void crearPlaylist() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nueva Playlist");
        dialog.setHeaderText("Crear nueva Playlist");
        dialog.setContentText("Nombre de la Playlist:");

        java.util.Optional<String> resultado = dialog.showAndWait();
        resultado.ifPresent(nombre -> {
            Playlist playlist = playlistController.crearPlaylist(usuarioActual, nombre);
            if (playlist != null) {
                playlistList.getItems().add(nombre);
            }
        });
    }

    public void setControllers(CancionController cancionController, PlaylistController playlistController) {
        this.cancionController = cancionController;
        this.playlistController = playlistController;
    }

    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
        usuarioLabel.setText("Bienvenido: " + usuario.getNombre());
    }
}
