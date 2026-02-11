package ASP.BanCroak.filtros;

import ASP.BanCroak.domain.Gasto;

import java.util.ArrayList;
import java.util.List;

/**
 * Composite de filtros con lógica AND.
 */
public final class FiltroCompuesto extends Filtro {
    private final List<Filtro> filtros;

    public FiltroCompuesto() {
        this.filtros = new ArrayList<>();
    }

    public FiltroCompuesto(List<Filtro> filtros) {
        this.filtros = new ArrayList<>();
        if (filtros != null) {
            for (Filtro f : filtros) {
                añadirFiltro(f);
            }
        }
    }

    public void añadirFiltro(Filtro filtro) {
        if (filtro == null) {
            throw new IllegalArgumentException("El filtro no puede ser null");
        }
        if (!filtros.contains(filtro)) {
            filtros.add(filtro);
        }
    }

    public void eliminarFiltro(Filtro filtro) {
        if (filtro == null) {
            throw new IllegalArgumentException("El filtro no puede ser null");
        }
        filtros.remove(filtro);
    }

    public void limpiar() {
        filtros.clear();
    }

    @Override
    public boolean filtrar(Gasto gasto) {
        if (gasto == null) {
            return false;
        }
        if (filtros.isEmpty()) {
            return true;
        }
        for (Filtro f : filtros) {
            if (!f.filtrar(gasto)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "FiltroCompuesto{" +
            "filtros=" + filtros +
            '}';
    }
}
