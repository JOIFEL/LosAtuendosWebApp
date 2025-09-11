// Archivo: Empleado.java
package com.losatuendos.model;

public class Empleado extends Persona {
    private String cargo;

    public Empleado(String id, String nombre, String direccion, String telefono, String cargo) {
        super(id, nombre, direccion, telefono);
        this.cargo = cargo;
    }
    
    public String getCargo() {
        return cargo;
    }
}