package ASP.BanCroak.persistence;

import ASP.BanCroak.domain.Cuenta;
import ASP.BanCroak.repo.RepositorioCuentas;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CuentasPersistence {
    private final ObjectMapper mapper;
    private final Path path;

    public CuentasPersistence(Path path) {
        this.mapper = new ObjectMapper();
        this.path = path;
    }

    public void load(RepositorioCuentas repo, String nombreCuentaPersonal) {
        if (repo == null) {
            throw new IllegalArgumentException("Repositorio no puede ser null");
        }
        repo.limpiar();
        if (Files.exists(path)) {
            try {
                CuentasData data = mapper.readValue(path.toFile(), CuentasData.class);
                if (data.cuentas != null) {
                    for (CuentaData c : data.cuentas) {
                        Map<String, Double> porcentajes = new LinkedHashMap<>();
                        if (c.porcentajes != null) {
                            porcentajes.putAll(c.porcentajes);
                        }
                        Cuenta cuenta = Cuenta.crearConPorcentajes(
                            c.idCuenta,
                            c.nombreCuenta,
                            c.miembros == null ? List.of() : c.miembros,
                            porcentajes
                        );
                        repo.añadirCuenta(cuenta);
                    }
                }
                if (data.nextIdCuenta > 0) {
                    repo.setNextIdCuenta(Math.max(repo.getNextIdCuenta(), data.nextIdCuenta));
                }
            } catch (IOException e) {
                throw new RuntimeException("Error cargando cuentas: " + e.getMessage(), e);
            }
        }

        if (repo.listarCuentas().isEmpty()) {
            String nombre = (nombreCuentaPersonal == null || nombreCuentaPersonal.isBlank())
                ? "Mi Cuenta"
                : nombreCuentaPersonal.trim();
            List<String> miembros = List.of(nombre);
            Map<String, Double> porcentajes = Map.of(nombre, 100.0);
            repo.crearCuentaConPorcentajes(nombre, miembros, porcentajes);
            save(repo);
        }
    }

    public void save(RepositorioCuentas repo) {
        if (repo == null) {
            throw new IllegalArgumentException("Repositorio no puede ser null");
        }
        CuentasData data = new CuentasData();
        data.nextIdCuenta = repo.getNextIdCuenta();
        data.cuentas = new ArrayList<>();
        for (Cuenta c : repo.listarCuentas()) {
            CuentaData cd = new CuentaData();
            cd.idCuenta = c.getIdCuenta();
            cd.nombreCuenta = c.getNombreCuenta();
            cd.miembros = new ArrayList<>(c.getMiembros());
            cd.porcentajes = new LinkedHashMap<>(c.getPorcentajes());
            data.cuentas.add(cd);
        }
        try {
            ensureParent(path);
            mapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), data);
        } catch (IOException e) {
            throw new RuntimeException("Error guardando cuentas: " + e.getMessage(), e);
        }
    }

    private void ensureParent(Path path) throws IOException {
        Path parent = path.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
    }

    public static class CuentasData {
        public int nextIdCuenta;
        public List<CuentaData> cuentas;
    }

    public static class CuentaData {
        public int idCuenta;
        public String nombreCuenta;
        public List<String> miembros;
        public Map<String, Double> porcentajes;
    }
}
