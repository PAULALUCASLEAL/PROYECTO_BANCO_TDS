package ASP.BanCroak;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Repositorio en memoria para gestionar gastos (singleton + CRUD) y categorías.
 */
public final class RepositorioGastos {
    private static RepositorioGastos instancia;

    private final List<Gasto> listaGastos;
    private final List<String> listaCategorias;
    private int nextId;

    private RepositorioGastos() {
        this.listaGastos = new ArrayList<>();
        this.listaCategorias = new ArrayList<>();
        this.nextId = 1;
    }

    public static RepositorioGastos getInstancia() {
        if (instancia == null) instancia = new RepositorioGastos();
        return instancia;
    }

    public void añadirGasto(Gasto gasto) {
        if (gasto == null) throw new IllegalArgumentException("El gasto no puede ser null");
        if (gasto.getID() == 0) {
        	gasto.asignarId(nextId++);
        } else if (buscarPorId(gasto.getID()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un gasto con el id " + gasto.getID());
        }
        listaGastos.add(gasto);
    }

    // Nueva versión: busca por id y delega la actualización en la entidad
    public void editarGasto(int id, double cantidad, LocalDate fecha, String categoria, String pagador) {
        if (id <= 0) throw new IllegalArgumentException("Id de gasto no válido");
        String cat = normalizarCategoria(categoria);
        if (cat.isEmpty()) throw new IllegalArgumentException("La categoría no puede estar vacía");
        if (!listaCategorias.contains(cat)) listaCategorias.add(cat);

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

    public void eliminarGasto(Gasto gasto) {
        if (gasto == null) throw new IllegalArgumentException("El gasto no puede ser null");
        int id = gasto.getID();
        boolean removed = listaGastos.removeIf(g -> g.getID() == id);
        if (!removed) throw new IllegalArgumentException("No existe un gasto con el id " + id);
    }

    public List<Gasto> getListaGastos() {
        return List.copyOf(listaGastos);
    }

    public Optional<Gasto> buscarPorId(int id) {
        if (id <= 0) return Optional.empty();
        return listaGastos.stream().filter(g -> g.getID() == id).findFirst();
    }

    // --- Categorías (sin repositorio aparte) ---
    public void añadirCategoria(String categoria) {
        String cat = normalizarCategoria(categoria);
        if (cat.isEmpty()) throw new IllegalArgumentException("La categoría no puede estar vacía");
        if (!listaCategorias.contains(cat)) listaCategorias.add(cat);
    }

    public void eliminarCategoria(String categoria) {
        String cat = normalizarCategoria(categoria);
        if (cat.isEmpty()) throw new IllegalArgumentException("La categoría no puede estar vacía");
        boolean removed = listaCategorias.remove(cat);
        if (!removed) throw new IllegalArgumentException("No existe la categoría: " + cat);
    }

    public List<String> getListaCategorias() {
        return List.copyOf(listaCategorias);
    }

    private String normalizarCategoria(String categoria) {
        return categoria == null ? "" : categoria.trim();
    }

    /**
     * Limpia el repositorio y reinicia el contador de ids (útil para tests).
     */
    public void limpiar() {
        listaGastos.clear();
        listaCategorias.clear();
        nextId = 1;

    }
}
