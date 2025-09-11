// Archivo: ServicioAlquiler.java
package com.losatuendos.model;

import java.util.Date;

public class ServicioAlquiler {

    private int numero;
    private Date fechaSolicitud;
    private Date fechaAlquiler; // La fecha para la cual se reserva la prenda
    private Cliente cliente;
    private Empleado empleado;
    private Prenda prenda;

    /**
     * Constructor mejorado que acepta una fecha de alquiler específica.
     */
    public ServicioAlquiler(int numero, Cliente cliente, Empleado empleado, Prenda prenda, Date fechaAlquiler) {
        this.numero = numero;
        this.cliente = cliente;
        this.empleado = empleado;
        this.prenda = prenda;
        this.fechaAlquiler = fechaAlquiler;
        this.fechaSolicitud = new Date(); // La fecha de solicitud siempre es la actual
    }

    // --- Getters que necesitaremos ---
    public int getNumero() { return numero; }
    public Cliente getCliente() { return cliente; }
    public Prenda getPrenda() { return prenda; }
    public Date getFechaSolicitud() { return fechaSolicitud; }
    public Date getFechaAlquiler() { return fechaAlquiler; }
}