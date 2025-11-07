package ASP.BanCroak;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class EventosDemo extends Application {
	@Override
	public void start(Stage stage) {
		VBox root = new VBox(10);
		root.setStyle("-fx-background-color: #D0D0D0; -fx-aligment: top_center;");
		Scene scene =new Scene(root,400,500);
		
		HBox cajaH= new HBox(10);
		cajaH.setPrefHeight(300);
		cajaH.setStyle("-fx-background-color: #A0D0A0; -fx-aligment:center;");
		
		VBox cajaV = new VBox(10);
		cajaV.setPrefSize(150, 150);
		cajaV.setMaxSize(150, 150);
		cajaV.setStyle("-fx-background-color: #00A0A0; -fx-aligment:center;");

		
		TextField texto = new TextField();
		texto.setPromptText("Escribe hola");
		Button boton = new Button("pulsa");
		Rectangle rect= new Rectangle(100,50);
		rect.setFill(Color.STEELBLUE);
		//evento
		/*boton.addEventFilter(ActionEvent.ACTION, e -> { System.out.println("Paso por el filtro");
														if(!texto.getText().equals("hola")) {System.out.println("No has escrito hola");
														e.consume();}
														});
		boton.setOnAction(e -> { System.out.println("click en el botón");});
		
		cajaH.addEventHandler(ActionEvent.ACTION, e ->{
			System.out.println("El evento sube y llega a HBox");
		}
		);*/
		
		/*boton.addEventHandler(ActionEvent.ACTION, e ->{
			System.out.println("click en el boton");
		});
		boton.addEventHandler(ActionEvent.ACTION, e ->{
			System.out.println("click2 en el boton");
		}
		);*/
		/*boton.addEventHandler(MouseEvent.MOUSE_RELEASED, e ->{
			System.out.println("click en el boton");
		}
		);
		boton.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->{
			if(e.getClickCount()==5) System.out.println("5");
			System.out.println("click en el boton");
		}
		);
		boton.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->{
			if(e.getButton()==MouseButton.SECONDARY) System.out.println("D");
			System.out.println("click en el boton");
		}
		);*/
		rect.addEventFilter(MouseEvent.MOUSE_CLICKED, e ->{System.out.println("click en el rectangulo");});
		cajaH.addEventFilter(MouseEvent.MOUSE_CLICKED, e ->{
			if(e.getTarget()==rect)
			System.out.println("click en Rectangulo");});
		cajaH.addEventFilter(MouseEvent.MOUSE_CLICKED, e ->{System.out.println("click en el HBox");});
		
		
		
		cajaV.getChildren().addAll(texto,rect);
		cajaH.getChildren().add(cajaV);
		root.getChildren().add(cajaH);
		rect.requestFocus();
		stage.setScene(scene);
		stage.setTitle("Ejemplo de eventos JavaFX");
		stage.show();
	}
	public static void main(String[] args) {
		launch(args);
	}
}
