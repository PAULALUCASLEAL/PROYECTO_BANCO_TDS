package ASP.BanCroak.filtros;

import ASP.BanCroak.Gasto;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Filtra por categorías. Si la lista está vacía o null, no restringe.
 */
public final class FiltroCategoria extends Filtro {
    private final List<String> categorias;

    public FiltroCategoria(List<String> categorias) {
        if (categorias == null) {
            this.categorias = List.of();
            return;
        }
        List<String> normalizadas = new ArrayList<>();
        for (String c : categorias) {
            String n = normalizarTexto(c);
            if (!n.isEmpty() && !normalizadas.contains(n)) {
                normalizadas.add(n);
            }
        }
        this.categorias = List.copyOf(normalizadas);
    }

    public FiltroCategoria(String categoria) {
        this(categoria == null ? null : List.of(categoria));
    }

    @Override
    public boolean filtrar(Gasto gasto) {
        if (gasto == null) {
            return false;
        }
        if (categorias.isEmpty()) {
            return true;
        }
        String gastoCat = normalizarTexto(gasto.getCategoria());
        for (String categoria : categorias) {
            if (Objects.equals(gastoCat, categoria)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "FiltroCategoria{" +
            "categorias=" + categorias +
            '}';
    }

    private static String normalizarTexto(String texto) {
        if (texto == null) {
            return "";
        }
        String normalized = Normalizer.normalize(texto, Normalizer.Form.NFD);
        String sinAcentos = normalized.replaceAll("\\p{M}", "");
        return sinAcentos.trim().toLowerCase(Locale.ROOT);
    }
}
