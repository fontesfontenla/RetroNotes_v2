package com.example.theho.retronotes;

public class Nota {
    private String titulo, contenido, hora, color;

    //private boolean favorito;


    public Nota(String titulo, String contenido, String hora, String color) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.color = color;
        //this.favorito = favorito;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
/*
    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }*/
}
