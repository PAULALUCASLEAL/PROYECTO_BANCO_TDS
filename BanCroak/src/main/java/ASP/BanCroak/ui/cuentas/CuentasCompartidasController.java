package ASP.BanCroak.ui.cuentas;

import ASP.BanCroak.domain.Cuenta;
import ASP.BanCroak.repo.RepositorioCuentas;
import ASP.BanCroak.ui.app.AppContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.ArrayList;
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

    public CuentasCompartidasController(AppContext context, CuentasCompartidasView view) {
        this.context = context;
        this.repo = context.getRepoCuentas();
        this.view = view;
        this.cuentasData = FXCollections.observableArrayList();
        this.view.getTablaCuentas().setItems(cuentasData);
    }

    public void init() {
        refresh();
        view.getCrearButton().setOnAction(e -> onCrearCuenta());
        view.getAbrirButton().setOnAction(e -> onAbrirCuenta());
        view.getVolverButton().setOnAction(e -> context.getNavigator().goToMain());
        view.getTablaCuentas().setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                onAbrirCuenta();
            }
        });
    }

    private void onCrearCuenta() {
        view.getFeedbackLabel().setText("");
        try {
            String nombre = view.getNombreField().getText().trim();
            if (nombre.isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }
            List<String> miembros = parseLista(view.getMiembrosField().getText());
            if (miembros.isEmpty()) {
                throw new IllegalArgumentException("Debes indicar al menos un miembro");
            }
            String porcentajesRaw = view.getPorcentajesField().getText().trim();
            if (porcentajesRaw.isEmpty()) {
                repo.crearCuentaConPartesIguales(nombre, miembros);
            } else {
                List<Double> porcentajes = parsePorcentajes(porcentajesRaw);
                if (porcentajes.size() != miembros.size()) {
                    throw new IllegalArgumentException("Cantidad de porcentajes no coincide con miembros");
                }
                double suma = porcentajes.stream().mapToDouble(Double::doubleValue).sum();
                if (Math.abs(100.0 - suma) > 0.01) {
                    throw new IllegalArgumentException("La suma de porcentajes debe ser 100");
                }
                Map<String, Double> mapa = new LinkedHashMap<>();
                for (int i = 0; i < miembros.size(); i++) {
                    mapa.put(miembros.get(i), porcentajes.get(i));
                }
                repo.crearCuentaConPorcentajes(nombre, miembros, mapa);
            }
            context.getCuentasPersistence().save(repo);
            refresh();
            view.getNombreField().clear();
            view.getMiembrosField().clear();
            view.getPorcentajesField().clear();
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

    private List<Double> parsePorcentajes(String raw) {
        List<Double> result = new ArrayList<>();
        String[] parts = raw.split(",");
        for (String p : parts) {
            String s = p.trim();
            if (!s.isEmpty()) {
                try {
                    result.add(Double.parseDouble(s));
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Porcentaje no válido: " + s);
                }
            }
        }
        return result;
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
