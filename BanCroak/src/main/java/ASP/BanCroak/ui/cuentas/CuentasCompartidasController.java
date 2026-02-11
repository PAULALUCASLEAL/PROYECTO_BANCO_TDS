package ASP.BanCroak.ui.cuentas;

import ASP.BanCroak.domain.Cuenta;
import ASP.BanCroak.repo.RepositorioCuentas;
import ASP.BanCroak.ui.app.AppContext;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CuentasCompartidasController {
    private final AppContext context;
    private final RepositorioCuentas repo;
    private final CuentasCompartidasView view;
    private final ObservableList<Cuenta> cuentasData;
    private final ObservableList<MiembroPorcentajeRow> miembrosData;

    public CuentasCompartidasController(AppContext context, CuentasCompartidasView view) {
        this.context = context;
        this.repo = context.getRepoCuentas();
        this.view = view;
        this.cuentasData = FXCollections.observableArrayList();
        this.miembrosData = FXCollections.observableArrayList();
        this.view.getTablaCuentas().setItems(cuentasData);
        this.view.getTablaMiembros().setItems(miembrosData);
    }

    public void init() {
        refresh();
        configurarEliminarMiembro();
        miembrosData.addListener((ListChangeListener<MiembroPorcentajeRow>) c -> updateTotalLabel());

        view.getAddMiembroButton().setOnAction(e -> onAddMiembro());
        view.getRepartoEquitativoButton().setOnAction(e -> onRepartoEquitativo());
        view.getCrearButton().setOnAction(e -> onCrearCuenta());
        view.getAbrirButton().setOnAction(e -> onAbrirCuenta());
        view.getVolverButton().setOnAction(e -> context.getNavigator().goToMain());
        view.getTablaCuentas().setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                onAbrirCuenta();
            }
        });
        updateTotalLabel();
    }

    private void onAddMiembro() {
        view.getFeedbackLabel().setText("");
        try {
            String nombre = view.getMiembroNombreField().getText().trim();
            Integer porcentaje = view.getPorcentajeSpinner().getValue();
            if (nombre.isEmpty()) {
                throw new IllegalArgumentException("El nombre del miembro no puede estar vacío");
            }
            if (porcentaje == null || porcentaje <= 0 || porcentaje > 100) {
                throw new IllegalArgumentException("El porcentaje debe estar entre 1 y 100");
            }
            boolean duplicado = miembrosData.stream()
                .anyMatch(m -> m.getNombre().equalsIgnoreCase(nombre));
            if (duplicado) {
                throw new IllegalArgumentException("El miembro ya existe");
            }
            miembrosData.add(new MiembroPorcentajeRow(nombre, porcentaje));
            view.getMiembroNombreField().clear();
            view.getPorcentajeSpinner().getValueFactory().setValue(50);
            view.getMiembroNombreField().requestFocus();
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage(), ex);
        }
    }

    private void onRepartoEquitativo() {
        view.getFeedbackLabel().setText("");
        try {
            if (miembrosData.isEmpty()) {
                throw new IllegalArgumentException("Debes añadir al menos un miembro");
            }
            repartirEquitativo();
            updateTotalLabel();
            view.getFeedbackLabel().setText("Reparto equitativo aplicado");
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage(), ex);
        }
    }

    private void onCrearCuenta() {
        view.getFeedbackLabel().setText("");
        try {
            String nombreCuenta = view.getNombreCuentaField().getText().trim();
            if (nombreCuenta.isEmpty()) {
                throw new IllegalArgumentException("El nombre de la cuenta no puede estar vacío");
            }
            if (miembrosData.isEmpty()) {
                throw new IllegalArgumentException("Debes añadir al menos un miembro");
            }
            int suma = miembrosData.stream().mapToInt(MiembroPorcentajeRow::getPorcentaje).sum();
            if (suma != 100) {
                throw new IllegalArgumentException("La suma de porcentajes debe ser 100");
            }

            List<String> miembros = miembrosData.stream()
                .map(MiembroPorcentajeRow::getNombre)
                .collect(Collectors.toList());
            Map<String, Double> mapa = new LinkedHashMap<>();
            for (MiembroPorcentajeRow row : miembrosData) {
                mapa.put(row.getNombre(), (double) row.getPorcentaje());
            }

            repo.crearCuentaConPorcentajes(nombreCuenta, miembros, mapa);
            context.getCuentasPersistence().save(repo);

            refresh();
            clearForm();
            view.getFeedbackLabel().setText("Guardado");
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage(), ex);
        }
    }

    private void onAbrirCuenta() {
        Cuenta seleccionada = view.getTablaCuentas().getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            showError("Selecciona una cuenta.", null);
            return;
        }
        context.getNavigator().goToGastos(seleccionada.getIdCuenta());
    }

    private void refresh() {
        List<Cuenta> shared = repo.listarCuentas().stream()
            .filter(c -> !c.esPersonal())
            .sorted(Comparator.comparing(Cuenta::getNombreCuenta, String.CASE_INSENSITIVE_ORDER))
            .collect(Collectors.toList());
        cuentasData.setAll(shared);
    }

    private void clearForm() {
        view.getNombreCuentaField().clear();
        view.getMiembroNombreField().clear();
        view.getPorcentajeSpinner().getValueFactory().setValue(50);
        miembrosData.clear();
        updateTotalLabel();
    }

    private void configurarEliminarMiembro() {
        TableColumn<MiembroPorcentajeRow, Void> col = view.getColEliminarMiembro();
        col.setCellFactory(tc -> new TableCell<>() {
            private final Button btn = new Button("Eliminar");

            {
                btn.getStyleClass().add("danger-button");
                btn.setOnAction(e -> {
                    MiembroPorcentajeRow row = getTableView().getItems().get(getIndex());
                    miembrosData.remove(row);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });
    }

    private void updateTotalLabel() {
        int total = miembrosData.stream().mapToInt(MiembroPorcentajeRow::getPorcentaje).sum();
        view.getTotalPorcentajeLabel().setText("Total: " + total + "%");
        if (total == 100) {
            view.getTotalPorcentajeLabel().setStyle("");
        } else if (total > 100) {
            view.getTotalPorcentajeLabel().setStyle("-fx-text-fill: #c0392b;");
        } else {
            view.getTotalPorcentajeLabel().setStyle("-fx-text-fill: #0b5d5a;");
        }
    }

    private void repartirEquitativo() {
        int n = miembrosData.size();
        if (n <= 0) {
            return;
        }
        int base = 100 / n;
        int resto = 100 - (base * n);
        for (int i = 0; i < miembrosData.size(); i++) {
            int valor = base + (i < resto ? 1 : 0);
            miembrosData.get(i).setPorcentaje(valor);
        }
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
