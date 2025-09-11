// Archivo: PrendaDecorator.java
package com.losatuendos.patrones.decorator;

import com.losatuendos.model.Prenda;

// El decorador abstracto "envuelve" una Prenda.
public abstract class PrendaDecorator extends Prenda {
    protected Prenda prendaDecorada;

    public PrendaDecorator(Prenda prendaDecorada) {
        // El constructor del decorador no necesita los atributos base,
        // ya que los delega a la prenda envuelta.
        super(prendaDecorada.getRef(), prendaDecorada.getTalla(), 0);
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