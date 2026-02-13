package ASP.BanCroak.ui.notificaciones;

import ASP.BanCroak.domain.Notificacion;
import ASP.BanCroak.repo.RepositorioNotificaciones;
import ASP.BanCroak.ui.app.AppContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class NotificacionesController {
    private final AppContext context;
    private final RepositorioNotificaciones repo;
    private final NotificacionesView view;
    private final ObservableList<Notificacion> data;

    public NotificacionesController(AppContext context, NotificacionesView view) {
        this.context = context;
        this.repo = context.getRepoNotificaciones();
        this.view = view;
        this.data = FXCollections.observableArrayList();
        this.view.getTabla().setItems(data);
    }

    public void init() {
        refresh();
        view.getVolverButton().setOnAction(e -> context.getNavigator().showVentanaCrearGasto());
    }

    private void refresh() {
        List<Notificacion> ordenadas = repo.listarNotificaciones().stream()
            .sorted(Comparator.comparing(Notificacion::getTimestamp).reversed())
            .collect(Collectors.toList());
        data.setAll(ordenadas);
    }
}
