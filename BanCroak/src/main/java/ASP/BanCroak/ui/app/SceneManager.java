package ASP.BanCroak.ui.app;


import ASP.BanCroak.ui.gastos.GastosView;

import ASP.BanCroak.ui.main.MainView;
import ASP.BanCroak.ui.notificaciones.NotificacionesView;
import ASP.BanCroak.ui.notificaciones.HistorialNotificacionesView;
import ASP.BanCroak.ui.notificaciones.ToastManager;
import ASP.BanCroak.ui.cuentas.CuentasCompartidasView;
import ASP.BanCroak.ui.visualizar.VisualizarTab;
import ASP.BanCroak.ui.visualizar.VisualizarView;
import javafx.animation.TranslateTransition;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SceneManager {
    private final AppContext context;
    private Stage stage;
    private Scene scenaActual;
    private StackPane rootStack;
    private static boolean saltando = false;
    private static final double ANCHO = 900; 
    private static final double ALTO = 650;
	private HistorialNotificacionesView historial;
    private final ToastManager toastManager;

    public SceneManager(AppContext context) {
        this.context = context;
        this.historial = new HistorialNotificacionesView(this);
        this.toastManager = new ToastManager();
    }
    public AppContext getContext() {
        return this.context;
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
            rootStack = new StackPane();
            rootStack.getChildren().addAll(nuevaVista, toastManager.getContainer());
            scenaActual = new Scene(rootStack, ANCHO, ALTO); 
            stage.setScene(scenaActual);
            stage.show();
        } else {
            if (rootStack == null) {
                rootStack = new StackPane();
                rootStack.getChildren().addAll(nuevaVista, toastManager.getContainer());
                scenaActual.setRoot(rootStack);
            } else {
                if (rootStack.getChildren().isEmpty()) {
                    rootStack.getChildren().addAll(nuevaVista, toastManager.getContainer());
                } else {
                    rootStack.getChildren().set(0, nuevaVista);
                }
            }
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
	    toastManager.showToast("Alerta", texto, ToastManager.Tipo.fromMessage(texto));
	    historial.refresh();
	}
    public void showVentanaHistorialNotificaciones() {
        historial.refresh();
        cambiarVista(historial);
    }
    
    public void showVentanaCuentaCompartida() {
    	CuentasCompartidasView vista = new CuentasCompartidasView(this); 
        cambiarVista(vista);
    }
    
    public void showTabla() {
    	showVisualizar(VisualizarTab.TABLA);
    }

    public void showVisualizar(VisualizarTab tab) {
        VisualizarView vista = new VisualizarView(this, tab);
        cambiarVista(vista);
    }

    public void showBarras() {
        showVisualizar(VisualizarTab.BARRAS);
    }

    public void showCirculares() {
        showVisualizar(VisualizarTab.PIE);
    }

    public void showCalendario() {
        showVisualizar(VisualizarTab.CALENDARIO);
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
