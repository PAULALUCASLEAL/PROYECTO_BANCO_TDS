package ASP.BanCroak.ui.notificaciones;

import java.util.List;

import ASP.BanCroak.ui.app.SceneManager;
import ASP.BanCroak.ui.main.BarraMenuView;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;

public class HistorialNotificacionesView extends VBox{
	private VBox historial = new VBox(10);
public HistorialNotificacionesView(SceneManager sm) {
	this.setSpacing(0);
    this.setAlignment(Pos.CENTER);
    this.setId("estilo_HistorialNotificacionesView");
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

    Label lTitulo = new Label("HISTORIAL DE NOTIFICACIONES");
    ScrollPane scroll = new ScrollPane(historial);
    scroll.setFitToWidth(true);
    scroll.setPrefHeight(300);
    scroll.setStyle("-fx-border-color: #2e7d32; -fx-border-width: 5;-fx-background-color: #2e7d32; ");
    VBox.setVgrow(scroll, Priority.ALWAYS);
    gastoVView.getChildren().addAll(lTitulo,scroll);
    gastoHView.getChildren().addAll(nenufarView,gastoVView,ranaView);
    this.getChildren().addAll(barra,gastoHView);
        
}
public void actualizar(List<NotificacionView> lista) {
    historial.getChildren().clear();
    
    for (NotificacionView n : lista) {

    	HBox fila = new HBox(10);
        fila.setStyle("-fx-background-color: white; -fx-border-color: #eee;");

        Label lHora = new Label("[" + n.getHora() + "]");
        Label lNotificacion = new Label(n.getMensaje());
        lNotificacion.setWrapText(true);
        lHora.setMinWidth(Region.USE_PREF_SIZE);
        HBox.setHgrow(lNotificacion, Priority.ALWAYS);
        fila.getChildren().addAll(lHora, lNotificacion);
        
        historial.getChildren().add(0, fila);
    }
}
}
