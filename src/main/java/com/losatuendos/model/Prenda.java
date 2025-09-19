// Archivo: Prenda.java
package com.losatuendos.model;

public abstract class Prenda {
    // El 'ref' se asignará en la base de datos
    protected String ref;
    protected String color;
    protected String marca;
    protected String talla;
    protected double valorAlquiler;

    public Prenda(String color, String marca, String talla, double valorAlquiler) {
        this.color = color;
        this.marca = marca;
        this.talla = talla;
        this.valorAlquiler = valorAlquiler;
    }
    
    // Getters para todos los atributos
    public String getRef() { return ref; }
    public String getColor() { return color; }
    public String getMarca() { return marca; }
    public String getTalla() { return talla; }
    public double getValorAlquiler() { return valorAlquiler; }
    
    // Setter para la ref, que la fachada usará después de crearla.
    public void setRef(String ref) { this.ref = ref; }

    public abstract String getDescripcion();
}