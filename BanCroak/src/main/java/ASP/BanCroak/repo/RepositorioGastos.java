package ASP.BanCroak.repo;

import ASP.BanCroak.domain.Gasto;
import ASP.BanCroak.filtros.Filtro;

import java.text.Normalizer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Repositorio en memoria para gestionar gastos y categorías.
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
        String categoria = normalizarCategoria(gasto.getCategoria());
        if (categoria.isEmpty()) throw new IllegalArgumentException("La categoría no puede estar vacía");
        if (!categorias.contains(categoria)) throw new IllegalArgumentException("La categoría '"+categoria+"' no existe");

        // Normaliza la categoría almacenada en el gasto para mantener consistencia.
        gasto.actualizarGasto(gasto.getCantidad(), gasto.getFecha(), categoria, gasto.getPagador());

        if (gasto.getID() == 0) {
            gasto.asignarId(nextId++);
        } else if (buscarPorId(gasto.getID()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un gasto con el id " + gasto.getID());
        } else if (gasto.getID() >= nextId) {
            nextId = gasto.getID() + 1;
        }
        listaGastos.add(gasto);
    }

    // Busca por id y delega la actualización en la entidad (sin crear categorías nuevas)
    public void editarGasto(int id, double cantidad, LocalDate fecha, String categoria, String pagador) {
        if (id <= 0) throw new IllegalArgumentException("Id de gasto no válido");
        String cat = normalizarCategoria(categoria);
        if (cat.isEmpty()) throw new IllegalArgumentException("La categoría no puede estar vacía");
        if (!categorias.contains(cat)) throw new IllegalArgumentException("La categoría no existe");

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
        //devolvemos una copia de la lista para que nadie la pueda modificar
        return List.copyOf(listaGastos);
    }

    public List<Gasto> filtrar(Filtro filtro) {
        if (filtro == null) {
            throw new IllegalArgumentException("El filtro no puede ser null");
        }
        return listaGastos.stream()
            .filter(g -> filtro.filtrar(g)) //devuelve true si el gasto pasa el filtro
            .collect(Collectors.toList()); //devolvemos la lista de gastos que cumplen el filtro
    }

    // Categorías (Set, sin duplicados)
    public void añadirCategoria(String categoria) {
        String cat = normalizarCategoria(categoria);
        if (cat.isEmpty()) throw new IllegalArgumentException("La categoría no puede estar vacía");
        if (categorias.contains(cat)) throw new IllegalArgumentException("La categoría ya existe");
        categorias.add(cat);
    }

    public void eliminarCategoria(String categoria) {
        String cat = normalizarCategoria(categoria);
        if (cat.isEmpty()) throw new IllegalArgumentException("La categoría no puede estar vacía");
        if (!categorias.remove(cat)) throw new IllegalArgumentException("La categoría no existe");
    }

    public boolean existeCategoria(String categoria) {
        String cat = normalizarCategoria(categoria);
        if (cat.isEmpty()) return false;
        return categorias.contains(cat);
    }

    public String normalizarCategoriaPublic(String categoria) {
        return normalizarCategoria(categoria);
    }

    public Set<String> getCategorias() {
        //lo mismo con la copia para que nadie pueda modificar la lista
        return Set.copyOf(categorias);
    }

    private String normalizarCategoria(String categoria) {
        if (categoria == null) {
            return "";
        }
        String normalized = Normalizer.normalize(categoria, Normalizer.Form.NFD);
        String sinAcentos = normalized.replaceAll("\\p{M}", "");
        return sinAcentos.trim().toLowerCase(Locale.ROOT);
    }

    /**
     * Limpia el repositorio y reinicia el contador de ids.
     * Deja el repositorio como recién creado.
     */
    public void limpiar() {
        listaGastos.clear();
        categorias.clear();
        nextId = 1;
    }

    public int getNextId() {
        return nextId;
    }

    public void setNextId(int nextId) {
        if (nextId <= 0) {
            throw new IllegalArgumentException("nextId debe ser positivo");
        }
        this.nextId = nextId;
    }
}
