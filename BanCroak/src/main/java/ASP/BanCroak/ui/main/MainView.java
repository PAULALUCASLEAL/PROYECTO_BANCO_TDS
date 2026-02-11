package ASP.BanCroak.ui.main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class MainView {

    private final BorderPane root;

    private final Button miCuentaButton;
    private final Button otrasCuentasButton;

    public MainView() {
        root = new BorderPane();
        VBox centerBox = new VBox(20);
        centerBox.setPadding(new Insets(40));
        centerBox.setAlignment(Pos.CENTER);

        miCuentaButton = new Button("MI CUENTA");
        otrasCuentasButton = new Button("OTRAS CUENTAS");

        miCuentaButton.setPrefWidth(300);
        otrasCuentasButton.setPrefWidth(300);
        miCuentaButton.setFont(Font.font(20));
        otrasCuentasButton.setFont(Font.font(20));

        centerBox.getChildren().addAll(miCuentaButton, otrasCuentasButton);
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
