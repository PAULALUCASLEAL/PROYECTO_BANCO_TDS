package ASP.BanCroak.ui.app;

import ASP.BanCroak.domain.Gasto;
import ASP.BanCroak.ui.cuentas.CuentasCompartidasController;
import ASP.BanCroak.ui.cuentas.CuentasCompartidasView;
import ASP.BanCroak.ui.gastos.GastosController;
import ASP.BanCroak.ui.gastos.GastosView;
import ASP.BanCroak.ui.graficas.GraficasController;
import ASP.BanCroak.ui.graficas.GraficasView;
import ASP.BanCroak.ui.main.MainView;
import ASP.BanCroak.ui.notificaciones.NotificacionesController;
import ASP.BanCroak.ui.notificaciones.NotificacionesView;
import ASP.BanCroak.vista.GastoView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.List;

public class SceneManager {
    private final AppContext context;
    private Stage stage;
    private Scene scenaActual;
    private static final double ANCHO = 800; 
    private static final double ALTO = 650;

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

}
