# Arquitectura de la aplicación y decisiones de diseño

## 1. Visión general

La aplicación está diseñada siguiendo una arquitectura modular basada en la separación de responsabilidades. El objetivo principal de esta organización es evitar el acoplamiento entre componentes, facilitar el mantenimiento y permitir que cada parte del sistema tenga una función clara y bien definida.

El sistema se divide en varias capas principales:

- Dominio (`domain`)
- Filtros (`filtros`)
- Persistencia (`persistence`)
- Repositorios (`repo`)
- Servicios (`service`)
- Interfaz de usuario (`ui.*`)

Además, el proyecto utiliza:
- JSON para almacenamiento de datos (mediante Jackson)
- JavaFX para la interfaz gráfica
- una carpeta `data` como almacenamiento persistente

---

## 2. Estructura y responsabilidades

### 2.1. Capa de dominio

El paquete `ASP.BanCroak.domain` contiene las clases principales del sistema:

- `Gasto`
- `Cuenta`
- `AlertaGasto`
- `Notificacion`

Estas clases representan el modelo conceptual de la aplicación.

#### Decisiones de diseño

Una decisión importante ha sido tratar `Gasto` como una entidad con identidad propia:

- Cada gasto se identifica mediante `idGasto`
- El método `equals` compara únicamente este identificador
- Si el id es 0, el gasto se considera no persistido

Esto permite separar claramente:
- gastos nuevos (no guardados)
- gastos persistidos (con identidad real)

Otra decisión relevante es la **creación controlada de objetos**:

- El constructor de `Gasto` es privado
- Se utiliza un método `crearGasto(...)` para instanciar objetos

Esto permite:
- centralizar validaciones
- evitar estados inválidos
- garantizar la coherencia del modelo

Además, se introduce un método específico para persistencia:

- `reconstruirGasto(...)`

Este método se utiliza exclusivamente al cargar datos desde disco, permitiendo recuperar objetos manteniendo su identidad original.

---

### 2.2. Capa de filtros

El paquete `ASP.BanCroak.filtros` contiene la lógica de filtrado:

- `Filtro`
- `FiltroCategoria`
- `FiltroMeses`
- `FiltroIntervaloFechas`
- `FiltroCompuesto`

#### Decisiones de diseño

Se ha decidido **no incluir la lógica de filtrado dentro del repositorio**, ya que esto habría provocado:

- un crecimiento excesivo del repositorio
- mezcla de responsabilidades

En su lugar, se ha separado en una capa independiente.

Esto permite:
- mantener alta cohesión (cada clase hace una cosa)
- reducir acoplamiento
- reutilizar filtros en distintas partes de la aplicación

Además, se permite combinar filtros dinámicamente mediante `FiltroCompuesto`, lo que evita estructuras condicionales complejas y facilita la extensibilidad.

---

### 2.3. Capa de persistencia

El paquete `ASP.BanCroak.persistence` contiene:

- `GastosPersistence`
- `AlertasPersistence`
- `CuentasPersistence`
- `NotificacionesPersistence`

#### Responsabilidad

- Leer y escribir datos en JSON
- Traducir entre objetos Java y almacenamiento

#### Decisiones de diseño

Se ha decidido **aislar completamente la persistencia** del resto del sistema.

Esto implica que:
- ni la UI ni el dominio conocen cómo se almacenan los datos
- cualquier cambio en el formato de persistencia no afecta al resto del sistema

Además, el uso de JSON permite:
- simplicidad
- facilidad de depuración
- independencia de bases de datos externas

---

### 2.4. Capa de repositorios

El paquete `ASP.BanCroak.repo` incluye:

- `RepositorioGastos`
- `RepositorioAlertas`
- `RepositorioCuentas`
- `RepositorioNotificaciones`

#### Responsabilidad

- Gestionar el acceso a los datos
- Centralizar operaciones CRUD
- Actuar como intermediario con la persistencia

#### Decisiones de diseño

Una de las decisiones más importantes ha sido establecer el repositorio como **fuente única de verdad**.

Esto implica que:
- todos los gastos se gestionan desde un único punto
- no existen copias duplicadas en distintas partes del sistema

El repositorio también controla:
- la generación de identificadores (`nextId`)
- la validación de categorías
- la coherencia de los datos

Otra decisión clave es la implementación como **singleton mediante enum**:

- asegura una única instancia
- evita inconsistencias
- simplifica el acceso global

También se han tomado decisiones de encapsulación:

- `getListaGastos()` devuelve una copia inmutable
- las búsquedas devuelven `Optional`
- se evita la exposición directa de estructuras internas

---

### 2.5. Capa de servicios

El paquete `ASP.BanCroak.service` incluye:

- `AlertaService`
- `GastosQueryService`
- `FilterState`
- `CuentaTipo`

#### Responsabilidad

- Contener lógica de aplicación que no pertenece al dominio puro
- Coordinar operaciones entre capas

#### Decisiones de diseño

Se ha evitado introducir lógica compleja en los controladores de la interfaz.

En su lugar:
- las consultas y operaciones se delegan a servicios
- se centralizan comportamientos reutilizables

Por ejemplo:
- `GastosQueryService` gestiona consultas sobre gastos
- `AlertaService` gestiona la lógica de alertas

Esto mejora la mantenibilidad y evita duplicación de código.

---

### 2.6. Capa de interfaz de usuario

La interfaz se organiza en varios paquetes:

- `ui.app`
- `ui.gastos`
- `ui.graficas`
- `ui.cuentas`
- `ui.notificaciones`
- `ui.visualizar`
- `ui.main`

#### Responsabilidad

- Gestionar la interacción con el usuario
- Mostrar datos
- Recoger acciones del usuario

#### Decisiones de diseño

Se ha decidido organizar la UI por funcionalidades:

- gastos
- cuentas compartidas
- gráficas
- notificaciones

Esto permite:
- modularidad
- facilidad de navegación en el código
- desarrollo más organizado

Además, se ha incluido soporte para:
- interfaz gráfica (JavaFX)
- línea de comandos (`GastoCLI`)

Esto permite distintos modos de uso del sistema.

También se utiliza un `AppContext` para compartir información entre distintas partes de la aplicación, evitando dependencias directas entre controladores.

---

## 3. Flujo de ejecución

El flujo general del sistema es:

1. El usuario interactúa con la interfaz
2. El controlador recibe la acción
3. Se delega en servicios o repositorios
4. El repositorio accede a persistencia
5. Se actualizan los datos
6. La interfaz refleja los cambios

Este flujo evita dependencias directas entre capas y mantiene la estructura clara.

---

## 4. Decisiones globales de diseño

### Separación de responsabilidades

Cada capa tiene una función clara, lo que permite:
- mejorar la mantenibilidad
- reducir errores
- facilitar pruebas

---

### Control de la creación de objetos

Se evita el uso de constructores públicos en entidades clave, garantizando:
- validaciones centralizadas
- objetos consistentes

---

### Fuente única de datos

El uso de repositorios centralizados evita:
- inconsistencias
- duplicación de información

---

### Encapsulación

Se protege el estado interno mediante:
- copias inmutables
- uso de `Optional`
- ocultación de estructuras internas

---

### Extensibilidad

La arquitectura permite añadir fácilmente:
- nuevos filtros
- nuevas formas de visualización
- nuevas fuentes de datos

sin modificar partes existentes del sistema.

---

## 5. Conclusión

La arquitectura del sistema está diseñada para ser clara, modular y mantenible. La separación entre dominio, persistencia, lógica y presentación permite construir una aplicación robusta y fácilmente ampliable.

Las decisiones tomadas, como el uso de repositorios, la separación de filtros o el control de creación de objetos, contribuyen a mantener un código limpio, coherente y alineado con buenas prácticas de diseño.