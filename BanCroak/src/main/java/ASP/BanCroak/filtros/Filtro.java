package ASP.BanCroak.filtros;

import ASP.BanCroak.domain.Gasto;


public abstract class Filtro {
    public abstract boolean filtrar(Gasto gasto);
}
