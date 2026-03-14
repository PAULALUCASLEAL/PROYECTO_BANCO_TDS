package ASP.BanCroak.ui.gastos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class GastoCLI {
	private final GastosController controller;
    private final Scanner scanner;

    public GastoCLI(GastosController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        System.out.println("\n--- BanCroak: Modo Línea de Comandos ---");
        while (true) {
            System.out.println("\nComandos: [1] Listar | [2] Añadir | [3] Modificar | [4] Borrar | [0] Salir");
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":  listarGastos(); break;
                case "2":  añadirGasto();break;
                case "3":  modificarGasto();break;
                case "4":  borrarGasto();break;
                case "0":  System.exit(0);break;
                default:   System.out.println("Opción no válida.");
            }
        }
    }

    private void listarGastos() {
        System.out.println("\n--- Listado de Gastos ---");
        controller.getListaGastos().forEach(g -> 
            System.out.printf("ID: %d | %s | %.2f€ | %s | Pagador: %s\n", 
                g.getID(), g.getFecha(), g.getCantidad(), g.getCategoria(), g.getPagador()));
    }

    private void añadirGasto() {
        try {
            System.out.print("Cantidad: ");
            double cant = Double.parseDouble(scanner.nextLine());
            System.out.print("Fecha(yyyy-M-d): ");
            String date = scanner.nextLine();
            DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-M-d");
            LocalDate fecha = LocalDate.parse(date, formatoFecha);
            System.out.print("Categoría: ");
            String cat = scanner.nextLine();
            System.out.print("Pagador: ");
            String pag = scanner.nextLine();
            System.out.print("Cuenta: ");
            String C = scanner.nextLine();

            controller.registrarGasto(cant, fecha, cat, pag, C);
            System.out.println("Gasto añadido");
        } catch (Exception e) {
            System.out.println("Error en los datos: " + e.getMessage());
        }
    }

    private void borrarGasto() {
        System.out.print("ID del gasto a borrar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            controller.borrarGasto(id);
            System.out.println("Gasto eliminado.");
        } catch (Exception e) {
            System.out.println("Error: ID no válido.");
        }
    }

    private void modificarGasto() {
        System.out.print("ID del gasto a modificar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            System.out.print("Cantidad: ");
            double cant = Double.parseDouble(scanner.nextLine());
            System.out.print("Fecha(yyyy-M-d): ");
            String date = scanner.nextLine();
            DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-M-d");
            LocalDate fecha = LocalDate.parse(date, formatoFecha);
            System.out.print("Categoría: ");
            String cat = scanner.nextLine();
            System.out.print("Pagador: ");
            String pag = scanner.nextLine();

            controller.modificarGasto(id,cant, fecha, cat, pag);
            System.out.println("Gasto modificado");
        } catch (Exception e) {
            System.out.println("Error en los datos: " + e.getMessage());
        }
        
    }
}
