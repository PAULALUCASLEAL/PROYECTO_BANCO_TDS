package ASP.BanCroak.ui.cuentas;


import ASP.BanCroak.ui.main.BarraMenuView;
import ASP.BanCroak.ui.app.SceneManager;
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
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import java.util.HashMap;
import java.util.Map;

public class CuentasCompartidasView extends VBox{
	
	private CuentasCompartidasController controller;
	public static class Persona {
	    String nombre;
	    double porcentaje;
	    Label labelPorcentaje = new Label("0.0%");

	    Persona(String nombre, double porcentaje) {
	        this.nombre = nombre;
	        redondeo(porcentaje);
	    }

		private void redondeo(double nuevoPorcentaje) {
			this.porcentaje = Math.round(nuevoPorcentaje * 10.0) / 10.0;
	        this.labelPorcentaje.setText(String.format("%.1f%%", this.porcentaje));
		}
	}
	
    private ObservableList<Persona> listaPersonas = FXCollections.observableArrayList();
    private VBox contenedorLista = new VBox(10);
    private boolean porcentajesEditables = false;
    private final Label totalPorcentajeLabel = new Label("Total: 0.0%");
    private final Map<Persona, TextField> inputsPorcentaje = new HashMap<>();
	
	public CuentasCompartidasView(SceneManager sm) {
		this.controller = new CuentasCompartidasController(sm.getContext());
		this.setSpacing(0);
	    this.setAlignment(Pos.CENTER);
	    this.setId("estilo_CuentaCompartidaView");
	    this.getStylesheets().add(getClass().getResource("/estilos.css").toExternalForm());
	    BarraMenuView barra = new BarraMenuView(sm);
	    
        
        VBox contenedor = buildCuentaPane(sm);
        VBox.setVgrow(contenedor, Priority.ALWAYS);
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
            if (!nombrePersona.getText().trim().isEmpty()) {
                añadirPersona(nombrePersona.getText().trim());
                nombrePersona.clear();
            }
        });

        HBox nombrePersonaH = new HBox(10, nombrePersona, bAnadir);

        Button bDefinirPorcentajes = new Button("Definir porcentajes");
        bDefinirPorcentajes.setOnAction(e -> {
            if (listaPersonas.isEmpty()) {
                mostrarAviso("Añade al menos una persona.");
                return;
            }
            porcentajesEditables = true;
            nombrePersona.setDisable(true);
            bAnadir.setDisable(true);
            actualizarUI();
        });

        Button bCrear = new Button("Crear cuenta");
        bCrear.setMaxWidth(Double.MAX_VALUE);
        bCrear.setOnAction(ev -> {
        	if (nombre.getText().trim().isEmpty()) {
                mostrarAviso("Añade un nombre a la cuenta.");
                return;
            }
            if (!porcentajesEditables) {
                mostrarAviso("Define los porcentajes antes de crear la cuenta.");
                return;
            }
            if (!aplicarTodosDesdeInputs()) 
                return;
            
            double total = calcularTotalPorcentaje();
            if (Math.abs(total - 100.0) > 0.1) {
                mostrarAviso("La suma de porcentajes debe dar 100%.");
                return;
            }
            controller.crearCuenta(nombre.getText(), listaPersonas);
            sonidoRana.play();
            sm.salto(ranaView);
            limpiarCuentas(nombre, nombrePersona, bAnadir);
        });

        ScrollPane scroll = new ScrollPane(contenedorLista);
        scroll.setFitToWidth(true);
        scroll.setPrefViewportHeight(180);
        scroll.setMinHeight(180);
        VBox personas = new VBox(10, nombrePersonaH, bDefinirPorcentajes, totalPorcentajeLabel, bCrear, new Separator(), scroll);

        gastoVView.getChildren().addAll(lTitulo, lNombre, nombre, personas);
        gastoHView.getChildren().addAll(nenufarView, gastoVView, ranaView);
        return new VBox(gastoHView);
    }

	private void actualizarUI() {
        contenedorLista.getChildren().clear();
        inputsPorcentaje.clear();
        for (Persona p : listaPersonas) {
        	TextField teclado = new TextField();
        	teclado.setPrefWidth(70);
        	teclado.setPromptText("%");
            teclado.setDisable(!porcentajesEditables);
            teclado.setText(String.format("%.1f", p.porcentaje).replace(',', '.'));
            teclado.setTextFormatter(new TextFormatter<>(c -> {
                String nuevo = c.getControlNewText();
                return nuevo.matches("\\d{0,3}([\\.,]\\d{0,1})?") ? c : null;
            }));
            Runnable aplicar = () -> {
            	if (!porcentajesEditables || teclado.getText().isBlank()) return;
                try {
                    double valor = Double.parseDouble(teclado.getText().replace(',', '.'));
                    p.redondeo(valor);
                    actualizarTotal();
                } catch (NumberFormatException ex) {
                    mostrarAviso("Introduce un porcentaje válido.");
                }
            };
            teclado.focusedProperty().addListener((obs, oldV, newV) -> {
                if (!newV) aplicar.run();
            });
            inputsPorcentaje.put(p, teclado);
            Label nombreLabel = new Label(p.nombre);
            nombreLabel.setStyle("-fx-text-fill: #1d1d1d;");
            nombreLabel.setMinWidth(140);
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            HBox fila = new HBox(12, nombreLabel, spacer, p.labelPorcentaje, teclado);
            fila.setAlignment(Pos.CENTER_LEFT);
            fila.setPadding(new Insets(6, 8, 6, 8));
            fila.setMinHeight(36);
            contenedorLista.getChildren().add(fila);
        }
        actualizarTotal();
    }
	private void añadirPersona(String nombre) {
        listaPersonas.add(new Persona(nombre, 0));
        repartirEquitativamente();
        actualizarUI();
    }

    private void repartirEquitativamente() {
    	int n = listaPersonas.size();
    	if (n == 0) return;
    	double base = Math.floor((100.0 / n) * 10.0) / 10.0;
        double suma = 0;
        for (int i = 0; i < n - 1; i++) {
            listaPersonas.get(i).redondeo(base);
            suma += base;
        }
        double resto = 100.0 - suma; //La ultima persona se lleva el resto
        listaPersonas.get(n - 1).redondeo(resto);
        actualizarTotal();
    }

    public void limpiarCuentas(TextField nombre, TextField nombrePersona, Button bAnadir) {
    	
    	nombre.clear();
        nombrePersona.clear();
        nombrePersona.setDisable(false);
        bAnadir.setDisable(false);
        porcentajesEditables = false;
    	listaPersonas.clear();
    	actualizarUI();
    }

    private double calcularTotalPorcentaje() {
        return listaPersonas.stream().mapToDouble(p -> p.porcentaje).sum();
    }

    private void actualizarTotal() {
        totalPorcentajeLabel.setText(String.format("Total: %.1f%%", calcularTotalPorcentaje()));
    }

    private boolean aplicarTodosDesdeInputs() {
    	for (Map.Entry<Persona, TextField> entry : inputsPorcentaje.entrySet()) {
            try {
                double valor = Double.parseDouble(entry.getValue().getText().replace(',', '.'));
                entry.getKey().redondeo(valor);
            } catch (Exception e) {
                mostrarAviso("Revisa que los porcentajes sean válidos.");
                return false;
            }
        }
        actualizarTotal();
        return true;
    }

    private void mostrarAviso(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
 
}
