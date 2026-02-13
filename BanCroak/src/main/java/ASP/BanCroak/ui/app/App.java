package ASP.BanCroak.ui.app;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException{
        AppContext context = new AppContext();
        SceneManager sm = new SceneManager(context);
        context.setNavigator(sm);

        sm.inicializar(stage);
        sm.showVentanaPrincipal();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
