package ASP.BanCroak;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public enum RepositorioCuentas {
    INSTANCE;

    private final List<Cuenta> cuentas;
    private int nextIdCuenta;

    private RepositorioCuentas() {
        this.cuentas = new ArrayList<>();
        this.nextIdCuenta = 1;
    }

    public Cuenta crearCuentaConPartesIguales(String nombreCuenta, List<String> miembros) {
        Cuenta cuenta = Cuenta.crearConPartesIguales(nextIdCuenta, nombreCuenta, miembros);
        añadirCuenta(cuenta);
        return cuenta;
    }

    public Cuenta crearCuentaConPorcentajes(String nombreCuenta, List<String> miembros, java.util.Map<String, Double> porcentajes) {
        Cuenta cuenta = Cuenta.crearConPorcentajes(nextIdCuenta, nombreCuenta, miembros, porcentajes);
        añadirCuenta(cuenta);
        return cuenta;
    }

    public void añadirCuenta(Cuenta cuenta) {
        if (cuenta == null) {
            throw new IllegalArgumentException("La cuenta no puede ser null");
        }
        if (cuenta.getIdCuenta() == 0) {
            throw new IllegalArgumentException("La cuenta debe tener id asignado");
        }
        if (buscarPorId(cuenta.getIdCuenta()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una cuenta con el id " + cuenta.getIdCuenta());
        }
        cuentas.add(cuenta);
        if (cuenta.getIdCuenta() >= nextIdCuenta) {
            nextIdCuenta = cuenta.getIdCuenta() + 1;
        }
    }

    public Optional<Cuenta> buscarPorId(int idCuenta) {
        if (idCuenta <= 0) return Optional.empty();
        return cuentas.stream().filter(c -> c.getIdCuenta() == idCuenta).findFirst();
    }

    public List<Cuenta> listarCuentas() {
        return List.copyOf(cuentas);
    }

    public void limpiar() {
        cuentas.clear();
        nextIdCuenta = 1;
    }
}
