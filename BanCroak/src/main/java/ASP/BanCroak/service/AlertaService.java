package ASP.BanCroak.service;

import ASP.BanCroak.domain.AlertaGasto;
import ASP.BanCroak.domain.Gasto;
import ASP.BanCroak.domain.Notificacion;
import ASP.BanCroak.repo.RepositorioAlertas;
import ASP.BanCroak.repo.RepositorioGastos;
import ASP.BanCroak.repo.RepositorioNotificaciones;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AlertaService {

    public List<Notificacion> evaluarYNotificar(int cuentaId, RepositorioGastos repoGastos,
                                                RepositorioAlertas repoAlertas,
                                                RepositorioNotificaciones repoNotificaciones) {
        if (cuentaId <= 0) {
            return List.of();
        }
        LocalDate hoy = LocalDate.now();
        List<Notificacion> nuevas = new ArrayList<>();

        for (AlertaGasto alerta : repoAlertas.listarAlertas()) {
            if (!alerta.isActiva()) {
                continue;
            }
            String periodoKey = calcularPeriodoKey(alerta.getPeriodo(), hoy);
            if (repoNotificaciones.existeNotificacion(alerta.getId(), periodoKey)) {
                continue;
            }
            double total = calcularTotal(alerta, repoGastos, cuentaId, hoy);
            if (total > alerta.getLimite()) {
                String mensaje = construirMensaje(alerta, total, periodoKey);
                Notificacion n = repoNotificaciones.crearNotificacion(
                    mensaje,
                    alerta.getId(),
                    periodoKey,
                    total,
                    alerta.getCategoria()
                );
                nuevas.add(n);
            }
        }
        return nuevas;
    }

    private double calcularTotal(AlertaGasto alerta, RepositorioGastos repoGastos, int cuentaId, LocalDate hoy) {
        String categoriaObjetivo = alerta.getCategoria();
        double total = 0.0;
        for (Gasto g : repoGastos.getListaGastos()) {
            if (g.getIDCuenta() != cuentaId) {
                continue;
            }
            if (g.getFecha() == null) {
                continue;
            }
            if (!enPeriodo(g.getFecha(), hoy, alerta.getPeriodo())) {
                continue;
            }
            if (categoriaObjetivo != null && !categoriaObjetivo.isBlank()) {
                String cat = repoGastos.normalizarCategoriaPublic(g.getCategoria());
                if (!categoriaObjetivo.equals(cat)) {
                    continue;
                }
            }
            total += g.getCantidad();
        }
        return total;
    }

    private boolean enPeriodo(LocalDate fecha, LocalDate hoy, AlertaGasto.Periodo periodo) {
        if (periodo == AlertaGasto.Periodo.MENSUAL) {
            return fecha.getYear() == hoy.getYear() && fecha.getMonth() == hoy.getMonth();
        }
        WeekFields wf = WeekFields.ISO;
        int weekFecha = fecha.get(wf.weekOfWeekBasedYear());
        int yearFecha = fecha.get(wf.weekBasedYear());
        int weekHoy = hoy.get(wf.weekOfWeekBasedYear());
        int yearHoy = hoy.get(wf.weekBasedYear());
        return weekFecha == weekHoy && yearFecha == yearHoy;
    }

    private String calcularPeriodoKey(AlertaGasto.Periodo periodo, LocalDate hoy) {
        if (periodo == AlertaGasto.Periodo.MENSUAL) {
            return String.format("MES-%d-%02d", hoy.getYear(), hoy.getMonthValue());
        }
        WeekFields wf = WeekFields.ISO;
        int week = hoy.get(wf.weekOfWeekBasedYear());
        int year = hoy.get(wf.weekBasedYear());
        return String.format("SEM-%d-%02d", year, week);
    }

    private String construirMensaje(AlertaGasto alerta, double total, String periodoKey) {
        String categoria = alerta.getCategoria() == null || alerta.getCategoria().isBlank() ? "todas las categorías" : "la categoría '" + alerta.getCategoria() + "'";
        return String.format(Locale.ROOT,
            "Alerta %s: superado el límite de %.2f € en %s. Total: %.2f € (%s)",
            alerta.getPeriodo().name().toLowerCase(Locale.ROOT),
            alerta.getLimite(),
            periodoKey,
            total,
            categoria
        );
    }
}
