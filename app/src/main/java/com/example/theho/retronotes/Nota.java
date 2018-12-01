package com.example.theho.retronotes;

/**
 * CLASE NOTA: clase contenedora de todos los datos que contendrá la nota
 */
public class Nota {

    private String titulo, contenido, color;

    /**
     * Nota: metodo de llamada para crear notas y luego poder guardarlas
     * @param titulo titulo de la nota
     * @param contenido contenido de la nota
     * @param color color de fondo de la nota que se mostrará en el ListView
     */
    public Nota(String titulo, String contenido,  String color) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.color = color;

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
}
