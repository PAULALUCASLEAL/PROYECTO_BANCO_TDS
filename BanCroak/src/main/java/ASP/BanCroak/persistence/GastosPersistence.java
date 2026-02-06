package ASP.BanCroak.persistence;

import ASP.BanCroak.Gasto;
import ASP.BanCroak.RepositorioGastos;
import ASP.BanCroak.dto.DatosGastosDTO;
import ASP.BanCroak.dto.GastoDTO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class GastosPersistence {
    private static final Path DATA_PATH = Path.of("data", "gastos.json");

    private final ObjectMapper mapper;

    public GastosPersistence() {
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void loadInto(RepositorioGastos repo) throws IOException {
        Objects.requireNonNull(repo, "repo");
        if (!Files.exists(DATA_PATH)) {
            return;
        }
        DatosGastosDTO dto = mapper.readValue(DATA_PATH.toFile(), DatosGastosDTO.class);
        repo.limpiar();

        if (dto.getCategorias() != null) {
            for (String categoria : dto.getCategorias()) {
                if (categoria != null && !categoria.isBlank()) {
                    repo.añadirCategoria(categoria.trim());
                }
            }
        }

        List<GastoDTO> gastos = dto.getGastos();
        if (gastos == null) {
            return;
        }
        for (GastoDTO g : gastos) {
            if (g == null) {
                continue;
            }
            String categoria = g.getCategoria() == null ? "" : g.getCategoria().trim();
            if (!categoria.isEmpty()) {
                repo.añadirCategoria(categoria);
            }
            LocalDate fecha = g.getFecha() == null ? null : LocalDate.parse(g.getFecha());
            Gasto gasto = Gasto.reconstruir(
                g.getCantidad(),
                fecha,
                categoria,
                g.getPagador(),
                g.getIdCuenta(),
                g.getIdGasto()
            );
            repo.añadirGasto(gasto);
        }
    }

    public void save(RepositorioGastos repo) throws IOException {
        Objects.requireNonNull(repo, "repo");
        DatosGastosDTO dto = new DatosGastosDTO();
        dto.setCategorias(repo.getCategorias().stream().collect(Collectors.toList()));
        dto.setGastos(repo.getListaGastos().stream().map(this::toDto).collect(Collectors.toList()));

        Files.createDirectories(DATA_PATH.getParent());
        mapper.writeValue(DATA_PATH.toFile(), dto);
    }

    private GastoDTO toDto(Gasto gasto) {
        GastoDTO dto = new GastoDTO();
        dto.setCantidad(gasto.getCantidad());
        dto.setFecha(gasto.getFecha() == null ? null : gasto.getFecha().toString());
        dto.setCategoria(gasto.getCategoria());
        dto.setPagador(gasto.getPagador());
        dto.setIdCuenta(gasto.getIDCuenta());
        dto.setIdGasto(gasto.getID());
        return dto;
    }
}
