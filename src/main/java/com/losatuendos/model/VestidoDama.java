// Archivo: VestidoDama.java
package com.losatuendos.model;

public class VestidoDama extends Prenda {
    private boolean tienePedreria;

    public VestidoDama(String ref, String talla, double valorAlquiler, boolean tienePedreria) {
        super(ref, talla, valorAlquiler);
        this.tienePedreria = tienePedreria;
    }

    @Override
    public double getValorAlquiler() {
        return this.valorAlquiler;
    }

    @Override
    public String getDescripcion() {
        return "Vestido de Dama (Ref: " + this.ref + ", Talla: " + this.talla + ")";
    }
}