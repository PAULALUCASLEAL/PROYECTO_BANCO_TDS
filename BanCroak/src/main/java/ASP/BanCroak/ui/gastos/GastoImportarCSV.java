package ASP.BanCroak.ui.gastos;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GastoImportarCSV implements GastoImportar {
    @Override
    public List<GastoImportado> importar(File archivo) throws Exception {
        List<GastoImportado> resultados = new ArrayList<>();
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("M/d/yyyy H:m");

        try (Scanner lector = new Scanner(archivo, StandardCharsets.UTF_8)) {
            if (lector.hasNextLine()) lector.nextLine(); // Salta la cabecera del CSV

            while (lector.hasNextLine()) {
                String[] datos = lector.nextLine().split(",");
                if (datos.length >= 7) {
                    resultados.add(new GastoImportado(
                        LocalDateTime.parse(datos[0], formatoFecha).toLocalDate(),
                        datos[1], 
                        datos[3], 
                        datos[5], 
                        Double.parseDouble(datos[6]) 
                    ));
                }
            }
        }
        return resultados;
    }
    public boolean esMiCuenta(String cuenta){
    	if(cuenta.equals("Personal"))
    		return true;
    	else
    		return false;
    }
}