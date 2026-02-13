package ASP.BanCroak.ui.main;

import ASP.BanCroak.ui.app.SceneManager;
import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

public class BarraMenuView extends MenuBar {
	public BarraMenuView(SceneManager sm) {
        Menu menuMenu = new Menu("Menu");
        Menu menuVer = new Menu("Visualizar...");
        Menu menuNotificaciones = new Menu("Notificaciones");
        
        MenuItem itemCrearGasto = new MenuItem("Crear gasto");
        MenuItem itemCrearCuentaCompartida = new MenuItem("Crear cuenta compartida");
        MenuItem itemSalir = new MenuItem("Salir");
        MenuItem itemTabla = new MenuItem("Tabla");
        MenuItem itemBarras = new MenuItem("Barras");
        MenuItem itemCirculares = new MenuItem("Circulares");
        MenuItem itemCalendario = new MenuItem("Calendario");
        MenuItem itemCrearNotificacion = new MenuItem("Crear notificación");
        MenuItem itemHistorial = new MenuItem("Historial de notificaciones");
        
        

        itemCrearGasto.setOnAction(e -> sm.showVentanaCrearGasto());
        //itemCrearCuentaCompartida.setOnAction(e -> sm.showVentanaCuentaCompartida());
        itemCrearNotificacion.setOnAction(e -> sm.showVentanaCrearNotificaciones());
        //itemTabla.setOnAction(e -> sm.showTabla());
        //itemHistorial.setOnAction(e -> sm.showVentanaHistorialNotificaciones());*/
        itemSalir.setOnAction(e -> Platform.exit());

        menuMenu.getItems().addAll(itemCrearGasto, new SeparatorMenuItem(), itemCrearCuentaCompartida, new SeparatorMenuItem(),itemSalir);
        menuVer.getItems().addAll(itemTabla,new SeparatorMenuItem(), itemBarras,new SeparatorMenuItem(), itemCirculares, new SeparatorMenuItem(), itemCalendario);
        menuNotificaciones.getItems().addAll(itemCrearNotificacion, new SeparatorMenuItem(),itemHistorial);

        this.getMenus().addAll(menuMenu, menuVer,menuNotificaciones);
    }
}
