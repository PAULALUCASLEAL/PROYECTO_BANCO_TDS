package ASP.BanCroak.service;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;
import java.time.YearMonth;

public final class FilterState {
    private final StringProperty categoria = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> desde = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> hasta = new SimpleObjectProperty<>();
    private final ObjectProperty<YearMonth> mes = new SimpleObjectProperty<>();

    public StringProperty categoriaProperty() {
        return categoria;
    }

    public ObjectProperty<LocalDate> desdeProperty() {
        return desde;
    }

    public ObjectProperty<LocalDate> hastaProperty() {
        return hasta;
    }

    public ObjectProperty<YearMonth> mesProperty() {
        return mes;
    }

    public String getCategoria() {
        return categoria.get();
    }

    public LocalDate getDesde() {
        return desde.get();
    }

    public LocalDate getHasta() {
        return hasta.get();
    }

    public YearMonth getMes() {
        return mes.get();
    }

    public void clear() {
        categoria.set(null);
        desde.set(null);
        hasta.set(null);
        mes.set(null);
    }
}
