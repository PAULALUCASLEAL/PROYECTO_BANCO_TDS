# Patrones de diseño usados

## 1. Singleton

En el proyecto se utiliza el patrón Singleton en los repositorios:

- `RepositorioGastos`
- `RepositorioCuentas`
- `RepositorioAlertas`
- `RepositorioNotificaciones`

Estos repositorios están implementados como `enum` con una única instancia (`INSTANCE`), lo que garantiza que solo exista un objeto en toda la aplicación.

### Por qué se usa

Tiene sentido porque los repositorios representan el estado global de la aplicación. Por ejemplo, todos los gastos deben estar en un único sitio. Si hubiera varias instancias, cada una podría tener datos distintos, lo que generaría inconsistencias.

Además, simplifica el acceso desde cualquier parte del sistema sin tener que pasar referencias constantemente.

---
## 2. Repository 

Los repositorios actúan como una capa intermedia entre el dominio y el almacenamiento de datos.

Aparece en:

- `RepositorioGastos`
- `RepositorioCuentas`
- `RepositorioAlertas`
- `RepositorioNotificaciones`

Se encargan de:

- almacenar entidades en memoria;
- proporcionar operaciones de acceso (buscar, listar, añadir, eliminar);
- gestionar identificadores (por ejemplo, `nextId` en gastos).

### Por qué se usa

Evita que otras partes del sistema accedan directamente a estructuras de datos (listas, mapas, etc.).  
Esto hace que el código sea más mantenible, porque si en el futuro cambia la forma de almacenar los datos, solo hay que modificar el repositorio.

Además, centraliza la lógica de acceso a datos, lo que mejora la coherencia del sistema.

---


### Por qué se usa

Permite:

- validar datos antes de crear el objeto;
- asegurar que siempre se cumple el estado correcto (invariantes);
- diferenciar entre distintos tipos de creación (por ejemplo, nuevo vs reconstruido).

Esto es especialmente importante en el dominio, donde no queremos objetos mal construidos.

---

## 4. Composite

El patrón Composite aparece en el sistema de filtros. Este patrón permite tratar de la misma manera a un filtro individual que a un conjunto de filtros agrupados.

Aparece en:

- `Filtro` (interfaz o clase base)
- filtros simples (`FiltroCategoria`, `FiltroMeses`, etc.)
- `FiltroCompuesto`


### Por qué se usa

Porque el filtrado puede ser:

- simple → un solo filtro;
- complejo → varios filtros combinados.

En lugar de escribir lógica complicada con muchos `if`, el Composite permite construir estructuras de filtros que se evalúan de forma uniforme.

Esto hace el sistema más flexible y fácil de ampliar.

---
## 5. Estrategia

También aparece en los filtros.

Cada filtro implementa una forma distinta de evaluar un gasto:

- por categoría;
- por fecha;
- por meses;
- etc.

Todos comparten una misma interfaz (`filtrar(Gasto)`).

### Por qué se usa

Porque existen múltiples formas de realizar la misma operación (filtrar), y queremos poder intercambiarlas fácilmente. Básicamente, el patrón estrategia nos ofrece: 

- Desacoplamiento: El RepositorioGastos no necesita conocer los detalles de cómo se filtra. Simplemente recibe un objeto de tipo Filtro y ejecuta su método .filtrar(). No le importa si está validando una fecha o una etiqueta.

- Eliminación de condicionales complejos: Evitamos el uso de bloques if-else o switch interminables que intentarían adivinar qué tipo de filtro aplicar en tiempo de ejecución.

- Extensibilidad: Si en el futuro queremos añadir otro filtro diferente, solo tenemos que crear una nueva clase que herede de Filtro. No hace falta modificar ni una sola línea de código del repositorio o de los filtros existentes.

---

## 8. Separación Modelo-Vista-Controlador

La aplicación separa:

- Modelo → clases del dominio;
- Vista → JavaFX;
- Controlador → gestiona acciones del usuario.

### Por qué se usa

Evita mezclar interfaz gráfica con lógica de negocio.

Esto hace que:

- el código sea más claro;
- sea más fácil cambiar la interfaz sin romper el dominio;
- se pueda reutilizar la lógica en otros contextos (por ejemplo, CLI).

---

