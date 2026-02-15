package ASP.BanCroak.ui.notificaciones;

import ASP.BanCroak.ui.app.SceneManager;
import ASP.BanCroak.ui.main.BarraMenuView;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;

public class NotificacionesView extends VBox{

	
	public NotificacionesView(SceneManager sm) {
	this.setSpacing(0);
    this.setAlignment(Pos.CENTER);
    this.setId("estilo_NotificacionesView");
    this.getStylesheets().add(getClass().getResource("/estilos.css").toExternalForm());
    BarraMenuView barra = new BarraMenuView(sm);
    
    
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

    ranaView.setOnMouseClicked(e -> {
        sonidoRana.play();}); 

    Label lTitulo = new Label("CREAR NOTIFICACIÓN");
    Label lNombre = new Label("Cantidad (€):");
    TextField nombre = new TextField();
    nombre.setPromptText("Ej: 5.50");
    ComboBox<String> frecuencia = new ComboBox<>();
    frecuencia.getItems().addAll("semanal","Mensual");
    frecuencia.setPromptText("Frecuencia de notificación");
    
    Label lCategoria = new Label("Categoría (Opcional):");
    ComboBox<String> categoria = new ComboBox<>();
    categoria.getItems().addAll("-","Alimentación", "Transporte", "entretenimiento");//hace falta una funcion
    categoria.getSelectionModel().selectFirst();
    categoria.setMaxWidth(Double.MAX_VALUE);


    Button bCrear = new Button("Crear Notificación");
    bCrear.setMaxWidth(Double.MAX_VALUE);
    bCrear.setOnAction(ev -> {
        sonidoRana.play();
        
        sm.salto(ranaView);
    });

    gastoVView.getChildren().addAll(lTitulo,lNombre,nombre,frecuencia,lCategoria,categoria,bCrear);
    gastoHView.getChildren().addAll(nenufarView,gastoVView,ranaView);
    this.getChildren().addAll(barra,gastoHView);
}
	
}
