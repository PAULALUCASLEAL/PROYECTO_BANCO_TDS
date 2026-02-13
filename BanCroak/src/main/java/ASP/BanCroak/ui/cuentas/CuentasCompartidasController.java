package ASP.BanCroak.ui.cuentas;

import ASP.BanCroak.repo.RepositorioCuentas;
import ASP.BanCroak.ui.app.AppContext;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CuentasCompartidasController {
	private final AppContext context;
    private final RepositorioCuentas repoCuentas;

    public CuentasCompartidasController(AppContext context) {
        this.context = context;
        this.repoCuentas = context.getRepoCuentas();
    }

    public void crearCuenta(String nombreCuenta, List<CuentasCompartidasView.Persona> personas) {
        if (nombreCuenta == null || nombreCuenta.isBlank()) {
            throw new IllegalArgumentException("El nombre de la cuenta no puede estar vacío");
        }
        if (personas.isEmpty()) {
            throw new IllegalArgumentException("Debe haber al menos una persona en la cuenta");
        }

        List<String> nombres = personas.stream()
                .map(p -> p.nombre)
                .collect(Collectors.toList());

        Map<String, Double> porcentajes = personas.stream()
                .collect(Collectors.toMap(
                    p -> p.nombre, 
                    p -> p.porcentaje
                ));

        repoCuentas.crearCuentaConPorcentajes(nombreCuenta, nombres, porcentajes);
        context.getCuentasPersistence().save(repoCuentas);
     }
}
