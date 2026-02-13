package ASP.BanCroak.ui.gastos;

import ASP.BanCroak.domain.Cuenta;
import ASP.BanCroak.domain.Gasto;
import ASP.BanCroak.repo.RepositorioCuentas;
import ASP.BanCroak.repo.RepositorioGastos;
import ASP.BanCroak.ui.app.AppContext;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Set;

public class GastosController {
	private final AppContext context;
    private final RepositorioGastos repoGastos;
    private final RepositorioCuentas repoCuentas;

    public GastosController(AppContext context) {
        this.context = context;
        this.repoGastos = context.getRepoGastos();
        this.repoCuentas = context.getRepoCuentas();
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


    public void registrarGasto(double cantidad, LocalDate fecha, String categoria, String pagador, int idCuenta) {
        try {
            Gasto gasto = Gasto.crearGasto(cantidad, fecha, categoria, pagador, idCuenta);

            repoGastos.añadirGasto(gasto);
            context.getGastosPersistence().save(repoGastos);
            
            System.out.println("Gasto registrado en la cuenta: " + idCuenta);
        } catch (Exception e) {
            System.err.println("Error al registrar gasto: " + e.getMessage());
            throw e;
        }
    }
}
