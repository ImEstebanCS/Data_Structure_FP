package co.edu.uniquindio.syncup.Model.Trie;


import co.edu.uniquindio.syncup.Model.Entidades.Cancion;

public class NodoTrieAvanzado {
    private NodoTrieAvanzado izquierda;
    private NodoTrieAvanzado derecha;
    private char caracter;
    private Cancion cancion;
    private int altura;

    public NodoTrieAvanzado(char caracter) {
        this.caracter = caracter;
        this.altura = 1;
        this.izquierda = null;
        this.derecha = null;
        this.cancion = null;
    }

    public NodoTrieAvanzado getIzquierda() {
        return izquierda;
    }

    public void setIzquierda(NodoTrieAvanzado izquierda) {
        this.izquierda = izquierda;
    }

    public NodoTrieAvanzado getDerecha() {
        return derecha;
    }

    public void setDerecha(NodoTrieAvanzado derecha) {
        this.derecha = derecha;
    }

    public char getCaracter() {
        return caracter;
    }

    public void setCaracter(char caracter) {
        this.caracter = caracter;
    }

    public Cancion getCancion() {
        return cancion;
    }

    public void setCancion(Cancion cancion) {
        this.cancion = cancion;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }
}
