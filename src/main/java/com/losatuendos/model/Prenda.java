// Archivo: Prenda.java
package com.losatuendos.model;

// Esta será la base para nuestro patrón Decorator.
// Es abstracta para que no podamos crear una "Prenda" genérica.
public abstract class Prenda {
    protected String ref;
    protected String talla;
    protected double valorAlquiler;

    public Prenda(String ref, String talla, double valorAlquiler) {
        this.ref = ref;
        this.talla = talla;
        this.valorAlquiler = valorAlquiler;
    }

    public String getRef() {
        return ref;
    }
    
    public String getTalla() {
        return talla;
    }

    // Este método podrá ser modificado por los decoradores.
    public abstract double getValorAlquiler();
    public abstract String getDescripcion();
}