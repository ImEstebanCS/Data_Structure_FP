package co.edu.uniquindio.syncup.Model.Entidades;

import java.util.LinkedList;

public class Playlist {
    private String nombre;
    private Usuario creador;
    private LinkedList<Cancion> canciones;

    public Playlist() {
        this.canciones = new LinkedList<>();
    }

    public Playlist(String nombre, Usuario creador) {
        this.nombre = nombre;
        this.creador = creador;
        this.canciones = new LinkedList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Usuario getCreador() {
        return creador;
    }

    public void setCreador(Usuario creador) {
        this.creador = creador;
    }

    public LinkedList<Cancion> getCanciones() {
        return canciones;
    }

    public void setCanciones(LinkedList<Cancion> canciones) {
        this.canciones = canciones;
    }

    public void agregarCancion(Cancion cancion) {
        if (!canciones.contains(cancion)) {
            canciones.add(cancion);
        }
    }

    public void eliminarCancion(Cancion cancion) {
        canciones.remove(cancion);
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "nombre='" + nombre + '\'' +
                ", canciones=" + canciones.size() +
                '}';
    }
}