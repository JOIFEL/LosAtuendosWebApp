// Archivo: Usuario.java
package com.losatuendos.model;

public class Usuario {
    private int id;
    private String username;
    private String rol;
    private String clienteId; // Opcional

    public Usuario(int id, String username, String rol, String clienteId) {
        this.id = id;
        this.username = username;
        this.rol = rol;
        this.clienteId = clienteId;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getRol() { return rol; }
    public String getClienteId() { return clienteId; }
}