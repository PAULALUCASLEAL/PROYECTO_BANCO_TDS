# Memoria de decisiones de diseño (TDS)

## Descripción
Este README explica solo dos piezas del proyecto: `Gasto` y `RepositorioGastos`. La idea es dejar claro por qué están diseñados así y cómo se usan, sin hablar de otras partes de la app.

## Decisiones de diseño
- **GRASP**: aplico *Experto en Información* (cada clase conoce lo suyo), *Bajo Acoplamiento* (evitar dependencias innecesarias) y *Alta Cohesión* (cada clase con responsabilidades claras).
- **Cómo se implementa GRASP**: `Gasto` se crea solo desde `Gasto` (constructor privado + `Gasto.crearGasto(...)`) para concentrar validaciones y mantener invariantes. `RepositorioGastos` concentra la gestión de gastos y categorías, evitando dependencias cruzadas entre componentes.
- **Fuente única de verdad**: los gastos y categorías se gestionan desde un único repositorio.

## Gasto

### Entidad con identidad
`Gasto` es una **entidad**: su identidad la marca `idGasto`. Por eso:
- `equals` compara solo `idGasto`.
- Si `idGasto == 0`, **no** se consideran iguales (son gastos “nuevos” sin registrar).

Esto es típico en entidades: dos objetos distintos con el mismo id representan el mismo concepto real.

Además, conviene implementar:
- `hashCode` coherente con `equals` (para colecciones como `Set` o `Map`).
- `toString` para facilitar el debug.

### Creación controlada
El constructor de `Gasto` es **privado**. No se permite `new Gasto(...)` desde fuera.

Se crea con:
- `Gasto.crearGasto(...)`

Esto centraliza validaciones y evita gastos inconsistentes. El `idGasto` se inicia en `0` para indicar que aún **no está registrado**.
Esto centraliza validaciones y evita gastos inconsistentes. El `idGasto` se inicia en `0` para indicar que aún **no está registrado**.

### Reconstrucción de gasto (persistencia)
Existe un método especial:
- `reconstruirGasto(...)`

Se usa **solo** al cargar desde almacenamiento. Permite recrear el gasto manteniendo el `idGasto` guardado. Así la identidad no se pierde al leer de disco.

## RepositorioGastos

### Singleton con enum
El repositorio es un **singleton** implementado como `enum`:
- `RepositorioGastos.INSTANCE`

Justificación:
- Hay **una sola fuente de verdad** de gastos y categorías.
- Evita duplicar listas en distintas partes.
- El enum es más seguro y simple que el singleton clásico (maneja serialización y reflexión mejor).

### Responsabilidades
`RepositorioGastos` gestiona:
- CRUD de gastos.
- Categorías disponibles.

Esto mantiene alta cohesión y evita un `RepositorioCategorias` separado.

### Categorías dentro del repositorio
- Las categorías son `String`.
- Se guardan en un `Set<String>` para **no repetir**.
- Al añadir/editar un gasto, el repositorio **valida** que la categoría exista (validación defensiva).
- El controlador (UI) es quien llama a `añadirCategoria(...)` cuando el usuario crea una categoría desde la ventana.

### Generación de ids (`nextId`)
El repositorio controla la unicidad de ids:
- `nextId` es un contador.
- Si un gasto tiene `idGasto == 0`, el repositorio asigna `nextId` y luego incrementa.

Esto asegura ids únicos en toda la app y evita que cada `Gasto` se autogenere el suyo sin control.

### Búsquedas seguras y encapsulación
- `buscarGasto(...) / buscarPorId(...)` devuelven `Optional` porque puede no existir el gasto.
- `getListaGastos()` devuelve `List.copyOf(...)` para **no exponer la lista interna** (evitar `add/remove` desde fuera).

### Eliminar
Se usa `removeIf(...)`:
- Recorre y elimina los gastos que cumplan la condición.
- Devuelve `boolean` para saber si se eliminó algo.

## Filtros y patrón Composite

### 1) ¿Por qué no filtrar directamente en el repositorio?
Meter toda la lógica de filtrado en el repositorio lo haría crecer demasiado.
El repositorio solo debe gestionar datos, no decidir cómo se filtran.
El filtrado es una responsabilidad distinta.

Esto aplica GRASP:
- **Alta Cohesión**: el repositorio se centra en almacenamiento y acceso a datos.
- **Bajo Acoplamiento**: los filtros no dependen del repositorio ni lo modifican.
- **Experto en Información**: cada filtro conoce su propio criterio.

### 2) Abstracción Filtro
Existe una clase abstracta o interfaz `Filtro` con el método:

```java
boolean filtrar(Gasto gasto);
```

Representa un criterio de filtrado genérico.
Cualquier filtro concreto solo tiene que implementar ese método.

### 3) Filtros simples (hojas del Composite)
Existen filtros simples como:
- filtro por categoría,
- filtro por intervalo de fechas,
- filtro por meses.

Cada uno:
- encapsula su propia lógica,
- sabe decidir si un gasto cumple o no el criterio,
- no conoce otros filtros.

### 4) Filtro compuesto (Composite)
Existe un `FiltroCompuesto` que contiene una lista de `Filtro`.
Su método `filtrar(Gasto)` devuelve `true` solo si todos los filtros internos devuelven `true` (AND lógico).
Si no contiene filtros, no restringe (devuelve `true`).

Justificación:
- permite combinar filtros dinámicamente,
- evita condicionales grandes,
- hace el sistema extensible (añadir nuevos filtros sin tocar los existentes).

### Relación con GRASP y con el patrón Composite
El patrón Composite se usa cuando se quiere tratar de la misma forma a objetos simples y a conjuntos de objetos.
Define una estructura en forma de árbol donde todos los elementos comparten una interfaz común, lo que permite usar un objeto individual o un conjunto de ellos sin distinguirlos.

En este patrón existen tres tipos de elementos:
- **Componente (`Filtro`)**: es la abstracción común. Define la operación que deben implementar todos los elementos, en este caso `filtrar(Gasto)`. Gracias a esto, cualquier filtro se puede usar de la misma manera.
- **Hoja (filtros simples)**: son los objetos individuales que realizan una operación concreta. En el proyecto, corresponden a filtros como el de categoría, intervalo de fechas o meses. Cada hoja contiene la lógica de un único criterio de filtrado.
- **Composite (`FiltroCompuesto`)**: es un objeto que contiene otros componentes (filtros). Implementa la misma interfaz que las hojas y combina el resultado de sus filtros internos, aplicando una lógica conjunta (AND lógico). De este modo permite construir filtros complejos a partir de filtros simples.

El comportamiento clave es que el cliente no necesita saber si está usando un filtro simple o uno compuesto, ya que ambos se tratan igual. Esto hace el sistema más flexible, extensible y fácil de mantener.

## Dependencias Maven (solo lo relevante)
- **JavaFX**: solo como consumidor del repositorio/controlador, sin entrar en detalles de UI.
