package ASP.BanCroak.domain;

public class AlertaGasto {
    public enum Periodo {
        SEMANAL,
        MENSUAL
    }

    private final int id;
    private final Periodo periodo;
    private final double limite;
    private final String categoria; // normalizada, null o vacia = todas
    private boolean activa;

    public AlertaGasto(int id, Periodo periodo, double limite, String categoria, boolean activa) {
        if (id <= 0) {
            throw new IllegalArgumentException("El id de alerta debe ser positivo");
        }
        if (periodo == null) {
            throw new IllegalArgumentException("El periodo no puede ser null");
        }
        if (limite <= 0) {
            throw new IllegalArgumentException("El limite debe ser mayor que 0");
        }
        this.id = id;
        this.periodo = periodo;
        this.limite = limite;
        this.categoria = categoria == null || categoria.isBlank() ? null : categoria.trim();
        this.activa = activa;
    }

    public int getId() {
        return id;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public double getLimite() {
        return limite;
    }

    public String getCategoria() {
        return categoria;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public String getCategoriaDisplay() {
        return categoria == null || categoria.isBlank() ? "Todas" : categoria;
    }
}
