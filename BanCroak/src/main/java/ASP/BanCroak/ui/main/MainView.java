package ASP.BanCroak.ui.main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MainView {

    private final BorderPane root;

    private final Button miCuentaButton;
    private final Button otrasCuentasButton;

    public MainView() {
        root = new BorderPane();
        root.getStyleClass().add("main-root");

        Label title = new Label("BanCroak");
        title.getStyleClass().add("main-title");

        Label subtitle = new Label("Gestiona tus gastos y cuentas compartidas");
        subtitle.getStyleClass().add("main-subtitle");

        miCuentaButton = new Button("MI CUENTA");
        otrasCuentasButton = new Button("OTRAS CUENTAS");

        miCuentaButton.getStyleClass().addAll("card-button", "primary-button");
        otrasCuentasButton.getStyleClass().addAll("card-button", "secondary-button");

        miCuentaButton.setMinWidth(360);
        miCuentaButton.setMinHeight(80);
        otrasCuentasButton.setMinWidth(360);
        otrasCuentasButton.setMinHeight(80);

        VBox centerBox = new VBox(18, title, subtitle, miCuentaButton, otrasCuentasButton);
        centerBox.setPadding(new Insets(48));
        centerBox.setAlignment(Pos.CENTER);

        root.setCenter(centerBox);
    }

    public Parent getRoot() {
        return root;
    }

    public Button getMiCuentaButton() {
        return miCuentaButton;
    }

    public Button getOtrasCuentasButton() {
        return otrasCuentasButton;
    }
}
