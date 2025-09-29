package com.losatuendos;

import com.losatuendos.model.Prenda;
import com.losatuendos.model.ServicioAlquiler;
import com.losatuendos.model.Empleado;
import com.losatuendos.model.Cliente;
import com.losatuendos.model.patrones.*;
import com.losatuendos.model.patrones.Observer;

import java.util.*;

public class TestPatrones {
    public static void main(String[] args) {
        // --- Observer ---
        Prenda prenda = new Prenda("Rojo", "Nike", "M", 10000) {
            @Override
            public String getDescripcion() {
                return "Prenda de prueba";
            }
        };
        Observer obs = new Observer() {
            @Override
            public void update(Prenda p) {
                System.out.println("[Observer] Estado cambiado a: " + p.getEstado());
            }
        };
        prenda.addObserver(obs);
        prenda.setEstado("Alquilada");
        prenda.setEstado("Devuelta");

        // --- Strategy ---
        // Cliente requiere: String id, String nombre, String direccion, String
        // telefono, String mail
        Cliente cliente = new Cliente("1", "Juan", "Calle 123", "555-1234", "juan@email.com");
        // Empleado requiere: String id, String nombre, String direccion, String
        // telefono, String cargo
        Empleado empleado = new Empleado("2", "Ana", "Calle 456", "555-5678", "Vendedor");
        ServicioAlquiler alquiler = new ServicioAlquiler(1, cliente, empleado, prenda, new Date());
        List<Prenda> prendas = new ArrayList<>();
        prendas.add(prenda);
        alquiler.setEstrategia(new RecomendacionPorClima());
        System.out.println("[Strategy] Recomendación por clima:");
        alquiler.recomendarPrendas(prendas);
        alquiler.setEstrategia(new RecomendacionPorOcasión());
        System.out.println("[Strategy] Recomendación por ocasión:");
        alquiler.recomendarPrendas(prendas);
    }
}
