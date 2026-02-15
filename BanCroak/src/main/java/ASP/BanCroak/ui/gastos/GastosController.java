package ASP.BanCroak.ui.gastos;

import ASP.BanCroak.domain.Cuenta;
import ASP.BanCroak.domain.Gasto;
import ASP.BanCroak.repo.RepositorioCuentas;
import ASP.BanCroak.repo.RepositorioGastos;
import ASP.BanCroak.ui.app.AppContext;
import ASP.BanCroak.ui.app.GastosStore;

import java.io.File;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Set;

public class GastosController {
	private final AppContext context;
    private final RepositorioGastos repoGastos;
    private final RepositorioCuentas repoCuentas;
    private final GastosStore gastosStore;

    public GastosController(AppContext context) {
        this.context = context;
        this.repoGastos = context.getRepoGastos();
        this.repoCuentas = context.getRepoCuentas();
        this.gastosStore = context.getGastosStore();
    }

    public List<String> getCuentas() {
        return repoCuentas.listarCuentas().stream()
                .map(Cuenta::getNombreCuenta)
                .collect(Collectors.toList());
    }

    public List<String> getPersonasDeCuenta(String nombreCuenta) {
    	return repoCuentas.listarCuentas().stream()
    			.filter(c -> c.getNombreCuenta().equals(nombreCuenta))
                .flatMap(c -> c.getMiembros().stream()) 
                .collect(Collectors.toList());
    }


    public Set<String> getCategorias() {
        return repoGastos.getCategorias();
    }


    public void añadirCategoria(String nombre) {
        try {
            repoGastos.añadirCategoria(nombre);
            context.getGastosPersistence().save(repoGastos);
        } catch (IllegalArgumentException e) {
            System.err.println("Error al añadir categoría: " + e.getMessage());
            throw e; 
        }
    }


    public void registrarGasto(double cantidad, LocalDate fecha, String categoria, String pagador, String nombreCuenta) {
    	int idCuenta = repoCuentas.listarCuentas().stream()
                .filter(c -> c.getNombreCuenta().equals(nombreCuenta))
                .map(Cuenta::getIdCuenta)
                .findFirst()
                .get();
        Gasto gasto = Gasto.crearGasto(cantidad, fecha, categoria, pagador, idCuenta);

        gastosStore.añadirGasto(gasto);


    }
    public void importar(File archivo) {
        try {
            GastoImportarCSV importer = new GastoImportarCSV();
            List<GastoImportado> gastos = importer.importar(archivo);

            for (GastoImportado gasto : gastos) {
            	int id;
            	if(importer.esMiCuenta(gasto.cuenta)) {
            		id=1;
            		gasto.pagador="Mi Cuenta";
            		}
            	else {
                id = repoCuentas.listarCuentas().stream()
                        .filter(c -> c.getNombreCuenta().equalsIgnoreCase(gasto.cuenta))
                        .map(Cuenta::getIdCuenta)
                        .findFirst()
                        .orElse(context.getCuentaActivaId()); 
            	}
                Gasto nuevoGasto = Gasto.crearGasto(
                	gasto.cantidad, 
                	gasto.fecha, 
                	gasto.categoria, 
                	gasto.pagador, 
                    id
                );

                repoGastos.añadirGasto(nuevoGasto);
            }

            context.getGastosPersistence().save(repoGastos);
        } catch (Exception e) {
            System.err.println("Error al importar gastos del archivo: " + e.getMessage());
        }
    }
}
