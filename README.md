# Memoria de decisiones de diseño (TDS)

## Descripción
Este README explica solo dos piezas del proyecto: `Gasto` y `RepositorioGastos`. La idea es dejar claro por qué están diseñados así, cómo se usan y cómo se relacionan con la persistencia JSON, sin hablar de otras partes de la app.

## Decisiones de diseño
- **GRASP**: aplico *Information Expert* (cada clase conoce lo suyo), *Low Coupling* (evitar dependencias innecesarias) y *High Cohesion* (cada clase con responsabilidades claras).
- **Fuente única de verdad**: los gastos y categorías se gestionan desde un único repositorio.
- **Persistencia**: el JSON no crea objetos “a mano”, sino que se reconstruyen con un método específico para mantener su identidad.

## Gasto

### Entidad con identidad
`Gasto` es una **entidad**: su identidad la marca `idGasto`. Por eso:
- `equals` compara solo `idGasto`.
- Si `idGasto == 0`, **no** se consideran iguales (son gastos “nuevos” sin persistir).

Esto es típico en entidades: dos objetos distintos con el mismo id representan el mismo concepto real.

Además, conviene implementar:
- `hashCode` coherente con `equals` (para colecciones como `Set` o `Map`).
- `toString` para facilitar el debug.

### Creación controlada
El constructor de `Gasto` es **privado**. No se permite `new Gasto(...)` desde fuera.

Se crea con:
- `Gasto.crearGasto(...)`

Esto centraliza validaciones y evita gastos inconsistentes. El `idGasto` se inicia en `0` para indicar que aún **no está persistido**.

### Persistencia y reconstrucción
Existe un método especial:
- `reconstruirGasto(...)`

Se usa **solo** al cargar desde JSON. Permite recrear el gasto manteniendo el `idGasto` guardado. Así la identidad no se pierde al leer de disco.

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

## Persistencia (mínimo necesario)
La persistencia JSON se hace con **Jackson** (dependencia Maven). A nivel conceptual:
- Al guardar, se serializa el estado del repositorio.
- Al cargar, se usan métodos como `reconstruirGasto(...)` para mantener `idGasto` y coherencia de entidades.

No se entra en detalles de UI ni de otras capas.

## Ejemplos de uso

```java
// Crear categoría y gasto
RepositorioGastos repo = RepositorioGastos.INSTANCE;
repo.anadirCategoria("Transporte");

Gasto g = Gasto.crearGasto("Metro", 2.40, "Transporte");
repo.anadirGasto(g);
```

```java
// Buscar por id
Optional<Gasto> encontrado = repo.buscarPorId(1);
```

```java
// Eliminar un gasto por id
boolean eliminado = repo.eliminarGasto(1);
```

## Dependencias Maven (solo lo relevante)
- **Jackson**: para JSON en la persistencia.
- **JavaFX**: solo como consumidor del repositorio/controlador, sin entrar en detalles de UI.
