package com.losatuendos.model.patrones;

import com.losatuendos.model.Prenda;
import java.util.List;

public class RecomendacionPorClima implements RecomendacionStrategy {
    @Override
    public List<Prenda> recomendar(List<Prenda> prendas) {
        // Lógica de ejemplo para recomendar por clima
        System.out.println("Recomendando prendas por clima...");
        return prendas;
    }
}
