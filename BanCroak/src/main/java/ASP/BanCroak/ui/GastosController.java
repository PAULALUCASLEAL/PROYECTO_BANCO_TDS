package ASP.BanCroak.ui;

import ASP.BanCroak.Gasto;
import ASP.BanCroak.RepositorioGastos;
import ASP.BanCroak.persistence.GastosPersistence;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.List;

public class GastosController {
    private final RepositorioGastos repo;
    private final GastosPersistence persistence;
    private final GastosView view;

    private final ObservableList<Gasto> gastosData;
    private final ObservableList<String> categoriasData;

    public GastosController(RepositorioGastos repo, GastosPersistence persistence, GastosView view) {
        this.repo = repo;
        this.persistence = persistence;
        this.view = view;
        this.gastosData = FXCollections.observableArrayList();
        this.categoriasData = FXCollections.observableArrayList();

        this.view.getTablaGastos().setItems(gastosData);
        this.view.getCategoriaCombo().setItems(categoriasData);
    }

    public void init() {
        try {
            persistence.loadInto(repo);
        } catch (Exception ex) {
            repo.limpiar();
            showError("No se pudo cargar el JSON. Se inicia vacío.", ex);
        }
        refreshAll();

        view.getAddGastoButton().setOnAction(e -> onAddGasto());
        view.getAddCategoriaButton().setOnAction(e -> onAddCategoria());
        view.getEliminarSeleccionadoButton().setOnAction(e -> onEliminarSeleccionado());
    }

    private void onAddGasto() {
        view.getFeedbackLabel().setText("");
        try {
            double cantidad = Double.parseDouble(view.getCantidadField().getText().trim());
            LocalDate fecha = view.getFechaPicker().getValue();
            String categoria = getCategoriaInput();
            String pagador = view.getPagadorField().getText().trim();
            int idCuenta = Integer.parseInt(view.getIdCuentaField().getText().trim());

            if (categoria.isEmpty()) {
                throw new IllegalArgumentException("La categoría no puede estar vacía");
            }
            if (!repo.getCategorias().contains(categoria)) {
                repo.añadirCategoria(categoria);
            }

            Gasto gasto = Gasto.crearGasto(cantidad, fecha, categoria, pagador, idCuenta);
            repo.añadirGasto(gasto);
            persistence.save(repo);

            refreshAll();
            clearGastoInputs();
            view.getFeedbackLabel().setText("Guardado ✅");
        } catch (NumberFormatException ex) {
            showError("Cantidad o idCuenta no válido.", ex);
        } catch (IllegalArgumentException | IOException ex) {
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
            // Persistimos categorías aunque no haya gastos para mantenerlas visibles en el ComboBox.
            persistence.save(repo);
            refreshAll();
            view.getNuevaCategoriaField().clear();
            view.getFeedbackLabel().setText("Guardado ");
        } catch (IllegalArgumentException | IOException ex) {
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
            persistence.save(repo);
            refreshAll();
            view.getFeedbackLabel().setText("Guardado ");
        } catch (IllegalArgumentException | IOException ex) {
            showError(ex.getMessage(), ex);
        }
    }

    private void refreshAll() {
        gastosData.setAll(repo.getListaGastos());
        categoriasData.setAll(repo.getCategorias().stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList()));
    }

    private void clearGastoInputs() {
        view.getCantidadField().clear();
        view.getFechaPicker().setValue(null);
        view.getCategoriaCombo().getEditor().clear();
        view.getCategoriaCombo().setValue(null);
        view.getPagadorField().clear();
        view.getIdCuentaField().clear();
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
}
