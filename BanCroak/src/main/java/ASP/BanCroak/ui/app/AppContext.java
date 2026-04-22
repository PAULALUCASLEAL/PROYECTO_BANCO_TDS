package ASP.BanCroak.ui.app;

import ASP.BanCroak.application.BorrarGastoUseCase;
import ASP.BanCroak.application.ModificarGastoUseCase;
import ASP.BanCroak.application.RegistrarGastoUseCase;
import ASP.BanCroak.domain.Cuenta;
import ASP.BanCroak.persistence.AlertasPersistence;
import ASP.BanCroak.persistence.CuentasPersistence;
import ASP.BanCroak.persistence.GastosPersistence;
import ASP.BanCroak.persistence.NotificacionesPersistence;
import ASP.BanCroak.repo.RepositorioAlertas;
import ASP.BanCroak.repo.RepositorioCuentas;
import ASP.BanCroak.repo.RepositorioGastos;
import ASP.BanCroak.repo.RepositorioNotificaciones;
import ASP.BanCroak.service.AlertaService;
import ASP.BanCroak.service.FilterState;
import ASP.BanCroak.domain.Notificacion;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
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
    private final RegistrarGastoUseCase registrarGastoUseCase;
    private final ModificarGastoUseCase modificarGastoUseCase;
    private final BorrarGastoUseCase borrarGastoUseCase;
    private final GastosStore gastosStore;
    private final FilterState filterState;
    private SceneManager navigator;
    private int cuentaActivaId;

    public AppContext() {
        this.repoGastos = RepositorioGastos.INSTANCE;
        this.repoCuentas = RepositorioCuentas.INSTANCE;
        this.repoAlertas = RepositorioAlertas.INSTANCE;
        this.repoNotificaciones = RepositorioNotificaciones.INSTANCE;
        this.gastosPersistence = new GastosPersistence(dataFile("gastos.json"));
        this.cuentasPersistence = new CuentasPersistence(dataFile("cuentas.json"));
        this.alertasPersistence = new AlertasPersistence(dataFile("alertas.json"));
        this.notificacionesPersistence = new NotificacionesPersistence(dataFile("notificaciones.json"));

        cuentasPersistence.load(repoCuentas, NOMBRE_CUENTA_PERSONAL);
        gastosPersistence.load(repoGastos);
        alertasPersistence.load(repoAlertas);
        notificacionesPersistence.load(repoNotificaciones);

        this.registrarGastoUseCase = new RegistrarGastoUseCase(repoGastos, repoCuentas, gastosPersistence);
        this.modificarGastoUseCase = new ModificarGastoUseCase(repoGastos, gastosPersistence);
        this.borrarGastoUseCase = new BorrarGastoUseCase(repoGastos, gastosPersistence);
        this.gastosStore = new GastosStore(
            repoGastos,
            registrarGastoUseCase,
            modificarGastoUseCase,
            borrarGastoUseCase
        );
        this.filterState = new FilterState();

        Cuenta personal = getCuentaPersonal().orElse(null);
        this.cuentaActivaId = personal == null ? 0 : personal.getIdCuenta();
    }

    private Path dataFile(String fileName) {
        Path localData = Path.of("data", fileName);
        Path repoRootData = Path.of("BanCroak", "data", fileName);
        if (!Files.exists(localData.getParent()) && Files.exists(repoRootData.getParent())) {
            return repoRootData;
        }
        return localData;
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

    public GastosStore getGastosStore() {
        return gastosStore;
    }

    public RegistrarGastoUseCase getRegistrarGastoUseCase() {
        return registrarGastoUseCase;
    }

    public ModificarGastoUseCase getModificarGastoUseCase() {
        return modificarGastoUseCase;
    }

    public BorrarGastoUseCase getBorrarGastoUseCase() {
        return borrarGastoUseCase;
    }

    public FilterState getFilterState() {
        return filterState;
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

    public List<Notificacion> evaluarAlertasYNotificar() {
        return evaluarAlertasYNotificar(cuentaActivaId);
    }

    public List<Notificacion> evaluarAlertasYNotificar(int cuentaId) {
        if (cuentaId <= 0) {
            return List.of();
        }
        AlertaService service = new AlertaService();
        List<Notificacion> nuevas = service.evaluarYNotificar(cuentaId, repoGastos, repoAlertas, repoNotificaciones);
        if (!nuevas.isEmpty()) {
            notificacionesPersistence.save(repoNotificaciones);
            if (navigator != null) {
                for (Notificacion n : nuevas) {
                    navigator.showNotificacion(n.getMensaje());
                }
            }
        }
        return nuevas;
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
