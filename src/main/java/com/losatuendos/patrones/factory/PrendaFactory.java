// Archivo: PrendaFactory.java
package com.losatuendos.patrones.factory;

import com.losatuendos.model.*;

// Patrón Factory Method: Centraliza la creación de objetos.
public class PrendaFactory {

    // Un único método estático que decide qué objeto crear.
    public static Prenda crearPrenda(String tipo, String ref, String talla, double valor) {
        switch (tipo.toLowerCase()) {
            case "vestido":
                // Podríamos pedir más datos para la pedrería, pero lo simplificamos.
                return new VestidoDama(ref, talla, valor, false);
            case "traje":
                return new TrajeCaballero(ref, talla, valor, "Corbata");
            case "disfraz":
                return new Disfraz(ref, talla, valor, "Superhéroe");
            default:
                // Si el tipo no es válido, lanzamos un error.
                throw new IllegalArgumentException("Tipo de prenda desconocido: " + tipo);
        }
    }
}
