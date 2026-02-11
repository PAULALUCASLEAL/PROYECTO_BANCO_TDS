package ASP.BanCroak.ui.app;

import javafx.application.Application;
import javafx.stage.Stage;

public class BancoApp extends Application {
    @Override
    public void start(Stage stage) {
        AppContext context = new AppContext();
        Navigator navigator = new Navigator(context);
        context.setNavigator(navigator);

        navigator.init(stage);
        navigator.goToMain();

        stage.setTitle("BanCroak");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
