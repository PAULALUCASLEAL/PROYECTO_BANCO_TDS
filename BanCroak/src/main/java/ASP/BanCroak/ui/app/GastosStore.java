package ASP.BanCroak.ui.app;

import ASP.BanCroak.domain.Gasto;
import ASP.BanCroak.persistence.GastosPersistence;
import ASP.BanCroak.repo.RepositorioGastos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public final class GastosStore {
    private final RepositorioGastos repoGastos;
    private final GastosPersistence gastosPersistence;
    private final ObservableList<Gasto> gastos;

    public GastosStore(RepositorioGastos repoGastos, GastosPersistence gastosPersistence) {
        this.repoGastos = repoGastos;
        this.gastosPersistence = gastosPersistence;
        this.gastos = FXCollections.observableArrayList();
        refresh();
    }

    public ObservableList<Gasto> getGastos() {
        return gastos;
    }

    public List<Gasto> snapshot() {
        return List.copyOf(gastos);
    }

    public void refresh() {
        gastos.setAll(repoGastos.getListaGastos());
    }

    public void añadirGasto(Gasto gasto) {
        repoGastos.añadirGasto(gasto);
        gastosPersistence.save(repoGastos);
        refresh();
    }

    public void editarGasto(int id, double cantidad, java.time.LocalDate fecha, String categoria, String pagador) {
        repoGastos.editarGasto(id, cantidad, fecha, categoria, pagador);
        gastosPersistence.save(repoGastos);
        refresh();
    }

    public void eliminarGasto(Gasto gasto) {
        repoGastos.eliminarGasto(gasto);
        gastosPersistence.save(repoGastos);
        refresh();
    }
}
