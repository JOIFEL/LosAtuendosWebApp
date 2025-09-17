// Archivo: Cliente.java
package com.losatuendos.model;

public class Cliente extends Persona {
    private String mail;

    // El constructor llama al constructor de la clase padre (Persona) con super().
    public Cliente(String id, String nombre, String direccion, String telefono, String mail) {
        super(id, nombre, direccion, telefono);
        this.mail = mail;
    }
    
    public String getMail() {
        return mail;
    }
}