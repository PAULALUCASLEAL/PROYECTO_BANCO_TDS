package ASP.BanCroak.ui.gastos;

import ASP.BanCroak.domain.Cuenta;
import ASP.BanCroak.domain.Gasto;
import ASP.BanCroak.repo.RepositorioCuentas;
import ASP.BanCroak.repo.RepositorioGastos;
import ASP.BanCroak.ui.app.AppContext;
import ASP.BanCroak.ui.app.GastosStore;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class GastoEditorDialog extends Stage {
    private final AppContext context;
    private final GastosStore store;
    private final RepositorioCuentas repoCuentas;
    private final RepositorioGastos repoGastos;

    public GastoEditorDialog(AppContext context, GastosStore store, Gasto gasto) {
        this.context = context;
        this.store = store;
        this.repoCuentas = context.getRepoCuentas();
        this.repoGastos = context.getRepoGastos();

        build(gasto);
    }

    private void build(Gasto gasto) {
        initModality(Modality.APPLICATION_MODAL);
        setTitle("Editar Gasto");

        VBox root = new VBox(12);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER_LEFT);
        root.setId("estilo_GastoView");
        root.getStylesheets().add(getClass().getResource("/estilos.css").toExternalForm());

        Label titulo = new Label("EDITAR GASTO");
        titulo.getStyleClass().add("section-title");

        Label lCuenta = new Label("Cuenta:");
        Label cuentaValor = new Label(nombreCuenta(gasto.getIDCuenta()));
        cuentaValor.getStyleClass().add("muted");

        Label lCantidad = new Label("Cantidad (€):");
        TextField cantidad = new TextField(String.format(Locale.ROOT, "%.2f", gasto.getCantidad()));

        Label lFecha = new Label("Fecha:");
        DatePicker fecha = new DatePicker(gasto.getFecha());

        Label lCategoria = new Label("Categoría:");
        ComboBox<String> categoria = new ComboBox<>();
        categoria.getItems().addAll(repoGastos.getCategorias());
        categoria.setMaxWidth(Double.MAX_VALUE);
        categoria.getSelectionModel().select(gasto.getCategoria());

        Label lPagador = new Label("Pagador:");
        ComboBox<String> pagador = new ComboBox<>();
        pagador.setEditable(true);
        pagador.setMaxWidth(Double.MAX_VALUE);
        List<String> miembros = miembrosCuenta(gasto.getIDCuenta());
        pagador.getItems().addAll(miembros);
        pagador.getSelectionModel().select(gasto.getPagador());
        if (esCuentaPersonal(gasto.getIDCuenta(), miembros)) {
            String fijo = miembros.isEmpty() ? "yo" : miembros.get(0);
            pagador.getSelectionModel().select(fijo);
            pagador.setEditable(false);
            pagador.setDisable(true);
        }

        Button guardar = new Button("Guardar cambios");
        Button cancelar = new Button("Cancelar");
        HBox acciones = new HBox(10, guardar, cancelar);
        acciones.setAlignment(Pos.CENTER_RIGHT);

        guardar.setOnAction(e -> {
            try {
                double valor = Double.parseDouble(cantidad.getText().trim());
                LocalDate fechaValor = fecha.getValue();
                String categoriaValor = categoria.getValue();
                String pagadorValor = pagador.getEditor().getText();

                validar(valor, fechaValor, categoriaValor, pagadorValor);
                store.editarGasto(gasto.getID(), valor, fechaValor, categoriaValor, pagadorValor);
                mostrarConfirmacion();
                close();
            } catch (IllegalArgumentException ex) {
                cantidad.setStyle("-fx-border-color: #c62828;");
            }
        });

        cancelar.setOnAction(e -> close());

        VBox.setVgrow(acciones, Priority.NEVER);
        root.getChildren().addAll(titulo, lCuenta, cuentaValor, lCantidad, cantidad, lFecha, fecha, lCategoria, categoria, lPagador, pagador, acciones);
        Scene scene = new Scene(root, 420, 520);
        setScene(scene);
        show();
    }

    private void validar(double cantidad, LocalDate fecha, String categoria, String pagador) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("Cantidad inválida");
        }
        if (fecha == null) {
            throw new IllegalArgumentException("Fecha requerida");
        }
        if (categoria == null || categoria.isBlank()) {
            throw new IllegalArgumentException("Categoría requerida");
        }
        if (pagador == null || pagador.isBlank()) {
            throw new IllegalArgumentException("Pagador requerido");
        }
    }

    private boolean esCuentaPersonal(int idCuenta, List<String> miembros) {
        return repoCuentas.buscarPorId(idCuenta).map(Cuenta::esPersonal).orElse(false) || miembros.size() == 1;
    }

    private void mostrarConfirmacion() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Gasto actualizado");
        alert.setHeaderText(null);
        alert.setContentText("Los cambios se han guardado correctamente.");
        alert.showAndWait();
    }

    private String nombreCuenta(int idCuenta) {
        return repoCuentas.buscarPorId(idCuenta).map(Cuenta::getNombreCuenta).orElse("Cuenta " + idCuenta);
    }

    private List<String> miembrosCuenta(int idCuenta) {
        return repoCuentas.buscarPorId(idCuenta).map(Cuenta::getMiembros).orElse(List.of());
    }
}
