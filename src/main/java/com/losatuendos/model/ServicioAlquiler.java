// Archivo: ServicioAlquiler.java
package com.losatuendos.model;

import com.losatuendos.model.patrones.RecomendacionStrategy;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class ServicioAlquiler implements Iterable<Prenda> {

    private int numero;
    private Date fechaSolicitud;
    private Date fechaAlquiler;
    private Cliente cliente;
    private Empleado empleado;
    private Prenda prenda;

    // Para Iterator y Command
    private List<Prenda> prendasAlquiladas = new ArrayList<>();

    // Para Strategy
    private RecomendacionStrategy estrategia;

    /**
     * Constructor mejorado que acepta una fecha de alquiler específica.
     */
    public ServicioAlquiler(int numero, Cliente cliente, Empleado empleado, Prenda prenda, Date fechaAlquiler) {
        this.numero = numero;
        this.cliente = cliente;
        this.empleado = empleado;
        this.prenda = prenda;
        this.fechaAlquiler = fechaAlquiler;
        this.fechaSolicitud = new Date();
        if (prenda != null) {
            prendasAlquiladas.add(prenda);
        }
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public int getNumero() { return numero; }
    public Cliente getCliente() { return cliente; }
    public Prenda getPrenda() { return prenda; }
    public Date getFechaSolicitud() { return fechaSolicitud; }
    public Date getFechaAlquiler() { return fechaAlquiler; }

    // --- Métodos para Iterator y Command ---
    public void agregarPrenda(Prenda prenda) {
        prendasAlquiladas.add(prenda);
    }

    @Override
    public Iterator<Prenda> iterator() {
        return prendasAlquiladas.iterator();
    }

    // --- Métodos para Strategy ---
    public void setEstrategia(RecomendacionStrategy estrategia) {
        this.estrategia = estrategia;
    }

    public List<Prenda> recomendarPrendas(List<Prenda> prendas) {
        if (estrategia == null) throw new IllegalStateException("Estrategia no definida");
        return estrategia.recomendar(prendas);
    }
}