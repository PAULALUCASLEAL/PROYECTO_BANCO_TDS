package ASP.BanCroak.ui.visualizar;

import ASP.BanCroak.domain.Gasto;
import ASP.BanCroak.repo.RepositorioGastos;
import ASP.BanCroak.service.FilterState;
import ASP.BanCroak.ui.app.AppContext;
import ASP.BanCroak.ui.app.GastosStore;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class GastosFilterPane extends VBox {
    private final GastosStore store;
    private final FilterState filterState;
    private final RepositorioGastos repoGastos;

    private final ComboBox<String> categoriaCombo = new ComboBox<>();
    private final ObservableList<String> categoriasBase = FXCollections.observableArrayList();

    private final ComboBox<Month> mesCombo = new ComboBox<>();
    private final ComboBox<Integer> anioCombo = new ComboBox<>();
    private final DatePicker desde = new DatePicker();
    private final DatePicker hasta = new DatePicker();
    private final Label errorFechas = new Label("El rango de fechas es inválido");

    public GastosFilterPane(AppContext context, GastosStore store, FilterState filterState) {
        this.store = store;
        this.filterState = filterState;
        this.repoGastos = context.getRepoGastos();

        build();
        refreshChoices();
        registerListeners();
    }

    private void build() {
        this.setSpacing(10);
        this.setPadding(new Insets(10, 0, 10, 0));
        this.getStyleClass().add("card");

        Label titulo = new Label("Filtros");
        titulo.getStyleClass().add("section-title");

        categoriaCombo.setEditable(true);
        categoriaCombo.setPromptText("Categoría (todas)");
        categoriaCombo.setMaxWidth(Double.MAX_VALUE);
        categoriaCombo.setItems(categoriasBase);

        desde.setPromptText("Desde");
        hasta.setPromptText("Hasta");

        mesCombo.setPromptText("Mes");
        mesCombo.setMaxWidth(Double.MAX_VALUE);
        mesCombo.getItems().setAll(Month.values());
        mesCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(Month month) {
                if (month == null) {
                    return "";
                }
                return month.getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
            }

            @Override
            public Month fromString(String string) {
                return null;
            }
        });

        int yearActual = LocalDate.now().getYear();
        for (int y = yearActual - 5; y <= yearActual + 1; y++) {
            anioCombo.getItems().add(y);
        }
        anioCombo.setValue(yearActual);
        anioCombo.setPromptText("Año");
        anioCombo.setMaxWidth(Double.MAX_VALUE);

        errorFechas.getStyleClass().add("error-text");
        errorFechas.setVisible(false);
        errorFechas.setManaged(false);

        Button limpiar = new Button("Limpiar filtros");
        limpiar.setOnAction(e -> {
            filterState.clear();
            clearSelections();
        });

        HBox filaCategoria = new HBox(10, new Label("Categoría:"), categoriaCombo);
        filaCategoria.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(categoriaCombo, Priority.ALWAYS);

        HBox filaMes = new HBox(10, new Label("Mes/Año:"), mesCombo, anioCombo);
        filaMes.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(mesCombo, Priority.ALWAYS);
        HBox.setHgrow(anioCombo, Priority.ALWAYS);

        HBox filaFechas = new HBox(10, new Label("Rango:"), desde, hasta);
        filaFechas.setAlignment(Pos.CENTER_LEFT);

        this.getChildren().addAll(titulo, filaCategoria, filaMes, filaFechas, errorFechas, limpiar);
    }

    public void refreshChoices() {
        rebuildCategorias();
    }

    private void registerListeners() {
        store.getGastos().addListener((ListChangeListener<Gasto>) c -> refreshChoices());

        ChangeListener<String> categoriaListener = (obs, o, n) -> actualizarCategoriaDesdeEditor();
        categoriaCombo.getEditor().textProperty().addListener(categoriaListener);
        categoriaCombo.valueProperty().addListener((obs, o, n) -> {
            if (n == null || n.isBlank()) {
                filterState.categoriaProperty().set(null);
                return;
            }
            categoriaCombo.getEditor().setText(n);
            filterState.categoriaProperty().set(n);
        });
        categoriaCombo.setOnAction(e -> actualizarCategoriaDesdeEditor());
        categoriaCombo.getEditor().setOnAction(e -> actualizarCategoriaDesdeEditor());
        categoriaCombo.getEditor().focusedProperty().addListener((obs, o, n) -> {
            if (!n) {
                actualizarCategoriaDesdeEditor();
            }
        });
        categoriaCombo.getEditor().setOnMouseClicked(e -> {
            if (!categoriaCombo.isShowing()) {
                categoriaCombo.show();
            }
        });

        mesCombo.valueProperty().addListener((obs, o, n) -> actualizarMes());
        anioCombo.valueProperty().addListener((obs, o, n) -> actualizarMes());

        desde.valueProperty().addListener((obs, o, n) -> syncDates());
        hasta.valueProperty().addListener((obs, o, n) -> syncDates());
    }

    private void rebuildCategorias() {
        List<String> categorias = repoGastos.getCategorias().isEmpty()
            ? store.snapshot().stream()
                .map(Gasto::getCategoria)
                .filter(s -> s != null && !s.isBlank())
                .distinct()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList())
            : repoGastos.getCategorias().stream()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());

        categoriasBase.setAll(categorias);
        String actual = filterState.getCategoria();
        if (actual != null && !actual.isBlank()) {
            categoriaCombo.getEditor().setText(actual);
        }
    }

    private void clearSelections() {
        categoriaCombo.getEditor().clear();
        categoriaCombo.getSelectionModel().clearSelection();
        mesCombo.getSelectionModel().clearSelection();
        anioCombo.setValue(LocalDate.now().getYear());
        desde.setValue(null);
        hasta.setValue(null);
        errorFechas.setVisible(false);
        errorFechas.setManaged(false);
        desde.setStyle("");
        hasta.setStyle("");
        filterState.categoriaProperty().set(null);
        filterState.mesProperty().set(null);
        filterState.desdeProperty().set(null);
        filterState.hastaProperty().set(null);
        refreshChoices();
    }

    private void actualizarCategoriaDesdeEditor() {
        String texto = categoriaCombo.getEditor().getText();
        if (texto == null || texto.isBlank()) {
            filterState.categoriaProperty().set(null);
            return;
        }
        String limpio = texto.trim();
        String exacta = categoriasBase.stream()
            .filter(c -> c.equalsIgnoreCase(limpio))
            .findFirst()
            .orElse(null);
        if (exacta != null) {
            categoriaCombo.getEditor().setText(exacta);
            filterState.categoriaProperty().set(exacta);
        }
    }

    private void actualizarMes() {
        Month mes = mesCombo.getValue();
        Integer anio = anioCombo.getValue();
        if (mes == null || anio == null) {
            filterState.mesProperty().set(null);
            return;
        }
        filterState.mesProperty().set(YearMonth.of(anio, mes));
    }

    private void syncDates() {
        LocalDate d = desde.getValue();
        LocalDate h = hasta.getValue();
        if (d != null && h != null && d.isAfter(h)) {
            errorFechas.setVisible(true);
            errorFechas.setManaged(true);
            desde.setStyle("-fx-border-color: #c62828;");
            hasta.setStyle("-fx-border-color: #c62828;");
            return;
        }
        errorFechas.setVisible(false);
        errorFechas.setManaged(false);
        desde.setStyle("");
        hasta.setStyle("");
        filterState.desdeProperty().set(d);
        filterState.hastaProperty().set(h);
    }
}
