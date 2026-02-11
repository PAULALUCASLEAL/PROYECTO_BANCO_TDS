package ASP.BanCroak.ui.graficas;

import ASP.BanCroak.domain.Gasto;
import ASP.BanCroak.ui.app.AppContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GraficasController {
    private final AppContext context;
    private final GraficasView view;
    private final int cuentaId;
    private final List<Gasto> gastosFiltrados;

    public GraficasController(AppContext context, GraficasView view, int cuentaId, List<Gasto> gastosFiltrados) {
        this.context = context;
        this.view = view;
        this.cuentaId = cuentaId;
        this.gastosFiltrados = gastosFiltrados;
    }

    public void init() {
        view.getVolverButton().setOnAction(e -> context.getNavigator().goToGastos(cuentaId));
        renderCategorias();
        renderMeses();
    }

    private void renderCategorias() {
        Map<String, Double> totales = new LinkedHashMap<>();
        for (Gasto g : gastosFiltrados) {
            if (g.getIDCuenta() != cuentaId) {
                continue;
            }
            String categoria = g.getCategoria();
            totales.put(categoria, totales.getOrDefault(categoria, 0.0) + g.getCantidad());
        }
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        totales.forEach((cat, total) -> data.add(new PieChart.Data(cat, total)));
        view.getCategoriasChart().setData(data);
    }

    private void renderMeses() {
        Map<Integer, Double> totales = new LinkedHashMap<>();
        for (Gasto g : gastosFiltrados) {
            if (g.getIDCuenta() != cuentaId || g.getFecha() == null) {
                continue;
            }
            int mes = g.getFecha().getMonthValue();
            totales.put(mes, totales.getOrDefault(mes, 0.0) + g.getCantidad());
        }

        BarChart<String, Number> chart = view.getMesesChart();
        chart.getData().clear();
        BarChart.Series<String, Number> serie = new BarChart.Series<>();
        totales.entrySet().stream()
            .sorted(Comparator.comparingInt(Map.Entry::getKey))
            .forEach(entry -> {
                String label = Month.of(entry.getKey()).getDisplayName(TextStyle.SHORT, Locale.getDefault());
                serie.getData().add(new BarChart.Data<>(label, entry.getValue()));
            });
        chart.getData().add(serie);
    }
}
