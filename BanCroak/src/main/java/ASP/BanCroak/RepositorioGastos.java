package ASP.BanCroak;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Repositorio en memoria para gestionar gastos (singleton + CRUD).
 */
public final class RepositorioGastos {
    private static RepositorioGastos instancia;

    private final List<Gasto> listaGastos;
    private final AtomicInteger nextId;

    private RepositorioGastos() {
        this.listaGastos = new ArrayList<>();
        this.nextId = new AtomicInteger(1);
    }

    public static RepositorioGastos getInstancia() {
        if (instancia == null) {
            instancia = new RepositorioGastos();
        }
        return instancia;
    }

    public void añadirGasto(Gasto gasto) {
        if (gasto == null) {
            throw new IllegalArgumentException("El gasto no puede ser null");
        }
        if (gasto.getID() == 0) {
            int id = nextId.getAndIncrement();
            gasto.asignarId(id);
        } else if (buscarPorId(gasto.getID()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un gasto con el id " + gasto.getID());
        }
        listaGastos.add(gasto);
    }

    public void editarGasto(Gasto gasto) {
        if (gasto == null) {
            throw new IllegalArgumentException("El gasto no puede ser null");
        }
        int id = gasto.getID();
        if (id <= 0) {
            throw new IllegalArgumentException("El gasto debe tener id valido para editar");
        }
        for (int i = 0; i < listaGastos.size(); i++) {
            if (listaGastos.get(i).getID() == id) {
                listaGastos.set(i, gasto);
                return;
            }
        }
        throw new IllegalArgumentException("No existe un gasto con el id " + id);
    }

    public Optional<Gasto> buscarGasto(Gasto gasto) {
        if (gasto == null) {
            return Optional.empty();
        }
        return buscarPorId(gasto.getID());
    }

    public void eliminarGasto(Gasto gasto) {
        if (gasto == null) {
            throw new IllegalArgumentException("El gasto no puede ser null");
        }
        int id = gasto.getID();
        boolean removed = listaGastos.removeIf(g -> g.getID() == id);
        if (!removed) {
            throw new IllegalArgumentException("No existe un gasto con el id " + id);
        }
    }

    public List<Gasto> getListaGastos() {
        return List.copyOf(listaGastos);
    }

    public Optional<Gasto> buscarPorId(int id) {
        if (id <= 0) {
            return Optional.empty();
        }
        return listaGastos.stream()
            .filter(g -> g.getID() == id)
            .findFirst();
    }

    /**
     * Limpia el repositorio y reinicia el contador de ids (util para tests).
     */
    public void limpiar() {
        listaGastos.clear();
        nextId.set(1);
    }
}
