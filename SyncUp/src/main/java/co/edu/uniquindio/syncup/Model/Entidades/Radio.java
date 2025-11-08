package co.edu.uniquindio.syncup.Model.Entidades;

import java.util.*;

/**
 * Clase que representa una "Radio" que genera una cola de reproducción
 * RF-006: Iniciar una "Radio" a partir de una canción
 */

import java.util.*;

public class Radio {
    private String nombre;
    private Cancion cancionActual;
    private Queue<Cancion> colaDereproduccion;

    public Radio(String nombre) {
        this.nombre = nombre;
        this.colaDereproduccion = new LinkedList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Cancion getCancionActual() {
        return cancionActual;
    }

    public Queue<Cancion> getColaDereproduccion() {
        return new LinkedList<>(colaDereproduccion);
    }

    public void agregarCancionACola(Cancion cancion) {
        colaDereproduccion.add(cancion);
    }

    public void iniciarRadio(Cancion cancionInicial) {
        this.cancionActual = cancionInicial;
        colaDereproduccion.add(cancionInicial);
    }

    public Cancion siguienteCancion() {
        if (!colaDereproduccion.isEmpty()) {
            cancionActual = colaDereproduccion.poll();
            return cancionActual;
        }
        return null;
    }

    public int getTamañoCola() {
        return colaDereproduccion.size();
    }

    public void limpiarCola() {
        colaDereproduccion.clear();
        cancionActual = null;
    }

    @Override
    public String toString() {
        return "Radio{" +
                "nombre='" + nombre + '\'' +
                ", cancionActual=" + (cancionActual != null ? cancionActual.getTitulo() : "Ninguna") +
                ", colaDereproduccion=" + colaDereproduccion.size() +
                '}';
    }
}