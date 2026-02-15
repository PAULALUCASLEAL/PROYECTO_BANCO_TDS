package ASP.BanCroak.ui.notificaciones;

import ASP.BanCroak.domain.AlertaGasto;
import ASP.BanCroak.domain.Notificacion;
import ASP.BanCroak.repo.RepositorioAlertas;
import ASP.BanCroak.repo.RepositorioGastos;
import ASP.BanCroak.repo.RepositorioNotificaciones;
import ASP.BanCroak.ui.app.AppContext;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class NotificacionesController {
    private final AppContext context;
    private final RepositorioAlertas repoAlertas;
    private final RepositorioGastos repoGastos;
    private final RepositorioNotificaciones repoNotificaciones;

    public NotificacionesController(AppContext context) {
        this.context = context;
        this.repoAlertas = context.getRepoAlertas();
        this.repoGastos = context.getRepoGastos();
        this.repoNotificaciones = context.getRepoNotificaciones();
    }

    public Set<String> getCategorias() {
        return repoGastos.getCategorias();
    }

    public AlertaGasto crearAlerta(String nombre, AlertaGasto.Periodo periodo, double limite, String categoria, boolean activa) {
        validar(nombre, periodo, limite, categoria);
        String categoriaNorm = normalizarCategoria(categoria);
        AlertaGasto alerta = repoAlertas.crearAlerta(nombre.trim(), periodo, limite, categoriaNorm, activa);
        context.getAlertasPersistence().save(repoAlertas);
        return alerta;
    }

    public List<Notificacion> listarNotificacionesOrdenadas() {
        return repoNotificaciones.listarNotificaciones().stream()
            .sorted(Comparator.comparing(Notificacion::getTimestamp).reversed())
            .collect(Collectors.toList());
    }

    public void marcarLeida(int id, boolean leida) {
        repoNotificaciones.marcarLeida(id, leida);
        context.getNotificacionesPersistence().save(repoNotificaciones);
    }

    public void marcarTodasLeidas() {
        repoNotificaciones.marcarTodasLeidas();
        context.getNotificacionesPersistence().save(repoNotificaciones);
    }

    public void eliminarNotificacion(int id) {
        repoNotificaciones.eliminarNotificacion(id);
        context.getNotificacionesPersistence().save(repoNotificaciones);
    }

    public List<AlertaGasto> listarAlertasOrdenadas() {
        return repoAlertas.listarAlertas().stream()
            .sorted(Comparator.comparingInt(AlertaGasto::getId))
            .collect(Collectors.toList());
    }

    public void actualizarEstadoAlerta(int id, boolean activa) {
        repoAlertas.actualizarEstado(id, activa);
        context.getAlertasPersistence().save(repoAlertas);
    }

    public void eliminarAlerta(int id) {
        repoAlertas.eliminarAlerta(id);
        context.getAlertasPersistence().save(repoAlertas);
    }

    private void validar(String nombre, AlertaGasto.Periodo periodo, double limite, String categoria) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede ser vacío");
        }
        if (periodo == null) {
            throw new IllegalArgumentException("El periodo es obligatorio");
        }
        if (limite <= 0) {
            throw new IllegalArgumentException("El límite debe ser mayor que 0");
        }
        String categoriaNorm = normalizarCategoria(categoria);
        if (categoriaNorm != null && !repoGastos.existeCategoria(categoriaNorm)) {
            throw new IllegalArgumentException("La categoría seleccionada no es válida");
        }
    }

    private String normalizarCategoria(String categoria) {
        if (categoria == null || categoria.isBlank() || "todas".equalsIgnoreCase(categoria)) {
            return null;
        }
        return repoGastos.normalizarCategoriaPublic(categoria);
    }
}
