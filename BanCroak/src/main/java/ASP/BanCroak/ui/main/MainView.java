package ASP.BanCroak.ui.main;


import ASP.BanCroak.ui.app.SceneManager;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;

public class MainView extends VBox{


    public MainView(SceneManager sm) {
    	this.setSpacing(20);
        this.setAlignment(Pos.CENTER);
        this.setId("estilo_MainView");
        this.getStylesheets().add(getClass().getResource("/estilos.css").toExternalForm());
        //BarraMenuView barra = new BarraMenuView(sm);
        
        Image logo = new Image(getClass().getResource("/Imagenes/BanCroak Logo.png").toExternalForm());
        ImageView logoView = new ImageView(logo);
        logoView.setFitHeight(450); 
        logoView.setPreserveRatio(true);
        logoView.fitWidthProperty().bind(this.widthProperty().multiply(0.4));
        logoView.setPreserveRatio(true);

        Image rana = new Image(getClass().getResource("/Imagenes/Rana 1.png").toExternalForm());
        ImageView ranaView = new ImageView(rana);
        ranaView.setFitHeight(440); 
        ranaView.setPreserveRatio(true);
        ranaView.fitWidthProperty().bind(this.widthProperty().multiply(0.4));
        ranaView.setPreserveRatio(true);
        Label lInicio = new Label("Pulsa a la rana");
        
        
        String rutaSonido = getClass().getResource("/Audio/Boton .mp3").toExternalForm();
        AudioClip sonidoRana = new AudioClip(rutaSonido);

        ranaView.setOnMouseClicked(e -> {
            sonidoRana.play();});    

        this.getChildren().addAll( logoView, ranaView, lInicio);
    }

}
