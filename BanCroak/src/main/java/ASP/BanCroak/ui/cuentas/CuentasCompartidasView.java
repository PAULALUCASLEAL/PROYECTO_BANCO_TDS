package ASP.BanCroak.ui.cuentas;


import ASP.BanCroak.ui.main.BarraMenuView;
import ASP.BanCroak.ui.app.SceneManager;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;

public class CuentasCompartidasView extends VBox{
	
	
	public static class Persona {
	    String nombre;
	    double porcentaje;
	    Slider slider = new Slider(0, 100, 0);
	    Label labelPorcentaje = new Label("0.0%");

	    Persona(String nombre, double porcentaje) {
	        this.nombre = nombre;
	        this.porcentaje = porcentaje;
	        this.slider.setValue(porcentaje);
	    }
	}
	
	private ObservableList<Persona> listaPersonas = FXCollections.observableArrayList();
    private VBox contenedorLista = new VBox(10);
    private boolean ajustandoInternamente = false; 
	
	public CuentasCompartidasView(SceneManager sm) {
	this.setSpacing(0);
    this.setAlignment(Pos.CENTER);
    this.setId("estilo_CuentaCompartidaView");
    this.getStylesheets().add(getClass().getResource("/estilos.css").toExternalForm());
    BarraMenuView barra = new BarraMenuView(sm);
    
    
    HBox gastoHView = new HBox(10);
    gastoHView.setAlignment(Pos.CENTER);
    VBox gastoVView = new VBox(10);
    VBox.setVgrow(gastoHView, Priority.ALWAYS);
    
    Image rana = new Image(getClass().getResource("/Imagenes/Rana 2.png").toExternalForm());
    ImageView ranaView = new ImageView(rana);
    ranaView.setFitHeight(260); 
    ranaView.setPreserveRatio(true);
    ranaView.fitWidthProperty().bind(this.widthProperty().multiply(0.4));
    ranaView.setPreserveRatio(true);
    
    Image nenufar = new Image(getClass().getResource("/Imagenes/Nenúfar 1.png").toExternalForm());
    ImageView nenufarView = new ImageView(nenufar);
    nenufarView.setFitHeight(150); 
    nenufarView.setPreserveRatio(true);
    nenufarView.fitWidthProperty().bind(this.widthProperty().multiply(0.4));
    nenufarView.setPreserveRatio(true);


    
    String rutaSonido = getClass().getResource("/Audio/Boton .mp3").toExternalForm();
    AudioClip sonidoRana = new AudioClip(rutaSonido);

    ranaView.setOnMouseClicked(e -> {
        sonidoRana.play();}); 

    Label lTitulo = new Label("CREAR CUENTA COMPARTIDA");
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
        sonidoRana.play();
        
        TranslateTransition salto = new TranslateTransition(Duration.millis(200), ranaView);
        salto.setByY(-40);            
        salto.setCycleCount(2);
        salto.setAutoReverse(true);
        salto.setInterpolator(javafx.animation.Interpolator.EASE_OUT);            
        salto.play();
    });

    gastoVView.getChildren().addAll(lTitulo,lNombre,nombre,personas,bCrear);
    gastoHView.getChildren().addAll(nenufarView,gastoVView,ranaView);
    this.getChildren().addAll(barra ,gastoHView );
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
        double equitativo = 100.0 / listaPersonas.size();
        for (Persona p : listaPersonas) {
            p.porcentaje = equitativo;
            p.slider.setValue(equitativo);
            p.labelPorcentaje.setText(String.format("%.1f%%", equitativo));
        }
        ajustandoInternamente = false;
    }

    private void rebalancearDesde(Persona editada, double nuevoValor) {
        if (ajustandoInternamente || listaPersonas.size() <= 1) return;

        ajustandoInternamente = true;
        editada.porcentaje = nuevoValor;
        
        double sumaOtros = 0;
        for (Persona p : listaPersonas) {
            if (p != editada) sumaOtros += p.porcentaje;
        }

        double resto = 100.0 - nuevoValor;

        for (Persona p : listaPersonas) {
            if (p != editada) {
                if (sumaOtros == 0) {
                    p.porcentaje = resto / (listaPersonas.size() - 1);
                } else {
                    p.porcentaje = (p.porcentaje / sumaOtros) * resto;
                }
                p.slider.setValue(p.porcentaje);
            }
            p.labelPorcentaje.setText(String.format("%.1f%%", p.porcentaje));
        }
        ajustandoInternamente = false;
    }
 
}
