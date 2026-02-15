package ASP.BanCroak.ui.notificaciones;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ToastView extends HBox {
    public ToastView(String title, String message, ToastManager.Tipo tipo, Runnable onClose) {
        getStyleClass().add("toast-card");
        if (tipo == ToastManager.Tipo.SEMANAL) {
            getStyleClass().add("toast-weekly");
        } else if (tipo == ToastManager.Tipo.MENSUAL) {
            getStyleClass().add("toast-monthly");
        } else {
            getStyleClass().add("toast-generic");
        }

        Label lTitle = new Label(title == null ? "Alerta" : title);
        lTitle.getStyleClass().add("toast-title");
        Label lMessage = new Label(message == null ? "" : message);
        lMessage.getStyleClass().add("toast-message");
        lMessage.setWrapText(true);

        VBox content = new VBox(6, lTitle, lMessage);
        HBox.setHgrow(content, Priority.ALWAYS);

        Button close = new Button("✕");
        close.setGraphic(null);
        close.setText("✕");
        close.getStyleClass().add("toast-close");
        close.setOnAction(e -> onClose.run());
        close.setFocusTraversable(false);

        setAlignment(Pos.TOP_RIGHT);
        getChildren().addAll(content, close);
    }
}
