package ASP.BanCroak.filtros;

import ASP.BanCroak.domain.Gasto;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Filtra por meses. Acepta "1".."12", "01".."12" o nombres en español
 * (enero, febrero, ...). La comparación ignora mayúsculas, espacios y acentos.
 * Si la lista está vacía o null, no restringe.
 */
public final class FiltroMeses extends Filtro {
    private final List<String> meses;

    public FiltroMeses(List<String> meses) {
        if (meses == null) {
            this.meses = List.of();
            return;
        }
        List<String> normalizados = new ArrayList<>();
        for (String m : meses) {
            String n = normalizarMes(m);
            if (n != null && !normalizados.contains(n)) {
                normalizados.add(n);
            }
        }
        this.meses = List.copyOf(normalizados);
    }

    public FiltroMeses(String mes) {
        this(mes == null ? null : List.of(mes));
    }

    @Override
    public boolean filtrar(Gasto gasto) {
        if (gasto == null) {
            return false;
        }
        if (meses.isEmpty()) {
            return true;
        }
        return gasto.estaEnMeses(meses);
    }

    @Override
    public String toString() {
        return "FiltroMeses{" +
            "meses=" + meses +
            '}';
    }

    private static String normalizarMes(String mes) {
        if (mes == null) {
            return null;
        }
        String m = normalizarTexto(mes);
        if (m.isEmpty()) {
            return null;
        }
        if (m.chars().allMatch(Character::isDigit)) {
            try {
                int value = Integer.parseInt(m);
                if (value >= 1 && value <= 12) {
                    return String.valueOf(value);
                }
            } catch (NumberFormatException ignored) {
                return null;
            }
        }
        switch (m) {
            case "enero":
                return "1";
            case "febrero":
                return "2";
            case "marzo":
                return "3";
            case "abril":
                return "4";
            case "mayo":
                return "5";
            case "junio":
                return "6";
            case "julio":
                return "7";
            case "agosto":
                return "8";
            case "septiembre":
            case "setiembre":
                return "9";
            case "octubre":
                return "10";
            case "noviembre":
                return "11";
            case "diciembre":
                return "12";
            default:
                return null;
        }
    }

    private static String normalizarTexto(String texto) {
        String normalized = Normalizer.normalize(texto, Normalizer.Form.NFD);
        String sinAcentos = normalized.replaceAll("\\p{M}", "");
        return sinAcentos.trim().toLowerCase(Locale.ROOT);
    }
}
