PLAN DE IMPLEMENTACIÓN Y NORMATIVA DE ARQUITECTURA: ECONOMIX

Este documento establece el estándar técnico, los requisitos de negocio y las directrices de diseño de software para la implementación del sistema ECONOMIX/RENDIX. Es de carácter obligatorio para el desarrollo de módulos tanto en el Backend (Java REST) como en el Frontend (.NET Blazor).

SECCIÓN I: FILOSOFÍA DE DISEÑO Y NORMAS GLOBALES

Para asegurar la auditabilidad, consistencia financiera e inmutabilidad de los datos históricos, se decretan las siguientes reglas universales de persistencia:

1. Eliminaciones Lógicas (Soft Deletes) Únicas

Queda terminantemente prohibido el uso de sentencias DELETE físicas en la base de datos para registros operativos o maestros.
- Mecanismo: Cada tabla contiene un atributo estado (`esta_activo` o `activa` de tipo TINYINT(1) o `estado_fondo` de tipo ENUM).
- Regla de Consulta: Todo procedimiento almacenado de selección (pa_listar_..., pa_buscar_...) filtra por defecto únicamente los registros cuyo estado es activo, a menos que se especifique explícitamente un requerimiento de auditoría o histórico.

2. Trazabilidad de Auditoría en Operaciones

Cualquier procedimiento de modificación (pa_modificar_...) o inserción (pa_insertar_...) recibe obligatoriamente el ID del Usuario que realiza la acción (p_id_usuario_accion) para dejar traza en los campos de auditoría de las tablas correspondientes o en la tabla central de auditoría.

SECCIÓN II: AUTENTICACIÓN Y SESIÓN DEL USUARIO

El flujo de inicio de sesión es el punto de partida que unifica los datos de los actores en el sistema.

1. Procedimiento Almacenado de Login

El login del sistema recupera de forma consolidada toda la información del actor. El procedimiento almacenado unificado (`pa_buscar_usuario_por_correo` en `UsuarioStoreProcedure.sql`) realiza un LEFT JOIN entre usuarios, empleados, áreas, roles y jefes, asegurando la devolución obligatoria de los identificadores persistentes.

2. Estado de Sesión en el Frontend (.NET)

Cuando el servicio de consumo de .NET procese la respuesta del Login de Java, persistirá en la Cookie de Autenticación los siguientes datos clave (mediante Claims):
- ID_Usuario: Identificador base del usuario en el sistema.
- Correo: Dirección de correo electrónico.
- Nombre Completo: Nombres, Apellido Paterno, Apellido Materno.
- Rol: Rol jerárquico determinado por `DeterminarRolEmpleado` (Administrador, Jefe, Empleado, Tesoreria).

SECCIÓN III: PLANTEAMIENTO DE ACTORES Y FLUJOS TRANSACCIONALES

1. Agregado: Áreas, Cuentas Bancarias y Cajas Chicas

La estructura de flujo de fondos es altamente flexible y desacoplada por diseño:
- **Estructura Relacional**: Un Área (`rrhh_area`) puede poseer múltiples cuentas bancarias corporativas (`tes_cuenta_bancaria`), y a su vez, cada una de estas cuentas bancarias puede financiar múltiples cajas chicas (`tes_caja_chica`).
- **No Obligatoriedad**: El área de la empresa NO representa una fusión obligatoria inmediata con una caja chica al crearse. Es perfectamente posible crear un Área sin asignarle cuentas bancarias o cajas chicas en primera instancia.
- **Flujo de Creación (Desacoplado)**:
  - Creación del Área (`pa_insertar_area`) con el jefe inicial definido como NULL.
  - Opcionalmente, se pueden asociar cuentas bancarias corporativas (`pa_insertar_cuenta_bancaria`) al área.
  - Opcionalmente, sobre cada cuenta bancaria activa, se pueden inicializar y financiar múltiples cajas chicas (`pa_insertar_caja_chica`) especificando un monto techo o límite presupuestal.

2. Agregado: Empleados, Jerarquías y Medios de Pago

El empleado es el actor ejecutor de los flujos del sistema. Su registro requiere la herencia del usuario base y el alta de sus canales de transferencia financiera autorizados.

- Flujo de Creación (Transacción Atómica): Se realiza desde el BO de Empleados, registrando el usuario base (`pa_insertar_usuario`) y el registro específico del empleado (`pa_insertar_empleado`), asociándolo a un área y un rol.

3. Agregado: Comprobantes de Pago (Simulación de Archivos)

El sistema NO gestiona archivos físicos, blobs ni uploads en la base de datos. La trazabilidad documental se simula únicamente mediante el nombre del archivo.
- Campo en ComprobantePago: `nombre_archivo_comprobante` (VARCHAR(500)).
- Almacena el nombre simulado del comprobante (ej. "factura_001.pdf", "boleta_123.jpg"). No se valida extensión, tamaño ni existencia física; es puramente informativo para auditoría visual.

SECCIÓN IV: CONVENIO DE INTEROPERABILIDAD (.NET ↔ JAVA)

1. Convención de Mapeo en el Modelo de .NET

Las clases del modelo en .NET que actúen como "espejo" de las de Java cumplen la siguiente directriz para evitar pérdidas de tipos de datos en el canal JSON:
- Decoradores JSON obligatorios: Cada campo lleva el atributo `[JsonPropertyName("nombre_en_java")]` con la convención camelCase utilizada por el backend en Java para asegurar la autodeserialización.

2. Estándar del Módulo de Servicios (Proxy en .NET)

Cada actor principal dispone de una interfaz de servicio (ej. `IAreaWS`, `IEmpleadoWS`) y su respectiva implementación proxy que realiza la llamada HTTP síncrona o asíncrona hacia el WebService de Java.
