package ASP.BanCroak.ui.cuentas;


import ASP.BanCroak.ui.gastos.GastoEditorDialog;
import ASP.BanCroak.ui.gastos.GastosTableFactory;
import ASP.BanCroak.ui.main.BarraMenuView;
import ASP.BanCroak.ui.visualizar.GastosFilterPane;
import ASP.BanCroak.ui.visualizar.VisualizarViewModel;
import ASP.BanCroak.ui.app.SceneManager;
import ASP.BanCroak.domain.Gasto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;

public class CuentasCompartidasView extends VBox{
	
	private CuentasCompartidasController controller;
	public static class Persona {
	    String nombre;
	    double porcentaje;
	    Slider slider = new Slider(0, 100, 0);
	    Label labelPorcentaje = new Label("0.0%");

	    Persona(String nombre, double porcentaje) {
	        this.nombre = nombre;
	        redondeo(porcentaje);
	    }

		private void redondeo(double nuevoPorcentaje) {
			this.porcentaje = Math.round(nuevoPorcentaje * 10.0) / 10.0;
	        this.slider.setValue(this.porcentaje);
	        this.labelPorcentaje.setText(String.format("%.1f%%", this.porcentaje));
		}
	}
	
	private ObservableList<Persona> listaPersonas = FXCollections.observableArrayList();
    private VBox contenedorLista = new VBox(10);
    private boolean ajustandoInternamente = false; 
	
	public CuentasCompartidasView(SceneManager sm) {
		this.controller = new CuentasCompartidasController(sm.getContext());
		this.setSpacing(0);
	    this.setAlignment(Pos.CENTER);
	    this.setId("estilo_CuentaCompartidaView");
	    this.getStylesheets().add(getClass().getResource("/estilos.css").toExternalForm());
	    BarraMenuView barra = new BarraMenuView(sm);
	    
        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabs.getTabs().addAll(
            new Tab("Cuenta compartida", buildCuentaPane(sm)),
            new Tab("Gastos", buildGastosHub(sm))
        );

        VBox contenedor = new VBox(12, tabs);
        VBox.setVgrow(tabs, Priority.ALWAYS);
        contenedor.setPadding(new Insets(12, 20, 20, 20));
        this.getChildren().addAll(barra, contenedor);
}

    private VBox buildCuentaPane(SceneManager sm) {
        HBox gastoHView = new HBox(10);
        gastoHView.setAlignment(Pos.CENTER);
        VBox gastoVView = new VBox(10);
        VBox.setVgrow(gastoHView, Priority.ALWAYS);

        Image rana = new Image(getClass().getResource("/Imagenes/Rana 2.png").toExternalForm());
        ImageView ranaView = new ImageView(rana);
        ranaView.setFitHeight(180);
        ranaView.setPreserveRatio(true);
        ranaView.fitWidthProperty().bind(this.widthProperty().multiply(0.25));
        ranaView.setPreserveRatio(true);

        Image nenufar = new Image(getClass().getResource("/Imagenes/Nenúfar 1.png").toExternalForm());
        ImageView nenufarView = new ImageView(nenufar);
        nenufarView.setFitHeight(110);
        nenufarView.setPreserveRatio(true);
        nenufarView.fitWidthProperty().bind(this.widthProperty().multiply(0.2));
        nenufarView.setPreserveRatio(true);

        String rutaSonido = getClass().getResource("/Audio/Boton .mp3").toExternalForm();
        AudioClip sonidoRana = new AudioClip(rutaSonido);

        ranaView.setOnMouseClicked(e -> sonidoRana.play());

        Label lTitulo = new Label("CREAR CUENTA COMPARTIDA");
        lTitulo.getStyleClass().add("section-title");
        Label lNombre = new Label("Nombre:");
        TextField nombre = new TextField();
        nombre.setPromptText("Nombre de la cuenta");

        TextField nombrePersona = new TextField();
        nombrePersona.setPromptText("Nombre de la persona");
        Button bAnadir = new Button("Añadir Persona");

        bAnadir.setOnAction(e -> {
            if (!nombrePersona.getText().isEmpty()) {
                añadirPersona(nombrePersona.getText());
                nombrePersona.clear();
            }
        });

        HBox nombrePersonaH = new HBox(10, nombrePersona, bAnadir);

        ScrollPane scroll = new ScrollPane(contenedorLista);
        scroll.setFitToWidth(true);
        VBox personas = new VBox(10, nombrePersonaH, new Separator(), scroll);

        Button bCrear = new Button("Crear cuenta");
        bCrear.setMaxWidth(Double.MAX_VALUE);
        bCrear.setOnAction(ev -> {
            controller.crearCuenta(nombre.getText(), listaPersonas);
            sonidoRana.play();
            sm.salto(ranaView);
            limpiarCuentas(nombre);
        });

        gastoVView.getChildren().addAll(lTitulo, lNombre, nombre, personas, bCrear);
        gastoHView.getChildren().addAll(nenufarView, gastoVView, ranaView);
        return new VBox(gastoHView);
    }

    private VBox buildGastosHub(SceneManager sm) {
        VisualizarViewModel viewModel = new VisualizarViewModel(sm.getContext());
        GastosFilterPane filtros = new GastosFilterPane(sm.getContext(), viewModel.getStore(), viewModel.getFilterState());

        Label titulo = new Label("Qué puedo hacer con los gastos");
        titulo.getStyleClass().add("section-title");

        Button bNuevo = new Button("Añadir gasto");
        bNuevo.setOnAction(e -> sm.showVentanaCrearGasto());
        Button bEditar = new Button("Editar gasto");
        Button bEliminar = new Button("Eliminar gasto");
        Button bHistorial = new Button("Ver historial");
        bHistorial.setOnAction(e -> sm.showVentanaHistorialNotificaciones());
        Button bExportar = new Button("Exportar");
        bExportar.setDisable(true);

        HBox acciones = new HBox(10, bNuevo, bEditar, bEliminar, bHistorial, bExportar);
        acciones.setAlignment(Pos.CENTER_LEFT);

        TableView<Gasto> tabla = GastosTableFactory.crearTabla(sm.getContext(), viewModel.getStore());
        tabla.setItems(viewModel.getGastosFiltrados());
        VBox.setVgrow(tabla, Priority.ALWAYS);

        bEditar.setOnAction(e -> {
            Gasto gasto = tabla.getSelectionModel().getSelectedItem();
            if (gasto == null) {
                mostrarSeleccionRequerida();
                return;
            }
            new GastoEditorDialog(sm.getContext(), viewModel.getStore(), gasto);
        });

        bEliminar.setOnAction(e -> {
            Gasto gasto = tabla.getSelectionModel().getSelectedItem();
            if (gasto == null) {
                mostrarSeleccionRequerida();
                return;
            }
            if (GastosTableFactory.confirmarEliminar()) {
                viewModel.getStore().eliminarGasto(gasto);
            }
        });

        VBox repoPane = new VBox(12, acciones, tabla);
        VBox.setVgrow(tabla, Priority.ALWAYS);

        VBox filtrosPane = new VBox(12, filtros);
        filtrosPane.setPadding(new Insets(10, 0, 0, 0));

        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabs.getTabs().addAll(
            new Tab("Repositorio", repoPane),
            new Tab("Filtros", filtrosPane)
        );

        VBox contenedor = new VBox(12, titulo, tabs);
        VBox.setVgrow(tabs, Priority.ALWAYS);
        return contenedor;
    }

    private void mostrarSeleccionRequerida() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Selecciona un gasto");
        alert.setHeaderText(null);
        alert.setContentText("Selecciona un gasto en la tabla para continuar.");
        alert.showAndWait();
    }
	
	
	private void actualizarUI() {
        contenedorLista.getChildren().clear();
        for (Persona p : listaPersonas) {
        	TextField teclado = new TextField();
        	teclado.setPrefWidth(50);
        	teclado.setPromptText("%");
        	Button bTeclado = new Button("Aplicar");
        	bTeclado.setOnAction(e -> {
                double valor = Double.parseDouble(teclado.getText());
                rebalancearDesde(p, valor);
                p.slider.setValue(valor); 
                teclado.clear();

            });
            HBox fila = new HBox(15, new Label(p.nombre), p.slider, p.labelPorcentaje,teclado,bTeclado);
            fila.setPadding(new Insets(5));
            HBox.setHgrow(p.slider, Priority.ALWAYS);
            contenedorLista.getChildren().add(fila);
        }
    }
	private void añadirPersona(String nombre) {
        Persona nueva = new Persona(nombre, 0);
        
        nueva.slider.valueProperty().addListener((observer, viejoValor, nuevoValor) -> {
            rebalancearDesde(nueva, nuevoValor.doubleValue());
        });

        listaPersonas.add(nueva);
        actualizarUI();
        repartirEquitativamente();
    }

    private void repartirEquitativamente() {
        ajustandoInternamente = true;
        for (Persona p : listaPersonas) {
            p.redondeo(100.0 / listaPersonas.size());
        }
        ajustandoInternamente = false;
    }

    private void rebalancearDesde(Persona editada, double nuevoValor) {
    	if (ajustandoInternamente || listaPersonas.size() <= 1) return;
        ajustandoInternamente = true;

        editada.redondeo(nuevoValor); 
        double resto = 100.0 - editada.porcentaje;
        double sumaOtros = listaPersonas.stream().filter(p -> p != editada).mapToDouble(p -> p.porcentaje).sum();

        for (Persona p : listaPersonas) {
            if (p != editada) {
                double nuevoRatio = (sumaOtros == 0) ? (resto / (listaPersonas.size() - 1)) : (p.porcentaje / sumaOtros) * resto;
                p.redondeo(nuevoRatio); 
            }
        }
        ajustandoInternamente = false;
    }
    public void limpiarCuentas(TextField nombre) {
    	
    	nombre.clear();
    	listaPersonas.clear();
    	actualizarUI();
    }
 
}
