package com.losatuendos.model;

public class TrajeCaballero extends Prenda {
    private String tipo;
    private String aderezo;

    public TrajeCaballero(String color, String marca, String talla, double valor, String tipo, String aderezo) {
        super(color, marca, talla, valor);
        this.tipo = tipo;
        this.aderezo = aderezo;
    }

    // Getters para sus atributos específicos
    public String getTipo() { return tipo; }
    public String getAderezo() { return aderezo; }

    @Override
    public String getDescripcion() {
        return "Traje de Caballero " + marca + " (Ref: " + this.ref + ")";
    }
}