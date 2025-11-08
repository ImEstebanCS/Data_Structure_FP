package co.edu.uniquindio.syncup.Model.Entidades;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Radio - RF-006
 * Genera una cola de reproducción basada en una canción semilla
 */
public class Radio {
    private String nombre;
    private Cancion cancionSemilla;
    private Cancion cancionActual;
    private Queue<Cancion> colaReproduccion;

    public Radio() {
        this.colaReproduccion = new LinkedList<>();
    }

    public Radio(String nombre) {
        this.nombre = nombre;
        this.colaReproduccion = new LinkedList<>();
    }

    public Radio(String nombre, Cancion cancionSemilla) {
        this.nombre = nombre;
        this.cancionSemilla = cancionSemilla;
        this.colaReproduccion = new LinkedList<>();
    }

    // Getters y Setters
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

    public Cancion getCancionActual() {
        return cancionActual;
    }

    public void setCancionActual(Cancion cancionActual) {
        this.cancionActual = cancionActual;
    }

    public Queue<Cancion> getColaReproduccion() {
        return new LinkedList<>(colaReproduccion);
    }

    // Métodos de manipulación
    public void iniciarRadio(Cancion cancionInicial) {
        this.cancionSemilla = cancionInicial;
        this.cancionActual = cancionInicial;
        colaReproduccion.clear();
        colaReproduccion.add(cancionInicial);
    }

    public void agregarCancionACola(Cancion cancion) {
        if (cancion != null) {
            colaReproduccion.add(cancion);
        }
    }

    public Cancion siguienteCancion() {
        if (!colaReproduccion.isEmpty()) {
            cancionActual = colaReproduccion.poll();
            return cancionActual;
        }
        return null;
    }

    public int getTamañoCola() {
        return colaReproduccion.size();
    }

    public void limpiarCola() {
        colaReproduccion.clear();
        cancionActual = null;
    }

    public boolean tieneCancionesPendientes() {
        return !colaReproduccion.isEmpty();
    }

    @Override
    public String toString() {
        return nombre + " (basada en: " +
                (cancionSemilla != null ? cancionSemilla.getTitulo() : "ninguna") + ")";
    }
}