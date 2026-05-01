package ASP.BanCroak.domain;

import java.text.Normalizer;
import java.util.Locale;


//Creamos aquí la alerta y cuando se crea un gasto, el sistema debería hacer lo siguiente: mira todas las alertas que hay activas, dependiendo si la 
//alerta es mensual o semanal pues lo que hace es sumar todos los gastos mensuales o semanales y filtra por categoría (opcional) y suma pues solo las que toquen.
//Después se compara con el límite, si se supera, crea un aviso (que es la clase notificación)
public class AlertaGasto {
    public enum Periodo {
        SEMANAL,
        MENSUAL
    }

    private final int id;
    private final String nombre;
    private final Periodo periodo;
    private final double limite;
    private final String categoria; // normalizada, null o vacia = todas
    private boolean activa;

    public AlertaGasto(int id, String nombre, Periodo periodo, double limite, String categoria, boolean activa) {
        if (id <= 0) {
            throw new IllegalArgumentException("El id de alerta debe ser positivo");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de alerta no puede ser null o vacio");
        }
        if (periodo == null) {
            throw new IllegalArgumentException("El periodo no puede ser null");
        }
        if (limite <= 0) {
            throw new IllegalArgumentException("El limite debe ser mayor que 0");
        }
        this.id = id;
        this.nombre = nombre.trim();
        this.periodo = periodo;
        this.limite = limite;
        this.categoria = normalizarCategoria(categoria);
        this.activa = activa;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
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

    private String normalizarCategoria(String categoria) {
        if (categoria == null || categoria.isBlank()) {
            return null;
        }
        String normalized = Normalizer.normalize(categoria, Normalizer.Form.NFD);
        String sinAcentos = normalized.replaceAll("\\p{M}", "");
        String result = sinAcentos.trim().toLowerCase(Locale.ROOT);
        return result.isBlank() ? null : result;
    }
}
