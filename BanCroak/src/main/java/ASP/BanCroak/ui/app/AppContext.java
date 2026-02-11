package ASP.BanCroak.ui.app;

import ASP.BanCroak.domain.Cuenta;
import ASP.BanCroak.persistence.CuentasPersistence;
import ASP.BanCroak.persistence.GastosPersistence;
import ASP.BanCroak.repo.RepositorioCuentas;
import ASP.BanCroak.repo.RepositorioGastos;

import java.nio.file.Path;
import java.util.Optional;

public class AppContext {
    private static final String NOMBRE_CUENTA_PERSONAL = "Mi Cuenta";

    private final RepositorioGastos repoGastos;
    private final RepositorioCuentas repoCuentas;
    private final GastosPersistence gastosPersistence;
    private final CuentasPersistence cuentasPersistence;
    private Navigator navigator;
    private int cuentaActivaId;

    public AppContext() {
        this.repoGastos = RepositorioGastos.INSTANCE;
        this.repoCuentas = RepositorioCuentas.INSTANCE;
        this.gastosPersistence = new GastosPersistence(Path.of("data", "gastos.json"));
        this.cuentasPersistence = new CuentasPersistence(Path.of("data", "cuentas.json"));

        cuentasPersistence.load(repoCuentas, NOMBRE_CUENTA_PERSONAL);
        gastosPersistence.load(repoGastos);

        Cuenta personal = getCuentaPersonal().orElse(null);
        this.cuentaActivaId = personal == null ? 0 : personal.getIdCuenta();
    }

    public RepositorioGastos getRepoGastos() {
        return repoGastos;
    }

    public RepositorioCuentas getRepoCuentas() {
        return repoCuentas;
    }

    public GastosPersistence getGastosPersistence() {
        return gastosPersistence;
    }

    public CuentasPersistence getCuentasPersistence() {
        return cuentasPersistence;
    }

    public Navigator getNavigator() {
        return navigator;
    }

    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }

    public int getCuentaActivaId() {
        return cuentaActivaId;
    }

    public void setCuentaActivaId(int cuentaActivaId) {
        this.cuentaActivaId = cuentaActivaId;
    }

    public Optional<Cuenta> getCuentaPersonal() {
        return repoCuentas.listarCuentas().stream().filter(Cuenta::esPersonal).findFirst();
    }

    public Optional<Cuenta> getCuentaActiva() {
        if (cuentaActivaId <= 0) {
            return Optional.empty();
        }
        return repoCuentas.buscarPorId(cuentaActivaId);
    }
}
