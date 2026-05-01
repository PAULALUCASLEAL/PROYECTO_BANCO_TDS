package ASP.BanCroak.ui.graficas;

import ASP.BanCroak.ui.main.BarraMenuView;

import java.util.HashMap;
import java.util.Map;

import ASP.BanCroak.ui.app.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class TablaView extends VBox{
	
	public static class FilaReparto {
        private final String nombre;
        private final Map<String, Double> gastos;

        public FilaReparto(String nombre) {
            this.nombre = nombre;
            this.gastos = new HashMap<>();
        }

        public String getNombre() { 
        	return nombre; 
        	}
        public void setGasto(String cat, double cant) { 
        	gastos.put(cat, cant); 
        	}
        public double getGastoPorCategoria(String cat) { 
        	return gastos.getOrDefault(cat, 0.0); }
    }
	
	
	private TableView<FilaReparto> tabla;
    private ObservableList<FilaReparto> datosTabla = FXCollections.observableArrayList();
	public TablaView(SceneManager sm) {
        
		this.setSpacing(0);
	    this.setAlignment(Pos.CENTER);
	    this.setId("estilo_TablaView");
	    this.getStylesheets().add(getClass().getResource("/estilos.css").toExternalForm());
	    BarraMenuView barra = new BarraMenuView(sm);
	    tabla = new TableView<>(datosTabla);
	    tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        VBox.setVgrow(tabla, Priority.ALWAYS);

	    Button bFiltro = new Button("Filtrar gastos");
	    bFiltro.setOnAction(e -> new FiltroView(sm));
	    this.getChildren().addAll(barra,bFiltro,tabla);
	}


	}