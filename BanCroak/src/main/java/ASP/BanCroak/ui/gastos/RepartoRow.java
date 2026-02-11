package ASP.BanCroak.ui.gastos;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RepartoRow {
    private final StringProperty miembro;
    private final DoubleProperty porcentaje;
    private final DoubleProperty debePagar;

    public RepartoRow(String miembro, double porcentaje, double debePagar) {
        this.miembro = new SimpleStringProperty(miembro);
        this.porcentaje = new SimpleDoubleProperty(porcentaje);
        this.debePagar = new SimpleDoubleProperty(debePagar);
    }

    public String getMiembro() {
        return miembro.get();
    }

    public StringProperty miembroProperty() {
        return miembro;
    }

    public double getPorcentaje() {
        return porcentaje.get();
    }

    public DoubleProperty porcentajeProperty() {
        return porcentaje;
    }

    public double getDebePagar() {
        return debePagar.get();
    }

    public DoubleProperty debePagarProperty() {
        return debePagar;
    }
}
