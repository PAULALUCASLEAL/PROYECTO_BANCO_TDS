package ASP.BanCroak;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



public class App extends Application {

    @Override
    public void start(Stage stage) {
    	VBox inicio = new VBox(30);
    	Scene scene = new Scene(inicio, 900, 600);
    	scene.getStylesheets().add(getClass().getResource("/estilos.css").toExternalForm());
    	inicio.setId("estilo_inicio");
    	
    	Image Logo = new Image(getClass().getResource("/Imagenes/BanCroak Logo.png").toExternalForm());
    	ImageView logoView = new ImageView(Logo);
    	logoView.setFitHeight(250); 
    	logoView.setPreserveRatio(true);
    	Image rana = new Image(getClass().getResource("/Imagenes/Rana 1.png").toExternalForm());
    	ImageView ranaView = new ImageView(rana);
    	ranaView.setFitHeight(240); 
    	ranaView.setPreserveRatio(true);
    	Label lInicio = new Label("Pulsa a la rana");

    	inicio.getChildren().addAll(logoView,ranaView,lInicio);
        

        stage.setTitle("BanCroak");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}