package ASP.BanCroak.ui.visualizar;

import ASP.BanCroak.domain.Gasto;
import ASP.BanCroak.service.GastosQueryService;
import ASP.BanCroak.ui.app.SceneManager;
import ASP.BanCroak.ui.gastos.GastoEditorDialog;
import ASP.BanCroak.ui.gastos.GastosTableFactory;
import ASP.BanCroak.ui.main.BarraMenuView;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class VisualizarView extends VBox {
    private final VisualizarViewModel viewModel;

    public VisualizarView(SceneManager sm, VisualizarTab initialTab) {
        this.viewModel = new VisualizarViewModel(sm.getContext());

        this.setSpacing(0);
        this.setAlignment(Pos.TOP_CENTER);
        this.setId("estilo_TablaView");
        this.getStylesheets().add(getClass().getResource("/estilos.css").toExternalForm());

        BarraMenuView barra = new BarraMenuView(sm);
        GastosFilterPane filtros = new GastosFilterPane(sm.getContext(), viewModel.getStore(), viewModel.getFilterState());

        Label titulo = new Label(tituloPara(initialTab));
        titulo.getStyleClass().add("section-title");

        VBox contenido = new VBox(12);
        contenido.getChildren().add(construirContenido(sm, initialTab));
        VBox.setVgrow(contenido, Priority.ALWAYS);

        VBox contenedor = new VBox(12, titulo, filtros, contenido);
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
                VBox wrap = new VBox(buildCalendarioTab(sm));
                wrap.getStyleClass().add("card");
                VBox.setVgrow(wrap, Priority.ALWAYS);
                return wrap;
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

    private BorderPane buildCalendarioTab(SceneManager sm) {
        BorderPane layout = new BorderPane();

        DatePicker calendario = new DatePicker(LocalDate.now());
        calendario.setShowWeekNumbers(false);
        calendario.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                    return;
                }
                GastosQueryService.DayAggregate agg = viewModel.getCalendarioData().get(date);
                if (agg != null) {
                    setText(date.getDayOfMonth() + "\n" + String.format(java.util.Locale.ROOT, "€%.2f (%d)", agg.total, agg.count));
                } else {
                    setText(String.valueOf(date.getDayOfMonth()));
                }
            }
        });

        ListView<Gasto> listaDia = new ListView<>();
        listaDia.setCellFactory(lv -> new ListCell<>() {
            private final Button editar = new Button("Editar");
            private final Button eliminar = new Button("Eliminar");
            private final HBox acciones = new HBox(6, editar, eliminar);
            private final Label texto = new Label();
            private final VBox cont = new VBox(4, texto, acciones);

            {
                editar.setOnAction(e -> {
                    Gasto gasto = getItem();
                    if (gasto != null) {
                        new GastoEditorDialog(sm.getContext(), viewModel.getStore(), gasto);
                    }
                });
                eliminar.setOnAction(e -> {
                    Gasto gasto = getItem();
                    if (gasto != null && GastosTableFactory.confirmarEliminar()) {
                        viewModel.getStore().eliminarGasto(gasto);
                    }
                });
            }

            @Override
            protected void updateItem(Gasto gasto, boolean empty) {
                super.updateItem(gasto, empty);
                if (empty || gasto == null) {
                    setGraphic(null);
                    return;
                }
                texto.setText(formatearGasto(gasto));
                setGraphic(cont);
            }
        });

        ObservableList<Gasto> listaFiltrada = viewModel.getGastosFiltrados();
        FilteredList<Gasto> gastosDia = new FilteredList<>(listaFiltrada, g -> false);
        listaDia.setItems(gastosDia);

        calendario.valueProperty().addListener((obs, o, n) -> {
            gastosDia.setPredicate(g -> n != null && n.equals(g.getFecha()));
        });

        if (calendario.getValue() != null) {
            gastosDia.setPredicate(g -> calendario.getValue().equals(g.getFecha()));
        }

        viewModel.getCalendarioData().addListener((javafx.collections.MapChangeListener<LocalDate, GastosQueryService.DayAggregate>) c -> calendario.setDayCellFactory(calendario.getDayCellFactory()));

        VBox right = new VBox(8, new Label("Gastos del día"), listaDia);
        VBox.setVgrow(listaDia, Priority.ALWAYS);

        layout.setLeft(calendario);
        layout.setCenter(right);
        BorderPane.setMargin(calendario, new Insets(0, 20, 0, 0));
        BorderPane.setMargin(right, new Insets(0, 0, 0, 20));
        return layout;
    }

    private void actualizarResumen(Label resumen) {
        int total = viewModel.getGastosFiltrados().size();
        double suma = viewModel.getGastosFiltrados().stream().mapToDouble(Gasto::getCantidad).sum();
        resumen.setText("Gastos filtrados: " + total + " · Total: " + String.format(java.util.Locale.ROOT, "€%.2f", suma));
    }

    private String formatearGasto(Gasto gasto) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return gasto.getFecha().format(fmt) + " · " + gasto.getCategoria() + " · " + gasto.getPagador() + " · " + String.format(java.util.Locale.ROOT, "€%.2f", gasto.getCantidad());
    }
}
