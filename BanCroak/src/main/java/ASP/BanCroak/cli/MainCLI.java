package ASP.BanCroak.cli;

import ASP.BanCroak.application.BorrarGastoUseCase;
import ASP.BanCroak.application.ModificarGastoUseCase;
import ASP.BanCroak.application.RegistrarGastoUseCase;
import ASP.BanCroak.domain.Gasto;
import ASP.BanCroak.ui.app.AppContext;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class MainCLI {
    private final RegistrarGastoUseCase registrarGastoUseCase;
    private final ModificarGastoUseCase modificarGastoUseCase;
    private final BorrarGastoUseCase borrarGastoUseCase;

    public MainCLI(AppContext context) {
        // La CLI se conecta a la capa de aplicación a través de los mismos casos de uso que la GUI.
        this.registrarGastoUseCase = context.getRegistrarGastoUseCase();
        this.modificarGastoUseCase = context.getModificarGastoUseCase();
        this.borrarGastoUseCase = context.getBorrarGastoUseCase();
    }

    public static void main(String[] args) {
        int status = new MainCLI(new AppContext()).run(args);
        if (status != 0) {
            System.exit(status);
        }
    }

    public int run(String[] args) {
        if (args.length == 0 || esAyuda(args[0])) {
            imprimirUso();
            return args.length == 0 ? 1 : 0;
        }

        try {
            switch (args[0]) {
                case "registrar-gasto":
                    registrarGasto(args);
                    return 0;
                case "modificar-gasto":
                    modificarGasto(args);
                    return 0;
                case "borrar-gasto":
                    borrarGasto(args);
                    return 0;
                default:
                    System.err.println("Error: comando no reconocido: " + args[0]);
                    imprimirUso();
                    return 1;
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.err.println("Error: " + e.getMessage());
            return 1;
        }
    }

    private void registrarGasto(String[] args) {
        validarNumeroArgumentos(args, 4, "registrar-gasto <cantidad> <fecha> <categoria>");

        double cantidad = parseCantidad(args[1]);
        LocalDate fecha = parseFecha(args[2]);
        String categoria = parseTextoObligatorio(args[3], "categoria");

        Gasto gasto = registrarGastoUseCase.ejecutarEnCuentaPersonal(cantidad, fecha, categoria);
        System.out.println("Gasto registrado correctamente. ID: " + gasto.getID());
    }

    private void modificarGasto(String[] args) {
        validarNumeroArgumentos(args, 5, "modificar-gasto <id> <cantidad> <fecha> <categoria>");

        int id = parseId(args[1]);
        double cantidad = parseCantidad(args[2]);
        LocalDate fecha = parseFecha(args[3]);
        String categoria = parseTextoObligatorio(args[4], "categoria");

        modificarGastoUseCase.ejecutar(id, cantidad, fecha, categoria);
        System.out.println("Gasto modificado correctamente. ID: " + id);
    }

    private void borrarGasto(String[] args) {
        validarNumeroArgumentos(args, 2, "borrar-gasto <id>");

        int id = parseId(args[1]);
        borrarGastoUseCase.ejecutar(id);
        System.out.println("Gasto borrado correctamente. ID: " + id);
    }

    private void validarNumeroArgumentos(String[] args, int esperado, String uso) {
        if (args.length != esperado) {
            throw new IllegalArgumentException("uso: " + uso);
        }
    }

    private int parseId(String valor) {
        try {
            int id = Integer.parseInt(valor);
            if (id <= 0) {
                throw new IllegalArgumentException("el id debe ser mayor que 0");
            }
            return id;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("el id debe ser un numero entero");
        }
    }

    private double parseCantidad(String valor) {
        try {
            double cantidad = Double.parseDouble(valor);
            if (cantidad <= 0) {
                throw new IllegalArgumentException("la cantidad debe ser mayor que 0");
            }
            return cantidad;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("la cantidad debe ser un numero valido");
        }
    }

    private LocalDate parseFecha(String valor) {
        try {
            return LocalDate.parse(valor);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("la fecha debe tener formato yyyy-MM-dd");
        }
    }

    private String parseTextoObligatorio(String valor, String nombreCampo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("el campo " + nombreCampo + " no puede estar vacio");
        }
        return valor;
    }

    private boolean esAyuda(String comando) {
        return "-h".equals(comando) || "--help".equals(comando) || "help".equals(comando);
    }

    private void imprimirUso() {
        System.out.println("Uso:");
        System.out.println("  registrar-gasto <cantidad> <fecha> <categoria>");
        System.out.println("  modificar-gasto <id> <cantidad> <fecha> <categoria>");
        System.out.println("  borrar-gasto <id>");
        System.out.println();
        System.out.println("Ejemplos:");
        System.out.println("  registrar-gasto 25.50 2026-04-22 comida");
        System.out.println("  modificar-gasto 3 40.00 2026-04-23 transporte");
        System.out.println("  borrar-gasto 3");
    }
}
