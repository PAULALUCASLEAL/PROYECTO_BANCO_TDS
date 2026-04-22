package ASP.BanCroak.application;

import ASP.BanCroak.domain.Cuenta;
import ASP.BanCroak.domain.Gasto;
import ASP.BanCroak.persistence.GastosPersistence;
import ASP.BanCroak.repo.RepositorioCuentas;
import ASP.BanCroak.repo.RepositorioGastos;

import java.time.LocalDate;

public class RegistrarGastoUseCase {
    private final RepositorioGastos repoGastos;
    private final RepositorioCuentas repoCuentas;
    private final GastosPersistence gastosPersistence;

    public RegistrarGastoUseCase(RepositorioGastos repoGastos, GastosPersistence gastosPersistence) {
        this(repoGastos, null, gastosPersistence);
    }

    public RegistrarGastoUseCase(
        RepositorioGastos repoGastos,
        RepositorioCuentas repoCuentas,
        GastosPersistence gastosPersistence
    ) {
        this.repoGastos = repoGastos;
        this.repoCuentas = repoCuentas;
        this.gastosPersistence = gastosPersistence;
    }

    public Gasto ejecutar(double cantidad, LocalDate fecha, String categoria, String pagador, String nombreCuenta) {
        Cuenta cuenta = buscarCuentaPorNombre(nombreCuenta);
        Gasto gasto = Gasto.crearGasto(cantidad, fecha, categoria, pagador, cuenta.getIdCuenta());
        return ejecutar(gasto);
    }

    public Gasto ejecutarEnCuentaPersonal(double cantidad, LocalDate fecha, String categoria) {
        Cuenta cuenta = buscarCuentaPersonal();
        Gasto gasto = Gasto.crearGasto(
            cantidad,
            fecha,
            categoria,
            cuenta.getNombreCuenta(),
            cuenta.getIdCuenta()
        );
        return ejecutar(gasto);
    }

    public Gasto ejecutar(Gasto gasto) {
        repoGastos.añadirGasto(gasto);
        gastosPersistence.save(repoGastos);
        return gasto;
    }

    private Cuenta buscarCuentaPorNombre(String nombreCuenta) {
        if (repoCuentas == null) {
            throw new IllegalStateException("No hay repositorio de cuentas configurado");
        }
        return repoCuentas.listarCuentas().stream()
            .filter(c -> c.getNombreCuenta().equals(nombreCuenta))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No existe la cuenta: " + nombreCuenta));
    }

    private Cuenta buscarCuentaPersonal() {
        if (repoCuentas == null) {
            throw new IllegalStateException("No hay repositorio de cuentas configurado");
        }
        return repoCuentas.listarCuentas().stream()
            .filter(Cuenta::esPersonal)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No existe una cuenta personal para registrar el gasto"));
    }
}
