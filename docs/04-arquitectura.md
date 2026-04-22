# Arquitectura de la aplicación

## 1. Visión general

La aplicación sigue una arquitectura organizada por capas y responsabilidades, separando la lógica del dominio, la gestión de datos, la persistencia y la interfaz de usuario. Esta organización facilita el mantenimiento del código, la reutilización de componentes y la comprensión global del sistema.

De acuerdo con el enunciado, la aplicación utiliza **JavaFX** para la interfaz de usuario, **Jackson** para el almacenamiento en formato JSON y el patrón **Repositorio** para desacoplar la capa de almacenamiento del resto de la aplicación. :contentReference[oaicite:0]{index=0}

La estructura del proyecto se divide principalmente en los siguientes paquetes:

- `ASP.BanCroak.domain`
- `ASP.BanCroak.filtros`
- `ASP.BanCroak.persistence`
- `ASP.BanCroak.repo`
- `ASP.BanCroak.service`
- `ASP.BanCroak.ui.*`

Además, el proyecto incluye una carpeta `data` para los ficheros JSON persistidos y `src/main/resources` para recursos como imágenes, audio y hojas de estilo.

---

## 2. Organización por capas

### 2.1. Capa de dominio

La capa de dominio está formada por las clases del paquete `ASP.BanCroak.domain`:

- `Gasto`
- `Cuenta`
- `AlertaGasto`
- `Notificacion`

Estas clases representan los conceptos principales del problema que resuelve la aplicación. En esta capa se modelan los datos esenciales del sistema y las relaciones entre ellos.

Por ejemplo:

- `Gasto` representa un gasto individual con su información asociada.
- `Cuenta` representa una cuenta compartida entre varias personas.
- `AlertaGasto` representa una alerta configurada por el usuario.
- `Notificacion` representa una notificación generada por el sistema.

Esta capa constituye el núcleo conceptual de la aplicación y está separada de la interfaz y de los detalles técnicos de persistencia.

---

### 2.2. Capa de filtros

La funcionalidad de filtrado se encuentra separada en el paquete `ASP.BanCroak.filtros`, donde aparecen clases como:

- `Filtro`
- `FiltroCategoria`
- `FiltroMeses`
- `FiltroIntervaloFechas`
- `FiltroCompuesto`

Esta separación permite encapsular la lógica de filtrado y reutilizarla desde distintas partes de la interfaz. En lugar de mezclar condiciones de filtrado directamente en los controladores, se centraliza el comportamiento en clases específicas, haciendo el código más claro y extensible.

Gracias a esta organización, la aplicación puede soportar:

- filtrado por categoría,
- filtrado por meses,
- filtrado por intervalo de fechas,
- combinación de varios filtros.

---

### 2.3. Capa de persistencia

La persistencia está implementada en el paquete `ASP.BanCroak.persistence`, con clases como:

- `GastosPersistence`
- `AlertasPersistence`
- `CuentasPersistence`
- `NotificacionesPersistence`

Estas clases se encargan del acceso a los ficheros de datos en formato JSON. Su responsabilidad principal es leer y escribir la información persistida de la aplicación, evitando que la interfaz o el dominio conozcan directamente el formato de almacenamiento.

Esta decisión sigue lo pedido en el enunciado, que especifica el uso de **Jackson** para la persistencia en JSON. :contentReference[oaicite:1]{index=1}

---

### 2.4. Capa de repositorios

El paquete `ASP.BanCroak.repo` contiene:

- `RepositorioGastos`
- `RepositorioAlertas`
- `RepositorioCuentas`
- `RepositorioNotificaciones`

La función de esta capa es actuar como intermediaria entre la lógica de la aplicación y la persistencia. En lugar de que otras clases accedan directamente a los ficheros JSON, lo hacen a través de repositorios, lo que reduce el acoplamiento y mejora la organización del sistema.

El repositorio abstrae operaciones como:

- obtener elementos,
- añadir nuevos datos,
- actualizar registros,
- eliminar información,
- guardar cambios persistentes.

De esta forma, el resto de la aplicación trabaja con una interfaz más cercana al problema del dominio y no con detalles de lectura y escritura de archivos.

---

### 2.5. Capa de servicios

En el paquete `ASP.BanCroak.service` aparecen clases como:

- `AlertaService`
- `GastosQueryService`
- `FilterState`
- `CuentaTipo`

Esta capa concentra parte de la lógica de aplicación y de coordinación entre componentes.

Por ejemplo:

- `AlertaService` se encarga de la lógica asociada a las alertas y notificaciones.
- `GastosQueryService` centraliza consultas o transformaciones sobre la colección de gastos.
- `FilterState` permite gestionar el estado actual de los filtros aplicados.
- `CuentaTipo` ayuda a representar configuraciones relacionadas con cuentas compartidas.

La capa de servicios evita que los controladores de interfaz asuman demasiadas responsabilidades, favoreciendo una mejor separación entre presentación y lógica de negocio.

---

### 2.6. Capa de interfaz de usuario

La interfaz de usuario se encuentra en los paquetes `ASP.BanCroak.ui.*`, organizados por funcionalidad:

- `ui.app`
- `ui.gastos`
- `ui.graficas`
- `ui.cuentas`
- `ui.notificaciones`
- `ui.visualizar`
- `ui.main`

Esta organización modular facilita la navegación por el proyecto y permite localizar rápidamente las clases responsables de cada parte de la aplicación.

#### a) Paquete `ui.app`
Incluye clases de infraestructura general de la interfaz, como:

- `App`
- `AppContext`
- `GastosStore`
- `SceneManager`

Estas clases parecen encargarse del arranque de la aplicación, del contexto compartido y de la gestión de escenas o datos accesibles desde varias vistas.

#### b) Paquete `ui.gastos`
Contiene la interfaz relacionada con la gestión de gastos:

- `GastosController`
- `GastosView`
- `GastoEditorDialog`
- `GastoCLI`
- `GastoImportar`
- `GastoImportarCSV`
- `GastoImportado`
- `GastosTableFactory`
- `RepartoRow`

Aquí se concentran tanto la interfaz gráfica como la parte de línea de comandos relacionada con gastos.

#### c) Paquete `ui.graficas`
Incluye clases para la representación gráfica de la información:

- `GraficasController`
- `FiltroView`
- `TablaView`

#### d) Paquete `ui.cuentas`
Gestiona la interfaz de cuentas compartidas:

- `CuentasCompartidasController`
- `CuentasCompartidasView`
- `MiembroPorcentajeRow`

#### e) Paquete `ui.notificaciones`
Agrupa la gestión visual de notificaciones:

- `NotificacionesController`
- `NotificacionesView`
- `HistorialNotificacionesView`
- `ToastManager`
- `ToastView`

#### f) Paquete `ui.visualizar`
Incluye clases relacionadas con la visualización y navegación entre vistas:

- `VisualizarView`
- `VisualizarTab`
- `GastosFilterPane`
- `VisualizarViewModel`

#### g) Paquete `ui.main`
Contiene la estructura principal de la ventana:

- `MainView`
- `BarraMenuView`

---

## 3. Flujo general de funcionamiento

De forma general, el funcionamiento de la aplicación sigue este esquema:

1. El usuario interactúa con la interfaz gráfica o con la línea de comandos.
2. Los controladores de la interfaz recogen la acción del usuario.
3. La lógica necesaria se delega a servicios, filtros o repositorios, según el caso.
4. Los repositorios se encargan de acceder a la persistencia.
5. La persistencia lee o escribe los datos en los ficheros JSON.
6. La interfaz se actualiza mostrando la información resultante.

Este flujo permite que las distintas responsabilidades estén separadas y que cada capa tenga una función clara dentro del sistema.

---

## 4. Decisiones de diseño relevantes

### 4.1. Separación entre interfaz y lógica
La aplicación evita concentrar toda la lógica en la capa visual. La existencia de paquetes específicos para `service`, `repo`, `persistence` y `domain` muestra una separación clara entre presentación, lógica y datos.

### 4.2. Organización modular por funcionalidad
Dentro de la interfaz, las clases están agrupadas por áreas funcionales: gastos, gráficas, cuentas compartidas, notificaciones y visualización. Esto mejora la claridad del proyecto y facilita el desarrollo en paralelo.

### 4.3. Persistencia desacoplada
La lectura y escritura en JSON no se realiza directamente desde la interfaz, sino a través de clases específicas de persistencia y de repositorios. Esto mejora el mantenimiento y reduce dependencias innecesarias.

### 4.4. Reutilización de lógica de filtrado
La existencia de una jerarquía de filtros evita duplicar condiciones en varias vistas o controladores y permite combinar criterios de búsqueda de forma flexible.

### 4.5. Soporte para múltiples formas de interacción
El sistema no solo ofrece interfaz gráfica, sino también una interfaz de línea de comandos (`GastoCLI`), tal y como exige el enunciado. :contentReference[oaicite:2]{index=2}

---

## 5. Recursos y estructura auxiliar

Además del código fuente, el proyecto incorpora:

- `src/main/resources` para imágenes, sonidos y estilos CSS.
- `data` para los archivos JSON persistidos.
- `pom.xml` para la gestión de dependencias con Maven.

Esto se ajusta a una estructura típica de proyecto Java con Maven, manteniendo separados:

- código fuente,
- recursos,
- datos persistidos,
- documentación.

---

## 6. Conclusión

En conjunto, la arquitectura del proyecto está planteada de forma modular y por responsabilidades. El dominio se encuentra separado de la interfaz, la persistencia está desacoplada mediante repositorios y la interfaz está organizada por funcionalidades concretas.

Esta estructura favorece:

- la comprensión del proyecto,
- el mantenimiento del código,
- la ampliación de funcionalidades,
- y la separación de responsabilidades entre los distintos componentes del sistema.