package com.losatuendos.model.patrones;

import com.losatuendos.model.Prenda;
import java.util.List;

public interface RecomendacionStrategy {
    List<Prenda> recomendar(List<Prenda> prendas);
}
