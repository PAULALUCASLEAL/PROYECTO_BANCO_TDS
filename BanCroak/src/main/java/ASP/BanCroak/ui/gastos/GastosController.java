package ASP.BanCroak.ui.gastos;

import ASP.BanCroak.domain.Gasto;
import ASP.BanCroak.filtros.FiltroCategoria;
import ASP.BanCroak.filtros.FiltroCompuesto;
import ASP.BanCroak.filtros.FiltroIntervaloFechas;
import ASP.BanCroak.filtros.FiltroMeses;
import ASP.BanCroak.repo.RepositorioGastos;
import ASP.BanCroak.ui.app.AppContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GastosController {
    private final AppContext context;
    private final RepositorioGastos repo;
    private final GastosView view;
    private final int cuentaActivaId;

    private final ObservableList<Gasto> gastosData;
    private final ObservableList<String> categoriasData;
    private FiltroCompuesto filtroActual;

    public GastosController(AppContext context, GastosView view, int cuentaActivaId) {
        this.context = context;
        this.repo = context.getRepoGastos();
        this.view = view;
        this.cuentaActivaId = cuentaActivaId;
        this.gastosData = FXCollections.observableArrayList();
        this.categoriasData = FXCollections.observableArrayList();
        this.filtroActual = null;

        this.view.getTablaGastos().setItems(gastosData);
        this.view.getCategoriaCombo().setItems(categoriasData);
    }

    public void init() {
        view.setIdCuenta(cuentaActivaId);
        view.setIdCuentaEditable(false);
        context.getCuentaActiva().ifPresent(cuenta ->
            view.getCuentaLabel().setText("Cuenta activa: " + cuenta.getNombreCuenta())
        );
        refreshAll();

        view.getAddGastoButton().setOnAction(e -> onAddGasto());
        view.getAddCategoriaButton().setOnAction(e -> onAddCategoria());
        view.getEliminarSeleccionadoButton().setOnAction(e -> onEliminarSeleccionado());
        view.getAplicarFiltroButton().setOnAction(e -> onAplicarFiltros());
        view.getLimpiarFiltroButton().setOnAction(e -> onLimpiarFiltros());
        view.getGraficasButton().setOnAction(e -> onGraficas());
        view.getVolverButton().setOnAction(e -> context.getNavigator().goToMain());
    }

    private void onAddGasto() {
        view.getFeedbackLabel().setText("");
        try {
            double cantidad = Double.parseDouble(view.getCantidadField().getText().trim());
            LocalDate fecha = view.getFechaPicker().getValue();
            String categoria = getCategoriaInput();
            String pagador = view.getPagadorField().getText().trim();
            int idCuenta = cuentaActivaId;

            if (categoria.isEmpty()) {
                throw new IllegalArgumentException("La categoría no puede estar vacía");
            }
            if (!repo.getCategorias().contains(categoria)) {
                repo.añadirCategoria(categoria);
            }

            Gasto gasto = Gasto.crearGasto(cantidad, fecha, categoria, pagador, idCuenta);
            repo.añadirGasto(gasto);
            context.getGastosPersistence().save(repo);

            refreshAll();
            clearGastoInputs();
            view.getFeedbackLabel().setText("Guardado");
        } catch (NumberFormatException ex) {
            showError("Cantidad no válida.", ex);
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage(), ex);
        }
    }

    private void onAddCategoria() {
        view.getFeedbackLabel().setText("");
        try {
            String categoria = view.getNuevaCategoriaField().getText().trim();
            if (categoria.isEmpty()) {
                throw new IllegalArgumentException("La categoría no puede estar vacía");
            }
            repo.añadirCategoria(categoria);
            context.getGastosPersistence().save(repo);
            refreshAll();
            view.getNuevaCategoriaField().clear();
            view.getFeedbackLabel().setText("Guardado");
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage(), ex);
        }
    }

    private void onEliminarSeleccionado() {
        view.getFeedbackLabel().setText("");
        Gasto seleccionado = view.getTablaGastos().getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            showError("Selecciona un gasto para eliminar.", null);
            return;
        }
        try {
            repo.eliminarGasto(seleccionado);
            context.getGastosPersistence().save(repo);
            refreshAll();
            view.getFeedbackLabel().setText("Guardado");
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage(), ex);
        }
    }

    private void refreshAll() {
        List<Gasto> filtrados = filtrarGastos();
        gastosData.setAll(filtrados);
        categoriasData.setAll(repo.getCategorias().stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList()));
    }

    private void onAplicarFiltros() {
        view.getFeedbackLabel().setText("");
        try {
            filtroActual = construirFiltro();
            List<Gasto> filtrados = filtrarGastos();
            gastosData.setAll(filtrados);
            view.getFeedbackLabel().setText("Filtro aplicado (" + filtrados.size() + ")");
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage(), ex);
        }
    }

    private void onLimpiarFiltros() {
        view.getFiltroCategoriasField().clear();
        view.getFiltroMesesField().clear();
        view.getFiltroDesdePicker().setValue(null);
        view.getFiltroHastaPicker().setValue(null);
        filtroActual = null;
        refreshAll();
        view.getFeedbackLabel().setText("Filtros limpiados");
    }

    private void onGraficas() {
        List<Gasto> filtrados = filtrarGastos();
        context.getNavigator().goToGraficas(cuentaActivaId, filtrados);
    }

    private List<Gasto> filtrarGastos() {
        List<Gasto> base = repo.getListaGastos().stream()
            .filter(g -> g.getIDCuenta() == cuentaActivaId)
            .collect(Collectors.toList());
        if (filtroActual == null) {
            return base;
        }
        return base.stream()
            .filter(filtroActual::filtrar)
            .collect(Collectors.toList());
    }

    private FiltroCompuesto construirFiltro() {
        FiltroCompuesto compuesto = new FiltroCompuesto();

        List<String> categorias = parseLista(view.getFiltroCategoriasField().getText());
        if (!categorias.isEmpty()) {
            compuesto.añadirFiltro(new FiltroCategoria(categorias));
        }

        List<String> meses = parseLista(view.getFiltroMesesField().getText());
        if (!meses.isEmpty()) {
            compuesto.añadirFiltro(new FiltroMeses(meses));
        }

        LocalDate desde = view.getFiltroDesdePicker().getValue();
        LocalDate hasta = view.getFiltroHastaPicker().getValue();
        if (desde != null || hasta != null) {
            compuesto.añadirFiltro(new FiltroIntervaloFechas(desde, hasta));
        }

        return compuesto;
    }

    private void clearGastoInputs() {
        view.getCantidadField().clear();
        view.getFechaPicker().setValue(null);
        view.getCategoriaCombo().getEditor().clear();
        view.getCategoriaCombo().setValue(null);
        view.getPagadorField().clear();
    }

    private String getCategoriaInput() {
        String editorText = view.getCategoriaCombo().getEditor().getText();
        if (editorText != null && !editorText.isBlank()) {
            return editorText.trim();
        }
        String value = view.getCategoriaCombo().getValue();
        return value == null ? "" : value.trim();
    }

    private void showError(String message, Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setHeaderText("Error");
        alert.showAndWait();
        if (ex != null) {
            ex.printStackTrace();
        }
    }

    private List<String> parseLista(String raw) {
        List<String> result = new ArrayList<>();
        if (raw == null || raw.isBlank()) {
            return result;
        }
        String[] parts = raw.split(",");
        for (String p : parts) {
            String s = p.trim();
            if (!s.isEmpty()) {
                result.add(s);
            }
        }
        return result;
    }
}
