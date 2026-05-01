package ASP.BanCroak.domain;

import java.text.Normalizer;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

//aquí representamos los gastos de una cuenta, un gasto está asociado a una fecha, categoría, cantidad, pagador y una cuenta
public final class Gasto {
    private LocalDate fechaGasto;
    private String categoria;
    private double cantidad;
    private String pagador;
    private int idGasto;
    private int idCuenta;

    private Gasto(double cantidad, LocalDate fecha, String categoria, String pagador, int idCuenta) {
        validarDatos(cantidad, fecha, categoria, pagador, idCuenta);
        this.cantidad = cantidad;
        this.fechaGasto = fecha;
        this.categoria = categoria;
        this.pagador = pagador;
        this.idCuenta = idCuenta;
        this.idGasto = 0; //lo ponemos  a 0 porque el id real lo pone el repositorio 
    }

    //función a la que llamarán otras clases para crear un nuevo gasto, sin usar el new porque el contrustor es privado 
    public static Gasto crearGasto(double cantidad, LocalDate fecha, String categoria, String pagador, int idCuenta) {
        return new Gasto(cantidad, fecha, categoria, pagador, idCuenta);
    }
    
    //esta función la ponemos para recrear un gasto que ya existía antes (cuando lo tenemos en un JSON), y este mantiene el idGasto que ya tenía guardado
    public static Gasto reconstruirGasto(double cantidad, LocalDate fecha, String categoria, String pagador, int idCuenta, int idGasto) {
        Gasto gasto = new Gasto(cantidad, fecha, categoria, pagador, idCuenta);
        if (idGasto > 0) {
            gasto.asignarId(idGasto);
        }
        return gasto;
    }

    public int getID() {
        return idGasto;
    }

    public int getIDCuenta() {
        return idCuenta;
    }

    public String getCategoria() {
        return categoria;
    }

    public LocalDate getFecha() {
        return fechaGasto;
    }

    public double getCantidad() {
        return cantidad;
    }

    public String getPagador() {
        return pagador;
    }

  
     //Actualiza los datos (sin cambiar idGasto ni idCuenta).
     
    public void actualizarGasto(double cantidad, LocalDate fecha, String categoria, String pagador) {
        validarDatos(cantidad, fecha, categoria, pagador, this.idCuenta);
        this.cantidad = cantidad;
        this.fechaGasto = fecha;
        this.categoria = categoria;
        this.pagador = pagador;
    }

    
    //AQUÍ ENTRAMOS EN LA SECCIÓN DE MÉTODOS PARA FILTRAR
     
    //Comprueba si la categoría coincide (normalizando mayúsculas y espacios).
    public boolean perteneceACategoria(String categoria) {
        if (categoria == null) {
            return false;
        }
        return normalizarTexto(this.categoria).equals(normalizarTexto(categoria));
    }

    /**
     * Comprueba si la fecha del gasto está en alguno de los meses indicados.
     * La lista va a aceptar "1".."12", "01".."12" o nombres de mes en español
     * (por ejemplo: "enero", "febrero", "marzo", ...). La comparación ignora mayúsculas,
     * espacios y acentos.
     */
    public boolean estaEnMeses(List<String> meses) {
        if (meses == null || fechaGasto == null) {
            return false;
        }
        int mesGasto = fechaGasto.getMonthValue();
        for (String m : meses) {
            Integer mes = normalizarMes(m);
            if (mes != null && mes == mesGasto) {
                return true;
            }
        }
        return false;
    }

    
    //Comprueba si la fecha del gasto está entre desde y hasta (incluyente).
    public boolean estaEntre(LocalDate desde, LocalDate hasta) {
        if (fechaGasto == null || desde == null || hasta == null) {
            return false;
        }
        return !fechaGasto.isBefore(desde) && !fechaGasto.isAfter(hasta);
    }

    
     //Asigna idGasto desde el repositorio (uso interno del paquete).
    
    public void asignarId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El id debe ser positivo");
        }
        if (this.idGasto != 0) {
            throw new IllegalStateException("El gasto ya tiene id asignado");
        }
        this.idGasto = id;
    }
    
    
    //AQUÍ LOS MÉTODOS EQUALS Y HASHCODE 

    @Override
    public boolean equals(Object o) {
    	//si comparamos el objeto consigo mismo 
        if (this == o) {
            return true;
        }
        //si o no es un gasto 
        if (!(o instanceof Gasto)) {
            return false;
        }
        
        //aquí ya sabemos que es un gasto, pues hacemos un casting para poder tratarlo como tal y acceder a sus campos (porque nos pasaron un object en la función)
        Gasto other = (Gasto) o;
        if (this.idGasto == 0 || other.idGasto == 0) {
            return false;
        }
        //solo devuelve true si los id son iguales, porque se trata de una entidad 
        return this.idGasto == other.idGasto;
    }

    @Override
    public int hashCode() {
        if (idGasto == 0) {
            return System.identityHashCode(this);
        }
        return Integer.hashCode(idGasto);
    }

    @Override
    public String toString() {
        return "Gasto{" +
            "idGasto=" + idGasto +
            ", idCuenta=" + idCuenta +
            ", fechaGasto=" + fechaGasto +
            ", categoria='" + categoria + '\'' +
            ", cantidad=" + cantidad +
            ", pagador='" + pagador + '\'' +
            '}';
    }

    private static void validarDatos(double cantidad, LocalDate fecha, String categoria, String pagador, int idCuenta) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que 0");
        }
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser null");
        }
        if (categoria == null || categoria.isBlank()) {
            throw new IllegalArgumentException("La categoria no puede ser null o vacia");
        }
        if (pagador == null || pagador.isBlank()) {
            throw new IllegalArgumentException("El pagador no puede ser null o vacio");
        }
        if (idCuenta <= 0) {
            throw new IllegalArgumentException("El idCuenta debe ser mayor que 0");
        }
    }

    private static String normalizarTexto(String texto) {
        String normalized = Normalizer.normalize(texto, Normalizer.Form.NFD);
        String sinAcentos = normalized.replaceAll("\\p{M}", "");
        return sinAcentos.trim().toLowerCase(Locale.ROOT);
    }

    private static Integer normalizarMes(String mes) {
        if (mes == null) {
            return null;
        }
        String m = normalizarTexto(mes);
        if (m.isEmpty()) {
            return null;
        }
        if (m.chars().allMatch(Character::isDigit)) {
            try {
                int value = Integer.parseInt(m);
                if (value >= 1 && value <= 12) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
                return null;
            }
        }
        switch (m) {
            case "enero":
                return 1;
            case "febrero":
                return 2;
            case "marzo":
                return 3;
            case "abril":
                return 4;
            case "mayo":
                return 5;
            case "junio":
                return 6;
            case "julio":
                return 7;
            case "agosto":
                return 8;
            case "septiembre":
            case "setiembre":
                return 9;
            case "octubre":
                return 10;
            case "noviembre":
                return 11;
            case "diciembre":
                return 12;
            default:
                return null;
        }
    }
}
