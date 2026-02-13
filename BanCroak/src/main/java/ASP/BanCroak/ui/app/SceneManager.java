package ASP.BanCroak.ui.app;


import ASP.BanCroak.ui.gastos.GastosView;

import ASP.BanCroak.ui.main.MainView;
import ASP.BanCroak.ui.notificaciones.NotificacionesView;
import ASP.BanCroak.ui.notificaciones.NotificacionView;
import ASP.BanCroak.ui.notificaciones.HistorialNotificacionesView;
import javafx.animation.TranslateTransition;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class SceneManager {
    private final AppContext context;
    private Stage stage;
    private Scene scenaActual;
    private static boolean saltando = false;
    private static final double ANCHO = 800; 
    private static final double ALTO = 650;
    private final List<NotificacionView> notificaciones = new ArrayList<>();
	HistorialNotificacionesView historial = new HistorialNotificacionesView(this);

    public SceneManager(AppContext context) {
        this.context = context;
    }

    public void inicializar(Stage stage) {
        this.stage = stage;
		this.stage.setTitle("BanCroak");
		Image icono = new Image(getClass().getResource("/Imagenes/Nenúfar 1.png").toExternalForm());
        this.stage.getIcons().add(icono);
        this.stage.setMinWidth(ANCHO);
        this.stage.setMinHeight(ALTO);
    }

    private void cambiarVista(Parent nuevaVista) {
        if (scenaActual == null) {
            scenaActual = new Scene(nuevaVista, ANCHO, ALTO); 
            stage.setScene(scenaActual);
            stage.show();
        } else {
            scenaActual.setRoot(nuevaVista);
        }
    }
    public void showVentanaPrincipal() {
        MainView vista = new MainView(this);
        cambiarVista(vista);
    }

    public void showVentanaCrearGasto() {
        GastosView vista = new GastosView(this); 
        cambiarVista(vista);
    }
    public void showVentanaCrearNotificaciones() {
        NotificacionesView vista = new NotificacionesView(this); 
        cambiarVista(vista);
    }
    public void showNotificacion(String texto) {
	    NotificacionView notificacion = new NotificacionView(texto);
	    notificaciones.add(notificacion);
	    
	    NotificacionView.mostrar(texto);
	    
	    //historial.actualizar(notificaciones);
	}
    public void salto(ImageView ranaView) {
		if (saltando) return;
		TranslateTransition salto = new TranslateTransition(Duration.millis(200), ranaView);
        salto.setByY(-40);            
        salto.setCycleCount(2);
        salto.setAutoReverse(true);
        salto.setInterpolator(javafx.animation.Interpolator.EASE_OUT);   
        saltando = true;

        salto.setOnFinished(e -> saltando = false);
        salto.play();
	}

}
