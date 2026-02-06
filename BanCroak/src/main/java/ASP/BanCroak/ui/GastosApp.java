package ASP.BanCroak.ui;

import ASP.BanCroak.RepositorioGastos;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GastosApp extends Application {
    @Override
    public void start(Stage stage) {
        GastosView view = new GastosView();
        GastosController controller = new GastosController(RepositorioGastos.INSTANCE, view);
        controller.init();

        Scene scene = new Scene(view.getRoot(), 1100, 600);
        stage.setTitle("Demo Gastos");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
