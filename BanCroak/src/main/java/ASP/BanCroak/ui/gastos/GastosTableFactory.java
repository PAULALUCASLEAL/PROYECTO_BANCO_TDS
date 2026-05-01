package ASP.BanCroak.ui.gastos;

import ASP.BanCroak.domain.Cuenta;
import ASP.BanCroak.domain.Gasto;
import ASP.BanCroak.ui.app.AppContext;
import ASP.BanCroak.ui.app.GastosStore;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public final class GastosTableFactory {
    private GastosTableFactory() {
    }

    public static TableView<Gasto> crearTabla(AppContext context, GastosStore store) {
        TableView<Gasto> tabla = new TableView<>();
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        TableColumn<Gasto, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getID())));
        colId.setMinWidth(50);
        colId.setPrefWidth(60);
        colId.setMaxWidth(80);

        TableColumn<Gasto, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getFecha() == null ? "" : d.getValue().getFecha().format(fmt)));

        TableColumn<Gasto, String> colCategoria = new TableColumn<>("Categoría");
        colCategoria.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCategoria()));

        TableColumn<Gasto, String> colCantidad = new TableColumn<>("Importe (€)");
        colCantidad.setCellValueFactory(d -> new SimpleStringProperty(String.format(java.util.Locale.ROOT, "%.2f", d.getValue().getCantidad())));

        TableColumn<Gasto, String> colPagador = new TableColumn<>("Pagador");
        colPagador.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getPagador()));

        TableColumn<Gasto, String> colCuenta = new TableColumn<>("Cuenta");
        colCuenta.setCellValueFactory(d -> new SimpleStringProperty(nombreCuenta(context, d.getValue().getIDCuenta())));

        TableColumn<Gasto, Void> colAcciones = new TableColumn<>("Acciones");
        colAcciones.setCellFactory(accionesCellFactory(context, store));

        tabla.getColumns().addAll(List.of(colId, colFecha, colCategoria, colCantidad, colPagador, colCuenta, colAcciones));
        return tabla;
    }

    private static Callback<TableColumn<Gasto, Void>, TableCell<Gasto, Void>> accionesCellFactory(AppContext context, GastosStore store) {
        return col -> new TableCell<>() {
            private final Button editar = new Button("Editar");
            private final Button eliminar = new Button("Eliminar");
            private final HBox contenedor = new HBox(6, editar, eliminar);

            {
                editar.setOnAction(e -> {
                    Gasto gasto = getTableView().getItems().get(getIndex());
                    new GastoEditorDialog(context, store, gasto);
                });
                eliminar.setOnAction(e -> {
                    Gasto gasto = getTableView().getItems().get(getIndex());
                    if (confirmarEliminar()) {
                        store.eliminarGasto(gasto);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(contenedor);
                }
            }
        };
    }

    public static boolean confirmarEliminar() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar gasto");
        alert.setHeaderText("¿Seguro que deseas eliminar este gasto?");
        alert.setContentText("Esta acción no se puede deshacer.");
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private static String nombreCuenta(AppContext context, int idCuenta) {
        return context.getRepoCuentas().buscarPorId(idCuenta).map(Cuenta::getNombreCuenta).orElse("Cuenta " + idCuenta);
    }
}
