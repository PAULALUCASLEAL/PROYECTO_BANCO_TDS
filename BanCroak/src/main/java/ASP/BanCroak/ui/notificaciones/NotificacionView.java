package ASP.BanCroak.ui.notificaciones;

import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class NotificacionView {
	private String mensaje;
    private String hora;

    public NotificacionView(String mensaje) {
        this.mensaje = mensaje;
        this.hora = new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date());
    }
	public static void mostrar(String mensaje) {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT); 
        stage.setAlwaysOnTop(true);
        double anchoImagen = 400;
        Label notificacion = new Label(mensaje);
        notificacion.setWrapText(true); 
        notificacion.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        notificacion.setAlignment(Pos.CENTER);
        notificacion.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2e7d32;-fx-effect: dropshadow(three-pass-box, black, 2, 1.0, 0, 0);");
        notificacion.setPrefWidth(anchoImagen - 60);
        notificacion.setMaxWidth(anchoImagen - 60);
        
        StackPane root = new StackPane();
        StackPane.setAlignment(notificacion, Pos.CENTER);
        String rutaAgua = NotificacionView.class.getResource("/Imagenes/Agua.png").toExternalForm();
        Image agua = new Image(rutaAgua);
        ImageView aguaView = new ImageView(agua);
        aguaView.setFitHeight(100);
        aguaView.setPreserveRatio(true);
        aguaView.setFitWidth(anchoImagen);
        
        root.setStyle("-fx-border-color: #2e7d32; -fx-border-width: 5;-fx-background-color: #2e7d32; ");
        root.getChildren().addAll(aguaView,notificacion);
        stage.setScene(new Scene(root));
        stage.setTitle("Notificación");
        stage.show();

        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(e -> stage.close());
        delay.play();
    }
	public String getMensaje() { 
		return mensaje; 
		}
    public String getHora() { 
    	return hora; 
    	}
}
