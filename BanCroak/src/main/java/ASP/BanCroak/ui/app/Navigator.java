package ASP.BanCroak.ui.app;

import ASP.BanCroak.domain.Gasto;
import ASP.BanCroak.ui.cuentas.CuentasCompartidasController;
import ASP.BanCroak.ui.cuentas.CuentasCompartidasView;
import ASP.BanCroak.ui.gastos.GastosController;
import ASP.BanCroak.ui.gastos.GastosView;
import ASP.BanCroak.ui.graficas.GraficasController;
import ASP.BanCroak.ui.graficas.GraficasView;
import ASP.BanCroak.ui.main.MainController;
import ASP.BanCroak.ui.main.MainView;
import ASP.BanCroak.ui.notificaciones.NotificacionesController;
import ASP.BanCroak.ui.notificaciones.NotificacionesView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class Navigator {
    private final AppContext context;
    private Stage stage;
    private Scene scene;

    public Navigator(AppContext context) {
        this.context = context;
    }

    public void init(Stage stage) {
        this.stage = stage;
        if (scene == null) {
            scene = new Scene(new javafx.scene.layout.BorderPane(), 1100, 650);
            String css = getClass().getResource("/estilos.css").toExternalForm();
            scene.getStylesheets().add(css);
            String mainCss = getClass().getResource("/styles/bancroak.css").toExternalForm();
            scene.getStylesheets().add(mainCss);
        }
        stage.setScene(scene);
    }

    public void goToMain() {
        MainView view = new MainView();
        new MainController(context, view);
        setRoot(view.getRoot());
    }

    public void goToGastos(int cuentaId) {
        context.setCuentaActivaId(cuentaId);
        GastosView view = new GastosView();
        GastosController controller = new GastosController(context, view, cuentaId);
        controller.init();
        setRoot(view.getRoot());
    }

    public void goToCuentasCompartidas() {
        CuentasCompartidasView view = new CuentasCompartidasView();
        CuentasCompartidasController controller = new CuentasCompartidasController(context, view);
        controller.init();
        setRoot(view.getRoot());
    }

    public void goToGraficas(int cuentaId, List<Gasto> gastosFiltrados) {
        GraficasView view = new GraficasView();
        GraficasController controller = new GraficasController(context, view, cuentaId, gastosFiltrados);
        controller.init();
        setRoot(view.getRoot());
    }

    public void goToHistorialNotificaciones() {
        NotificacionesView view = new NotificacionesView();
        NotificacionesController controller = new NotificacionesController(context, view);
        controller.init();
        setRoot(view.getRoot());
    }

    private void setRoot(Parent root) {
        if (scene == null) {
            scene = new Scene(root, 1100, 650);
            stage.setScene(scene);
            return;
        }
        scene.setRoot(root);
    }
}
