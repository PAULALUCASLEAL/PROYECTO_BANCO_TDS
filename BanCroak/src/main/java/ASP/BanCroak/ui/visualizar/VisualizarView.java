package ASP.BanCroak.ui.visualizar;

import ASP.BanCroak.domain.Gasto;
import ASP.BanCroak.ui.app.SceneManager;
import ASP.BanCroak.ui.gastos.GastosTableFactory;
import ASP.BanCroak.ui.main.BarraMenuView;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class VisualizarView extends VBox {
    private final VisualizarViewModel viewModel;

    public VisualizarView(SceneManager sm, VisualizarTab initialTab) {
        this.viewModel = new VisualizarViewModel(sm.getContext());

        this.setSpacing(0);
        this.setAlignment(Pos.TOP_CENTER);
        this.setId("estilo_TablaView");
        this.getStylesheets().add(getClass().getResource("/estilos.css").toExternalForm());

        BarraMenuView barra = new BarraMenuView(sm);

        Label titulo = new Label(tituloPara(initialTab));
        titulo.getStyleClass().add("section-title");

        VBox contenido = new VBox(12);
        contenido.getChildren().add(construirContenido(sm, initialTab));
        VBox.setVgrow(contenido, Priority.ALWAYS);

        VBox contenedor = new VBox(12);
        contenedor.getChildren().add(titulo);
        if (initialTab != VisualizarTab.CALENDARIO) {
            GastosFilterPane filtros = new GastosFilterPane(sm.getContext(), viewModel.getStore(), viewModel.getFilterState());
            contenedor.getChildren().add(filtros);
        }
        contenedor.getChildren().add(contenido);
        contenedor.setPadding(new Insets(12, 20, 20, 20));
        VBox.setVgrow(contenedor, Priority.ALWAYS);

        this.getChildren().addAll(barra, contenedor);
    }

    private VBox construirContenido(SceneManager sm, VisualizarTab tab) {
        if (tab == null) {
            return buildTablaTab(sm);
        }
        switch (tab) {
            case BARRAS:
                return buildBarrasTab();
            case PIE:
                return buildPieTab();
            case CALENDARIO:
                return buildCalendarioTab(sm);
            case TABLA:
            default:
                return buildTablaTab(sm);
        }
    }

    private String tituloPara(VisualizarTab tab) {
        if (tab == null) {
            return "Visualizar gastos";
        }
        switch (tab) {
            case BARRAS:
                return "Gastos por categoría (barras)";
            case PIE:
                return "Distribución de gastos (circular)";
            case CALENDARIO:
                return "Calendario de gastos";
            case TABLA:
            default:
                return "Tabla de gastos";
        }
    }

    private VBox buildTablaTab(SceneManager sm) {
        TableView<Gasto> tabla = GastosTableFactory.crearTabla(sm.getContext(), viewModel.getStore());
        tabla.setItems(viewModel.getGastosFiltrados());

        Label resumen = new Label();
        resumen.getStyleClass().add("muted");
        viewModel.getGastosFiltrados().addListener((ListChangeListener<Gasto>) c -> actualizarResumen(resumen));
        actualizarResumen(resumen);

        VBox box = new VBox(10, resumen, tabla);
        box.getStyleClass().add("card");
        VBox.setVgrow(tabla, Priority.ALWAYS);
        return box;
    }

    private VBox buildBarrasTab() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setLegendVisible(false);
        chart.setAnimated(false);
        chart.getData().add(viewModel.getBarSeries());

        VBox box = new VBox(chart);
        box.getStyleClass().add("card");
        VBox.setVgrow(chart, Priority.ALWAYS);
        return box;
    }

    private VBox buildPieTab() {
        PieChart pieChart = new PieChart(viewModel.getPieData());
        pieChart.setLabelsVisible(true);
        pieChart.setLegendVisible(true);

        VBox box = new VBox(pieChart);
        box.getStyleClass().add("card");
        VBox.setVgrow(pieChart, Priority.ALWAYS);
        return box;
    }

    private VBox buildCalendarioTab(SceneManager sm) {
        ObjectProperty<YearMonth> mesActual = new SimpleObjectProperty<>(YearMonth.now());
        DateTimeFormatter formatoMes = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.of("es", "ES"));

        Button anterior = new Button("<");
        Button siguiente = new Button(">");
        Label mesLabel = new Label();
        mesLabel.getStyleClass().add("section-title");
        mesLabel.setMaxWidth(Double.MAX_VALUE);
        mesLabel.setAlignment(Pos.CENTER);

        HBox controlesMes = new HBox(12, anterior, mesLabel, siguiente);
        controlesMes.setAlignment(Pos.CENTER);
        HBox.setHgrow(mesLabel, Priority.ALWAYS);

        TableView<Gasto> tabla = GastosTableFactory.crearTabla(sm.getContext(), viewModel.getStore());
        FilteredList<Gasto> gastosMes = new FilteredList<>(viewModel.getStore().getGastos());
        tabla.setItems(gastosMes);

        Label resumen = new Label();
        resumen.getStyleClass().add("muted");

        Runnable actualizarMes = () -> {
            YearMonth mes = mesActual.get();
            mesLabel.setText(formatoMes.format(mes).toUpperCase(Locale.of("es", "ES")));
            gastosMes.setPredicate(g -> g.getFecha() != null && YearMonth.from(g.getFecha()).equals(mes));
            actualizarResumenMes(resumen, gastosMes);
        };

        anterior.setOnAction(e -> {
            mesActual.set(mesActual.get().minusMonths(1));
            actualizarMes.run();
        });
        siguiente.setOnAction(e -> {
            mesActual.set(mesActual.get().plusMonths(1));
            actualizarMes.run();
        });
        gastosMes.addListener((ListChangeListener<Gasto>) c -> actualizarResumenMes(resumen, gastosMes));

        actualizarMes.run();

        VBox box = new VBox(10, controlesMes, resumen, tabla);
        box.getStyleClass().add("card");
        VBox.setVgrow(tabla, Priority.ALWAYS);
        return box;
    }

    private void actualizarResumen(Label resumen) {
        int total = viewModel.getGastosFiltrados().size();
        double suma = viewModel.getGastosFiltrados().stream().mapToDouble(Gasto::getCantidad).sum();
        resumen.setText("Gastos filtrados: " + total + " · Total: " + String.format(java.util.Locale.ROOT, "€%.2f", suma));
    }

    private void actualizarResumenMes(Label resumen, ObservableList<Gasto> gastosMes) {
        int total = gastosMes.size();
        double suma = gastosMes.stream().mapToDouble(Gasto::getCantidad).sum();
        resumen.setText("Gastos del mes: " + total + " · Total: " + String.format(java.util.Locale.ROOT, "€%.2f", suma));
    }

}
