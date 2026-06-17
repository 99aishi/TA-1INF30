Especificación de Arquitectura Tecnológica y Patrones de Diseño

Este documento detalla la estructura física, los patrones de diseño y las reglas de comunicación que rigen el sistema Economix / Rendix. Describe cómo se organiza el código en la base de datos, el backend (Java) y el frontend (.NET Blazor).

1. Reglas de Comunicación y Flujo Unidireccional

El sistema se basa en una arquitectura de capas estricta. Las capas solo pueden invocar a su capa inmediatamente inferior. Está prohibido realizar llamadas entre clases de la misma capa (ej. un BO llamando a otro BO) o saltarse capas.

Flujo Transaccional:
Frontend UI (WA) ➔ Proxy .NET (WS) ➔ Backend Java (WS) ➔ Negocio (BO) ➔ Persistencia (DAO) ➔ DBManager ➔ Base de Datos MySQL

2. Interfaces Genéricas por Capa (Independencia Estricta)

Para mantener la independencia tecnológica y semántica de cada nivel, no existe una única interfaz global. En su lugar, cada capa define su propio contrato base genérico que dicta las operaciones CRUD.

Estas interfaces genéricas se ubican en paquetes de infraestructura o comunes:

Capa de Persistencia: Define IDAO<T> (insertar, listar, modificar, eliminar).

Capa de Negocio: Define IBO<T> (o IBaseBO<T>) para orquestar reglas de negocio.

Capa de Servicios Web: Define IWS<T> (o IBaseWS<T>) para los contratos de los endpoints.

Cada entidad de dominio luego implementará su versión específica heredando de estas bases.

3. Estructura de Paquetes en el Backend (Java)

El backend divide lógicamente el sistema en tres dominios horizontales: RRHH, Tesorería y Operaciones. Dentro de cada dominio, el código se segmenta por subpaquetes según su capa.

Nomenclatura base: pe.edu.pucp.economix.{dominio}.{capa}

Tomando como ejemplo la entidad Empleado del dominio rrhh:

Capa de Modelo (rrhh.model):

Contiene los POJOs (Empleado.java, Usuario.java) y un subpaquete para sus respectivos enums (rrhh.model.enums).

Capa de Persistencia (rrhh.dao y rrhh.daoi):

rrhh.dao.IEmpleadoDAO: Interfaz que hereda de IDAO<Empleado>.

rrhh.daoi.EmpleadoDAOImpl: Clase que implementa IEmpleadoDAO y utiliza DBManager para invocar a MySQL.

Capa de Negocio (rrhh.bo y rrhh.boi):

rrhh.bo.IEmpleadoBO: Interfaz que hereda de IBO<Empleado>.

rrhh.boi.EmpleadoBOImpl: Implementación que inyecta a EmpleadoDAOImpl y aplica validaciones.

Capa de Servicios Web (rrhh.ws y rrhh.wsi):

rrhh.ws.IEmpleadoWS: Interfaz que hereda de IWS<Empleado>.

rrhh.wsi.EmpleadoWSImpl: Endpoint JAX-RS que inyecta a EmpleadoBOImpl y expone JSON.

4. Arquitectura Frontend (.NET Blazor WA)

La interfaz de usuario (EconomixWA) está diseñada con componentes reactivos agrupados por la clase observable que representan (ej. Area, Empleado, Moneda).

4.1. El Patrón Page - Item - Detalle - SearchBar

Cada carpeta de dominio dentro de Components/Pages/ contiene 4 componentes principales:

{Entidad}Page.razor (Contenedor General):

Es la vista ruteable (ej. @page "/areas").

Maneja la lista de datos en memoria y orquesta la comunicación entre la barra de búsqueda y el panel de detalle.

{Entidad}Item.razor (Elemento de Lista):

Representa la unidad de visualización (tarjeta o fila) que será iterada por el Page.

Muestra la información resumida de un registro e incluye los botones de acción (Ver, Editar).

{Entidad}Detalle.razor (Panel Lateral / Drawer):

Vista superpuesta a la derecha que muestra la información completa.

Se utiliza tanto para Ver como para Editar/Crear. Si se recibe un Id == 0, se asume que es modo creación.

{Entidad}SearchBar.razor (Barra de Búsqueda Avanzada):

Componente encapsulado con inputs especializados (Nombre, Estado, Monto Mínimo, Monto Máximo, Rango de Fechas).

Dispara eventos hacia el Page para que vuelva a solicitar datos al backend filtrados.

4.2. Layouts y Gestión de Sesión (Cookies)

Layouts: Existen plantillas maestras como MainLayout.razor (con el sidemenu, barra superior) y LoginLayout.razor (pantalla centralizada sin navegación).

Autenticación por Cookies: El login invoca un endpoint en Program.cs (/auth/login) que interactúa con el backend Java. Si es exitoso, se genera una cookie cifrada en el navegador.

Recuperación del Perfil: En cada carga de pantalla o en el MainLayout, el sistema recupera la información de la sesión mediante los Claims de la cookie. Esto permite pintar datos del perfil en tiempo real (ej. saludar al usuario: "¡Hola, Juan!") y restringir accesos según su rol operativo (Jefe o Empleado).

5. Arquitectura de Base de Datos y Trazabilidad

Los scripts SQL no se manejan en un archivo gigante. La base de datos se estructura en directorios físicos (cada archivo es un lienzo/canvas separado en el proyecto):

5.1. Estructura de Directorios SQL

database/General/:

Creacion.sql: DDL de las tablas.

Eliminacion.sql: DROP de tablas y limpieza.

Auditoria.sql: Setup de la tabla genérica de log de auditoría.

database/{dominio}/ (rrhh, tesoreria, operaciones):

{Entidad}StoreProcedure.sql: Procedimientos pa_insertar, pa_modificar, etc.

{Entidad}TriggersAuditoria.sql: Disparadores para registro de cambios.

5.2. Viaje del ID de Usuario y Auditoría

Tablas: Toda tabla posee 4 columnas obligatorias para control: id_usuario_creacion, fecha_creacion, id_usuario_modificacion, fecha_modificacion.

Propagación: El id_usuario se extrae de la cookie en .NET ➔ Viaja en el JSON hacia Java (WS) ➔ Pasa por el BO ➔ Llega al DAO ➔ Entra como parámetro al Procedimiento Almacenado SQL.

Registro Inmutable: Los Triggers leen este parámetro durante un INSERT o UPDATE y graban automáticamente los valores anteriores y nuevos en la tabla centralizada de auditoría (log_auditoria).
