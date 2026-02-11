package ASP.BanCroak.ui.cuentas;

import ASP.BanCroak.domain.Cuenta;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CuentasCompartidasView {
    private final BorderPane root;
    private final TextField nombreField;
    private final TextField miembrosField;
    private final TextField porcentajesField;
    private final Button crearButton;
    private final Button abrirButton;
    private final Button volverButton;
    private final TableView<Cuenta> tablaCuentas;
    private final Label feedbackLabel;

    public CuentasCompartidasView() {
        this.root = new BorderPane();
        this.nombreField = new TextField();
        this.miembrosField = new TextField();
        this.porcentajesField = new TextField();
        this.crearButton = new Button("Crear cuenta");
        this.abrirButton = new Button("Abrir cuenta");
        this.volverButton = new Button("Volver");
        this.tablaCuentas = new TableView<>();
        this.feedbackLabel = new Label();

        build();
    }

    private void build() {
        root.setPadding(new Insets(16));

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(8);

        nombreField.setPromptText("Nombre cuenta");
        miembrosField.setPromptText("Miembros (coma)");
        porcentajesField.setPromptText("Porcentajes (coma, opcional, suma 100)");

        form.add(new Label("Nombre"), 0, 0);
        form.add(nombreField, 1, 0);
        form.add(new Label("Miembros"), 0, 1);
        form.add(miembrosField, 1, 1);
        form.add(new Label("Porcentajes"), 0, 2);
        form.add(porcentajesField, 1, 2);

        HBox acciones = new HBox(10, crearButton, abrirButton, volverButton, feedbackLabel);
        acciones.setAlignment(Pos.CENTER_LEFT);

        VBox left = new VBox(12, form, acciones);
        left.setPadding(new Insets(0, 16, 0, 0));

        buildTabla();

        root.setLeft(left);
        root.setCenter(tablaCuentas);
    }

    private void buildTabla() {
        TableColumn<Cuenta, Integer> colId = new TableColumn<>("Id");
        colId.setCellValueFactory(new PropertyValueFactory<>("idCuenta"));

        TableColumn<Cuenta, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCuenta"));

        TableColumn<Cuenta, String> colMiembros = new TableColumn<>("Miembros");
        colMiembros.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
            String.join(", ", cell.getValue().getMiembros())
        ));

        tablaCuentas.getColumns().addAll(colId, colNombre, colMiembros);
        tablaCuentas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    public Parent getRoot() {
        return root;
    }

    public TextField getNombreField() {
        return nombreField;
    }

    public TextField getMiembrosField() {
        return miembrosField;
    }

    public TextField getPorcentajesField() {
        return porcentajesField;
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
