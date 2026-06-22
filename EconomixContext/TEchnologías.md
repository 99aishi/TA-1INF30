Especificación de Arquitectura Tecnológica y Patrones de Diseño

Este documento detalla la estructura física, los patrones de diseño y las reglas de comunicación que rigen el sistema Economix / Rendix. Describe cómo se organiza el código en la base de datos, el backend (Java) y el frontend (.NET Blazor).

1. Reglas de Comunicación y Flujo Unidireccional

El sistema se basa en una arquitectura de capas estricta. Las capas solo pueden invocar a su capa inmediatamente inferior. Está prohibido realizar llamadas cruzadas sin seguir la jerarquía o saltarse capas.

Flujo Transaccional:
Frontend UI (WA) ➔ Proxy .NET (WS) ➔ Backend Java (WS) ➔ Negocio (BO) ➔ Persistencia (DAO) ➔ DBManager ➔ Base de Datos MySQL

2. Interfaces Genéricas por Capa (Independencia Estricta)

Para mantener la independencia tecnológica y semántica de cada nivel, cada capa define su propio contrato base genérico que dicta las operaciones CRUD.

Estas interfaces genéricas se ubican en paquetes de infraestructura o comunes:

Capa de Persistencia: Define IDAO<T> (insertar, listar, modificar, eliminar).

Capa de Negocio: Define IBaseBO<T> para orquestar reglas de negocio.

Capa de Servicios Web C# Proxy: Define IWS<T> para los contratos de los endpoints del lado del frontend.

Cada entidad de dominio luego implementará su versión específica heredando de estas bases.

3. Estructura de Paquetes en el Backend (Java)

El backend divide lógicamente el sistema en cuatro dominios horizontales: RRHH, Tesorería, Operaciones y Auditoría. Dentro de cada dominio, el código se segmenta por subpaquetes según su capa.

Nomenclatura base: pe.edu.pucp.economix.{dominio}

Tomando como ejemplo la entidad Empleado del dominio rrhh:

Capa de Modelo (pe.edu.pucp.economix.rrhh.model):
Contiene los POJOs (Empleado.java, Usuario.java) y un subpaquete para sus respectivos enums (pe.edu.pucp.economix.rrhh.model.enums o rrhh.model).

Capa de Persistencia (pe.edu.pucp.economix.rrhh.idao y pe.edu.pucp.economix.rrhh.daoi):
- IEmpleadoDAO: Interfaz que hereda de IDAO<Empleado>.
- EmpleadoDAOImpl: Clase que implementa IEmpleadoDAO y utiliza DBManager para invocar a MySQL.

Capa de Negocio (pe.edu.pucp.economix.rrhh.ibo y pe.edu.pucp.economix.rrhh.boi):
- IEmpleadoBO: Interfaz que hereda de IBaseBO<Empleado>.
- EmpleadoBOImpl: Implementación que inyecta a EmpleadoDAOImpl y aplica validaciones.

Capa de Servicios Web (pe.edu.pucp.economix.economixws.rrhh.ws):
- EmpleadoWS: Endpoint JAX-RS que expone los recursos JSON consumidos por el cliente .NET.

4. Arquitectura Frontend (.NET Blazor WA)

La interfaz de usuario (EconomixWA) está diseñada con componentes reactivos agrupados por la clase observable que representan (ej. Area, Empleado, Moneda).

4.1. El Patrón Page - Item - Detalle - SearchBar

Cada carpeta de dominio dentro de Components/Pages/ sigue un patrón unificado de componentes:

- {Entidad}Page.razor (Contenedor General): Es la vista ruteable (ej. @page "/areas"). Maneja la lista de datos en memoria y orquesta la comunicación entre la barra de búsqueda y el panel de detalle.
- {Entidad}Item.razor (Elemento de Lista): Representa la unidad de visualización (tarjeta o fila) que será iterada por el Page.
- {Entidad}Detalle.razor (Panel Lateral / Drawer): Vista superpuesta a la derecha que muestra la información completa para crear, ver o editar.
- {Entidad}SearchBar.razor (Barra de Búsqueda): Componente encapsulado con inputs especializados que dispara eventos hacia el Page para filtrar datos.

4.2. Layouts y Gestión de Sesión (Cookies)

- Layouts: Existen plantillas maestras como MainLayout.razor (con el sidemenu, barra superior) y LoginLayout.razor (pantalla centralizada sin navegación).
- Autenticación por Cookies: El login invoca un endpoint en Program.cs (/auth/login) que interactúa con el backend Java. Si es exitoso, se genera una cookie cifrada de 8 horas en el navegador con expiración deslizante.
- Recuperación del Perfil: Mediante los Claims de la cookie, el sistema recupera la información de la sesión para pintar datos del perfil y restringir accesos según su rol operativo (Administrador, Jefe, Empleado, Tesoreria).

5. Arquitectura de Base de Datos y Trazabilidad

Los scripts SQL se estructuran de manera limpia y modular en carpetas físicas respetando los dominios del sistema:

5.1. Estructura de Directorios SQL

database/General/:
- Creacion.sql: DDL de las tablas.
- Eliminacion.sql: DROP de tablas y limpieza.
- Auditoria.sql: Setup de la tabla genérica de log de auditoría.

database/{dominio}/ (RRHH, Tesoreria, Operaciones):
- {Entidad}StoreProcedure.sql: Procedimientos pa_insertar, pa_modificar, etc.
- {Entidad}TriggersAuditoria.sql: Disparadores para registro de cambios automáticos hacia `log_auditoria`.

5.2. Viaje del ID de Usuario y Auditoría

- Tablas: Toda tabla posee columnas de control: id_usuario_creacion, creado_at, id_usuario_modificacion, actualizado_at.
- Propagación: El id_usuario se extrae de la cookie en .NET ➔ Viaja en el JSON hacia Java (WS) ➔ Pasa por el BO ➔ Llega al DAO ➔ Entra como parámetro al Procedimiento Almacenado SQL.
- Registro Inmutable: Los Triggers de Auditoría interceptan automáticamente los cambios de datos y los registran en formato JSON dentro de `log_auditoria`.
