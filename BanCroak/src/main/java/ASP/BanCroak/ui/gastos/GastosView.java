package ASP.BanCroak.ui.gastos;

import ASP.BanCroak.domain.AlertaGasto;
import ASP.BanCroak.domain.Gasto;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class GastosView {
    private final BorderPane root;

    private final TextField cantidadField;
    private final DatePicker fechaPicker;
    private final ComboBox<String> categoriaCombo;
    private final ComboBox<String> pagadorCombo;
    private final TextField idCuentaField;
    private final Button addGastoButton;
    private final Button editarButton;
    private final Button guardarCambiosButton;
    private final Button cancelarEdicionButton;

    private final TextField nuevaCategoriaField;
    private final Button addCategoriaButton;

    private final ComboBox<String> filtroCategoriaCombo;
    private final TextField filtroMesesField;
    private final DatePicker filtroDesdePicker;
    private final DatePicker filtroHastaPicker;
    private final Button aplicarFiltroButton;
    private final Button limpiarFiltroButton;
    private final Button graficasButton;
    private final Button volverButton;
    private final Button historialButton;
    private final Label cuentaLabel;

    private final TableView<Gasto> tablaGastos;
    private final Button eliminarSeleccionadoButton;

    private final TableView<RepartoRow> tablaReparto;
    private final Label totalRepartoLabel;

    private final ComboBox<AlertaGasto.Periodo> periodoAlertaCombo;
    private final TextField limiteAlertaField;
    private final ComboBox<String> categoriaAlertaCombo;
    private final Button crearAlertaButton;
    private final TableView<AlertaGasto> tablaAlertas;
    private final TableColumn<AlertaGasto, Void> colAccionAlerta;

    private final Label feedbackLabel;

    public GastosView() {
        this.root = new BorderPane();
        this.cantidadField = new TextField();
        this.fechaPicker = new DatePicker();
        this.categoriaCombo = new ComboBox<>();
        this.pagadorCombo = new ComboBox<>();
        this.idCuentaField = new TextField();
        this.addGastoButton = new Button("Añadir gasto");
        this.editarButton = new Button("Editar");
        this.guardarCambiosButton = new Button("Guardar cambios");
        this.cancelarEdicionButton = new Button("Cancelar");

        this.nuevaCategoriaField = new TextField();
        this.addCategoriaButton = new Button("Añadir categoría");

        this.filtroCategoriaCombo = new ComboBox<>();
        this.filtroMesesField = new TextField();
        this.filtroDesdePicker = new DatePicker();
        this.filtroHastaPicker = new DatePicker();
        this.aplicarFiltroButton = new Button("Aplicar filtros");
        this.limpiarFiltroButton = new Button("Limpiar filtros");
        this.graficasButton = new Button("Gráficas");
        this.volverButton = new Button("Volver");
        this.historialButton = new Button("Historial");
        this.cuentaLabel = new Label();

        this.tablaGastos = new TableView<>();
        this.eliminarSeleccionadoButton = new Button("Eliminar seleccionado");

        this.tablaReparto = new TableView<>();
        this.totalRepartoLabel = new Label("Total visible: 0.00 €");

        this.periodoAlertaCombo = new ComboBox<>();
        this.limiteAlertaField = new TextField();
        this.categoriaAlertaCombo = new ComboBox<>();
        this.crearAlertaButton = new Button("Crear alerta");
        this.tablaAlertas = new TableView<>();
        this.colAccionAlerta = new TableColumn<>("Acción");

        this.feedbackLabel = new Label();

        build();
    }

    private void build() {
        root.setPadding(new Insets(18));
        root.getStyleClass().add("app-root");

        totalRepartoLabel.getStyleClass().add("summary-label");
        cuentaLabel.getStyleClass().add("section-title");

        addGastoButton.getStyleClass().add("primary-button");
        editarButton.getStyleClass().add("secondary-button");
        guardarCambiosButton.getStyleClass().add("primary-button");
        cancelarEdicionButton.getStyleClass().add("ghost-button");
        addCategoriaButton.getStyleClass().add("secondary-button");
        aplicarFiltroButton.getStyleClass().add("secondary-button");
        limpiarFiltroButton.getStyleClass().add("ghost-button");
        graficasButton.getStyleClass().add("secondary-button");
        eliminarSeleccionadoButton.getStyleClass().add("danger-button");
        volverButton.getStyleClass().add("ghost-button");
        historialButton.getStyleClass().add("secondary-button");
        crearAlertaButton.getStyleClass().add("primary-button");

        VBox left = new VBox(12, buildGastoCard(), buildAlertasCard());
        left.setAlignment(Pos.TOP_LEFT);
        VBox.setVgrow(left, Priority.ALWAYS);

        buildTablaGastos();
        buildTablaReparto();

        VBox repartoCard = new VBox(8, buildRepartoHeader(), totalRepartoLabel, tablaReparto);
        repartoCard.getStyleClass().add("panel-card");

        VBox right = new VBox(12, repartoCard, tablaGastos, eliminarSeleccionadoButton);
        right.setAlignment(Pos.TOP_LEFT);
        VBox.setVgrow(tablaGastos, Priority.ALWAYS);
        VBox.setVgrow(right, Priority.ALWAYS);

        SplitPane split = new SplitPane(left, right);
        split.setDividerPositions(0.45);
        split.setStyle("-fx-background-color: transparent;");

        root.setCenter(split);
    }

    private VBox buildGastoCard() {
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(8);

        cantidadField.setPromptText("Cantidad");
        cantidadField.getStyleClass().add("input-field");
        fechaPicker.setPromptText("Fecha");
        categoriaCombo.setEditable(true);
        categoriaCombo.setPromptText("Categoría");
        categoriaCombo.getStyleClass().add("input-field");
        pagadorCombo.setPromptText("Pagador");
        pagadorCombo.getStyleClass().add("input-field");
        pagadorCombo.setEditable(false);
        idCuentaField.setPromptText("Id Cuenta");
        idCuentaField.getStyleClass().add("input-field");

        form.add(new Label("Cantidad"), 0, 0);
        form.add(cantidadField, 1, 0);
        form.add(new Label("Fecha"), 0, 1);
        form.add(fechaPicker, 1, 1);
        form.add(new Label("Categoría"), 0, 2);
        form.add(categoriaCombo, 1, 2);
        form.add(new Label("Pagador"), 0, 3);
        form.add(pagadorCombo, 1, 3);
        form.add(new Label("Id Cuenta"), 0, 4);
        form.add(idCuentaField, 1, 4);

        HBox acciones = new HBox(10, addGastoButton, editarButton, guardarCambiosButton, cancelarEdicionButton, feedbackLabel);
        acciones.setAlignment(Pos.CENTER_LEFT);

        nuevaCategoriaField.setPromptText("Nueva categoría");
        nuevaCategoriaField.getStyleClass().add("input-field");
        HBox categoriaRow = new HBox(10, new Label("Nueva categoría"), nuevaCategoriaField, addCategoriaButton);
        categoriaRow.setAlignment(Pos.CENTER_LEFT);

        filtroCategoriaCombo.setPromptText("Categoría");
        filtroCategoriaCombo.getStyleClass().add("input-field");
        filtroMesesField.setPromptText("Meses (coma)");
        filtroMesesField.getStyleClass().add("input-field");
        filtroDesdePicker.setPromptText("Desde");
        filtroHastaPicker.setPromptText("Hasta");

        HBox filtrosRow1 = new HBox(10, new Label("Filtro categoría"), filtroCategoriaCombo);
        filtrosRow1.setAlignment(Pos.CENTER_LEFT);

        HBox filtrosRow2 = new HBox(10, new Label("Filtro meses"), filtroMesesField);
        filtrosRow2.setAlignment(Pos.CENTER_LEFT);

        HBox filtrosRow3 = new HBox(10, new Label("Filtro fechas"), filtroDesdePicker, filtroHastaPicker);
        filtrosRow3.setAlignment(Pos.CENTER_LEFT);

        HBox filtrosRow4 = new HBox(10, aplicarFiltroButton, limpiarFiltroButton, graficasButton, historialButton);
        filtrosRow4.setAlignment(Pos.CENTER_LEFT);

        HBox header = new HBox(10, volverButton, cuentaLabel);
        header.setAlignment(Pos.CENTER_LEFT);
        header.getStyleClass().add("header-bar");

        VBox left = new VBox(12, header, form, acciones, categoriaRow, filtrosRow1, filtrosRow2, filtrosRow3, filtrosRow4);
        left.getStyleClass().add("panel-card");
        VBox.setVgrow(left, Priority.ALWAYS);
        return left;
    }

    private VBox buildAlertasCard() {
        Label title = new Label("Alertas");
        title.getStyleClass().add("section-title");

        periodoAlertaCombo.getItems().addAll(AlertaGasto.Periodo.SEMANAL, AlertaGasto.Periodo.MENSUAL);
        periodoAlertaCombo.setPromptText("Periodo");
        periodoAlertaCombo.getStyleClass().add("input-field");

        limiteAlertaField.setPromptText("Límite (€)");
        limiteAlertaField.getStyleClass().add("input-field");

        categoriaAlertaCombo.setEditable(true);
        categoriaAlertaCombo.setPromptText("Categoría (opcional)");
        categoriaAlertaCombo.getStyleClass().add("input-field");

        HBox crearRow = new HBox(10, periodoAlertaCombo, limiteAlertaField, categoriaAlertaCombo, crearAlertaButton);
        crearRow.setAlignment(Pos.CENTER_LEFT);

        buildTablaAlertas();
        VBox.setVgrow(tablaAlertas, Priority.ALWAYS);

        VBox card = new VBox(10, title, crearRow, tablaAlertas);
        card.getStyleClass().add("panel-card");
        VBox.setVgrow(card, Priority.ALWAYS);
        return card;
    }

    private HBox buildRepartoHeader() {
        Label title = new Label("Reparto por porcentaje");
        title.getStyleClass().add("section-title");
        HBox box = new HBox(title);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    private void buildTablaGastos() {
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

    private void buildTablaReparto() {
        TableColumn<RepartoRow, String> colMiembro = new TableColumn<>("Miembro");
        colMiembro.setCellValueFactory(new PropertyValueFactory<>("miembro"));

        TableColumn<RepartoRow, Double> colPorcentaje = new TableColumn<>("%" );
        colPorcentaje.setCellValueFactory(new PropertyValueFactory<>("porcentaje"));
        colPorcentaje.setCellFactory(col -> new javafx.scene.control.TableCell<>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else if (Math.abs(value - Math.round(value)) < 0.001) {
                    setText(String.format("%.0f%%", value));
                } else {
                    setText(String.format("%.2f%%", value));
                }
            }
        });

        TableColumn<RepartoRow, Double> colDebe = new TableColumn<>("Debe pagar");
        colDebe.setCellValueFactory(new PropertyValueFactory<>("debePagar"));
        colDebe.setCellFactory(col -> new javafx.scene.control.TableCell<>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f €", value));
                }
            }
        });

        tablaReparto.getColumns().addAll(colMiembro, colPorcentaje, colDebe);
        tablaReparto.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tablaReparto.setPrefHeight(180);
    }

    private void buildTablaAlertas() {
        TableColumn<AlertaGasto, Integer> colId = new TableColumn<>("Id");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setMaxWidth(70);

        TableColumn<AlertaGasto, String> colPeriodo = new TableColumn<>("Periodo");
        colPeriodo.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getPeriodo().name()));

        TableColumn<AlertaGasto, Double> colLimite = new TableColumn<>("Límite");
        colLimite.setCellValueFactory(new PropertyValueFactory<>("limite"));
        colLimite.setCellFactory(col -> new javafx.scene.control.TableCell<>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f €", value));
                }
            }
        });

        TableColumn<AlertaGasto, String> colCategoria = new TableColumn<>("Categoría");
        colCategoria.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getCategoriaDisplay()));

        TableColumn<AlertaGasto, String> colActiva = new TableColumn<>("Estado");
        colActiva.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().isActiva() ? "Activa" : "Pausada"));

        colAccionAlerta.setMinWidth(110);

        tablaAlertas.getColumns().addAll(colId, colPeriodo, colLimite, colCategoria, colActiva, colAccionAlerta);
        tablaAlertas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tablaAlertas.setPrefHeight(180);
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

    public ComboBox<String> getPagadorCombo() {
        return pagadorCombo;
    }

    public TextField getIdCuentaField() {
        return idCuentaField;
    }

    public Button getAddGastoButton() {
        return addGastoButton;
    }

    public Button getEditarButton() {
        return editarButton;
    }

    public Button getGuardarCambiosButton() {
        return guardarCambiosButton;
    }

    public Button getCancelarEdicionButton() {
        return cancelarEdicionButton;
    }

    public TextField getNuevaCategoriaField() {
        return nuevaCategoriaField;
    }

    public Button getAddCategoriaButton() {
        return addCategoriaButton;
    }

    public ComboBox<String> getFiltroCategoriaCombo() {
        return filtroCategoriaCombo;
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

    public Button getHistorialButton() {
        return historialButton;
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

    public TableView<RepartoRow> getTablaReparto() {
        return tablaReparto;
    }

    public Label getTotalRepartoLabel() {
        return totalRepartoLabel;
    }

    public ComboBox<AlertaGasto.Periodo> getPeriodoAlertaCombo() {
        return periodoAlertaCombo;
    }

    public TextField getLimiteAlertaField() {
        return limiteAlertaField;
    }

    public ComboBox<String> getCategoriaAlertaCombo() {
        return categoriaAlertaCombo;
    }

    public Button getCrearAlertaButton() {
        return crearAlertaButton;
    }

    public TableView<AlertaGasto> getTablaAlertas() {
        return tablaAlertas;
    }

    public TableColumn<AlertaGasto, Void> getColAccionAlerta() {
        return colAccionAlerta;
    }

    public Label getFeedbackLabel() {
        return feedbackLabel;
    }
}
