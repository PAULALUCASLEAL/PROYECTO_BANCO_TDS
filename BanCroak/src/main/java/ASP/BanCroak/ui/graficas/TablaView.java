package ASP.BanCroak.ui.graficas;

import ASP.BanCroak.ui.main.BarraMenuView;
import ASP.BanCroak.ui.app.SceneManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class TablaView extends VBox{
	public TablaView(SceneManager sm) {
        
		this.setSpacing(0);
	    this.setAlignment(Pos.CENTER);
	    this.setId("estilo_HistorialNotificacionesView");
	    this.getStylesheets().add(getClass().getResource("/estilos.css").toExternalForm());
	    BarraMenuView barra = new BarraMenuView(sm);
	    



	    Button bFiltro = new Button();
	    bFiltro.setOnAction(e -> {FiltroView filtro = new FiltroView(sm);
	    	
	    });
	    this.getChildren().addAll(barra,bFiltro);
	}


	}