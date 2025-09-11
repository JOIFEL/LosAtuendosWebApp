// Archivo: Persona.java
package com.losatuendos.model;

// Clase abstracta 
public abstract class Persona {
    // Atributos protegidos para que las clases hijas puedan acceder a ellos.
    protected String id;
    protected String nombre;
    protected String direccion;
    protected String telefono;

    // Constructor
    public Persona(String id, String nombre, String direccion, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    // Métodos para obtener la información (Getters).
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
}