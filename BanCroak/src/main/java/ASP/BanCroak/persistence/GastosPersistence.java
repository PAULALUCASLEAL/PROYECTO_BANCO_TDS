package ASP.BanCroak.persistence;

import ASP.BanCroak.domain.Gasto;
import ASP.BanCroak.repo.RepositorioGastos;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GastosPersistence {
    private final ObjectMapper mapper;
    private final Path path;

    public GastosPersistence(Path path) {
        this.mapper = new ObjectMapper();
        this.path = path;
    }

    public void load(RepositorioGastos repo) {
        if (repo == null) {
            throw new IllegalArgumentException("Repositorio no puede ser null");
        }
        repo.limpiar();
        if (!Files.exists(path)) {
            return;
        }
        try {
            GastosData data = mapper.readValue(path.toFile(), GastosData.class);
            if (data.categorias != null) {
                for (String categoria : data.categorias) {
                    if (!repo.existeCategoria(categoria)) {
                        repo.añadirCategoria(categoria);
                    }
                }
            }
            if (data.gastos != null) {
                for (GastoData g : data.gastos) {
                    LocalDate fecha = g.fecha == null ? null : LocalDate.parse(g.fecha);
                    Gasto gasto = Gasto.reconstruirGasto(
                        g.cantidad,
                        fecha,
                        g.categoria,
                        g.pagador,
                        g.idCuenta,
                        g.idGasto
                    );
                    repo.añadirGasto(gasto);
                }
            }
            if (data.nextId > 0) {
                repo.setNextId(Math.max(repo.getNextId(), data.nextId));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error cargando gastos: " + e.getMessage(), e);
        }
    }

    public void save(RepositorioGastos repo) {
        if (repo == null) {
            throw new IllegalArgumentException("Repositorio no puede ser null");
        }
        GastosData data = new GastosData();
        data.nextId = repo.getNextId();
        data.categorias = new ArrayList<>(repo.getCategorias());
        data.gastos = new ArrayList<>();
        for (Gasto g : repo.getListaGastos()) {
            GastoData gd = new GastoData();
            gd.idGasto = g.getID();
            gd.idCuenta = g.getIDCuenta();
            gd.categoria = g.getCategoria();
            gd.pagador = g.getPagador();
            gd.cantidad = g.getCantidad();
            gd.fecha = g.getFecha() == null ? null : g.getFecha().toString();
            data.gastos.add(gd);
        }
        try {
            ensureParent(path);
            mapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), data);
        } catch (IOException e) {
            throw new RuntimeException("Error guardando gastos: " + e.getMessage(), e);
        }
    }

    private void ensureParent(Path path) throws IOException {
        Path parent = path.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
    }

    public static class GastosData {
        public int nextId;
        public List<GastoData> gastos;
        public List<String> categorias;
    }

    public static class GastoData {
        public int idGasto;
        public int idCuenta;
        public String categoria;
        public String pagador;
        public double cantidad;
        public String fecha;
    }
}
