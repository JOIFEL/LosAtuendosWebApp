// Archivo: Disfraz.java
package com.losatuendos.model;

/**
 * Representa un disfraz.
 * Su atributo principal es el nombre que lo identifica.
 */
public class Disfraz extends Prenda {

    private String nombre; // E.g., "Batman", "Princesa Peach", "Vaquero"

    /**
     * Constructor para crear una nueva instancia de Disfraz.
     */
    public Disfraz(String ref, String talla, double valorAlquiler, String nombre) {
        // Llama al constructor de la clase padre (Prenda) para inicializar lo básico.
        super(ref, talla, valorAlquiler);
        this.nombre = nombre;
    }

    /**
     * Sobrescribe el método para devolver el valor del alquiler del disfraz.
     */
    @Override
    public double getValorAlquiler() {
        return this.valorAlquiler;
    }

    /**
     * Sobrescribe el método para dar una descripción clara del disfraz.
     */
    @Override
    public String getDescripcion() {
        return "Disfraz: " + this.nombre + " (Ref: " + this.ref + ", Talla: " + this.talla + ")";
    }
}