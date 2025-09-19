package com.losatuendos.model;

public class Disfraz extends Prenda {
    private String nombreDisfraz;

    public Disfraz(String color, String marca, String talla, double valor, String nombreDisfraz) {
        super(color, marca, talla, valor);
        this.nombreDisfraz = nombreDisfraz;
    }

    // Getter para su atributo específico
    public String getNombreDisfraz() { return nombreDisfraz; }
    
    @Override
    public String getDescripcion() {
        return "Disfraz: " + this.nombreDisfraz + " (Ref: " + this.ref + ")";
    }
}