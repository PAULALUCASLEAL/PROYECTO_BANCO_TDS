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

## 3. Composite

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
## 4. Estrategia

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

## 5. Factoría

El patrón Factoría aparece cuando el sistema necesita crear objetos de forma controlada, sin dejar que cualquier clase los construya directamente con `new`.

### Cómo se usa en los gastos

La clase `Gasto` tiene el constructor privado. Eso significa que desde fuera no se puede hacer `new Gasto(...)` libremente.  
En su lugar, otras partes del sistema tienen que pedirle a la propia clase que cree el objeto mediante métodos estáticos de factoría.

Esto lo usamos directamente en los casos de uso de gasto. Por ejemplo, en `RegistrarGastoUseCase`, cuando el sistema ya ha obtenido la cuenta correcta y tiene todos los datos validados a nivel de aplicación, no construye el objeto manualmente, sino que llama a:

- `Gasto.crearGasto(cantidad, fecha, categoria, pagador, cuenta.getIdCuenta())`

Así, el caso de uso delega la creación a la propia entidad de dominio, que es quien mejor conoce cómo debe nacer un gasto válido.

### Por qué se usa

Se usa por varias razones importantes:

- Centraliza la creación de objetos. Toda la lógica de construcción de `Gasto` está en un único sitio.
- Protege las invariantes del dominio. La propia clase valida cantidad, fecha, categoría, pagador e identificador de cuenta antes de permitir que exista el objeto.
- Evita objetos inconsistentes. Ninguna otra clase puede saltarse las validaciones usando el constructor directamente.
- Desacopla a los casos de uso del detalle de construcción. `RegistrarGastoUseCase` se centra en orquestar el proceso, no en conocer los detalles internos del constructor.

---

## 6. Separación Modelo-Vista-Controlador

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

## 7. Principios GRASP: Experto y Creador

En el diseño del dominio y la gestión de objetos, se han aplicado los principios GRASP para asegurar una asignación de responsabilidades coherente.

### Experto

Se ha aplicado este principio delegando las operaciones en las clases que contienen los datos necesarios para ejecutarlas.

*   **En la clase `Gasto`**: El objeto no es un simple contenedor de datos. Él mismo es el experto encargado de validar sus atributos, comprobar si pertenece a un rango de fechas o si coincide con ciertos meses o categorías.
*   **En los Filtros**: Cada filtro (`FiltroCategoria`, `FiltroMeses`, etc.) conoce su propia lógica de normalización y comparación.
*   **En el Repositorio**: `RepositorioGastos` es el experto en la gestión de la colección, encargándose de generar IDs únicos y organizar el almacenamiento.

**Por qué se usa:** 
Se reduce el acoplamiento y se favorece la encapsulación, ya que la lógica de decisión reside junto a los datos.

### Creador

La responsabilidad de creación de objetos se ha resuelto mediante una implementación de **Creación Controlada** (mezcla de GRASP Creador con el patrón Factoría Estática).

*   **Constructor Privado**: La clase `Gasto` impide la creación arbitraria desde el exterior mediante `new`.
*   **Métodos de Factoría**: La creación se delega a métodos específicos como `Gasto.crearGasto()` (para nuevos registros) y `Gasto.reconstruirGasto()` (para persistencia).
*   **Delegación en Casos de Uso**: Clases como `RegistrarGastoUseCase` no instancian el objeto directamente, sino que solicitan a la clase experta (`Gasto`) que lo fabrique.

**Por qué se usa:** 
Se garantiza que ningún objeto `Gasto` sea creado en un estado inválido o inconsistente, centralizando las reglas de integridad en un solo punto.

---

