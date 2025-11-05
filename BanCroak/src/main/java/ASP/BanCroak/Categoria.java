package ASP.BanCroak;

import java.util.Objects;

public class Categoria {

    // ======== ATRIBUTOS ========
    private String nombre;

    // ======== CONSTRUCTORES ========

    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    // Constructor vacío (para frameworks o serialización)
    public Categoria() {}

    // ======== GETTERS Y SETTERS ========

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // ======== MÉTODOS AUXILIARES ========

    @Override
    public String toString() {
        return "Categoria{" +
                "nombre='" + nombre + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Categoria)) return false;
        Categoria that = (Categoria) o;
        return Objects.equals(nombre, that.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }
}