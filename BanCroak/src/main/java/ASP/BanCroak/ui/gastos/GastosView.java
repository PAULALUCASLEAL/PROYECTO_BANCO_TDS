package ASP.BanCroak.ui.gastos;

import ASP.BanCroak.domain.Gasto;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GastosView {
    private final BorderPane root;

    private final TextField cantidadField;
    private final DatePicker fechaPicker;
    private final ComboBox<String> categoriaCombo;
    private final TextField pagadorField;
    private final TextField idCuentaField;
    private final Button addGastoButton;

    private final TextField nuevaCategoriaField;
    private final Button addCategoriaButton;

    private final TextField filtroCategoriasField;
    private final TextField filtroMesesField;
    private final DatePicker filtroDesdePicker;
    private final DatePicker filtroHastaPicker;
    private final Button aplicarFiltroButton;
    private final Button limpiarFiltroButton;
    private final Button graficasButton;
    private final Button volverButton;
    private final Label cuentaLabel;

    private final TableView<Gasto> tablaGastos;
    private final Button eliminarSeleccionadoButton;

    private final Label feedbackLabel;

    public GastosView() {
        this.root = new BorderPane();
        this.cantidadField = new TextField();
        this.fechaPicker = new DatePicker();
        this.categoriaCombo = new ComboBox<>();
        this.pagadorField = new TextField();
        this.idCuentaField = new TextField();
        this.addGastoButton = new Button("Añadir gasto");

        this.nuevaCategoriaField = new TextField();
        this.addCategoriaButton = new Button("Añadir categoría");

        this.filtroCategoriasField = new TextField();
        this.filtroMesesField = new TextField();
        this.filtroDesdePicker = new DatePicker();
        this.filtroHastaPicker = new DatePicker();
        this.aplicarFiltroButton = new Button("Aplicar filtros");
        this.limpiarFiltroButton = new Button("Limpiar filtros");
        this.graficasButton = new Button("Gráficas");
        this.volverButton = new Button("Volver");
        this.cuentaLabel = new Label();

        this.tablaGastos = new TableView<>();
        this.eliminarSeleccionadoButton = new Button("Eliminar seleccionado");

        this.feedbackLabel = new Label();

        build();
    }

    private void build() {
        root.setPadding(new Insets(16));

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(8);

        cantidadField.setPromptText("Cantidad");
        fechaPicker.setPromptText("Fecha");
        categoriaCombo.setEditable(true);
        categoriaCombo.setPromptText("Categoría");
        pagadorField.setPromptText("Pagador");
        idCuentaField.setPromptText("Id Cuenta");

        form.add(new Label("Cantidad"), 0, 0);
        form.add(cantidadField, 1, 0);
        form.add(new Label("Fecha"), 0, 1);
        form.add(fechaPicker, 1, 1);
        form.add(new Label("Categoría"), 0, 2);
        form.add(categoriaCombo, 1, 2);
        form.add(new Label("Pagador"), 0, 3);
        form.add(pagadorField, 1, 3);
        form.add(new Label("Id Cuenta"), 0, 4);
        form.add(idCuentaField, 1, 4);

        HBox addRow = new HBox(10, addGastoButton, feedbackLabel);
        addRow.setAlignment(Pos.CENTER_LEFT);

        HBox categoriaRow = new HBox(10, new Label("Nueva categoría"), nuevaCategoriaField, addCategoriaButton);
        categoriaRow.setAlignment(Pos.CENTER_LEFT);

        filtroCategoriasField.setPromptText("Categorías (coma)");
        filtroMesesField.setPromptText("Meses (coma)");
        filtroDesdePicker.setPromptText("Desde");
        filtroHastaPicker.setPromptText("Hasta");

        HBox filtrosRow1 = new HBox(10, new Label("Filtro categorías"), filtroCategoriasField);
        filtrosRow1.setAlignment(Pos.CENTER_LEFT);

        HBox filtrosRow2 = new HBox(10, new Label("Filtro meses"), filtroMesesField);
        filtrosRow2.setAlignment(Pos.CENTER_LEFT);

        HBox filtrosRow3 = new HBox(10, new Label("Filtro fechas"), filtroDesdePicker, filtroHastaPicker);
        filtrosRow3.setAlignment(Pos.CENTER_LEFT);

        HBox filtrosRow4 = new HBox(10, aplicarFiltroButton, limpiarFiltroButton, graficasButton);
        filtrosRow4.setAlignment(Pos.CENTER_LEFT);

        HBox header = new HBox(10, volverButton, cuentaLabel);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox left = new VBox(12, header, form, addRow, categoriaRow, filtrosRow1, filtrosRow2, filtrosRow3, filtrosRow4);
        left.setPadding(new Insets(0, 16, 0, 0));

        buildTabla();

        VBox right = new VBox(10, tablaGastos, eliminarSeleccionadoButton);
        right.setAlignment(Pos.TOP_LEFT);

        root.setLeft(left);
        root.setCenter(right);
    }

    private void buildTabla() {
        TableColumn<Gasto, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
            cell.getValue().getFecha() == null ? "" : cell.getValue().getFecha().toString()
        ));

        TableColumn<Gasto, String> colCategoria = new TableColumn<>("Categoría");
        colCategoria.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getCategoria()));

        TableColumn<Gasto, Double> colCantidad = new TableColumn<>("Cantidad");
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        TableColumn<Gasto, String> colPagador = new TableColumn<>("Pagador");
        colPagador.setCellValueFactory(new PropertyValueFactory<>("pagador"));

        TableColumn<Gasto, Integer> colIdCuenta = new TableColumn<>("IdCuenta");
        colIdCuenta.setCellValueFactory(new PropertyValueFactory<>("IDCuenta"));

        TableColumn<Gasto, Integer> colIdGasto = new TableColumn<>("IdGasto");
        colIdGasto.setCellValueFactory(new PropertyValueFactory<>("ID"));

        tablaGastos.getColumns().addAll(colFecha, colCategoria, colCantidad, colPagador, colIdCuenta, colIdGasto);
        tablaGastos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    public Parent getRoot() {
        return root;
    }

    public TextField getCantidadField() {
        return cantidadField;
    }

    public DatePicker getFechaPicker() {
        return fechaPicker;
    }

    public ComboBox<String> getCategoriaCombo() {
        return categoriaCombo;
    }

    public TextField getPagadorField() {
        return pagadorField;
    }

    public TextField getIdCuentaField() {
        return idCuentaField;
    }

    public Button getAddGastoButton() {
        return addGastoButton;
    }

    public TextField getNuevaCategoriaField() {
        return nuevaCategoriaField;
    }

    public Button getAddCategoriaButton() {
        return addCategoriaButton;
    }

    public TextField getFiltroCategoriasField() {
        return filtroCategoriasField;
    }

    public TextField getFiltroMesesField() {
        return filtroMesesField;
    }

    public DatePicker getFiltroDesdePicker() {
        return filtroDesdePicker;
    }

    public DatePicker getFiltroHastaPicker() {
        return filtroHastaPicker;
    }

    public Button getAplicarFiltroButton() {
        return aplicarFiltroButton;
    }

    public Button getLimpiarFiltroButton() {
        return limpiarFiltroButton;
    }

    public Button getGraficasButton() {
        return graficasButton;
    }

    public Button getVolverButton() {
        return volverButton;
    }

    public Label getCuentaLabel() {
        return cuentaLabel;
    }

    public void setIdCuenta(int idCuenta) {
        idCuentaField.setText(String.valueOf(idCuenta));
    }

    public void setIdCuentaEditable(boolean editable) {
        idCuentaField.setEditable(editable);
    }

    public TableView<Gasto> getTablaGastos() {
        return tablaGastos;
    }

    public Button getEliminarSeleccionadoButton() {
        return eliminarSeleccionadoButton;
    }

    public Label getFeedbackLabel() {
        return feedbackLabel;
    }
}
