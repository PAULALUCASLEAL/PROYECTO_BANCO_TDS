package ASP.BanCroak.domain;

import java.time.LocalDateTime;

public class Notificacion {
    private final int id;
    private final LocalDateTime timestamp;
    private final String mensaje;
    private final int alertaId;
    private final String alertaNombre;
    private final AlertaGasto.Periodo periodo;
    private final String periodoKey;
    private final double limite;
    private final double totalDetectado;
    private final String categoria;
    private boolean leida;

    public Notificacion(int id, LocalDateTime timestamp, String mensaje, int alertaId, String alertaNombre,
                        AlertaGasto.Periodo periodo, String periodoKey, double limite,
                        double totalDetectado, String categoria, boolean leida) {
        if (id <= 0) {
            throw new IllegalArgumentException("El id de notificacion debe ser positivo");
        }
        if (timestamp == null) {
            throw new IllegalArgumentException("El timestamp no puede ser null");
        }
        if (mensaje == null || mensaje.isBlank()) {
            throw new IllegalArgumentException("El mensaje no puede ser null o vacio");
        }
        if (periodoKey == null || periodoKey.isBlank()) {
            throw new IllegalArgumentException("El periodo no puede ser null o vacio");
        }
        this.id = id;
        this.timestamp = timestamp;
        this.mensaje = mensaje;
        this.alertaId = alertaId;
        this.alertaNombre = (alertaNombre == null || alertaNombre.isBlank()) ? ("Alerta " + alertaId) : alertaNombre.trim();
        this.periodo = periodo == null ? AlertaGasto.Periodo.MENSUAL : periodo;
        this.periodoKey = periodoKey;
        if (limite < 0) {
            throw new IllegalArgumentException("El limite no puede ser negativo");
        }
        this.limite = limite;
        if (totalDetectado < 0) {
            throw new IllegalArgumentException("El total detectado no puede ser negativo");
        }
        this.totalDetectado = totalDetectado;
        this.categoria = categoria;
        this.leida = leida;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMensaje() {
        return mensaje;
    }

    public int getAlertaId() {
        return alertaId;
    }

    public String getAlertaNombre() {
        return alertaNombre;
    }

    public AlertaGasto.Periodo getPeriodo() {
        return periodo;
    }

    public String getPeriodoKey() {
        return periodoKey;
    }

    public double getLimite() {
        return limite;
    }

    public double getTotalDetectado() {
        return totalDetectado;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getCategoriaDisplay() {
        return categoria == null || categoria.isBlank() ? "todas" : categoria;
    }

    public boolean isLeida() {
        return leida;
    }

    public void setLeida(boolean leida) {
        this.leida = leida;
    }
}
