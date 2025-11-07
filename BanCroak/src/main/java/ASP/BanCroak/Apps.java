package ASP.BanCroak;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Apps extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Hola JavaFX");
        VBox root= new VBox(10);
    	Scene scene = new Scene(root,400,300);
    	scene.getStylesheets().add(getClass().getResource("/estilos.css").toExternalForm());
    	
    	//root.setStyle("-fx-aligment: center"); //centra horizontal y vertical
    	//root.setStyle("-fx-background-color: #00E0FF ; -fx-alignment: center;");
    	root.setId("elemento_root");
    	HBox cajaDatos = new HBox(10);
    	cajaDatos.setPrefSize(400, 40);
    	//cajaDatos.setStyle("-fx-background-color: #50D000; -fx-alignment: center_left;");
    	cajaDatos.getStyleClass().add("estilos_datos");
    	Label lbNombre = new Label("Nombre: ");
    	//lbNombre.setPrefWidth(100);
    	//lbNombre.setPrefHeight(30);
    	//lbNombre.setPrefSize(100, 30);
    	lbNombre.setStyle("-fx-pref-width: 100;"+
    						"-fx-pref-height: 30;"+
    						"-fx-pref-alignment: center-right;");
    	cajaDatos.getChildren().add(lbNombre);
    	TextField txtNombre= new TextField();
    	cajaDatos.getChildren().add(txtNombre);
    	
    	Label lb1 = new Label("Hello");
    	Label lb2 = new Label("JavaFX");
    	root.getChildren().add(cajaDatos);
    	root.getChildren().add(lb1);
    	root.getChildren().add(lb2);
    	
    	
    	
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}