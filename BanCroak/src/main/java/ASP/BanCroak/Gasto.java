package ASP.BanCroak;

import java.time.LocalDate;
import java.util.Objects;

public class Gasto {

    // ======== ATRIBUTOS ========
    private double cantidad;
    private LocalDate fecha;
    private String descripcion;
    private Categoria categoria;
    private CuentaCompartida cuentaCompartida; // opcional

    // ======== CONSTRUCTORES ========

    // Constructor básico
    public Gasto(double cantidad, LocalDate fecha, String descripcion, Categoria categoria) {
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.cuentaCompartida = null; // por defecto no pertenece a una cuenta compartida
    }

    // Constructor completo (para gasto compartido)
    public Gasto(double cantidad, LocalDate fecha, String descripcion, Categoria categoria, CuentaCompartida cuentaCompartida) {
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.cuentaCompartida = cuentaCompartida;
    }

    // ======== GETTERS Y SETTERS ========

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public CuentaCompartida getCuentaCompartida() {
        return cuentaCompartida;
    }

    public void setCuentaCompartida(CuentaCompartida cuentaCompartida) {
        this.cuentaCompartida = cuentaCompartida;
    }

    // ======== MÉTODOS AUXILIARES ========

    @Override
    public String toString() {
        String cuenta = (cuentaCompartida != null) ? cuentaCompartida.getNombre() : "Personal";
        return String.format("Gasto[cantidad=%.2f, fecha=%s, descripcion=%s, categoria=%s, cuenta=%s]",
                cantidad, fecha, descripcion, categoria.getNombre(), cuenta);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Gasto)) return false;
        Gasto gasto = (Gasto) o;
        return Double.compare(gasto.cantidad, cantidad) == 0 &&
                Objects.equals(fecha, gasto.fecha) &&
                Objects.equals(descripcion, gasto.descripcion) &&
                Objects.equals(categoria, gasto.categoria) &&
                Objects.equals(cuentaCompartida, gasto.cuentaCompartida);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cantidad, fecha, descripcion, categoria, cuentaCompartida);
    }
}