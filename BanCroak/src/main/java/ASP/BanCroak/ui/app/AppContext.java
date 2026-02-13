package ASP.BanCroak.ui.app;

import ASP.BanCroak.domain.Cuenta;
import ASP.BanCroak.persistence.AlertasPersistence;
import ASP.BanCroak.persistence.CuentasPersistence;
import ASP.BanCroak.persistence.GastosPersistence;
import ASP.BanCroak.persistence.NotificacionesPersistence;
import ASP.BanCroak.repo.RepositorioAlertas;
import ASP.BanCroak.repo.RepositorioCuentas;
import ASP.BanCroak.repo.RepositorioGastos;
import ASP.BanCroak.repo.RepositorioNotificaciones;

import java.nio.file.Path;
import java.util.Optional;

public class AppContext {
    private static final String NOMBRE_CUENTA_PERSONAL = "Mi Cuenta";

    private final RepositorioGastos repoGastos;
    private final RepositorioCuentas repoCuentas;
    private final RepositorioAlertas repoAlertas;
    private final RepositorioNotificaciones repoNotificaciones;
    private final GastosPersistence gastosPersistence;
    private final CuentasPersistence cuentasPersistence;
    private final AlertasPersistence alertasPersistence;
    private final NotificacionesPersistence notificacionesPersistence;
    private SceneManager navigator;
    private int cuentaActivaId;

    public AppContext() {
        this.repoGastos = RepositorioGastos.INSTANCE;
        this.repoCuentas = RepositorioCuentas.INSTANCE;
        this.repoAlertas = RepositorioAlertas.INSTANCE;
        this.repoNotificaciones = RepositorioNotificaciones.INSTANCE;
        this.gastosPersistence = new GastosPersistence(Path.of("data", "gastos.json"));
        this.cuentasPersistence = new CuentasPersistence(Path.of("data", "cuentas.json"));
        this.alertasPersistence = new AlertasPersistence(Path.of("data", "alertas.json"));
        this.notificacionesPersistence = new NotificacionesPersistence(Path.of("data", "notificaciones.json"));

        cuentasPersistence.load(repoCuentas, NOMBRE_CUENTA_PERSONAL);
        gastosPersistence.load(repoGastos);
        alertasPersistence.load(repoAlertas);
        notificacionesPersistence.load(repoNotificaciones);

        Cuenta personal = getCuentaPersonal().orElse(null);
        this.cuentaActivaId = personal == null ? 0 : personal.getIdCuenta();
    }

    public RepositorioGastos getRepoGastos() {
        return repoGastos;
    }

    public RepositorioCuentas getRepoCuentas() {
        return repoCuentas;
    }

    public RepositorioAlertas getRepoAlertas() {
        return repoAlertas;
    }

    public RepositorioNotificaciones getRepoNotificaciones() {
        return repoNotificaciones;
    }

    public GastosPersistence getGastosPersistence() {
        return gastosPersistence;
    }

    public CuentasPersistence getCuentasPersistence() {
        return cuentasPersistence;
    }

    public AlertasPersistence getAlertasPersistence() {
        return alertasPersistence;
    }

    public NotificacionesPersistence getNotificacionesPersistence() {
        return notificacionesPersistence;
    }

    public SceneManager getNavigator() {
        return navigator;
    }

    public void setNavigator(SceneManager navigator) {
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
