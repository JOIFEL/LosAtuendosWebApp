package com.losatuendos.model.patrones;

import com.losatuendos.model.Prenda;
import java.util.List;

public class RecomendacionPorOcasión implements RecomendacionStrategy {
    @Override
    public List<Prenda> recomendar(List<Prenda> prendas) {
        // Lógica de ejemplo para recomendar por ocasión
        System.out.println("Recomendando prendas por ocasión...");
        return prendas;
    }
}
