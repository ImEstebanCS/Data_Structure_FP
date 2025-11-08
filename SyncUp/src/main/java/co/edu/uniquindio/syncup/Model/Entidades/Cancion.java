package co.edu.uniquindio.syncup.Model.Entidades;

import java.util.Objects;

/**
 * Entidad Cancion - RF-016, RF-017, RF-018
 * Representa una pista musical en el catálogo
 */
public class Cancion {
    private int id;
    private String titulo;
    private String artista;
    private String album;
    private String genero;
    private int año;
    private double duracion; // en segundos
    private String portadaUrl;
    private int popularidad; // 0-100
    private AudioFeatures audioFeatures;

    public Cancion() {
    }

    public Cancion(int id, String titulo, String artista, String genero, int año, double duracion) {
        this.id = id;
        this.titulo = titulo;
        this.artista = artista;
        this.genero = genero;
        this.año = año;
        this.duracion = duracion;
        this.album = "";
        this.portadaUrl = "";
        this.popularidad = 50;
        this.audioFeatures = new AudioFeatures();
    }

    // Constructor completo
    public Cancion(int id, String titulo, String artista, String album, String genero,
                   int año, double duracion, String portadaUrl, int popularidad,
                   AudioFeatures audioFeatures) {
        this.id = id;
        this.titulo = titulo;
        this.artista = artista;
        this.album = album;
        this.genero = genero;
        this.año = año;
        this.duracion = duracion;
        this.portadaUrl = portadaUrl;
        this.popularidad = popularidad;
        this.audioFeatures = audioFeatures != null ? audioFeatures : new AudioFeatures();
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getAño() {
        return año;
    }

    public void setAño(int año) {
        this.año = año;
    }

    public double getDuracion() {
        return duracion;
    }

    public void setDuracion(double duracion) {
        this.duracion = duracion;
    }

    public String getPortadaUrl() {
        return portadaUrl;
    }

    public void setPortadaUrl(String portadaUrl) {
        this.portadaUrl = portadaUrl;
    }

    public int getPopularidad() {
        return popularidad;
    }

    public void setPopularidad(int popularidad) {
        this.popularidad = popularidad;
    }

    public AudioFeatures getAudioFeatures() {
        return audioFeatures;
    }

    public void setAudioFeatures(AudioFeatures audioFeatures) {
        this.audioFeatures = audioFeatures;
    }

    /**
     * Formatea la duración en formato MM:SS
     */
    public String getDuracionFormateada() {
        int minutos = (int) (duracion / 60);
        int segundos = (int) (duracion % 60);
        return String.format("%d:%02d", minutos, segundos);
    }

    /**
     * RF-018: hashCode basado en id
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * RF-018: equals basado en id
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cancion cancion = (Cancion) obj;
        return id == cancion.id;
    }

    @Override
    public String toString() {
        return titulo + " - " + artista;
    }

    /**
     * Clase interna para características de audio
     */
    public static class AudioFeatures {
        private double tempo;
        private double energia;
        private double bailabilidad;

        public AudioFeatures() {
            this.tempo = 120.0;
            this.energia = 0.5;
            this.bailabilidad = 0.5;
        }

        public AudioFeatures(double tempo, double energia, double bailabilidad) {
            this.tempo = tempo;
            this.energia = energia;
            this.bailabilidad = bailabilidad;
        }

        public double getTempo() {
            return tempo;
        }

        public void setTempo(double tempo) {
            this.tempo = tempo;
        }

        public double getEnergia() {
            return energia;
        }

        public void setEnergia(double energia) {
            this.energia = energia;
        }

        public double getBailabilidad() {
            return bailabilidad;
        }

        public void setBailabilidad(double bailabilidad) {
            this.bailabilidad = bailabilidad;
        }
    }
}