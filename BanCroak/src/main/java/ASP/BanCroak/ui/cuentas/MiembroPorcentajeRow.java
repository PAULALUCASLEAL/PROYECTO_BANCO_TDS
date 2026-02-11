package ASP.BanCroak.ui.cuentas;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MiembroPorcentajeRow {
    private final StringProperty nombre;
    private final IntegerProperty porcentaje;

    public MiembroPorcentajeRow(String nombre, int porcentaje) {
        this.nombre = new SimpleStringProperty(nombre);
        this.porcentaje = new SimpleIntegerProperty(porcentaje);
    }

    public String getNombre() {
        return nombre.get();
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public int getPorcentaje() {
        return porcentaje.get();
    }

    public void setPorcentaje(int porcentaje) {
        this.porcentaje.set(porcentaje);
    }

    public IntegerProperty porcentajeProperty() {
        return porcentaje;
    }
}
