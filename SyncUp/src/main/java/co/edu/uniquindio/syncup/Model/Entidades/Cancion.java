package co.edu.uniquindio.syncup.Model.Entidades;

public class Cancion {
    private int id;
    private String titulo;
    private String artista;
    private String genero;
    private int año;
    private double duracion;
    private String portadaUrl;
    private String youtubeUrl;

    public Cancion() {
    }

    public Cancion(int id, String titulo, String artista, String genero, int año, double duracion) {
        this.id = id;
        this.titulo = titulo;
        this.artista = artista;
        this.genero = genero;
        this.año = año;
        this.duracion = duracion;
        this.portadaUrl = "https://via.placeholder.com/150";
        this.youtubeUrl = "https://www.youtube.com/results?search_query=" + titulo.replace(" ", "+");
    }

    public Cancion(int id, String titulo, String artista, String genero, int año, double duracion, String portadaUrl, String youtubeUrl) {
        this.id = id;
        this.titulo = titulo;
        this.artista = artista;
        this.genero = genero;
        this.año = año;
        this.duracion = duracion;
        this.portadaUrl = portadaUrl;
        this.youtubeUrl = youtubeUrl;
    }

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

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cancion cancion = (Cancion) obj;
        return id == cancion.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "Cancion{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", artista='" + artista + '\'' +
                ", genero='" + genero + '\'' +
                ", año=" + año +
                '}';
    }
}