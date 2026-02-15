package ASP.BanCroak.ui.gastos;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ASP.BanCroak.ui.app.SceneManager;
import ASP.BanCroak.ui.main.BarraMenuView;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GastosView  extends VBox{
	private GastosController controller;
public GastosView(SceneManager sm) {
	
	this.controller= new GastosController(sm.getContext());
	this.setSpacing(0);
    this.setAlignment(Pos.CENTER);
    this.setId("estilo_GastoView");
    this.getStylesheets().add(getClass().getResource("/estilos.css").toExternalForm());
    BarraMenuView barra = new BarraMenuView(sm);

    
    StackPane nenufarStackView = new StackPane();
    nenufarStackView.setPickOnBounds(false);
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
    
    Image nenufar = new Image(getClass().getResource("/Imagenes/Nenúfar 2.png").toExternalForm());
    ImageView nenufarView = new ImageView(nenufar);
    nenufarView.setFitHeight(110); 
    nenufarView.setPreserveRatio(true);
    nenufarView.fitWidthProperty().bind(this.widthProperty().multiply(0.2));
    nenufarView.setPreserveRatio(true);
	Label importar = new Label("Importar Gasto");
    importar.getStyleClass().add("label-sombreado");

    
    String rutaSonidoRana = getClass().getResource("/Audio/Boton .mp3").toExternalForm();
    AudioClip sonidoRana = new AudioClip(rutaSonidoRana);
    String rutaSonidoNotificaion = getClass().getResource("/Audio/Notificacion.mp3").toExternalForm();
    AudioClip sonidoNotificacion = new AudioClip(rutaSonidoNotificaion);


    ranaView.setOnMouseClicked(e -> {
        sonidoRana.play();}); 
    nenufarStackView.setOnMouseClicked(e -> {
    	FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo para importar gasto");
        File archivo = fileChooser.showOpenDialog(this.getScene().getWindow());
        if (archivo != null) {
            controller.importar(archivo);
        }
    }); 
    Label lTitulo = new Label("AÑADIR GASTO");

    ComboBox<String> cuenta = new ComboBox<>();
    cuenta.getItems().addAll(controller.getCuentas());
    cuenta.setPromptText("Cuenta");
    cuenta.setMaxWidth(Double.MAX_VALUE);
    
    ComboBox<String> cuentaNombrePersona = new ComboBox<>();
    cuentaNombrePersona.setPromptText("Persona");
    cuentaNombrePersona.setDisable(true);
    cuentaNombrePersona.setMaxWidth(Double.MAX_VALUE);

    
    cuenta.valueProperty().addListener((observable, viejaCuenta, nuevaCuenta) -> {
    	if (nuevaCuenta != null) {
            cuentaNombrePersona.setDisable(false);           
            List<String> miembros = controller.getPersonasDeCuenta(nuevaCuenta);         
            cuentaNombrePersona.getItems().setAll(miembros);
            
            cuentaNombrePersona.getSelectionModel().selectFirst();
        } else {
            cuentaNombrePersona.setDisable(true);
            cuentaNombrePersona.getItems().clear();
        }
    });
    
    
    Label lCantidad = new Label("Cantidad (€):");
    TextField cantidad = new TextField();
    cantidad.setPromptText("Ej: 5.50");

    Label lFecha = new Label("Fecha:");
    DatePicker fecha = new DatePicker();
    fecha.setPromptText("Selecciona una fecha");

    Label lCategoria = new Label("Categoría:");
    HBox categoriaH = new HBox(0);
    ComboBox<String> categoria = new ComboBox<>();
    categoria.getItems().addAll(controller.getCategorias());
    categoria.setPromptText("Selecciona...");
    categoria.setMaxWidth(Double.MAX_VALUE); 
    HBox.setHgrow(categoria, Priority.ALWAYS);
    
    Button bCategoria= new Button("+"); 
    bCategoria.setMinWidth(35);
    bCategoria.setOnMouseClicked(e -> {
    	Stage nuevaCategoria = new Stage();
    	nuevaCategoria.initModality(Modality.APPLICATION_MODAL);
    	nuevaCategoria.setTitle("Nueva Categoría");
    	Image icono = new Image(getClass().getResource("/Imagenes/Nenúfar 2.png").toExternalForm());
        nuevaCategoria.getIcons().add(icono);

        Label nuevaCategorial = new Label("Introduzca el nombre de la categoría:");
        TextField nuevaCategoriaNombre = new TextField();
        nuevaCategoriaNombre.setPromptText("Nombre de la categoría");

        Button bAceptar = new Button("Registrar Categoría");
        bAceptar.setMaxWidth(Double.MAX_VALUE);

        bAceptar.setOnAction(ev -> {
            String nombre = nuevaCategoriaNombre.getText().trim();
            if (!nombre.isEmpty()) {

            	if (!categoria.getItems().contains(nombre)) {
                    categoria.getItems().add(nombre); 
                    controller.añadirCategoria(nombre);
                }
                categoria.getSelectionModel().select(nombre);
                
                nuevaCategoria.close();
            }
        });
        VBox categoriaV = new VBox(15, nuevaCategorial, nuevaCategoriaNombre, bAceptar);
        categoriaV.setAlignment(Pos.CENTER);
        categoriaV.setId("estilo_GastoView");
        Scene escenaCategoria = new Scene(categoriaV, 500, 180);
        escenaCategoria.getStylesheets().add(getClass().getResource("/estilos.css").toExternalForm());
        nuevaCategoria.setScene(escenaCategoria);
        
        nuevaCategoria.initOwner(this.getScene().getWindow()); 
        nuevaCategoria.showAndWait();
        });
    
    Button bGuardar = new Button("Registrar Gasto");
    bGuardar.setMaxWidth(Double.MAX_VALUE);
    bGuardar.setOnAction(ev -> {
    	controller.registrarGasto(Double.parseDouble(cantidad.getText()),fecha.getValue(),categoria.getValue(),cuentaNombrePersona.getValue(),cuenta.getValue());
        limpiarGasto(cuenta,cuentaNombrePersona,cantidad,fecha,categoria);
        /*if (valor== 0) {
            sm.showNotificacion("Se ha superado el limite de 100 eutos semanal en la categoria alimentacion");
            sonidoNotificacion.play();
        }*/

        sonidoRana.play();
        
        sm.salto(ranaView);
        
    });
    
    HBox.setHgrow(categoriaH, Priority.ALWAYS);
    categoriaH.getChildren().addAll(categoria,bCategoria);
    gastoVView.getChildren().addAll(lTitulo,cuenta,cuentaNombrePersona,lCantidad,cantidad,lFecha,fecha,lCategoria,categoriaH,bGuardar);
    nenufarStackView.getChildren().addAll(nenufarView,importar);
    gastoHView.getChildren().addAll(nenufarStackView,gastoVView,ranaView);
    this.getChildren().addAll(barra ,gastoHView );
    
}

private void limpiarGasto(ComboBox<String> cuenta,ComboBox<String> cuentaNombrePersona,TextField cantidad,DatePicker fecha,ComboBox<String> categoria) {
	cuenta.getSelectionModel().clearSelection();
	cuentaNombrePersona.getSelectionModel().clearSelection();
	cuentaNombrePersona.setDisable(true); 
	cantidad.clear();
	fecha.setValue(null);
	categoria.getSelectionModel().clearSelection();
	}
}
