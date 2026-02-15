package ASP.BanCroak.ui.visualizar;

import ASP.BanCroak.domain.Gasto;
import ASP.BanCroak.service.FilterState;
import ASP.BanCroak.service.GastosQueryService;
import ASP.BanCroak.ui.app.AppContext;
import ASP.BanCroak.ui.app.GastosStore;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class VisualizarViewModel {
    private final AppContext context;
    private final GastosStore store;
    private final FilterState filterState;

    private final ObservableList<Gasto> gastosFiltrados = FXCollections.observableArrayList();
    private final ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
    private final XYChart.Series<String, Number> barSeries = new XYChart.Series<>();
    private final ObservableMap<LocalDate, GastosQueryService.DayAggregate> calendarioData = FXCollections.observableHashMap();

    public VisualizarViewModel(AppContext context) {
        this.context = context;
        this.store = context.getGastosStore();
        this.filterState = context.getFilterState();

        registerListeners();
        recompute();
    }

    public GastosStore getStore() {
        return store;
    }

    public FilterState getFilterState() {
        return filterState;
    }

    public ObservableList<Gasto> getGastosFiltrados() {
        return gastosFiltrados;
    }

    public ObservableList<PieChart.Data> getPieData() {
        return pieData;
    }

    public XYChart.Series<String, Number> getBarSeries() {
        return barSeries;
    }

    public ObservableMap<LocalDate, GastosQueryService.DayAggregate> getCalendarioData() {
        return calendarioData;
    }

    private void registerListeners() {
        store.getGastos().addListener((ListChangeListener<Gasto>) c -> recompute());

        filterState.categoriaProperty().addListener((obs, o, n) -> recompute());
        filterState.desdeProperty().addListener((obs, o, n) -> recompute());
        filterState.hastaProperty().addListener((obs, o, n) -> recompute());
        filterState.mesProperty().addListener((obs, o, n) -> recompute());
    }

    private void recompute() {
        List<Gasto> filtrados = GastosQueryService.aplicarFiltros(store.snapshot(), filterState);
        gastosFiltrados.setAll(filtrados);

        Map<String, Double> totalesCategoria = GastosQueryService.totalPorCategoria(filtrados);
        updateBarSeries(totalesCategoria);
        updatePieData(totalesCategoria);

        calendarioData.clear();
        calendarioData.putAll(GastosQueryService.totalPorDia(filtrados));
    }

    private void updateBarSeries(Map<String, Double> totalesCategoria) {
        barSeries.getData().clear();
        List<Map.Entry<String, Double>> orden = totalesCategoria.entrySet().stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .collect(Collectors.toList());
        for (Map.Entry<String, Double> e : orden) {
            barSeries.getData().add(new XYChart.Data<>(e.getKey(), e.getValue()));
        }
    }

    private void updatePieData(Map<String, Double> totalesCategoria) {
        pieData.clear();
        List<Map.Entry<String, Double>> orden = totalesCategoria.entrySet().stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .collect(Collectors.toList());
        for (Map.Entry<String, Double> e : orden) {
            pieData.add(new PieChart.Data(e.getKey(), e.getValue()));
        }
    }
}
