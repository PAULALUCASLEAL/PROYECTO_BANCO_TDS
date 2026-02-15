package ASP.BanCroak.ui.notificaciones;

import java.time.format.DateTimeFormatter;
import java.util.List;

import ASP.BanCroak.domain.Notificacion;
import ASP.BanCroak.ui.app.SceneManager;
import ASP.BanCroak.ui.main.BarraMenuView;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
    private final NotificacionesController controller;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
public HistorialNotificacionesView(SceneManager sm) {
    this.controller = new NotificacionesController(sm.getContext());
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

    Label lTitulo = new Label("HISTORIAL DE NOTIFICACIONES");
    Button bMarcarTodas = new Button("Marcar todas como leídas");
    bMarcarTodas.setOnAction(e -> {
        controller.marcarTodasLeidas();
        refresh();
    });
    ScrollPane scroll = new ScrollPane(historial);
    scroll.setFitToWidth(true);
    scroll.setPrefHeight(300);
    scroll.setStyle("-fx-border-color: #2e7d32; -fx-border-width: 5;-fx-background-color: #2e7d32; ");
    VBox.setVgrow(scroll, Priority.ALWAYS);
    gastoVView.getChildren().addAll(lTitulo,bMarcarTodas,scroll);
    gastoHView.getChildren().addAll(nenufarView,gastoVView,ranaView);
    this.getChildren().addAll(barra,gastoHView);
        
}
public void refresh() {
    actualizar(controller.listarNotificacionesOrdenadas());
}

public void actualizar(List<Notificacion> lista) {
    historial.getChildren().clear();
    
    for (Notificacion n : lista) {

        HBox fila = new HBox(10);
        fila.getStyleClass().add("notif-card");
        if (!n.isLeida()) {
            fila.getStyleClass().add("notif-card-unread");
        } else {
            fila.getStyleClass().add("notif-card-read");
        }
        if (n.getPeriodo() == ASP.BanCroak.domain.AlertaGasto.Periodo.SEMANAL) {
            fila.getStyleClass().add("notif-card-weekly");
        } else {
            fila.getStyleClass().add("notif-card-monthly");
        }

        Label lHora = new Label("[" + formatter.format(n.getTimestamp()) + "]");
        Label lNotificacion = new Label(n.getMensaje());
        lHora.getStyleClass().add("notif-time");
        lNotificacion.getStyleClass().add("notif-message");
        lNotificacion.setWrapText(true);
        lHora.setMinWidth(Region.USE_PREF_SIZE);
        HBox.setHgrow(lNotificacion, Priority.ALWAYS);
        CheckBox leida = new CheckBox("Leída");
        leida.getStyleClass().add("notif-check");
        leida.setSelected(n.isLeida());
        leida.selectedProperty().addListener((obs, oldVal, newVal) -> {
            controller.marcarLeida(n.getId(), newVal);
            fila.getStyleClass().removeAll("notif-card-unread", "notif-card-read");
            fila.getStyleClass().add(newVal ? "notif-card-read" : "notif-card-unread");
        });
        Button eliminar = new Button("✕");
        eliminar.setGraphic(null);
        eliminar.setText("✕");
        eliminar.getStyleClass().add("icon-button");
        eliminar.setOnAction(e -> {
            controller.eliminarNotificacion(n.getId());
            refresh();
        });
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        fila.getChildren().addAll(lHora, lNotificacion, spacer, leida, eliminar);
        
        historial.getChildren().add(fila);
    }
}
}
