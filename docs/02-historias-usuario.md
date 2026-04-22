# Historias de Usuario

Este documento recoge las historias de usuario del sistema de gestión de gastos personales.

---

## Gestión de gastos

### 1. Registrar nuevo gasto
**Como usuario,**  
quiero registrar un nuevo gasto con cantidad, fecha y categoría,  
para llevar un control de mis gastos personales.

**Criterios de aceptación:**
- Dado que el usuario accede a la opción de registrar gasto,  
- cuando introduce la cantidad, la fecha y la categoría,  
- entonces el sistema guarda el gasto de forma persistente.

---

### 2. Editar gasto existente
**Como usuario,**  
quiero modificar un gasto ya registrado,  
para corregir errores o actualizar información.

**Criterios de aceptación:**
- Dado que el usuario selecciona un gasto,  
- cuando edita los datos y confirma los cambios,  
- entonces el sistema actualiza el registro correctamente.

---

### 3. Eliminar gasto existente
**Como usuario,**  
quiero eliminar un gasto registrado,  
para borrar información que ya no es necesaria o fue introducida por error.

**Criterios de aceptación:**
- Dado que el usuario selecciona un gasto,  
- cuando pulsa “Eliminar”,  
- entonces el sistema borra el registro y actualiza la lista de gastos.

---

### 4. Crear nueva categoría de gasto
**Como usuario,**  
quiero crear nuevas categorías personalizadas,  
para clasificar mis gastos según mis propias necesidades.

**Criterios de aceptación:**
- Dado que el usuario accede a la sección de categorías,  
- cuando introduce un nombre y confirma,  
- entonces el sistema añade la nueva categoría al listado disponible.

---

### 5. Gestionar gastos desde la línea de comandos
**Como usuario avanzado,**  
quiero registrar, editar o eliminar gastos desde la línea de comandos,  
para poder gestionar mis gastos sin usar la interfaz gráfica.

**Criterios de aceptación:**
- Dado que el usuario abre la aplicación en modo consola,  
- cuando introduce un comando válido (añadir, modificar, eliminar),  
- entonces el sistema ejecuta la acción y guarda los cambios de forma persistente.

---

## Visualización de datos

### 6. Ver tabla/lista de gastos
**Como usuario,**  
quiero visualizar mis gastos en formato de tabla/lista,  
para revisar y comparar mis gastos de forma clara y rápida.

**Criterios de aceptación:**
- Dado que el usuario selecciona la vista de tabla/lista,  
- cuando se cargan los datos o se aplican cambios,  
- entonces el sistema muestra la tabla actualizada.

---

### 7. Ver gráficos de gastos
**Como usuario,**  
quiero visualizar mis gastos mediante gráficos,  
para entender mejor la distribución de mis gastos.

**Criterios de aceptación:**
- Dado que el usuario elige el modo gráfico,  
- cuando selecciona el tipo de gráfico,  
- entonces el sistema genera la visualización correspondiente.

---

### 8. Visualizar datos en calendario
**Como usuario,**  
quiero ver mis gastos en un calendario,  
para identificar fácilmente en qué días gasto más.

**Criterios de aceptación:**
- Dado que el usuario selecciona la vista calendario,  
- cuando se carga el periodo,  
- entonces el sistema muestra los gastos distribuidos por fecha.

---

## Filtrado de gastos

### 9. Filtrar gastos por categoría
**Como usuario,**  
quiero filtrar mis gastos por categorías,  
para analizar áreas específicas.

**Criterios de aceptación:**
- Dado que el usuario selecciona una categoría,  
- cuando aplica el filtro,  
- entonces el sistema muestra solo los gastos correspondientes.

---

### 10. Filtrar gastos por lista de meses
**Como usuario,**  
quiero filtrar mis gastos por meses,  
para analizar periodos concretos.

**Criterios de aceptación:**
- Dado que el usuario selecciona meses,  
- cuando aplica el filtro,  
- entonces el sistema muestra los gastos correspondientes.

---

### 11. Filtrar gastos por intervalo de fechas
**Como usuario,**  
quiero filtrar mis gastos por un intervalo,  
para analizar un periodo concreto.

**Criterios de aceptación:**
- Dado que el usuario define fechas,  
- cuando aplica el filtro,  
- entonces el sistema muestra los gastos en ese intervalo.

---

### 12. Filtrar por múltiples criterios combinados
**Como usuario,**  
quiero combinar filtros,  
para obtener resultados más precisos.

**Criterios de aceptación:**
- Dado que el usuario activa varios filtros,  
- cuando ejecuta la búsqueda,  
- entonces el sistema muestra los resultados correctos.

---

### 13. Limpiar filtros aplicados
**Como usuario,**  
quiero eliminar los filtros,  
para ver todos los gastos.

**Criterios de aceptación:**
- Dado que hay filtros activos,  
- cuando el usuario limpia los filtros,  
- entonces el sistema muestra todos los gastos.

---

## Alertas informativas

### 14. Configurar alertas de gasto
**Como usuario,**  
quiero establecer límites de gasto,  
para recibir avisos.

**Criterios de aceptación:**
- Dado que el usuario define un límite,  
- cuando se supera,  
- entonces el sistema genera una notificación.

---

### 15. Vincular alerta a categoría específica
**Como usuario,**  
quiero crear alertas por categoría,  
para controlar mejor mis gastos.

**Criterios de aceptación:**
- Dado que el usuario selecciona categoría y límite,  
- cuando se supera,  
- entonces el sistema muestra una notificación.

---

### 16. Consultar historial de notificaciones
**Como usuario,**  
quiero ver alertas pasadas,  
para revisar mis excesos de gasto.

**Criterios de aceptación:**
- Dado que el usuario accede al historial,  
- entonces el sistema muestra las notificaciones.

---

## Gestión de cuentas compartidas

### 17. Crear nueva cuenta compartida
**Como usuario,**  
quiero crear una cuenta compartida,  
para repartir gastos.

**Criterios de aceptación:**
- Dado que el usuario introduce miembros,  
- cuando confirma,  
- entonces se crea la cuenta.

- Dado que la cuenta ya existe,  
- cuando intenta modificar miembros,  
- entonces el sistema lo bloquea.

---

### 18. Registrar gasto en cuenta compartida
**Como usuario,**  
quiero registrar gastos compartidos,  
para actualizar saldos.

**Criterios de aceptación:**
- Dado que el usuario introduce un gasto,  
- entonces el sistema recalcula los saldos.

---

### 19. Definir porcentajes de reparto
**Como usuario,**  
quiero asignar porcentajes,  
para personalizar el reparto.

**Criterios de aceptación:**
- Dado que la suma es 100%,  
- entonces el sistema guarda y aplica los valores.

---

## Importación de datos

### 20. Importar ficheros de gastos
**Como usuario,**  
quiero importar gastos desde un fichero,  
para añadirlos automáticamente.

**Criterios de aceptación:**
- Dado que el usuario selecciona un archivo válido,  
- cuando confirma,  
- entonces el sistema importa los datos.

- Dado que el archivo tiene un formato soportado,  
- entonces el sistema lo procesa correctamente.