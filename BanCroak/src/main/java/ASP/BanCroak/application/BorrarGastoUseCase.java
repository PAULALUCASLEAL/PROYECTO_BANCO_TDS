package ASP.BanCroak.application;

import ASP.BanCroak.domain.Gasto;
import ASP.BanCroak.persistence.GastosPersistence;
import ASP.BanCroak.repo.RepositorioGastos;

public class BorrarGastoUseCase {
    private final RepositorioGastos repoGastos;
    private final GastosPersistence gastosPersistence;

    public BorrarGastoUseCase(RepositorioGastos repoGastos, GastosPersistence gastosPersistence) {
        this.repoGastos = repoGastos;
        this.gastosPersistence = gastosPersistence;
    }

    public void ejecutar(int id) {
        Gasto gasto = repoGastos.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("No existe un gasto con el id " + id));
        repoGastos.eliminarGasto(gasto);
        gastosPersistence.save(repoGastos);
    }
}
