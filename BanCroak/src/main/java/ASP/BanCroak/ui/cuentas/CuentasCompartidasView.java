package ASP.BanCroak.ui.cuentas;

import ASP.BanCroak.domain.Cuenta;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;

public class CuentasCompartidasView {
    private final BorderPane root;
    private final TextField nombreCuentaField;
    private final TextField miembroNombreField;
    private final Spinner<Integer> porcentajeSpinner;
    private final Button addMiembroButton;
    private final Button repartoEquitativoButton;
    private final TableView<MiembroPorcentajeRow> tablaMiembros;
    private final TableColumn<MiembroPorcentajeRow, Void> colEliminarMiembro;
    private final Label totalPorcentajeLabel;
    private final Button crearButton;
    private final Button abrirButton;
    private final Button volverButton;
    private final TableView<Cuenta> tablaCuentas;
    private final Label feedbackLabel;

    public CuentasCompartidasView() {
        this.root = new BorderPane();
        this.nombreCuentaField = new TextField();
        this.miembroNombreField = new TextField();
        this.porcentajeSpinner = new Spinner<>();
        this.addMiembroButton = new Button("Añadir miembro");
        this.repartoEquitativoButton = new Button("Reparto equitativo");
        this.tablaMiembros = new TableView<>();
        this.colEliminarMiembro = new TableColumn<>("Acción");
        this.totalPorcentajeLabel = new Label("Total: 0%");
        this.crearButton = new Button("Crear cuenta");
        this.abrirButton = new Button("Abrir cuenta");
        this.volverButton = new Button("Volver");
        this.tablaCuentas = new TableView<>();
        this.feedbackLabel = new Label();

        build();
    }

    private void build() {
        root.setPadding(new Insets(18));
        root.getStyleClass().add("app-root");

        crearButton.getStyleClass().add("primary-button");
        addMiembroButton.getStyleClass().add("secondary-button");
        repartoEquitativoButton.getStyleClass().add("secondary-button");
        abrirButton.getStyleClass().add("primary-button");
        volverButton.getStyleClass().add("ghost-button");
        feedbackLabel.getStyleClass().add("summary-label");

        Label titulo = new Label("Cuentas compartidas");
        titulo.getStyleClass().add("view-title");

        HBox header = new HBox(12, volverButton, titulo);
        header.setAlignment(Pos.CENTER_LEFT);
        header.getStyleClass().add("header-bar");
        root.setTop(header);

        VBox left = buildCrearCuentaPanel();
        VBox right = buildListadoPanel();

        SplitPane split = new SplitPane(left, right);
        split.setDividerPositions(0.48);
        split.setStyle("-fx-background-color: transparent;");

        root.setCenter(split);
    }

    private VBox buildCrearCuentaPanel() {
        Label section = new Label("Nueva cuenta");
        section.getStyleClass().add("section-title");

        nombreCuentaField.setPromptText("Nombre de la cuenta");
        nombreCuentaField.getStyleClass().add("input-field");

        HBox nombreRow = new HBox(10, new Label("Nombre"), nombreCuentaField);
        nombreRow.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(nombreCuentaField, Priority.ALWAYS);

        Label addTitle = new Label("Añadir miembro");
        addTitle.getStyleClass().add("subsection-title");

        miembroNombreField.setPromptText("Nombre del miembro");
        miembroNombreField.getStyleClass().add("input-field");

        porcentajeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 50));
        porcentajeSpinner.setEditable(true);
        porcentajeSpinner.getStyleClass().add("input-field");
        porcentajeSpinner.setPrefWidth(100);

        HBox addRow = new HBox(10, miembroNombreField, porcentajeSpinner, addMiembroButton);
        addRow.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(miembroNombreField, Priority.ALWAYS);

        buildTablaMiembros();
        VBox.setVgrow(tablaMiembros, Priority.ALWAYS);

        totalPorcentajeLabel.getStyleClass().add("summary-label");
        HBox acciones = new HBox(12, crearButton, repartoEquitativoButton, totalPorcentajeLabel, feedbackLabel);
        acciones.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(12, section, nombreRow, addTitle, addRow, tablaMiembros, acciones);
        card.getStyleClass().add("panel-card");
        VBox.setVgrow(card, Priority.ALWAYS);
        return card;
    }

    private VBox buildListadoPanel() {
        Label section = new Label("Cuentas creadas");
        section.getStyleClass().add("section-title");

        buildTablaCuentas();
        tablaCuentas.setPlaceholder(new Label("Sin cuentas compartidas"));
        VBox.setVgrow(tablaCuentas, Priority.ALWAYS);

        HBox acciones = new HBox(10, abrirButton);
        acciones.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(12, section, tablaCuentas, acciones);
        card.getStyleClass().add("panel-card");
        VBox.setVgrow(card, Priority.ALWAYS);
        return card;
    }

    private void buildTablaMiembros() {
        TableColumn<MiembroPorcentajeRow, String> colNombre = new TableColumn<>("Miembro");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<MiembroPorcentajeRow, Integer> colPorcentaje = new TableColumn<>("Porcentaje");
        colPorcentaje.setCellValueFactory(new PropertyValueFactory<>("porcentaje"));

        colEliminarMiembro.setMinWidth(90);

        tablaMiembros.getColumns().addAll(colNombre, colPorcentaje, colEliminarMiembro);
        tablaMiembros.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tablaMiembros.setPrefHeight(220);
    }

    private void buildTablaCuentas() {
        TableColumn<Cuenta, Integer> colId = new TableColumn<>("Id");
        colId.setCellValueFactory(new PropertyValueFactory<>("idCuenta"));
        colId.setMaxWidth(80);

        TableColumn<Cuenta, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCuenta"));

        TableColumn<Cuenta, String> colMiembros = new TableColumn<>("Miembros");
        colMiembros.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
            String.join(", ", cell.getValue().getMiembros())
        ));

        TableColumn<Cuenta, String> colReparto = new TableColumn<>("Reparto");
        colReparto.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
            cell.getValue().getPorcentajes().entrySet().stream()
                .map(e -> e.getKey() + " " + formatPorcentaje(e.getValue()))
                .reduce((a, b) -> a + " | " + b)
                .orElse("")
        ));

        tablaCuentas.getColumns().addAll(colId, colNombre, colMiembros, colReparto);
        tablaCuentas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    private String formatPorcentaje(double valor) {
        if (Math.abs(valor - Math.round(valor)) < 0.001) {
            return String.format("%.0f%%", valor);
        }
        return String.format("%.2f%%", valor);
    }

    public Parent getRoot() {
        return root;
    }

    public TextField getNombreCuentaField() {
        return nombreCuentaField;
    }

    public TextField getMiembroNombreField() {
        return miembroNombreField;
    }

    public Spinner<Integer> getPorcentajeSpinner() {
        return porcentajeSpinner;
    }

    public Button getAddMiembroButton() {
        return addMiembroButton;
    }

    public Button getRepartoEquitativoButton() {
        return repartoEquitativoButton;
    }

    public TableView<MiembroPorcentajeRow> getTablaMiembros() {
        return tablaMiembros;
    }

    public TableColumn<MiembroPorcentajeRow, Void> getColEliminarMiembro() {
        return colEliminarMiembro;
    }

    public Label getTotalPorcentajeLabel() {
        return totalPorcentajeLabel;
    }

    public Button getCrearButton() {
        return crearButton;
    }

    public Button getAbrirButton() {
        return abrirButton;
    }

    public Button getVolverButton() {
        return volverButton;
    }

    public TableView<Cuenta> getTablaCuentas() {
        return tablaCuentas;
    }

    public Label getFeedbackLabel() {
        return feedbackLabel;
    }
}
