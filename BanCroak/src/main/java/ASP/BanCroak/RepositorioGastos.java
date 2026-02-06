package ASP.BanCroak;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Repositorio en memoria para gestionar gastos (singleton + CRUD) y categorías.
 * Flujo: primero se crean categorías; luego los gastos solo pueden usar categorías existentes.
 */
public enum RepositorioGastos {
    INSTANCE;

    private final List<Gasto> listaGastos;
    private final Set<String> categorias;
    private int nextId;

    private RepositorioGastos() {
        this.listaGastos = new ArrayList<>();
        this.categorias = new HashSet<>();
        this.nextId = 1;
    }

    public void añadirGasto(Gasto gasto) {
        if (gasto == null) throw new IllegalArgumentException("El gasto no puede ser null");
        String cat = normalizarCategoria(gasto.getCategoria());
        if (cat.isEmpty()) throw new IllegalArgumentException("La categoría no puede estar vacía");
        if (!categorias.contains(cat)) throw new IllegalArgumentException("La categoría no existe: " + cat);

        if (gasto.getID() == 0) {
            gasto.asignarId(nextId++);
        } else if (buscarPorId(gasto.getID()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un gasto con el id " + gasto.getID());
        }
        listaGastos.add(gasto);
    }

    // Busca por id y delega la actualización en la entidad (sin crear categorías nuevas)
    public void editarGasto(int id, double cantidad, LocalDate fecha, String categoria, String pagador) {
        if (id <= 0) throw new IllegalArgumentException("Id de gasto no válido");
        String cat = normalizarCategoria(categoria);
        if (cat.isEmpty()) throw new IllegalArgumentException("La categoría no puede estar vacía");
        if (!categorias.contains(cat)) throw new IllegalArgumentException("La categoría no existe: " + cat);

        for (Gasto g : listaGastos) {
            if (g.getID() == id) {
                g.actualizarGasto(cantidad, fecha, cat, pagador);
                return;
            }
        }
        throw new IllegalArgumentException("No existe un gasto con el id " + id);
    }

    public Optional<Gasto> buscarGasto(Gasto gasto) {
        if (gasto == null) return Optional.empty();
        return buscarPorId(gasto.getID());
    }

    public Optional<Gasto> buscarPorId(int id) {
        if (id <= 0) return Optional.empty();
        return listaGastos.stream().filter(g -> g.getID() == id).findFirst();
    }

    public void eliminarGasto(Gasto gasto) {
        if (gasto == null) throw new IllegalArgumentException("El gasto no puede ser null");
        int id = gasto.getID();
        boolean removed = listaGastos.removeIf(g -> g.getID() == id);
        if (!removed) throw new IllegalArgumentException("No existe un gasto con el id " + id);
    }

    public List<Gasto> getListaGastos() {
        return List.copyOf(listaGastos);
    }

    // --- Categorías (Set, sin duplicados) ---
    public void añadirCategoria(String categoria) {
        String cat = normalizarCategoria(categoria);
        if (cat.isEmpty()) throw new IllegalArgumentException("La categoría no puede estar vacía");
        categorias.add(cat);
    }

    public void eliminarCategoria(String categoria) {
        String cat = normalizarCategoria(categoria);
        if (cat.isEmpty()) throw new IllegalArgumentException("La categoría no puede estar vacía");
        if (!categorias.remove(cat)) throw new IllegalArgumentException("No existe la categoría: " + cat);
    }

    public Set<String> getCategorias() {
        return Set.copyOf(categorias);
    }

    private String normalizarCategoria(String categoria) {
        return categoria == null ? "" : categoria.trim();
    }

    /**
     * Limpia el repositorio y reinicia el contador de ids (útil para tests).
     */
    public void limpiar() {
        listaGastos.clear();
        categorias.clear();
        nextId = 1;
    }
}

