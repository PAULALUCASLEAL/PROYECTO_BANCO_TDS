package ASP.BanCroak.ui.notificaciones;

import java.util.List;

import ASP.BanCroak.domain.AlertaGasto;
import ASP.BanCroak.ui.app.SceneManager;
import ASP.BanCroak.ui.main.BarraMenuView;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;

public class NotificacionesView extends VBox{

	
	public NotificacionesView(SceneManager sm) {
    NotificacionesController controller = new NotificacionesController(sm.getContext());
	this.setSpacing(0);
    this.setAlignment(Pos.CENTER);
    this.setId("estilo_NotificacionesView");
    this.getStylesheets().add(getClass().getResource("/estilos.css").toExternalForm());
    BarraMenuView barra = new BarraMenuView(sm);

    String rutaSonido = getClass().getResource("/Audio/Boton .mp3").toExternalForm();
    AudioClip sonidoRana = new AudioClip(rutaSonido);

    Image rana = new Image(getClass().getResource("/Imagenes/Rana 2.png").toExternalForm());
    ImageView ranaView = new ImageView(rana);
    ranaView.setFitHeight(190);
    ranaView.setPreserveRatio(true);
    ranaView.setOpacity(0.95);

    Image nenufar = new Image(getClass().getResource("/Imagenes/Nenúfar 1.png").toExternalForm());
    ImageView nenufarView = new ImageView(nenufar);
    nenufarView.setFitHeight(130);
    nenufarView.setPreserveRatio(true);
    nenufarView.setOpacity(0.9);

    ranaView.setOnMouseClicked(e -> sonidoRana.play());

    Label lTitulo = new Label("CREAR ALERTA");
    lTitulo.getStyleClass().add("section-title");
    Label lNombre = new Label("Nombre/Título:");
    TextField nombre = new TextField();
    nombre.setPromptText("Ej: Alerta comida semanal");

    Label lLimite = new Label("Límite (€):");
    TextField limite = new TextField();
    limite.setPromptText("Ej: 500.00");

    Label lPeriodo = new Label("Periodo:");
    ComboBox<String> frecuencia = new ComboBox<>();
    frecuencia.getItems().addAll("Semanal","Mensual");
    frecuencia.setPromptText("Periodo");
    frecuencia.setMaxWidth(Double.MAX_VALUE);
    
    Label lCategoria = new Label("Categoría (Opcional):");
    ComboBox<String> categoria = new ComboBox<>();
    categoria.getItems().add("Todas");
    categoria.getItems().addAll(controller.getCategorias());
    categoria.getSelectionModel().selectFirst();
    categoria.setMaxWidth(Double.MAX_VALUE);

    CheckBox activa = new CheckBox("Activa");
    activa.setSelected(true);

    Button bCrear = new Button("Crear Alerta");
    bCrear.setMaxWidth(Double.MAX_VALUE);
    ObservableList<AlertaGasto> alertasData = FXCollections.observableArrayList(controller.listarAlertasOrdenadas());
    TableView<AlertaGasto> tablaAlertas = new TableView<>(alertasData);
    tablaAlertas.getStyleClass().add("alerts-table");
    tablaAlertas.setEditable(true);
    tablaAlertas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    tablaAlertas.setMinHeight(280);
    VBox.setVgrow(tablaAlertas, Priority.ALWAYS);

    TableColumn<AlertaGasto, String> colNombre = new TableColumn<>("Nombre");
    colNombre.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getNombre()));

    TableColumn<AlertaGasto, String> colPeriodo = new TableColumn<>("Periodo");
    colPeriodo.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getPeriodo().name()));

    TableColumn<AlertaGasto, String> colLimite = new TableColumn<>("Límite (€)");
    colLimite.setCellValueFactory(c -> new ReadOnlyStringWrapper(String.format(java.util.Locale.ROOT, "%.2f", c.getValue().getLimite())));

    TableColumn<AlertaGasto, String> colCategoria = new TableColumn<>("Categoría");
    colCategoria.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getCategoriaDisplay()));

    TableColumn<AlertaGasto, Boolean> colActiva = new TableColumn<>("Activa");
    colActiva.setCellValueFactory(c -> {
        SimpleBooleanProperty prop = new SimpleBooleanProperty(c.getValue().isActiva());
        prop.addListener((obs, oldVal, newVal) -> {
            controller.actualizarEstadoAlerta(c.getValue().getId(), newVal);
            c.getValue().setActiva(newVal);
        });
        return prop;
    });
    colActiva.setCellFactory(CheckBoxTableCell.forTableColumn(colActiva));
    colActiva.setEditable(true);

    TableColumn<AlertaGasto, Void> colEliminar = new TableColumn<>("Eliminar");
    colEliminar.setCellFactory(col -> new TableCell<>() {
        private final Button btn = new Button("✕");
        {
            btn.getStyleClass().add("icon-button");
            btn.setOnAction(e -> {
                AlertaGasto alerta = getTableView().getItems().get(getIndex());
                controller.eliminarAlerta(alerta.getId());
                alertasData.setAll(controller.listarAlertasOrdenadas());
            });
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                setGraphic(btn);
            }
        }
    });

    tablaAlertas.getColumns().addAll(List.of(colNombre, colPeriodo, colLimite, colCategoria, colActiva, colEliminar));

    Label lMisAlertas = new Label("MIS ALERTAS");
    lMisAlertas.getStyleClass().add("section-title");
    VBox panelAlertas = new VBox(12, lMisAlertas, tablaAlertas);
    panelAlertas.getStyleClass().add("card");

    bCrear.setOnAction(ev -> {
        sonidoRana.play();
        try {
            String nombreValor = nombre.getText();
            String limiteValor = limite.getText() == null ? "" : limite.getText().trim();
            String periodoValor = frecuencia.getValue();
            String categoriaValor = categoria.getValue();
            boolean activaValor = activa.isSelected();

            double limiteNum = Double.parseDouble(limiteValor);
            if (periodoValor == null || periodoValor.isBlank()) {
                throw new IllegalArgumentException("El periodo es obligatorio");
            }
            AlertaGasto.Periodo periodo = "Semanal".equalsIgnoreCase(periodoValor) ? AlertaGasto.Periodo.SEMANAL : AlertaGasto.Periodo.MENSUAL;
            String categoriaFinal = categoriaValor == null || "Todas".equalsIgnoreCase(categoriaValor) ? null : categoriaValor;

            controller.crearAlerta(nombreValor, periodo, limiteNum, categoriaFinal, activaValor);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alerta creada");
            alert.setHeaderText(null);
            alert.setContentText("La alerta se ha guardado correctamente.");
            alert.showAndWait();

            nombre.clear();
            limite.clear();
            frecuencia.getSelectionModel().clearSelection();
            categoria.getSelectionModel().selectFirst();
            activa.setSelected(true);
            alertasData.setAll(controller.listarAlertasOrdenadas());
            sm.salto(ranaView);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Límite inválido");
            alert.setContentText("Introduce un número válido para el límite.");
            alert.showAndWait();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Datos inválidos");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    });

    VBox formulario = new VBox(10, lTitulo, lNombre, nombre, lLimite, limite, lPeriodo, frecuencia, lCategoria, categoria, activa, bCrear);
    formulario.getStyleClass().add("card");
    formulario.setMinWidth(360);
    formulario.setMaxWidth(420);
    formulario.setPrefWidth(380);
    formulario.setFillWidth(true);

    VBox.setVgrow(panelAlertas, Priority.ALWAYS);
    panelAlertas.setMaxWidth(Double.MAX_VALUE);

    SplitPane splitHorizontal = new SplitPane(formulario, panelAlertas);
    splitHorizontal.setDividerPositions(0.34);
    splitHorizontal.setPadding(new Insets(4, 6, 4, 6));
    splitHorizontal.getStyleClass().add("alerts-split");

    SplitPane splitVertical = new SplitPane(formulario, panelAlertas);
    splitVertical.setOrientation(javafx.geometry.Orientation.VERTICAL);
    splitVertical.setDividerPositions(0.42);
    splitVertical.setPadding(new Insets(4, 6, 4, 6));
    splitVertical.getStyleClass().add("alerts-split");

    StackPane decoracion = new StackPane();
    decoracion.getChildren().addAll(nenufarView, ranaView);
    StackPane.setAlignment(nenufarView, Pos.BOTTOM_LEFT);
    StackPane.setAlignment(ranaView, Pos.BOTTOM_RIGHT);
    StackPane.setMargin(nenufarView, new Insets(0, 0, 10, 10));
    StackPane.setMargin(ranaView, new Insets(0, 10, 20, 0));
    decoracion.setMouseTransparent(true);
    decoracion.setPickOnBounds(false);

    StackPane contentLayer = new StackPane(splitHorizontal);
    StackPane centerStack = new StackPane();
    BorderPane.setMargin(centerStack, new Insets(18, 24, 24, 24));
    centerStack.getChildren().addAll(decoracion, contentLayer);

    this.widthProperty().addListener((obs, oldVal, newVal) -> {
        if (newVal.doubleValue() < 900) {
            if (contentLayer.getChildren().isEmpty() || contentLayer.getChildren().get(0) != splitVertical) {
                contentLayer.getChildren().setAll(splitVertical);
            }
        } else {
            if (contentLayer.getChildren().isEmpty() || contentLayer.getChildren().get(0) != splitHorizontal) {
                contentLayer.getChildren().setAll(splitHorizontal);
            }
        }
    });

    BorderPane root = new BorderPane();
    root.setTop(barra);
    root.setCenter(centerStack);
    BorderPane.setAlignment(barra, Pos.TOP_CENTER);

    this.getChildren().add(root);
}
	
}
