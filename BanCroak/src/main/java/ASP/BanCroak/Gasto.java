package ASP.BanCroak;

import java.time.LocalDate;
import java.util.Objects;

public class Gasto {

    // ======== ATRIBUTOS ========
    private double cantidad;
    private LocalDate fecha;
    private String descripcion;
    private Categoria categoria;
    private Cuenta cuenta; // opcional

    // ======== CONSTRUCTORES ========

    // Constructor básico
    public Gasto(double cantidad, LocalDate fecha, String descripcion, Categoria categoria) {
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.cuenta = null; // por defecto no pertenece a una cuenta compartida
    }

    // Constructor completo (para gasto compartido)
    public Gasto(double cantidad, LocalDate fecha, String descripcion, Categoria categoria, Cuenta cuenta) {
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.cuenta = cuenta;
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

    public Cuenta getCuentaCompartida() {
        return cuenta;
    }

    public void setCuentaCompartida(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    // ======== MÉTODOS AUXILIARES ========

    @Override
    public String toString() {
        String cuenta = (this.cuenta != null) ? this.cuenta.getNombre() : "Personal";
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
                Objects.equals(cuenta, gasto.cuenta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cantidad, fecha, descripcion, categoria, cuenta);
    }
}