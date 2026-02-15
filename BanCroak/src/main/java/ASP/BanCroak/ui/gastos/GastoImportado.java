package ASP.BanCroak.ui.gastos;

import java.time.LocalDate;

public class GastoImportado {
    public LocalDate fecha;
    public String cuenta;
    public String categoria;
    public String pagador;
    public double cantidad;

    public GastoImportado(LocalDate fecha, String cuenta, String categoria, String pagador, double cantidad) {
        this.fecha = fecha;
        this.cuenta = cuenta;
        this.categoria = categoria;
        this.pagador = pagador;
        this.cantidad = cantidad;
    }
}