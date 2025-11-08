package co.edu.uniquindio.syncup.Model.Entidades;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Playlist - RF-005
 * Representa una lista de reproducción de canciones
 */
public class Playlist {
    private String nombre;
    private Usuario propietario;
    private List<Cancion> canciones;
    private LocalDate fechaCreacion;
    private String descripcion;

    public Playlist() {
        this.canciones = new ArrayList<>();
        this.fechaCreacion = LocalDate.now();
    }

    public Playlist(String nombre, Usuario propietario) {
        this.nombre = nombre;
        this.propietario = propietario;
        this.canciones = new ArrayList<>();
        this.fechaCreacion = LocalDate.now();
        this.descripcion = "";
    }

    public Playlist(String nombre, Usuario propietario, String descripcion) {
        this.nombre = nombre;
        this.propietario = propietario;
        this.canciones = new ArrayList<>();
        this.fechaCreacion = LocalDate.now();
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Usuario getPropietario() {
        return propietario;
    }

    public void setPropietario(Usuario propietario) {
        this.propietario = propietario;
    }

    public List<Cancion> getCanciones() {
        return new ArrayList<>(canciones);
    }

    public void setCanciones(List<Cancion> canciones) {
        this.canciones = new ArrayList<>(canciones);
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // Métodos de manipulación
    public void agregarCancion(Cancion cancion) {
        if (!canciones.contains(cancion)) {
            canciones.add(cancion);
        }
    }

    public void removerCancion(Cancion cancion) {
        canciones.remove(cancion);
    }

    public boolean contieneCancion(Cancion cancion) {
        return canciones.contains(cancion);
    }

    public int getTamaño() {
        return canciones.size();
    }

    public double getDuracionTotal() {
        return canciones.stream()
                .mapToDouble(Cancion::getDuracion)
                .sum();
    }

    public String getDuracionTotalFormateada() {
        double totalSegundos = getDuracionTotal();
        int horas = (int) (totalSegundos / 3600);
        int minutos = (int) ((totalSegundos % 3600) / 60);

        if (horas > 0) {
            return String.format("%d h %d min", horas, minutos);
        } else {
            return String.format("%d min", minutos);
        }
    }

    @Override
    public String toString() {
        return nombre + " (" + canciones.size() + " canciones)";
    }
}