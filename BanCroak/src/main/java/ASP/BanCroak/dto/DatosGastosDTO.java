package ASP.BanCroak.dto;

import java.util.ArrayList;
import java.util.List;

public class DatosGastosDTO {
    private List<GastoDTO> gastos;
    private List<String> categorias;

    public DatosGastosDTO() {
        this.gastos = new ArrayList<>();
        this.categorias = new ArrayList<>();
    }

    public List<GastoDTO> getGastos() {
        return gastos;
    }

    public void setGastos(List<GastoDTO> gastos) {
        this.gastos = gastos == null ? new ArrayList<>() : gastos;
    }

    public List<String> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<String> categorias) {
        this.categorias = categorias == null ? new ArrayList<>() : categorias;
    }
}
