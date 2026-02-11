package ASP.BanCroak.persistence;

import ASP.BanCroak.domain.Notificacion;
import ASP.BanCroak.repo.RepositorioNotificaciones;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificacionesPersistence {
    private final ObjectMapper mapper;
    private final Path path;

    public NotificacionesPersistence(Path path) {
        this.mapper = new ObjectMapper();
        this.path = path;
    }

    public void load(RepositorioNotificaciones repo) {
        if (repo == null) {
            throw new IllegalArgumentException("Repositorio no puede ser null");
        }
        repo.limpiar();
        if (!Files.exists(path)) {
            return;
        }
        try {
            NotificacionesData data = mapper.readValue(path.toFile(), NotificacionesData.class);
            if (data.notificaciones != null) {
                for (NotificacionData n : data.notificaciones) {
                    LocalDateTime ts = n.timestamp == null ? LocalDateTime.now() : LocalDateTime.parse(n.timestamp);
                    Notificacion notificacion = new Notificacion(
                        n.id,
                        ts,
                        n.mensaje,
                        n.alertaId,
                        n.periodoKey,
                        n.totalDetectado,
                        n.categoria,
                        n.leida
                    );
                    repo.añadirNotificacion(notificacion);
                }
            }
            if (data.nextId > 0) {
                repo.setNextId(Math.max(repo.getNextId(), data.nextId));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error cargando notificaciones: " + e.getMessage(), e);
        }
    }

    public void save(RepositorioNotificaciones repo) {
        if (repo == null) {
            throw new IllegalArgumentException("Repositorio no puede ser null");
        }
        NotificacionesData data = new NotificacionesData();
        data.nextId = repo.getNextId();
        data.notificaciones = new ArrayList<>();
        for (Notificacion n : repo.listarNotificaciones()) {
            NotificacionData nd = new NotificacionData();
            nd.id = n.getId();
            nd.timestamp = n.getTimestamp().toString();
            nd.mensaje = n.getMensaje();
            nd.alertaId = n.getAlertaId();
            nd.periodoKey = n.getPeriodoKey();
            nd.totalDetectado = n.getTotalDetectado();
            nd.categoria = n.getCategoria();
            nd.leida = n.isLeida();
            data.notificaciones.add(nd);
        }
        try {
            ensureParent(path);
            mapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), data);
        } catch (IOException e) {
            throw new RuntimeException("Error guardando notificaciones: " + e.getMessage(), e);
        }
    }

    private void ensureParent(Path path) throws IOException {
        Path parent = path.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
    }

    public static class NotificacionesData {
        public int nextId;
        public List<NotificacionData> notificaciones;
    }

    public static class NotificacionData {
        public int id;
        public String timestamp;
        public String mensaje;
        public int alertaId;
        public String periodoKey;
        public double totalDetectado;
        public String categoria;
        public boolean leida;
    }
}
