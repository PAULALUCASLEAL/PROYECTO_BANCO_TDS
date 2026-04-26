# BankCroak 🐸

BanCroak es una aplicación de escritorio para la gestión y control de gastos personales y compartidos. Esta aplicación combina una interfaz gráfica con una línea de comandos para ofrecer flexibilidad y eficiencia en la gestión financiera.

---

## Integrantes del Grupo
* **Sergio Navarro Martínez** - [sergio.n.m@um.es] - 1.3
* **Paula Lucas Leal** - [paula.lucasl@um.es] - 1.3
* **Aarón Lasheras Guillén** - [a.lasherasguillen@um.es] - 1.2

---

## Descripción del Proyecto
La aplicación permite a los usuarios registrar, categorizar y analizar sus gastos. Sus principales características son:

* **Gestión Dual:** Control mediante **Interfaz Gráfica (GUI)** y **Línea de Comandos (CLI)**.
* **Visualización de Datos:** Tablas, gráficos de barras/circulares y vista de calendario mediante **CalendarFX**.
* **Cuentas Compartidas:** Sistema de saldos para grupos con reparto equitativo o basado en porcentajes personalizados.
* **Sistema de Alertas:** Notificaciones configurables (semanales/mensuales) con historial de avisos.
* **Importación Bancaria:** Importar movimientos desde ficheros de texto plano (CSV).

---

## Documentación
* [Diagrama de clases del dominio](./docs/01-diagrama-clases.md)
* [Historias de usuario](./docs/02-historias-usuario.md)
* [Diagrama de interacción](./docs/03-diagrama-interaccion.md)
* [Explicación de la arquitectura de la aplicación y decisiones de diseño](./docs/04-arquitectura.md)
* [Explicación de los patrones de diseño usados](./docs/05-patrones.md)
* [Breve manual de usuario](./docs/06-manual-usuario.md)

---

## Guía de Ejecución

Esta aplicación cuenta con un sistema de arranque dual: detecta automáticamente si el usuario desea usar la interfaz gráfica o la línea de comandos basándose en los argumentos proporcionados.