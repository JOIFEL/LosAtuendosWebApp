// Archivo: FachadaAlquiler.java
package com.losatuendos.patrones.facade;

import com.losatuendos.model.*;
import com.losatuendos.patrones.factory.PrendaFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Date;
import java.text.SimpleDateFormat; 

public class FachadaAlquiler {

    private List<Prenda> inventario = new ArrayList<>();
    private List<Cliente> clientes = new ArrayList<>();
    private List<Empleado> empleados = new ArrayList<>();
    private List<ServicioAlquiler> alquileres = new ArrayList<>();
    private List<Prenda> listaParaLavado = new ArrayList<>();

    public FachadaAlquiler() {
        // Datos de prueba para iniciar la aplicación
        inventario.add(PrendaFactory.crearPrenda("vestido", "V01", "M", 150000));
        inventario.add(PrendaFactory.crearPrenda("traje", "T01", "L", 200000));
        inventario.add(PrendaFactory.crearPrenda("disfraz", "D01", "S", 100000));
        clientes.add(new Cliente("123", "Carlos Perez", "Calle Falsa 123", "555-1234", "carlos@mail.com"));
        empleados.add(new Empleado("E01", "Ana García", "Av. Siempre Viva", "555-5678", "Vendedora"));
    }

    // --- Métodos para la aplicación web ---
    public List<Prenda> getInventario() {
        return this.inventario;
    }
    
    public List<Prenda> getListaParaLavado() {
        return this.listaParaLavado;
    }

    public List<Cliente> getClientes() {
        return this.clientes;
    }
    
    public List<Prenda> consultarPrendasPorTalla(String talla) {
        // Usamos la API de Streams de Java para una búsqueda limpia y moderna.
        return inventario.stream()
                         .filter(prenda -> prenda.getTalla().equalsIgnoreCase(talla))
                         .collect(Collectors.toList());
    }
    
    public List<ServicioAlquiler> consultarServiciosPorCliente(String clienteId) {
        return alquileres.stream()
                         .filter(alquiler -> alquiler.getCliente().getId().equals(clienteId))
                         .collect(Collectors.toList());
    }
    
    public List<ServicioAlquiler> consultarServiciosPorFecha(Date fecha) {
        // Formateador para comparar solo el día, mes y año, ignorando la hora.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fechaBuscada = sdf.format(fecha);
        
        return alquileres.stream()
                         .filter(a -> sdf.format(a.getFechaSolicitud()).equals(fechaBuscada))
                         .collect(Collectors.toList());
    }
    
    public List<Empleado> getEmpleados() {
        return this.empleados;
    }

    public void registrarCliente(String id, String nombre, String dir, String tel, String mail) {
        clientes.add(new Cliente(id, nombre, dir, tel, mail));
    }
    
    public void registrarEmpleado(String id, String nombre, String dir, String tel, String cargo) {
        empleados.add(new Empleado(id, nombre, dir, tel, cargo));
    }    
    
    public void agregarPrenda(Prenda nuevaPrenda) {
        if (nuevaPrenda != null) {
            this.inventario.add(nuevaPrenda);
        }
    }
    
    public void enviarALavado(String refPrenda, boolean conPrioridad) {
        // Buscamos la prenda original en el inventario.
        Prenda prendaAEnviar = inventario.stream()
                                         .filter(p -> p.getRef().equals(refPrenda))
                                         .findFirst()
                                         .orElse(null);

        if (prendaAEnviar != null) {
            
            if (conPrioridad) {
                // Si se necesita prioridad, envolvemos el objeto original con el decorador.
                prendaAEnviar = new com.losatuendos.patrones.decorator.PrioridadLavadoDecorator(prendaAEnviar);
            }
            listaParaLavado.add(prendaAEnviar);
        }
    }
    
    public int confirmarEnvioLavanderia() {
        int cantidadEnviada = listaParaLavado.size();
        listaParaLavado.clear(); // Vaciamos la lista
        return cantidadEnviada;
    }

    public String realizarAlquiler(String idCliente, String refPrenda, Date fechaAlquiler) {
    // Formateador para comparar fechas sin tener en cuenta la hora.
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String fechaBuscada = sdf.format(fechaAlquiler);

    // --- VERIFICACIÓN DE DISPONIBILIDAD ---
    for (ServicioAlquiler alquilerExistente : alquileres) {
        String fechaExistente = sdf.format(alquilerExistente.getFechaAlquiler());
        if (alquilerExistente.getPrenda().getRef().equals(refPrenda) && fechaExistente.equals(fechaBuscada)) {
            // Si encontramos la misma prenda para el mismo día, no está disponible.
            return "Error: La prenda " + refPrenda + " ya se encuentra alquilada para la fecha seleccionada.";
        }
    }

    // Si pasa la verificación, procedemos a registrar el alquiler.
    Cliente cliente = buscarCliente(idCliente);
    Prenda prenda = inventario.stream().filter(p -> p.getRef().equals(refPrenda)).findFirst().orElse(null);
    Empleado empleado = empleados.get(0); // Tomamos el primer empleado por defecto

    if (cliente != null && prenda != null) {
        int nuevoNumero = alquileres.size() + 1;
        ServicioAlquiler nuevoAlquiler = new ServicioAlquiler(nuevoNumero, cliente, empleado, prenda, fechaAlquiler);
        alquileres.add(nuevoAlquiler);
        return "¡Alquiler #" + nuevoNumero + " registrado exitosamente para el día " + fechaBuscada + "!";
    } else {
        return "Error: No se pudo encontrar el cliente o la prenda especificada.";
    }
    }
    
    public Cliente buscarCliente(String clienteId) {
        return clientes.stream()
                       .filter(c -> c.getId().equals(clienteId))
                       .findFirst()
                       .orElse(null);
    }
    
     public ServicioAlquiler buscarServicioPorNumero(int numero) {
        return alquileres.stream()
                         .filter(a -> a.getNumero() == numero)
                         .findFirst()
                         .orElse(null);
    }
}