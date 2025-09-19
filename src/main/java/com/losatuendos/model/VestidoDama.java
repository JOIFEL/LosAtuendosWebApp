package com.losatuendos.model;

public class VestidoDama extends Prenda {
    private boolean pedreria;
    private String altura;
    private int cantPiezas;

    public VestidoDama(String color, String marca, String talla, double valor, boolean pedreria, String altura, int cantPiezas) {
        super(color, marca, talla, valor);
        this.pedreria = pedreria;
        this.altura = altura;
        this.cantPiezas = cantPiezas;
    }

    // Getters para sus atributos específicos
    public boolean tienePedreria() { return pedreria; }
    public String getAltura() { return altura; }
    public int getCantPiezas() { return cantPiezas; }
    
    @Override
    public String getDescripcion() {
        return "Vestido de Dama " + marca + " (Ref: " + this.ref + ")";
    }
}