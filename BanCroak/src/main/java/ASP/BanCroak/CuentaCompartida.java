package ASP.BanCroak;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CuentaCompartida {

    // ======== ATRIBUTOS ========
    private String nombre;
    private String descripcion;
    private List<Gasto> gastos;

    // ======== CONSTRUCTORES ========

    public CuentaCompartida(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.gastos = new ArrayList<>();
    }

    // Constructor vacío (para frameworks o serialización)
    public CuentaCompartida() {
        this.gastos = new ArrayList<>();
    }

    // ======== GETTERS Y SETTERS ========

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Gasto> getGastos() {
        return gastos;
    }

    public void setGastos(List<Gasto> gastos) {
        this.gastos = gastos;
    }

    // ======== MÉTODOS DE LÓGICA ========

    public void agregarGasto(Gasto gasto) {
        if (gasto != null) {
            gastos.add(gasto);
        }
    }

    public void eliminarGasto(Gasto gasto) {
        gastos.remove(gasto);
    }

    public double calcularTotalGastos() {
        return gastos.stream()
                     .mapToDouble(Gasto::getCantidad)
                     .sum();
    }

    // ======== MÉTODOS AUXILIARES ========

    @Override
    public String toString() {
        return "CuentaCompartida{" +
                "nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", totalGastos=" + calcularTotalGastos() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CuentaCompartida)) return false;
        CuentaCompartida that = (CuentaCompartida) o;
        return Objects.equals(nombre, that.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }
}