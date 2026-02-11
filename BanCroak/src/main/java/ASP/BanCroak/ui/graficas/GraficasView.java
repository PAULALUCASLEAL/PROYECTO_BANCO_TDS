package ASP.BanCroak.ui.graficas;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GraficasView {
    private final BorderPane root;
    private final PieChart categoriasChart;
    private final BarChart<String, Number> mesesChart;
    private final Button volverButton;
    private final Label tituloLabel;

    public GraficasView() {
        this.root = new BorderPane();
        this.volverButton = new Button("Volver");
        this.tituloLabel = new Label("Gráficas");

        this.categoriasChart = new PieChart();
        this.categoriasChart.setTitle("Gasto por categoría");

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        this.mesesChart = new BarChart<>(xAxis, yAxis);
        this.mesesChart.setTitle("Gasto por mes");
        xAxis.setLabel("Mes");
        yAxis.setLabel("Total");

        build();
    }

    private void build() {
        root.setPadding(new Insets(16));

        HBox top = new HBox(10, volverButton, tituloLabel);
        top.setAlignment(Pos.CENTER_LEFT);
        root.setTop(top);

        VBox center = new VBox(16, categoriasChart, mesesChart);
        center.setAlignment(Pos.CENTER);
        root.setCenter(center);
    }

    public Parent getRoot() {
        return root;
    }

    public PieChart getCategoriasChart() {
        return categoriasChart;
    }

    public BarChart<String, Number> getMesesChart() {
        return mesesChart;
    }

    public Button getVolverButton() {
        return volverButton;
    }

    public Label getTituloLabel() {
        return tituloLabel;
    }
}
