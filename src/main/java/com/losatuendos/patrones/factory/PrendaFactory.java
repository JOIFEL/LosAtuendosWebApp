// Archivo: PrendaFactory.java
package com.losatuendos.patrones.factory;

import com.losatuendos.model.*;

public class PrendaFactory {

    // CORRECCIÓN: El método ahora acepta los nuevos parámetros y los pasa
    // a los constructores correctos.
    public static Prenda crearPrenda(String tipo, String color, String marca, String talla, double valor) {
        switch (tipo.toLowerCase()) {
            case "vestido":
                // Creamos con valores por defecto para los atributos específicos.
                return new VestidoDama(color, marca, talla, valor, false, "N/A", 1);
            case "traje":
                return new TrajeCaballero(color, marca, talla, valor, "Convencional", "Corbata");
            case "disfraz":
                return new Disfraz(color, marca, talla, valor, "Genérico");
            default:
                throw new IllegalArgumentException("Tipo de prenda desconocido: " + tipo);
        }
    }
}