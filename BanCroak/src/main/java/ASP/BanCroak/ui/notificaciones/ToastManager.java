package ASP.BanCroak.ui.notificaciones;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ToastManager {
    public enum Tipo {
        SEMANAL,
        MENSUAL,
        GENERICO;

        public static Tipo fromMessage(String mensaje) {
            if (mensaje == null) {
                return GENERICO;
            }
            String lower = mensaje.toLowerCase();
            if (lower.contains("semanal")) {
                return SEMANAL;
            }
            if (lower.contains("mensual")) {
                return MENSUAL;
            }
            return GENERICO;
        }
    }

    private final VBox container;

    public ToastManager() {
        this.container = new VBox(12);
        this.container.getStyleClass().add("toast-container");
        this.container.setPickOnBounds(false);
        this.container.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        this.container.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        StackPane.setAlignment(this.container, Pos.TOP_RIGHT);
        StackPane.setMargin(this.container, new Insets(24, 24, 24, 24));
        String css = getClass().getResource("/estilos.css").toExternalForm();
        this.container.getStylesheets().add(css);
    }

    public VBox getContainer() {
        return container;
    }

    public void showToast(String title, String message, Tipo tipo) {
        final ToastView[] holder = new ToastView[1];
        holder[0] = new ToastView(title, message, tipo, () -> container.getChildren().remove(holder[0]));
        container.getChildren().add(0, holder[0]);
    }
}
