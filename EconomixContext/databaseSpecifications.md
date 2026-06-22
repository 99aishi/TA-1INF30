Especificación y Arquitectura de Base de Datos (Economix)

Este documento centraliza todas las normativas, estándares de nomenclatura y la arquitectura física de los archivos SQL para el proyecto Economix / Rendix. Además, detalla el flujo de auditoría inmutable impulsado por el ID de sesión del usuario.

1. Filosofía de Diseño y Nomenclatura

Estilo Base: snake_case puro en MINÚSCULAS. Prohibido el uso de tildes, espacios o la letra 'ñ'.

Singularidad: Las tablas representan entidades (ej. rrhh_usuario, rrhh_area).

Prefijos de Módulos (Obligatorios):
- rrhh_: Recursos Humanos y Seguridad (ej. rrhh_usuario, rrhh_rol).
- tes_: Tesorería y estructura estática (ej. tes_caja_chica, tes_cuenta_bancaria).
- ope_: Operaciones y flujos dinámicos (ej. ope_solicitud_gasto, ope_ciclo_caja).

1.1. Restricciones y Objetos de Programación

Para facilitar el mapeo de errores, todas las restricciones y objetos deben tener nombres explícitos:
- Llaves: pk_[tabla] (Primarias), fk_[origen]_[destino] (Foráneas), uk_[tabla]_[columna] (Únicas).
- Procedimientos Almacenados: pa_[accion]_[entidad] (ej. pa_insertar_usuario).
- Triggers: trg_[entidad]_[momento]_[accion] (ej. trg_solicitud_gasto_after_update).

2. Distribución Física de Archivos (Módulos SQL)

El código SQL se estructura en carpetas físicas respetando los dominios del sistema:

database/
  ├── General/
  │     ├── Creacion.sql              <-- Contiene TODOS los CREATE TABLE (DDL) y FKs
  │     ├── Eliminacion.sql           <-- Contiene TODOS los DROP TABLE (en orden inverso)
  │     └── Auditoria.sql             <-- Crea la tabla central 'log_auditoria'
  │
  ├── RRHH/
  │     ├── UsuarioStoreProcedure.sql <-- Todos los pa_* de Usuario (Login / CRUD)
  │     ├── UsuarioTriggersAuditoria.sql
  │     ├── EmpleadoStoreProcedure.sql
  │     └── EmpleadoTriggersAuditoria.sql
  │
  ├── Tesoreria/
  │     ├── CajaChicaStoreProcedure.sql
  │     ├── CajaChicaTriggersAuditoria.sql
  │     └── ...
  │
  └── Operaciones/
        ├── SolicitudGastoStoreProcedure.sql
        ├── SolicitudGastoTriggersAuditoria.sql
        ├── TransaccionStoreProcedure.sql
        └── ...


3. Trazabilidad Base: Columnas de Control de Auditoría

Toda tabla maestra o transaccional de los tres módulos principales (rrhh_, tes_, ope_) incluye columnas para rastrear el ciclo de vida del registro directamente en la tabla:
- creado_at DATETIME (Fecha de creación)
- actualizado_at DATETIME (Fecha de última modificación)
- id_usuario_creacion INT (Usuario que creó)
- id_usuario_modificacion INT (Usuario que modificó)


4. El Viaje del ID de Sesión (Contexto de Usuario mediante Parámetros)

Para que la auditoría no sea anónima, el flujo del id_usuario funciona así:
1. La Interfaz (Blazor): El empleado realiza una acción. Blazor lee su Cookie cifrada y extrae su id_usuario.
2. Envío en el DTO/JSON: El ID de usuario se inyecta en el objeto enviado hacia Java (ej. `idUsuarioCreacion` o `idUsuarioModificacion`).
3. Paso por las Capas (Java): El ID viaja por el WS ➔ BO ➔ DAO.
4. Parámetro en el Procedimiento Almacenado: La capa DAO lo inserta en la llamada a la base de datos a través del parámetro reservado `p_id_usuario_accion`.

Ejemplo Práctico en `UsuarioStoreProcedure.sql`:
```sql
CREATE PROCEDURE pa_modificar_usuario(
    IN p_id_usuario_accion INT,
    IN p_id_usuario INT,
    IN p_nombres VARCHAR(60),
    IN p_apellido_paterno VARCHAR(40),
    IN p_apellido_materno VARCHAR(40),
    IN p_password_hash VARCHAR(255),
    IN p_correo VARCHAR(255)
)
BEGIN
    UPDATE rrhh_usuario
       SET nombres = p_nombres,
           apellido_paterno = p_apellido_paterno,
           apellido_materno = p_apellido_materno,
           password_hash = p_password_hash,
           correo = p_correo,
           actualizado_at = NOW(),
           id_usuario_modificacion = p_id_usuario_accion
     WHERE id_usuario = p_id_usuario;
END$$
```


5. Auditoría Forense y Triggers Centralizados

Además de las columnas internas, el sistema mantiene un historial de qué valores exactos se cambiaron para auditoría.

5.1. Tabla Central log_auditoria (Definida en General/Auditoria.sql)

```sql
CREATE TABLE IF NOT EXISTS log_auditoria (
    id_log INT AUTO_INCREMENT,
    nombre_tabla VARCHAR(100) NOT NULL,
    accion VARCHAR(10) NOT NULL, -- INSERT, UPDATE, DELETE
    id_registro_afectado INT NOT NULL,
    valor_anterior JSON NULL,
    valor_nuevo JSON NULL,
    id_usuario_accion INT NULL,
    momento_cambio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_log_auditoria PRIMARY KEY (id_log)
) ENGINE=InnoDB;
```

5.2. Captura Automática mediante Triggers

Los archivos de la categoría `*TriggersAuditoria.sql` contienen la lógica que intercepta automáticamente las operaciones para poblar la tabla central `log_auditoria` con los cambios críticos en formato JSON.
