package ASP.BanCroak.persistence;

import ASP.BanCroak.domain.AlertaGasto;
import ASP.BanCroak.repo.RepositorioAlertas;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AlertasPersistence {
    private final ObjectMapper mapper;
    private final Path path;

    public AlertasPersistence(Path path) {
        this.mapper = new ObjectMapper();
        this.path = path;
    }

    public void load(RepositorioAlertas repo) {
        if (repo == null) {
            throw new IllegalArgumentException("Repositorio no puede ser null");
        }
        repo.limpiar();
        if (!Files.exists(path)) {
            return;
        }
        try {
            AlertasData data = mapper.readValue(path.toFile(), AlertasData.class);
            if (data.alertas != null) {
                for (AlertaData a : data.alertas) {
                    String nombre = a.nombre == null || a.nombre.isBlank() ? "Alerta " + a.id : a.nombre;
                    AlertaGasto alerta = new AlertaGasto(a.id, nombre, AlertaGasto.Periodo.valueOf(a.periodo), a.limite, a.categoria, a.activa);
                    repo.añadirAlerta(alerta);
                }
            }
            if (data.nextId > 0) {
                repo.setNextId(Math.max(repo.getNextId(), data.nextId));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error cargando alertas: " + e.getMessage(), e);
        }
    }

    public void save(RepositorioAlertas repo) {
        if (repo == null) {
            throw new IllegalArgumentException("Repositorio no puede ser null");
        }
        AlertasData data = new AlertasData();
        data.nextId = repo.getNextId();
        data.alertas = new ArrayList<>();
        for (AlertaGasto a : repo.listarAlertas()) {
            AlertaData ad = new AlertaData();
            ad.id = a.getId();
            ad.nombre = a.getNombre();
            ad.periodo = a.getPeriodo().name();
            ad.limite = a.getLimite();
            ad.categoria = a.getCategoria();
            ad.activa = a.isActiva();
            data.alertas.add(ad);
        }
        try {
            ensureParent(path);
            mapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), data);
        } catch (IOException e) {
            throw new RuntimeException("Error guardando alertas: " + e.getMessage(), e);
        }
    }

    private void ensureParent(Path path) throws IOException {
        Path parent = path.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
    }

    public static class AlertasData {
        public int nextId;
        public List<AlertaData> alertas;
    }

    public static class AlertaData {
        public int id;
        public String nombre;
        public String periodo;
        public double limite;
        public String categoria;
        public boolean activa;
    }
}
