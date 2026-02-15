package ASP.BanCroak.repo;

import ASP.BanCroak.domain.AlertaGasto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public enum RepositorioAlertas {
    INSTANCE;

    private final List<AlertaGasto> alertas;
    private int nextId;

    private RepositorioAlertas() {
        this.alertas = new ArrayList<>();
        this.nextId = 1;
    }

    public AlertaGasto crearAlerta(String nombre, AlertaGasto.Periodo periodo, double limite, String categoria, boolean activa) {
        AlertaGasto alerta = new AlertaGasto(nextId, nombre, periodo, limite, categoria, activa);
        alertas.add(alerta);
        nextId++;
        return alerta;
    }

    public void añadirAlerta(AlertaGasto alerta) {
        if (alerta == null) {
            throw new IllegalArgumentException("La alerta no puede ser null");
        }
        if (buscarPorId(alerta.getId()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una alerta con el id " + alerta.getId());
        }
        alertas.add(alerta);
        if (alerta.getId() >= nextId) {
            nextId = alerta.getId() + 1;
        }
    }

    public void eliminarAlerta(int id) {
        boolean removed = alertas.removeIf(a -> a.getId() == id);
        if (!removed) {
            throw new IllegalArgumentException("No existe una alerta con el id " + id);
        }
    }

    public void actualizarEstado(int id, boolean activa) {
        AlertaGasto alerta = buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("No existe una alerta con el id " + id));
        alerta.setActiva(activa);
    }

    public List<AlertaGasto> listarAlertas() {
        return List.copyOf(alertas);
    }

    public Optional<AlertaGasto> buscarPorId(int id) {
        if (id <= 0) return Optional.empty();
        return alertas.stream().filter(a -> a.getId() == id).findFirst();
    }

    public void limpiar() {
        alertas.clear();
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
