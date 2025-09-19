// Archivo: PrendaDecorator.java
package com.losatuendos.patrones.decorator;

import com.losatuendos.model.Prenda;

public abstract class PrendaDecorator extends Prenda {
    protected Prenda prendaDecorada;

    public PrendaDecorator(Prenda prendaDecorada) {
        
        super(prendaDecorada.getColor(), prendaDecorada.getMarca(), prendaDecorada.getTalla(), prendaDecorada.getValorAlquiler());
        this.prendaDecorada = prendaDecorada;
    }

    @Override
    public double getValorAlquiler() {
        return prendaDecorada.getValorAlquiler();
    }
    
    @Override
    public String getDescripcion() {
        return prendaDecorada.getDescripcion();
    }
}