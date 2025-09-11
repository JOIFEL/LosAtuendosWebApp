// Archivo: PrioridadLavadoDecorator.java
package com.losatuendos.patrones.decorator;

import com.losatuendos.model.Prenda;

// Este es un decorador concreto. Añade una nueva funcionalidad.
public class PrioridadLavadoDecorator extends PrendaDecorator {
    public PrioridadLavadoDecorator(Prenda prendaDecorada) {
        super(prendaDecorada);
    }

    // Sobrescribimos el método para añadir la nueva descripción.
    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " [LAVADO PRIORITARIO]";
    }
}
