package ASP.BanCroak.ui.gastos;

import java.io.File;
import java.util.List;

public interface GastoImportar {
    List<GastoImportado> importar(File archivo) throws Exception;
}