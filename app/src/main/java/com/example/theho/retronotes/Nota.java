package com.example.theho.retronotes;

public class Nota {
    String titulo, contenido, hora;
    int color;
    boolean favorito;

    public Nota(String titulo, String contenido, String hora, int color, boolean favorito) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.color = color;
        this.favorito = favorito;
        this.hora=hora;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }
}
