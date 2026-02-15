package ASP.BanCroak.service;

import ASP.BanCroak.domain.Gasto;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class GastosQueryService {
    private GastosQueryService() {
    }

    public static List<Gasto> aplicarFiltros(List<Gasto> gastos, FilterState state) {
        List<Gasto> salida = new ArrayList<>();
        if (gastos == null || gastos.isEmpty()) {
            return salida;
        }
        LocalDate desde = state.getDesde();
        LocalDate hasta = state.getHasta();
        YearMonth mes = state.getMes();
        String categoria = normalizar(state.getCategoria());
        for (Gasto g : gastos) {
            if (!pasaCategoria(categoria, g)) {
                continue;
            }
            if (!pasaMes(mes, g)) {
                continue;
            }
            if (!pasaFechas(desde, hasta, g)) {
                continue;
            }
            salida.add(g);
        }
        return salida;
    }

    public static Map<String, Double> totalPorCategoria(List<Gasto> gastos) {
        Map<String, Double> totales = new HashMap<>();
        if (gastos == null) {
            return totales;
        }
        for (Gasto g : gastos) {
            String cat = g.getCategoria();
            totales.put(cat, totales.getOrDefault(cat, 0.0) + g.getCantidad());
        }
        return totales;
    }

    public static Map<LocalDate, DayAggregate> totalPorDia(List<Gasto> gastos) {
        Map<LocalDate, DayAggregate> totales = new HashMap<>();
        if (gastos == null) {
            return totales;
        }
        for (Gasto g : gastos) {
            LocalDate fecha = g.getFecha();
            if (fecha == null) {
                continue;
            }
            DayAggregate agg = totales.computeIfAbsent(fecha, f -> new DayAggregate());
            agg.total += g.getCantidad();
            agg.count += 1;
        }
        return totales;
    }

    private static boolean pasaCategoria(String categoria, Gasto g) {
        if (categoria == null || categoria.isBlank()) {
            return true;
        }
        String cat = normalizar(g.getCategoria());
        return cat.equals(normalizar(categoria));
    }

    private static boolean pasaMes(YearMonth mes, Gasto g) {
        if (mes == null) {
            return true;
        }
        LocalDate fecha = g.getFecha();
        if (fecha == null) {
            return false;
        }
        return YearMonth.from(fecha).equals(mes);
    }

    private static boolean pasaFechas(LocalDate desde, LocalDate hasta, Gasto g) {
        if (desde == null && hasta == null) {
            return true;
        }
        LocalDate fecha = g.getFecha();
        if (fecha == null) {
            return false;
        }
        if (desde != null && fecha.isBefore(desde)) {
            return false;
        }
        if (hasta != null && fecha.isAfter(hasta)) {
            return false;
        }
        return true;
    }

    private static String normalizar(String texto) {
        if (texto == null) {
            return "";
        }
        String normalized = Normalizer.normalize(texto, Normalizer.Form.NFD);
        String sinAcentos = normalized.replaceAll("\\p{M}", "");
        return sinAcentos.trim().toLowerCase(Locale.ROOT);
    }

    public static final class DayAggregate {
        public double total;
        public int count;
    }
}
