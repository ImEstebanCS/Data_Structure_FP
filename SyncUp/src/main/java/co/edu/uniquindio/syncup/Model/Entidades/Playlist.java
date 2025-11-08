package co.edu.uniquindio.syncup.Model.Entidades;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una playlist de canciones
 * RF-005: Generar playlist "Descubrimiento Semanal"
 */


import java.time.LocalDate;
import java.util.*;

public class Playlist {
    private String nombre;
    private Usuario propietario;
    private List<Cancion> canciones;
    private LocalDate fechaCreacion;

    public Playlist(String nombre, Usuario propietario) {
        this.nombre = nombre;
        this.propietario = propietario;
        this.canciones = new ArrayList<>();
        this.fechaCreacion = LocalDate.now();
    }

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

    public void agregarCancion(Cancion cancion) {
        if (!canciones.contains(cancion)) {
            canciones.add(cancion);
        }
    }

    public void removerCancion(Cancion cancion) {
        canciones.remove(cancion);
    }

    public int getTamaño() {
        return canciones.size();
    }

    public double getDuracionTotal() {
        return canciones.stream().mapToDouble(Cancion::getDuracion).sum();
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "nombre='" + nombre + '\'' +
                ", propietario=" + propietario.getNombre() +
                ", canciones=" + canciones.size() +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}