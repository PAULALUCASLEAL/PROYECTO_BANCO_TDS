package ASP.BanCroak.filtros;

import ASP.BanCroak.Gasto;

import java.time.LocalDate;

/**
 * Filtra por intervalo de fechas (incluyente). Permite rango abierto.
 */
public final class FiltroIntervaloFechas extends Filtro {
    private final LocalDate desde;
    private final LocalDate hasta;

    public FiltroIntervaloFechas(LocalDate desde, LocalDate hasta) {
        if (desde != null && hasta != null && desde.isAfter(hasta)) {
            throw new IllegalArgumentException("La fecha 'desde' no puede ser posterior a 'hasta'");
        }
        this.desde = desde;
        this.hasta = hasta;
    }

    @Override
    public boolean filtrar(Gasto gasto) {
        if (gasto == null) {
            return false;
        }
        if (desde == null && hasta == null) {
            return true;
        }
        LocalDate fecha = gasto.getFecha();
        if (fecha == null) {
            return false;
        }
        if (desde != null && hasta != null) {
            return gasto.estaEntre(desde, hasta);
        }
        if (desde != null) {
            return !fecha.isBefore(desde);
        }
        return !fecha.isAfter(hasta);
    }

    @Override
    public String toString() {
        return "FiltroIntervaloFechas{" +
            "desde=" + desde +
            ", hasta=" + hasta +
            '}';
    }
}
