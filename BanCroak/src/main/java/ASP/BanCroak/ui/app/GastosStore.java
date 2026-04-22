package ASP.BanCroak.ui.app;

import ASP.BanCroak.application.BorrarGastoUseCase;
import ASP.BanCroak.application.ModificarGastoUseCase;
import ASP.BanCroak.application.RegistrarGastoUseCase;
import ASP.BanCroak.domain.Gasto;
import ASP.BanCroak.repo.RepositorioGastos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public final class GastosStore {
    private final RepositorioGastos repoGastos;
    private final RegistrarGastoUseCase registrarGastoUseCase;
    private final ModificarGastoUseCase modificarGastoUseCase;
    private final BorrarGastoUseCase borrarGastoUseCase;
    private final ObservableList<Gasto> gastos;

    public GastosStore(
        RepositorioGastos repoGastos,
        RegistrarGastoUseCase registrarGastoUseCase,
        ModificarGastoUseCase modificarGastoUseCase,
        BorrarGastoUseCase borrarGastoUseCase
    ) {
        this.repoGastos = repoGastos;
        this.registrarGastoUseCase = registrarGastoUseCase;
        this.modificarGastoUseCase = modificarGastoUseCase;
        this.borrarGastoUseCase = borrarGastoUseCase;
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
        registrarGastoUseCase.ejecutar(gasto);
        refresh();
    }

    public void editarGasto(int id, double cantidad, java.time.LocalDate fecha, String categoria, String pagador) {
        modificarGastoUseCase.ejecutar(id, cantidad, fecha, categoria, pagador);
        refresh();
    }

    public void eliminarGasto(Gasto gasto) {
        if (gasto == null) {
            throw new IllegalArgumentException("El gasto no puede ser null");
        }
        borrarGastoUseCase.ejecutar(gasto.getID());
        refresh();
    }
}
