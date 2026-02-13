package ASP.BanCroak.ui.graficas;

import ASP.BanCroak.domain.Gasto;
import ASP.BanCroak.ui.app.AppContext;
import ASP.BanCroak.ui.cuentas.CuentasCompartidasView.Persona;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GraficasController {
    private final AppContext context;
    private final TablaView view;
    private final int cuentaId;
    private final List<Gasto> gastosFiltrados;

    public GraficasController(AppContext context, TablaView view, int cuentaId, List<Gasto> gastosFiltrados) {
        this.context = context;
        this.view = view;
        this.cuentaId = cuentaId;
        this.gastosFiltrados = gastosFiltrados;
    }

    private void renderTabla() {
       
    }


}
