// Archivo: TrajeCaballero.java
package com.losatuendos.model;

/**
 * Representa un traje de caballero.
 * Contiene atributos propios como el tipo de traje y el aderezo que lo acompaña.
 */
public class TrajeCaballero extends Prenda {

    private String tipo;      // E.g., "Frac", "Sacoleva", "Convencional"
    private String aderezo;   // E.g., "Corbata", "Corbatín", "Plastrón"

    /**
     * Constructor para crear una nueva instancia de TrajeCaballero.
     */
    public TrajeCaballero(String ref, String talla, double valorAlquiler, String aderezo) {
        // Llama al constructor de la clase padre (Prenda) para inicializar los atributos comunes.
        super(ref, talla, valorAlquiler);
        this.tipo = "Convencional"; // Asignamos un valor por defecto
        this.aderezo = aderezo;
    }

    /**
     * Sobrescribe el método de la clase padre para devolver el valor del alquiler.
     * En el futuro, podría incluir lógica adicional (ej: cobrar extra por un tipo de traje).
     */
    @Override
    public double getValorAlquiler() {
        return this.valorAlquiler;
    }

    /**
     * Sobrescribe el método para proporcionar una descripción detallada de esta prenda.
     */
    @Override
    public String getDescripcion() {
        return "Traje de Caballero (Ref: " + this.ref + ", Talla: " + this.talla + ", Aderezo: " + this.aderezo + ")";
    }
}