package co.edu.uniquindio.syncup.Model.Entidades;

import java.util.LinkedList;
import java.util.Queue;

public class Radio {
    private String nombre;
    private Cancion cancionSemilla;
    private Queue<Cancion> colaReproduccion;

    public Radio(String nombre, Cancion cancionSemilla) {
        this.nombre = nombre;
        this.cancionSemilla = cancionSemilla;
        this.colaReproduccion = new LinkedList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Cancion getCancionSemilla() {
        return cancionSemilla;
    }

    public void setCancionSemilla(Cancion cancionSemilla) {
        this.cancionSemilla = cancionSemilla;
    }

    public Queue<Cancion> getColaReproduccion() {
        return colaReproduccion;
    }

    public void agregarALaCola(Cancion cancion) {
        colaReproduccion.add(cancion);
    }

    public Cancion siguienteCancion() {
        return colaReproduccion.poll();
    }

    @Override
    public String toString() {
        return "Radio{" +
                "nombre='" + nombre + '\'' +
                ", cancionSemilla=" + cancionSemilla.getTitulo() +
                ", cola=" + colaReproduccion.size() +
                '}';
    }
}