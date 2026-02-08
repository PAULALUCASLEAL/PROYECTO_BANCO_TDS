package ASP.BanCroak;

import java.util.Objects;

public final class Persona {
    private final int id;
    private final String nombre;

    public Persona(int id, String nombre) {
        if (id <= 0) {
            throw new IllegalArgumentException("El id debe ser positivo");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede ser null o vacio");
        }
        this.id = id;
        this.nombre = nombre.trim();
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Persona)) return false;
        Persona persona = (Persona) o;
        return id == persona.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "Persona{" +
            "id=" + id +
            ", nombre='" + nombre + '\'' +
            '}';
    }
}
