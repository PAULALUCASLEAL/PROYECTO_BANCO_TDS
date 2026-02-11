package ASP.BanCroak.ui.notificaciones;

import ASP.BanCroak.domain.Notificacion;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class NotificacionesView {
    private final BorderPane root;
    private final Button volverButton;
    private final TableView<Notificacion> tabla;

    public NotificacionesView() {
        this.root = new BorderPane();
        this.volverButton = new Button("Volver");
        this.tabla = new TableView<>();
        build();
    }

    private void build() {
        root.setPadding(new Insets(18));
        root.getStyleClass().add("app-root");

        Label titulo = new Label("Historial de notificaciones");
        titulo.getStyleClass().add("view-title");

        volverButton.getStyleClass().add("ghost-button");

        HBox header = new HBox(12, volverButton, titulo);
        header.setAlignment(Pos.CENTER_LEFT);
        header.getStyleClass().add("header-bar");
        root.setTop(header);

        buildTabla();
        VBox center = new VBox(10, tabla);
        center.getStyleClass().add("panel-card");

        root.setCenter(center);
    }

    private void buildTabla() {
        TableColumn<Notificacion, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
            cell.getValue().getTimestamp().toString()
        ));

        TableColumn<Notificacion, String> colMensaje = new TableColumn<>("Mensaje");
        colMensaje.setCellValueFactory(new PropertyValueFactory<>("mensaje"));

        TableColumn<Notificacion, String> colPeriodo = new TableColumn<>("Periodo");
        colPeriodo.setCellValueFactory(new PropertyValueFactory<>("periodoKey"));

        TableColumn<Notificacion, Double> colTotal = new TableColumn<>("Total");
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalDetectado"));
        colTotal.setCellFactory(col -> new javafx.scene.control.TableCell<>() {
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

        TableColumn<Notificacion, String> colCategoria = new TableColumn<>("Categoría");
        colCategoria.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
            cell.getValue().getCategoria() == null || cell.getValue().getCategoria().isBlank() ? "Todas" : cell.getValue().getCategoria()
        ));

        TableColumn<Notificacion, Integer> colAlerta = new TableColumn<>("AlertaId");
        colAlerta.setCellValueFactory(new PropertyValueFactory<>("alertaId"));

        tabla.getColumns().addAll(colFecha, colMensaje, colPeriodo, colTotal, colCategoria, colAlerta);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    public Parent getRoot() {
        return root;
    }

    public Button getVolverButton() {
        return volverButton;
    }

    public TableView<Notificacion> getTabla() {
        return tabla;
    }
}
