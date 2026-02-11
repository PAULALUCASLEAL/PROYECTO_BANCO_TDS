package ASP.BanCroak.domain;

import java.time.LocalDateTime;

public class Notificacion {
    private final int id;
    private final LocalDateTime timestamp;
    private final String mensaje;
    private final int alertaId;
    private final String periodoKey;
    private final double totalDetectado;
    private final String categoria;
    private boolean leida;

    public Notificacion(int id, LocalDateTime timestamp, String mensaje, int alertaId, String periodoKey,
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
        this.periodoKey = periodoKey;
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

    public String getPeriodoKey() {
        return periodoKey;
    }

    public double getTotalDetectado() {
        return totalDetectado;
    }

    public String getCategoria() {
        return categoria;
    }

    public boolean isLeida() {
        return leida;
    }

    public void setLeida(boolean leida) {
        this.leida = leida;
    }
}
