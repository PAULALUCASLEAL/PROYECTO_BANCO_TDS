package ASP.BanCroak.ui.main;

import ASP.BanCroak.domain.Cuenta;
import ASP.BanCroak.ui.app.AppContext;

public class MainController {
    private final AppContext context;
    private final MainView view;

    public MainController(AppContext context, MainView view) {
        this.context = context;
        this.view = view;
        init();
    }

    private void init() {
        view.getMiCuentaButton().setOnAction(e -> onMiCuenta());
        view.getOtrasCuentasButton().setOnAction(e -> onOtrasCuentas());
    }

    private void onMiCuenta() {
        Cuenta cuenta = context.getCuentaPersonal().orElse(null);
        if (cuenta != null) {
            context.getNavigator().goToGastos(cuenta.getIdCuenta());
        }
    }

    private void onOtrasCuentas() {
        context.getNavigator().goToCuentasCompartidas();
    }
}
