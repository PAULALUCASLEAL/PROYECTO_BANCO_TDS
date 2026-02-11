package ASP.BanCroak.repo;

import ASP.BanCroak.domain.Notificacion;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public enum RepositorioNotificaciones {
    INSTANCE;

    private final List<Notificacion> notificaciones;
    private int nextId;

    private RepositorioNotificaciones() {
        this.notificaciones = new ArrayList<>();
        this.nextId = 1;
    }

    public Notificacion crearNotificacion(Notificacion notificacion) {
        if (notificacion == null) {
            throw new IllegalArgumentException("La notificacion no puede ser null");
        }
        añadirNotificacion(notificacion);
        return notificacion;
    }

    public void añadirNotificacion(Notificacion notificacion) {
        if (notificacion == null) {
            throw new IllegalArgumentException("La notificacion no puede ser null");
        }
        if (buscarPorId(notificacion.getId()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una notificacion con el id " + notificacion.getId());
        }
        notificaciones.add(notificacion);
        if (notificacion.getId() >= nextId) {
            nextId = notificacion.getId() + 1;
        }
    }

    public Notificacion crearNotificacion(String mensaje, int alertaId, String periodoKey, double totalDetectado, String categoria) {
        Notificacion n = new Notificacion(nextId, java.time.LocalDateTime.now(), mensaje, alertaId, periodoKey, totalDetectado, categoria, false);
        notificaciones.add(n);
        nextId++;
        return n;
    }

    public boolean existeNotificacion(int alertaId, String periodoKey) {
        if (periodoKey == null || periodoKey.isBlank()) {
            return false;
        }
        return notificaciones.stream().anyMatch(n -> n.getAlertaId() == alertaId && periodoKey.equals(n.getPeriodoKey()));
    }

    public List<Notificacion> listarNotificaciones() {
        return List.copyOf(notificaciones);
    }

    public Optional<Notificacion> buscarPorId(int id) {
        if (id <= 0) return Optional.empty();
        return notificaciones.stream().filter(n -> n.getId() == id).findFirst();
    }

    public void limpiar() {
        notificaciones.clear();
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
