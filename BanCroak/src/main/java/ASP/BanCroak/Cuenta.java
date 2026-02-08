package ASP.BanCroak;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class Cuenta {
    private static final double TOLERANCIA_SUMA = 0.01;

    private final int idCuenta;
    private final String nombreCuenta;
    private final List<Persona> miembros;
    private final Map<Persona, Double> porcentajes;

    private Cuenta(int idCuenta, String nombreCuenta, List<Persona> miembros, Map<Persona, Double> porcentajes) {
        validarId(idCuenta);
        validarNombre(nombreCuenta);
        List<Persona> miembrosValidados = validarYNormalizarMiembros(miembros);
        Map<Persona, Double> porcentajesValidados = validarYNormalizarPorcentajes(miembrosValidados, porcentajes);

        this.idCuenta = idCuenta;
        this.nombreCuenta = nombreCuenta.trim();
        this.miembros = List.copyOf(miembrosValidados);
        this.porcentajes = Map.copyOf(porcentajesValidados);
    }

    public static Cuenta crearConPartesIguales(int idCuenta, String nombreCuenta, List<Persona> miembros) {
        List<Persona> miembrosValidados = validarYNormalizarMiembros(miembros);
        Map<Persona, Double> reparto = repartoIgual(miembrosValidados);
        return new Cuenta(idCuenta, nombreCuenta, miembrosValidados, reparto);
    }

    public static Cuenta crearConPorcentajes(int idCuenta, String nombreCuenta, List<Persona> miembros, Map<Persona, Double> porcentajes) {
        return new Cuenta(idCuenta, nombreCuenta, miembros, porcentajes);
    }

    public int getIdCuenta() {
        return idCuenta;
    }

    public String getNombreCuenta() {
        return nombreCuenta;
    }

    public List<Persona> getMiembros() {
        return List.copyOf(miembros);
    }

    public Map<Persona, Double> getPorcentajes() {
        return Map.copyOf(porcentajes);
    }

    public boolean esPersonal() {
        return miembros.size() == 1;
    }

    public Map<Persona, Double> calcularReparto(double total) {
        if (total < 0) {
            throw new IllegalArgumentException("El total no puede ser negativo");
        }
        if (miembros.isEmpty()) {
            return Map.of();
        }

        // Redondeo a 2 decimales y residuo al primer miembro para cuadrar el total.
        List<Persona> orden = ordenDeterminista();
        Map<Persona, Double> reparto = new LinkedHashMap<>();
        double suma = 0.0;
        for (Persona miembro : orden) {
            double porcentaje = porcentajes.get(miembro);
            double parte = redondear2(total * (porcentaje / 100.0));
            reparto.put(miembro, parte);
            suma += parte;
        }
        double residuo = redondear2(total - suma);
        if (!orden.isEmpty() && Math.abs(residuo) > 0.0) {
            Persona primero = orden.get(0);
            reparto.put(primero, redondear2(reparto.get(primero) + residuo));
        }
        return Map.copyOf(reparto);
    }

    public List<Gasto> gastosDeCuenta(RepositorioGastos repo) {
        if (repo == null) {
            throw new IllegalArgumentException("El repositorio no puede ser null");
        }
        return repo.getListaGastos().stream()
            .filter(g -> g.getIDCuenta() == idCuenta)
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cuenta)) return false;
        Cuenta cuenta = (Cuenta) o;
        return idCuenta == cuenta.idCuenta;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idCuenta);
    }

    @Override
    public String toString() {
        return "Cuenta{" +
            "idCuenta=" + idCuenta +
            ", nombreCuenta='" + nombreCuenta + '\'' +
            ", miembros=" + miembros +
            ", porcentajes=" + porcentajes +
            '}';
    }

    private static void validarId(int idCuenta) {
        if (idCuenta <= 0) {
            throw new IllegalArgumentException("El idCuenta debe ser positivo");
        }
    }

    private static void validarNombre(String nombreCuenta) {
        if (nombreCuenta == null || nombreCuenta.isBlank()) {
            throw new IllegalArgumentException("El nombreCuenta no puede ser null o vacio");
        }
    }

    private static List<Persona> validarYNormalizarMiembros(List<Persona> miembros) {
        if (miembros == null || miembros.isEmpty()) {
            throw new IllegalArgumentException("La lista de miembros no puede ser null o vacia");
        }
        List<Persona> copia = new ArrayList<>();
        for (Persona p : miembros) {
            if (p == null) {
                throw new IllegalArgumentException("No se permiten miembros null");
            }
            copia.add(p);
        }
        long distintos = copia.stream().distinct().count();
        if (distintos != copia.size()) {
            throw new IllegalArgumentException("No se permiten miembros duplicados");
        }
        return copia;
    }

    private static Map<Persona, Double> validarYNormalizarPorcentajes(List<Persona> miembros, Map<Persona, Double> porcentajes) {
        if (porcentajes == null || porcentajes.isEmpty()) {
            throw new IllegalArgumentException("El mapa de porcentajes no puede ser null o vacio");
        }
        if (porcentajes.size() != miembros.size()) {
            throw new IllegalArgumentException("El mapa de porcentajes debe tener los mismos miembros");
        }
        for (Persona miembro : miembros) {
            if (!porcentajes.containsKey(miembro)) {
                throw new IllegalArgumentException("Falta porcentaje para el miembro: " + miembro);
            }
        }
        double suma = 0.0;
        for (Map.Entry<Persona, Double> entry : porcentajes.entrySet()) {
            Persona miembro = entry.getKey();
            Double porcentaje = entry.getValue();
            if (miembro == null) {
                throw new IllegalArgumentException("No se permiten miembros null en porcentajes");
            }
            if (porcentaje == null) {
                throw new IllegalArgumentException("El porcentaje no puede ser null");
            }
            if (porcentaje < 0) {
                throw new IllegalArgumentException("No se permiten porcentajes negativos");
            }
            suma += porcentaje;
        }
        if (Math.abs(100.0 - suma) > TOLERANCIA_SUMA) {
            throw new IllegalArgumentException("La suma de porcentajes debe ser 100");
        }
        return new LinkedHashMap<>(porcentajes);
    }

    private static Map<Persona, Double> repartoIgual(List<Persona> miembros) {
        Map<Persona, Double> reparto = new LinkedHashMap<>();
        double porcentaje = 100.0 / miembros.size();
        double suma = 0.0;
        for (Persona miembro : miembros) {
            reparto.put(miembro, porcentaje);
            suma += porcentaje;
        }
        double residuo = 100.0 - suma;
        if (Math.abs(residuo) > 0.0) {
            Persona primero = miembros.get(0);
            reparto.put(primero, reparto.get(primero) + residuo);
        }
        return reparto;
    }

    private static double redondear2(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    private List<Persona> ordenDeterminista() {
        return miembros.stream()
            .sorted(Comparator.comparingInt(Persona::getId))
            .collect(Collectors.toList());
    }
}
