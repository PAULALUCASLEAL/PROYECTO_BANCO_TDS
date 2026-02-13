package ASP.BanCroak.ui.gastos;

import ASP.BanCroak.domain.AlertaGasto;
import ASP.BanCroak.domain.Cuenta;
import ASP.BanCroak.domain.Gasto;
import ASP.BanCroak.domain.Notificacion;
import ASP.BanCroak.filtros.FiltroCategoria;
import ASP.BanCroak.filtros.FiltroCompuesto;
import ASP.BanCroak.filtros.FiltroIntervaloFechas;
import ASP.BanCroak.filtros.FiltroMeses;
import ASP.BanCroak.repo.RepositorioAlertas;
import ASP.BanCroak.repo.RepositorioGastos;
import ASP.BanCroak.repo.RepositorioNotificaciones;
import ASP.BanCroak.service.AlertaService;
import ASP.BanCroak.ui.app.AppContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class GastosController {
    private static final String FILTRO_TODAS = "Todas";

    private final AppContext context;
    private final RepositorioGastos repo;
    private final RepositorioAlertas repoAlertas;
    private final RepositorioNotificaciones repoNotificaciones;
    private final AlertaService alertaService;
    private final GastosView view;
    private final int cuentaActivaId;

    private final ObservableList<Gasto> gastosData;
    private final ObservableList<String> categoriasData;
    private final ObservableList<RepartoRow> repartoData;
    private final ObservableList<AlertaGasto> alertasData;
    private FiltroCompuesto filtroActual;
    private Integer gastoEditandoId;

    public GastosController(AppContext context, GastosView view, int cuentaActivaId) {
        this.context = context;
        this.repo = context.getRepoGastos();
        this.repoAlertas = context.getRepoAlertas();
        this.repoNotificaciones = context.getRepoNotificaciones();
        this.alertaService = new AlertaService();
        this.view = view;
        this.cuentaActivaId = cuentaActivaId;
        this.gastosData = FXCollections.observableArrayList();
        this.categoriasData = FXCollections.observableArrayList();
        this.repartoData = FXCollections.observableArrayList();
        this.alertasData = FXCollections.observableArrayList();
        this.filtroActual = null;
        this.gastoEditandoId = null;

        this.view.getTablaGastos().setItems(gastosData);
        this.view.getCategoriaCombo().setItems(categoriasData);
        this.view.getTablaReparto().setItems(repartoData);
        this.view.getTablaAlertas().setItems(alertasData);
        this.view.getCategoriaAlertaCombo().setItems(categoriasData);
    }

    public void init() {
        view.setIdCuenta(cuentaActivaId);
        view.setIdCuentaEditable(false);
        context.getCuentaActiva().ifPresent(cuenta ->
            view.getCuentaLabel().setText("Cuenta activa: " + cuenta.getNombreCuenta())
        );
        refreshAll();
        configurarAccionAlertas();
        configurarPagadores();
        setEditMode(false);

        view.getAddGastoButton().setOnAction(e -> onAddGasto());
        view.getEditarButton().setOnAction(e -> onEditarSeleccionado());
        view.getGuardarCambiosButton().setOnAction(e -> onGuardarCambios());
        view.getCancelarEdicionButton().setOnAction(e -> onCancelarEdicion());
        view.getAddCategoriaButton().setOnAction(e -> onAddCategoria());
        view.getEliminarSeleccionadoButton().setOnAction(e -> onEliminarSeleccionado());
        view.getAplicarFiltroButton().setOnAction(e -> onAplicarFiltros());
        view.getLimpiarFiltroButton().setOnAction(e -> onLimpiarFiltros());
        view.getGraficasButton().setOnAction(e -> onGraficas());
        view.getCrearAlertaButton().setOnAction(e -> onCrearAlerta());
        view.getHistorialButton().setOnAction(e -> context.getNavigator().goToHistorialNotificaciones());

        view.getTablaGastos().setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                onEditarSeleccionado();
            }
        });
    }

    private void configurarPagadores() {
        Optional<Cuenta> cuentaOpt = context.getRepoCuentas().buscarPorId(cuentaActivaId);
        if (cuentaOpt.isEmpty()) {
            view.getPagadorCombo().getItems().clear();
            view.getPagadorCombo().setDisable(true);
            return;
        }
        Cuenta cuenta = cuentaOpt.get();
        List<String> miembros = cuenta.getMiembros();
        view.getPagadorCombo().getItems().setAll(miembros);
        if (cuenta.esPersonal()) {
            String nombre = miembros.isEmpty() ? "Yo" : miembros.get(0);
            view.getPagadorCombo().getSelectionModel().select(nombre);
            view.getPagadorCombo().setDisable(true);
        } else {
            view.getPagadorCombo().setDisable(false);
            if (!miembros.isEmpty()) {
                view.getPagadorCombo().getSelectionModel().select(0);
            }
        }
    }

    private void onAddGasto() {
        view.getFeedbackLabel().setText("");
        try {
            double cantidad = Double.parseDouble(view.getCantidadField().getText().trim());
            LocalDate fecha = view.getFechaPicker().getValue();
            String categoria = getCategoriaInput();
            String pagador = getPagadorInput();
            int idCuenta = cuentaActivaId;

            if (categoria.isEmpty()) {
                throw new IllegalArgumentException("La categoría no puede estar vacía");
            }
            if (!repo.existeCategoria(categoria)) {
                throw new IllegalArgumentException("La categoría no existe");
            }
            if (pagador.isEmpty()) {
                throw new IllegalArgumentException("Selecciona un pagador válido");
            }

            Gasto gasto = Gasto.crearGasto(cantidad, fecha, categoria, pagador, idCuenta);
            repo.añadirGasto(gasto);
            context.getGastosPersistence().save(repo);

            refreshAll();
            clearGastoInputs();
            view.getFeedbackLabel().setText("Guardado");
            evaluarAlertas();
        } catch (NumberFormatException ex) {
            showError("Cantidad no válida.", ex);
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage(), ex);
        }
    }

    private void onEditarSeleccionado() {
        Gasto seleccionado = view.getTablaGastos().getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            showError("Selecciona un gasto para editar.", null);
            return;
        }
        gastoEditandoId = seleccionado.getID();
        view.getCantidadField().setText(String.valueOf(seleccionado.getCantidad()));
        view.getFechaPicker().setValue(seleccionado.getFecha());
        view.getCategoriaCombo().getEditor().setText(seleccionado.getCategoria());
        view.getCategoriaCombo().setValue(seleccionado.getCategoria());
        view.getPagadorCombo().getSelectionModel().select(seleccionado.getPagador());
        setEditMode(true);
    }

    private void onGuardarCambios() {
        view.getFeedbackLabel().setText("");
        if (gastoEditandoId == null) {
            showError("No hay gasto en edición.", null);
            return;
        }
        try {
            double cantidad = Double.parseDouble(view.getCantidadField().getText().trim());
            LocalDate fecha = view.getFechaPicker().getValue();
            String categoria = getCategoriaInput();
            String pagador = getPagadorInput();

            if (categoria.isEmpty()) {
                throw new IllegalArgumentException("La categoría no puede estar vacía");
            }
            if (!repo.existeCategoria(categoria)) {
                throw new IllegalArgumentException("La categoría no existe");
            }
            if (pagador.isEmpty()) {
                throw new IllegalArgumentException("Selecciona un pagador válido");
            }

            repo.editarGasto(gastoEditandoId, cantidad, fecha, categoria, pagador);
            context.getGastosPersistence().save(repo);

            refreshAll();
            clearGastoInputs();
            setEditMode(false);
            view.getFeedbackLabel().setText("Actualizado");
            evaluarAlertas();
        } catch (NumberFormatException ex) {
            showError("Cantidad no válida.", ex);
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage(), ex);
        }
    }

    private void onCancelarEdicion() {
        clearGastoInputs();
        setEditMode(false);
    }

    private void setEditMode(boolean editing) {
        view.getAddGastoButton().setDisable(editing);
        view.getEditarButton().setDisable(editing);
        view.getGuardarCambiosButton().setDisable(!editing);
        view.getCancelarEdicionButton().setDisable(!editing);
        if (!editing) {
            gastoEditandoId = null;
        }
    }

    private void onAddCategoria() {
        view.getFeedbackLabel().setText("");
        try {
            String categoria = view.getNuevaCategoriaField().getText().trim();
            if (categoria.isEmpty()) {
                throw new IllegalArgumentException("La categoría no puede estar vacía");
            }
            repo.añadirCategoria(categoria);
            context.getGastosPersistence().save(repo);
            refreshAll();
            view.getNuevaCategoriaField().clear();
            view.getFeedbackLabel().setText("Guardado");
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage(), ex);
        }
    }

    private void onEliminarSeleccionado() {
        view.getFeedbackLabel().setText("");
        Gasto seleccionado = view.getTablaGastos().getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            showError("Selecciona un gasto para eliminar.", null);
            return;
        }
        try {
            repo.eliminarGasto(seleccionado);
            context.getGastosPersistence().save(repo);
            refreshAll();
            view.getFeedbackLabel().setText("Guardado");
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage(), ex);
        }
    }

    private void onCrearAlerta() {
        view.getFeedbackLabel().setText("");
        try {
            AlertaGasto.Periodo periodo = view.getPeriodoAlertaCombo().getValue();
            if (periodo == null) {
                throw new IllegalArgumentException("Selecciona un periodo para la alerta");
            }
            double limite = Double.parseDouble(view.getLimiteAlertaField().getText().trim());
            if (limite <= 0) {
                throw new IllegalArgumentException("El límite debe ser mayor que 0");
            }
            String categoriaRaw = getCategoriaAlertaInput();
            String categoriaNormalizada = null;
            if (!categoriaRaw.isEmpty()) {
                if (!repo.existeCategoria(categoriaRaw)) {
                    throw new IllegalArgumentException("La categoría no existe");
                }
                categoriaNormalizada = repo.normalizarCategoriaPublic(categoriaRaw);
            }
            repoAlertas.crearAlerta(periodo, limite, categoriaNormalizada, true);
            context.getAlertasPersistence().save(repoAlertas);
            refreshAlertas();
            view.getLimiteAlertaField().clear();
            view.getCategoriaAlertaCombo().getEditor().clear();
            view.getCategoriaAlertaCombo().setValue(null);
            view.getPeriodoAlertaCombo().setValue(null);
            view.getFeedbackLabel().setText("Alerta creada");
        } catch (NumberFormatException ex) {
            showError("Límite no válido.", ex);
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage(), ex);
        }
    }

    private void configurarAccionAlertas() {
        TableColumn<AlertaGasto, Void> col = view.getColAccionAlerta();
        col.setCellFactory(tc -> new TableCell<>() {
            private final Button btn = new Button();

            {
                btn.getStyleClass().add("secondary-button");
                btn.setOnAction(e -> {
                    AlertaGasto alerta = getTableView().getItems().get(getIndex());
                    alerta.setActiva(!alerta.isActiva());
                    context.getAlertasPersistence().save(repoAlertas);
                    refreshAlertas();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    AlertaGasto alerta = getTableView().getItems().get(getIndex());
                    btn.setText(alerta.isActiva() ? "Pausar" : "Activar");
                    setGraphic(btn);
                }
            }
        });
    }

    private void refreshAll() {
        List<Gasto> filtrados = filtrarGastos();
        gastosData.setAll(filtrados);
        refreshCategoriaCombos();
        updateReparto(filtrados);
        refreshAlertas();
        configurarPagadores();
    }

    private void refreshCategoriaCombos() {
        List<String> categorias = repo.getCategorias().stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
        categoriasData.setAll(categorias);

        String selectedFiltro = view.getFiltroCategoriaCombo().getValue();
        view.getFiltroCategoriaCombo().getItems().setAll(FILTRO_TODAS);
        view.getFiltroCategoriaCombo().getItems().addAll(categorias);
        if (selectedFiltro == null || selectedFiltro.isBlank()) {
            view.getFiltroCategoriaCombo().setValue(FILTRO_TODAS);
        } else if (view.getFiltroCategoriaCombo().getItems().contains(selectedFiltro)) {
            view.getFiltroCategoriaCombo().setValue(selectedFiltro);
        } else {
            view.getFiltroCategoriaCombo().setValue(FILTRO_TODAS);
        }
    }

    private void refreshAlertas() {
        List<AlertaGasto> lista = repoAlertas.listarAlertas().stream()
            .sorted(Comparator.comparingInt(AlertaGasto::getId))
            .collect(Collectors.toList());
        alertasData.setAll(lista);
    }

    private void onAplicarFiltros() {
        view.getFeedbackLabel().setText("");
        try {
            filtroActual = construirFiltro();
            List<Gasto> filtrados = filtrarGastos();
            gastosData.setAll(filtrados);
            updateReparto(filtrados);
            view.getFeedbackLabel().setText("Filtro aplicado (" + filtrados.size() + ")");
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage(), ex);
        }
    }

    private void onLimpiarFiltros() {
        view.getFiltroCategoriaCombo().setValue(FILTRO_TODAS);
        view.getFiltroMesesField().clear();
        view.getFiltroDesdePicker().setValue(null);
        view.getFiltroHastaPicker().setValue(null);
        filtroActual = null;
        refreshAll();
        view.getFeedbackLabel().setText("Filtros limpiados");
    }

    private void onGraficas() {
        List<Gasto> filtrados = filtrarGastos();
        context.getNavigator().goToGraficas(cuentaActivaId, filtrados);
    }

    private List<Gasto> filtrarGastos() {
        List<Gasto> base = repo.getListaGastos().stream()
            .filter(g -> g.getIDCuenta() == cuentaActivaId)
            .collect(Collectors.toList());
        if (filtroActual == null) {
            return base;
        }
        return base.stream()
            .filter(filtroActual::filtrar)
            .collect(Collectors.toList());
    }

    private void updateReparto(List<Gasto> gastosVisibles) {
        Optional<Cuenta> cuentaOpt = context.getRepoCuentas().buscarPorId(cuentaActivaId);
        if (cuentaOpt.isEmpty()) {
            repartoData.clear();
            view.getTotalRepartoLabel().setText("Total visible: 0.00 €");
            return;
        }
        Cuenta cuenta = cuentaOpt.get();
        double total = gastosVisibles.stream().mapToDouble(Gasto::getCantidad).sum();
        Map<String, Double> reparto = cuenta.calcularReparto(total);
        Map<String, Double> porcentajes = cuenta.getPorcentajes();

        List<RepartoRow> rows = reparto.entrySet().stream()
            .map(entry -> new RepartoRow(
                entry.getKey(),
                porcentajes.getOrDefault(entry.getKey(), 0.0),
                entry.getValue()
            ))
            .collect(Collectors.toList());

        repartoData.setAll(rows);
        view.getTotalRepartoLabel().setText(String.format("Total visible: %.2f €", total));
    }

    private FiltroCompuesto construirFiltro() {
        FiltroCompuesto compuesto = new FiltroCompuesto();

        String categoria = view.getFiltroCategoriaCombo().getValue();
        if (categoria != null && !categoria.equals(FILTRO_TODAS)) {
            List<String> categorias = List.of(categoria);
            compuesto.añadirFiltro(new FiltroCategoria(categorias));
        }

        List<String> meses = parseLista(view.getFiltroMesesField().getText());
        if (!meses.isEmpty()) {
            compuesto.añadirFiltro(new FiltroMeses(meses));
        }

        LocalDate desde = view.getFiltroDesdePicker().getValue();
        LocalDate hasta = view.getFiltroHastaPicker().getValue();
        if (desde != null || hasta != null) {
            compuesto.añadirFiltro(new FiltroIntervaloFechas(desde, hasta));
        }

        return compuesto;
    }

    private void clearGastoInputs() {
        view.getCantidadField().clear();
        view.getFechaPicker().setValue(null);
        view.getCategoriaCombo().getEditor().clear();
        view.getCategoriaCombo().setValue(null);
        if (!view.getPagadorCombo().isDisabled()) {
            view.getPagadorCombo().getSelectionModel().clearSelection();
        }
    }

    private String getCategoriaInput() {
        String editorText = view.getCategoriaCombo().getEditor().getText();
        if (editorText != null && !editorText.isBlank()) {
            return editorText.trim();
        }
        String value = view.getCategoriaCombo().getValue();
        return value == null ? "" : value.trim();
    }

    private String getPagadorInput() {
        String value = view.getPagadorCombo().getValue();
        return value == null ? "" : value.trim();
    }

    private String getCategoriaAlertaInput() {
        String editorText = view.getCategoriaAlertaCombo().getEditor().getText();
        if (editorText != null && !editorText.isBlank()) {
            return editorText.trim();
        }
        String value = view.getCategoriaAlertaCombo().getValue();
        return value == null ? "" : value.trim();
    }

    private void evaluarAlertas() {
        List<Notificacion> nuevas = alertaService.evaluarYNotificar(cuentaActivaId, repo, repoAlertas, repoNotificaciones);
        if (!nuevas.isEmpty()) {
            context.getNotificacionesPersistence().save(repoNotificaciones);
            String mensaje = nuevas.stream().map(Notificacion::getMensaje).collect(Collectors.joining("\n"));
            Alert alert = new Alert(Alert.AlertType.INFORMATION, mensaje, ButtonType.OK);
            alert.setHeaderText("Notificación de alertas");
            alert.showAndWait();
        }
    }

    private void showError(String message, Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setHeaderText("Error");
        alert.showAndWait();
        if (ex != null) {
            ex.printStackTrace();
        }
    }

    private List<String> parseLista(String raw) {
        List<String> result = new ArrayList<>();
        if (raw == null || raw.isBlank()) {
            return result;
        }
        String[] parts = raw.split(",");
        for (String p : parts) {
            String s = p.trim();
            if (!s.isEmpty()) {
                result.add(s);
            }
        }
        return result;
    }
}
