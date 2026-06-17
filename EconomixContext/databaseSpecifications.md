Especificación y Arquitectura de Base de Datos (Economix)

Este documento centraliza todas las normativas, estándares de nomenclatura y la arquitectura física de los archivos SQL para el proyecto Economix / Rendix. Además, detalla el flujo de auditoría inmutable impulsado por el ID de sesión del usuario.

1. Filosofía de Diseño y Nomenclatura

Estilo Base: snake_case puro en MINÚSCULAS. Prohibido el uso de tildes, espacios o la letra 'ñ'.

Singularidad: Las tablas representan entidades (ej. rrhh_empleado, nunca rrhh_empleados).

Prefijos de Módulos (Obligatorios):

rrhh_: Recursos Humanos y Seguridad (ej. rrhh_usuario, rrhh_rol).

tes_: Tesorería y estructura estática (ej. tes_caja_chica, tes_cuenta_bancaria).

ope_: Operaciones y flujos dinámicos (ej. ope_solicitud_gasto, ope_ciclo_caja).

1.1. Restricciones y Objetos de Programación

Para facilitar el mapeo de errores, todas las restricciones y objetos deben tener nombres explícitos:

Llaves: pk_[tabla] (Primarias), fk_[origen]_[destino] (Foráneas), uk_[tabla]_[columna] (Únicas).

Procedimientos Almacenados: pa_[accion]_[entidad] (ej. pa_insertar_solicitud_gasto).

Triggers: trg_[entidad]_[momento]_[accion] (ej. trg_solicitud_gasto_after_update).

2. Distribución Física de Archivos (Módulos SQL)

El código SQL no se debe escribir en un solo archivo inmanejable. Se estructura en carpetas físicas respetando los dominios del sistema para que cada archivo funcione como un lienzo/módulo aislado:

database/
  ├── General/
  │     ├── Creacion.sql              <-- Contiene TODOS los CREATE TABLE (DDL) y FKs
  │     ├── Eliminacion.sql           <-- Contiene TODOS los DROP TABLE (en orden inverso)
  │     └── Auditoria.sql             <-- Crea la tabla central 'log_auditoria'
  │
  ├── rrhh/
  │     ├── UsuarioStoreProcedure.sql <-- Todos los pa_* de Usuario
  │     ├── UsuarioTriggersAuditoria.sql <-- Todos los trg_* de Usuario
  │     ├── EmpleadoStoreProcedure.sql
  │     └── EmpleadoTriggersAuditoria.sql
  │
  ├── tesoreria/
  │     ├── CajaChicaStoreProcedure.sql
  │     ├── CajaChicaTriggersAuditoria.sql
  │     └── ...
  │
  └── operaciones/
        ├── SolicitudGastoStoreProcedure.sql
        ├── SolicitudGastoTriggersAuditoria.sql
        ├── TransaccionStoreProcedure.sql
        └── ...


3. Trazabilidad Base: Las 4 Columnas Mandatorias

Toda tabla maestra o transaccional de los tres módulos principales (rrhh_, tes_, ope_) debe incluir, sin excepción, las siguientes cuatro columnas para rastrear el ciclo de vida del registro directamente en la tabla:

-- Bloque estándar a incluir al final de la definición de cada CREATE TABLE
id_usuario_creacion INT NOT NULL,
fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
id_usuario_modificacion INT NULL,
fecha_modificacion TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP


4. El Viaje del ID de Sesión (Contexto de Usuario mediante QueryParam)

Para que la auditoría sea real y no anónima, el sistema debe saber quién está operando la base de datos, a pesar de que el backend de Java use una única conexión (Pool) con un usuario genérico (root o adminDB).

Para no contaminar los DTOs o los modelos de dominio con datos de sesión transitorios, el flujo del id_usuario funciona así:

La Interfaz (Blazor): El empleado realiza una acción. Blazor lee su Cookie cifrada y extrae su id_usuario.

Envío como QueryParam: Al llamar a la API REST de Java, la aplicación .NET inyecta este id_usuario directamente en la URL como un parámetro de consulta (ej. POST /api/operaciones/solicitudes?idUsuarioAccion=5). El JSON del cuerpo de la petición (DTO) se mantiene limpio y estricto al modelo de negocio.

Paso por las Capas (Java): El ID es capturado por la capa WS mediante @QueryParam("idUsuarioAccion") y viaja por parámetro en los métodos de WS ➔ BO ➔ DAO.

Parámetro en el Procedimiento Almacenado: La capa DAO lo inserta en la llamada a la base de datos a través del parámetro reservado p_id_usuario_accion.

Ejemplo Práctico en CajaChicaStoreProcedure.sql:

DELIMITER $$

CREATE PROCEDURE pa_modificar_caja_chica(
    IN p_id_caja_chica INT,
    IN p_monto_techo DECIMAL(12,2),
    IN p_id_usuario_accion INT -- <--- El ID que viajó como QueryParam desde la UI
)
BEGIN
    UPDATE tes_caja_chica
    SET monto_techo = p_monto_techo,
        id_usuario_modificacion = p_id_usuario_accion, -- Deja rastro interno
        fecha_modificacion = NOW()
    WHERE id_caja_chica = p_id_caja_chica;
END$$

DELIMITER ;


5. Auditoría Forense y Triggers Centralizados

Además de las 4 columnas internas, el sistema debe mantener un historial inmutable de qué valores exactos se cambiaron para detectar fraudes financieros.

5.1. Tabla Central log_auditoria (Definida en General/Auditoria.sql)

Esta tabla guardará una instantánea en formato JSON del estado anterior y el estado nuevo de los datos modificados.

CREATE TABLE log_auditoria (
    id_log INT AUTO_INCREMENT,
    nombre_tabla VARCHAR(100) NOT NULL,
    accion VARCHAR(10) NOT NULL, -- INSERT, UPDATE, DELETE
    id_registro_afectado INT NOT NULL,
    valor_anterior JSON NULL,
    valor_nuevo JSON NULL,
    id_usuario_accion INT NOT NULL,
    momento_cambio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_log_auditoria PRIMARY KEY (id_log)
);


5.2. Captura Automática mediante Triggers

Los archivos de la categoría *TriggersAuditoria.sql contienen lógica que intercepta automáticamente las operaciones para poblar la tabla central, basándose en la traza dejada en la tabla afectada.

Ejemplo de Intercepción:

DELIMITER $$

CREATE TRIGGER trg_solicitud_gasto_after_update
AFTER UPDATE ON ope_solicitud_gasto
FOR EACH ROW
BEGIN
    -- Se dispara solo si valores críticos cambian (ej. estado o monto financiero)
    IF (OLD.estado_solicitud <> NEW.estado_solicitud) THEN
        INSERT INTO log_auditoria (
            nombre_tabla,
            accion,
            id_registro_afectado,
            valor_anterior,
            valor_nuevo,
            id_usuario_accion,
            momento_cambio
        )
        VALUES (
            'ope_solicitud_gasto',
            'UPDATE',
            NEW.id_solicitud_gasto,
            JSON_OBJECT('estado', OLD.estado_solicitud),
            JSON_OBJECT('estado', NEW.estado_solicitud),
            NEW.id_usuario_modificacion, -- El ID que el Store Procedure dejó plasmado desde el QueryParam
            NOW()
        );
    END IF;
END$$

DELIMITER ;

