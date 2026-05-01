package ASP.BanCroak.application;

import ASP.BanCroak.domain.Gasto;
import ASP.BanCroak.persistence.GastosPersistence;
import ASP.BanCroak.repo.RepositorioGastos;

import java.time.LocalDate;

//busacmos un gasto que ya existe por su id, lo cambiamos y volvemos a persistirlo bien 
public class ModificarGastoUseCase {
    private final RepositorioGastos repoGastos;
    private final GastosPersistence gastosPersistence;

    public ModificarGastoUseCase(RepositorioGastos repoGastos, GastosPersistence gastosPersistence) {
        this.repoGastos = repoGastos;
        this.gastosPersistence = gastosPersistence;
    }

    public Gasto ejecutar(int id, double cantidad, LocalDate fecha, String categoria) {
        Gasto actual = buscarGasto(id);
        return ejecutar(id, cantidad, fecha, categoria, actual.getPagador());
    }

    public Gasto ejecutar(int id, double cantidad, LocalDate fecha, String categoria, String pagador) {
        buscarGasto(id);
        repoGastos.editarGasto(id, cantidad, fecha, categoria, pagador);
        gastosPersistence.save(repoGastos);
        return buscarGasto(id);
    }

    private Gasto buscarGasto(int id) {
        return repoGastos.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("No existe un gasto con el id " + id));
    }
}
