package ASP.BanCroak.ui.graficas;

import java.util.List;
import java.util.stream.Collectors;

import ASP.BanCroak.ui.app.SceneManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FiltroView extends Stage {
	public FiltroView(SceneManager sm) {
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Filtrar Gastos");

        VBox filtroV = new VBox(20);
        filtroV.setPadding(new Insets(25));
        filtroV.setAlignment(Pos.CENTER_LEFT);
        filtroV.setId("estilo_FiltroView");
        filtroV.getStylesheets().add(getClass().getResource("/estilos.css").toExternalForm());

        Label lTitulo = new Label("FILTROS:");    
        Label lMeses = new Label("Meses:");
        MenuButton comboMeses = new MenuButton("Seleccionar meses...");
        comboMeses.setMaxWidth(Double.MAX_VALUE);
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", 
                          "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        for (String m : meses) {
            comboMeses.getItems().add(new CheckMenuItem(m));
        }

        Label lCat = new Label("Categorías:");
        MenuButton comboCategorias = new MenuButton("Seleccionar categorías...");
        comboCategorias.setMaxWidth(Double.MAX_VALUE);
        String[] categorias = {"Transporte", "Comida", "Ocio", "Salud", "Hogar"};
        for (String c : categorias) {
            comboCategorias.getItems().add(new CheckMenuItem(c));
        }

        Label lFechas = new Label("Intervalo de fechas:");
        DatePicker fechaDesde = new DatePicker();
        fechaDesde.setPromptText("Desde");
        DatePicker fechaHasta = new DatePicker();
        fechaHasta.setPromptText("Hasta");
        HBox intervaloH = new HBox(10, fechaDesde, fechaHasta);

        Button bFiltrar = new Button("Aplicar Filtros");
        bFiltrar.setMaxWidth(Double.MAX_VALUE);

        bFiltrar.setOnAction(e -> {
            List<String> mesesElegidos = comboMeses.getItems().stream()
                .filter(item -> ((CheckMenuItem) item).isSelected())
                .map(MenuItem::getText)
                .collect(Collectors.toList());
            List<String> categoriasElegidas = comboCategorias.getItems().stream()
                    .filter(item -> ((CheckMenuItem) item).isSelected())
                    .map(MenuItem::getText)
                    .collect(Collectors.toList());
            System.out.println("Meses: " + mesesElegidos + "Categorias: "+categoriasElegidas+"fechas"+fechaDesde.getValue()+fechaHasta.getValue());
            this.close();
        });

        filtroV.getChildren().addAll(lTitulo, lMeses, comboMeses, lCat, comboCategorias, lFechas, intervaloH, bFiltrar);

        Scene scene = new Scene(filtroV, 400, 500);
        this.setScene(scene);
        this.show(); 
        
}
}
