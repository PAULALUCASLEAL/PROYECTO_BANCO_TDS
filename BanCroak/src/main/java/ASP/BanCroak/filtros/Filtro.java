package ASP.BanCroak.filtros;

import ASP.BanCroak.Gasto;

/**
 * Contrato base para filtros de gastos.
 */
public abstract class Filtro {
    public abstract boolean filtrar(Gasto gasto);
}
