DELIMITER //

-- ===============================================================================
-- 1. TABLA: rrhh_rol
-- ===============================================================================

CREATE TRIGGER trg_rrhh_rol_before_insert
BEFORE INSERT ON rrhh_rol
FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
    SET NEW.id_usuario_creacion = COALESCE(NEW.id_usuario_creacion, @id_usuario_actual);
    SET NEW.id_usuario_modificacion = COALESCE(NEW.id_usuario_modificacion, @id_usuario_actual);
END //

CREATE TRIGGER trg_rrhh_rol_before_update
BEFORE UPDATE ON rrhh_rol
FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
    SET NEW.id_usuario_modificacion = COALESCE(NEW.id_usuario_modificacion, @id_usuario_actual);
END //

CREATE TRIGGER trg_rrhh_rol_after_insert
AFTER INSERT ON rrhh_rol
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'rrhh_rol',
        'INSERT',
        CAST(NEW.id_rol AS CHAR),
        NULL,
        JSON_OBJECT(
            'id_rol', NEW.id_rol,
            'titulo_rol', NEW.titulo_rol,
            'descripcion_rol', NEW.descripcion_rol,
            'esta_activo', NEW.esta_activo
        ),
        NEW.id_usuario_creacion
    );
END //

CREATE TRIGGER trg_rrhh_rol_after_update
AFTER UPDATE ON rrhh_rol
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'rrhh_rol',
        'UPDATE',
        CAST(NEW.id_rol AS CHAR),
        JSON_OBJECT(
            'id_rol', OLD.id_rol,
            'titulo_rol', OLD.titulo_rol,
            'descripcion_rol', OLD.descripcion_rol,
            'esta_activo', OLD.esta_activo
        ),
        JSON_OBJECT(
            'id_rol', NEW.id_rol,
            'titulo_rol', NEW.titulo_rol,
            'descripcion_rol', NEW.descripcion_rol,
            'esta_activo', NEW.esta_activo
        ),
        NEW.id_usuario_modificacion
    );
END //

CREATE TRIGGER trg_rrhh_rol_after_delete
AFTER DELETE ON rrhh_rol
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'rrhh_rol',
        'DELETE',
        CAST(OLD.id_rol AS CHAR),
        JSON_OBJECT(
            'id_rol', OLD.id_rol,
            'titulo_rol', OLD.titulo_rol,
            'descripcion_rol', OLD.descripcion_rol,
            'esta_activo', OLD.esta_activo
        ),
        NULL,
        IFNULL(@id_usuario_actual, OLD.id_usuario_modificacion)
    );
END //

-- ===============================================================================
-- 2. TABLA: rrhh_usuario
-- ===============================================================================

CREATE TRIGGER trg_rrhh_usuario_before_insert
BEFORE INSERT ON rrhh_usuario
FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
    SET NEW.id_usuario_creacion = COALESCE(NEW.id_usuario_creacion, @id_usuario_actual);
    SET NEW.id_usuario_modificacion = COALESCE(NEW.id_usuario_modificacion, @id_usuario_actual);
END //

CREATE TRIGGER trg_rrhh_usuario_before_update
BEFORE UPDATE ON rrhh_usuario
FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
    SET NEW.id_usuario_modificacion = COALESCE(NEW.id_usuario_modificacion, @id_usuario_actual);
END //

CREATE TRIGGER trg_rrhh_usuario_after_insert
AFTER INSERT ON rrhh_usuario
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'rrhh_usuario',
        'INSERT',
        CAST(NEW.id_usuario AS CHAR),
        NULL,
        JSON_OBJECT(
            'id_usuario', NEW.id_usuario,
            'nombres', NEW.nombres,
            'apellido_paterno', NEW.apellido_paterno,
            'apellido_materno', NEW.apellido_materno,
            'password_hash', NEW.password_hash,
            'esta_activo', NEW.esta_activo
        ),
        NEW.id_usuario_creacion
    );
END //

CREATE TRIGGER trg_rrhh_usuario_after_update
AFTER UPDATE ON rrhh_usuario
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'rrhh_usuario',
        'UPDATE',
        CAST(NEW.id_usuario AS CHAR),
        JSON_OBJECT(
            'id_usuario', OLD.id_usuario,
            'nombres', OLD.nombres,
            'apellido_paterno', OLD.apellido_paterno,
            'apellido_materno', OLD.apellido_materno,
            'password_hash', OLD.password_hash,
            'esta_activo', OLD.esta_activo
        ),
        JSON_OBJECT(
            'id_usuario', NEW.id_usuario,
            'nombres', NEW.nombres,
            'apellido_paterno', NEW.apellido_paterno,
            'apellido_materno', NEW.apellido_materno,
            'password_hash', NEW.password_hash,
            'esta_activo', NEW.esta_activo
        ),
        NEW.id_usuario_modificacion
    );
END //

CREATE TRIGGER trg_rrhh_usuario_after_delete
AFTER DELETE ON rrhh_usuario
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'rrhh_usuario',
        'DELETE',
        CAST(OLD.id_usuario AS CHAR),
        JSON_OBJECT(
            'id_usuario', OLD.id_usuario,
            'nombres', OLD.nombres,
            'apellido_paterno', OLD.apellido_paterno,
            'apellido_materno', OLD.apellido_materno,
            'password_hash', OLD.password_hash,
            'esta_activo', OLD.esta_activo
        ),
        NULL,
        IFNULL(@id_usuario_actual, OLD.id_usuario_modificacion)
    );
END //

-- ===============================================================================
-- 3. TABLA: rrhh_area
-- ===============================================================================

CREATE TRIGGER trg_rrhh_area_before_insert
BEFORE INSERT ON rrhh_area
FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
    SET NEW.id_usuario_creacion = COALESCE(NEW.id_usuario_creacion, @id_usuario_actual);
    SET NEW.id_usuario_modificacion = COALESCE(NEW.id_usuario_modificacion, @id_usuario_actual);
END //

CREATE TRIGGER trg_rrhh_area_before_update
BEFORE UPDATE ON rrhh_area
FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
    SET NEW.id_usuario_modificacion = COALESCE(NEW.id_usuario_modificacion, @id_usuario_actual);
END //

CREATE TRIGGER trg_rrhh_area_after_insert
AFTER INSERT ON rrhh_area
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'rrhh_area',
        'INSERT',
        CAST(NEW.id_area AS CHAR),
        NULL,
        JSON_OBJECT(
            'id_area', NEW.id_area,
            'nombre_area', NEW.nombre_area,
            'descripcion_area', NEW.descripcion_area,
            'id_jefe', NEW.id_jefe,
            'esta_activo', NEW.esta_activo
        ),
        NEW.id_usuario_creacion
    );
END //

CREATE TRIGGER trg_rrhh_area_after_update
AFTER UPDATE ON rrhh_area
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'rrhh_area',
        'UPDATE',
        CAST(NEW.id_area AS CHAR),
        JSON_OBJECT(
            'id_area', OLD.id_area,
            'nombre_area', OLD.nombre_area,
            'descripcion_area', OLD.descripcion_area,
            'id_jefe', OLD.id_jefe,
            'esta_activo', OLD.esta_activo
        ),
        JSON_OBJECT(
            'id_area', NEW.id_area,
            'nombre_area', NEW.nombre_area,
            'descripcion_area', NEW.descripcion_area,
            'id_jefe', NEW.id_jefe,
            'esta_activo', NEW.esta_activo
        ),
        NEW.id_usuario_modificacion
    );
END //

CREATE TRIGGER trg_rrhh_area_after_delete
AFTER DELETE ON rrhh_area
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'rrhh_area',
        'DELETE',
        CAST(OLD.id_area AS CHAR),
        JSON_OBJECT(
            'id_area', OLD.id_area,
            'nombre_area', OLD.nombre_area,
            'descripcion_area', OLD.descripcion_area,
            'id_jefe', OLD.id_jefe,
            'esta_activo', OLD.esta_activo
        ),
        NULL,
        IFNULL(@id_usuario_actual, OLD.id_usuario_modificacion)
    );
END //

-- ===============================================================================
-- 4. TABLA: rrhh_empleado
-- ===============================================================================

CREATE TRIGGER trg_rrhh_empleado_before_insert
BEFORE INSERT ON rrhh_empleado
FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
    SET NEW.id_usuario_creacion = COALESCE(NEW.id_usuario_creacion, @id_usuario_actual);
    SET NEW.id_usuario_modificacion = COALESCE(NEW.id_usuario_modificacion, @id_usuario_actual);
END //

CREATE TRIGGER trg_rrhh_empleado_before_update
BEFORE UPDATE ON rrhh_empleado
FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
    SET NEW.id_usuario_modificacion = COALESCE(NEW.id_usuario_modificacion, @id_usuario_actual);
END //

CREATE TRIGGER trg_rrhh_empleado_after_insert
AFTER INSERT ON rrhh_empleado
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'rrhh_empleado',
        'INSERT',
        CAST(NEW.id_usuario AS CHAR),
        NULL,
        JSON_OBJECT(
            'id_usuario', NEW.id_usuario,
            'correo_institucional', NEW.correo_institucional,
            'numero_celular', NEW.numero_celular,
            'id_area', NEW.id_area,
            'id_rol', NEW.id_rol,
            'id_jefe_directo', NEW.id_jefe_directo
        ),
        NEW.id_usuario_creacion
    );
END //

CREATE TRIGGER trg_rrhh_empleado_after_update
AFTER UPDATE ON rrhh_empleado
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'rrhh_empleado',
        'UPDATE',
        CAST(NEW.id_usuario AS CHAR),
        JSON_OBJECT(
            'id_usuario', OLD.id_usuario,
            'correo_institucional', OLD.correo_institucional,
            'numero_celular', OLD.numero_celular,
            'id_area', OLD.id_area,
            'id_rol', OLD.id_rol,
            'id_jefe_directo', OLD.id_jefe_directo
        ),
        JSON_OBJECT(
            'id_usuario', NEW.id_usuario,
            'correo_institucional', NEW.correo_institucional,
            'numero_celular', NEW.numero_celular,
            'id_area', NEW.id_area,
            'id_rol', NEW.id_rol,
            'id_jefe_directo', NEW.id_jefe_directo
        ),
        NEW.id_usuario_modificacion
    );
END //

CREATE TRIGGER trg_rrhh_empleado_after_delete
AFTER DELETE ON rrhh_empleado
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'rrhh_empleado',
        'DELETE',
        CAST(OLD.id_usuario AS CHAR),
        JSON_OBJECT(
            'id_usuario', OLD.id_usuario,
            'correo_institucional', OLD.correo_institucional,
            'numero_celular', OLD.numero_celular,
            'id_area', OLD.id_area,
            'id_rol', OLD.id_rol,
            'id_jefe_directo', OLD.id_jefe_directo
        ),
        NULL,
        IFNULL(@id_usuario_actual, OLD.id_usuario_modificacion)
    );
END //

-- ===============================================================================
-- 5. TABLA: rrhh_administrador
-- ===============================================================================

CREATE TRIGGER trg_rrhh_administrador_before_insert
BEFORE INSERT ON rrhh_administrador
FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
    SET NEW.id_usuario_creacion = COALESCE(NEW.id_usuario_creacion, @id_usuario_actual);
    SET NEW.id_usuario_modificacion = COALESCE(NEW.id_usuario_modificacion, @id_usuario_actual);
END //

CREATE TRIGGER trg_rrhh_administrador_before_update
BEFORE UPDATE ON rrhh_administrador
FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
    SET NEW.id_usuario_modificacion = COALESCE(NEW.id_usuario_modificacion, @id_usuario_actual);
END //

CREATE TRIGGER trg_rrhh_administrador_after_insert
AFTER INSERT ON rrhh_administrador
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'rrhh_administrador',
        'INSERT',
        CAST(NEW.id_usuario AS CHAR),
        NULL,
        JSON_OBJECT(
            'id_usuario', NEW.id_usuario,
            'correo_soporte', NEW.correo_soporte
        ),
        NEW.id_usuario_creacion
    );
END //

CREATE TRIGGER trg_rrhh_administrador_after_update
AFTER UPDATE ON rrhh_administrador
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'rrhh_administrador',
        'UPDATE',
        CAST(NEW.id_usuario AS CHAR),
        JSON_OBJECT(
            'id_usuario', OLD.id_usuario,
            'correo_soporte', OLD.correo_soporte
        ),
        JSON_OBJECT(
            'id_usuario', NEW.id_usuario,
            'correo_soporte', NEW.correo_soporte
        ),
        NEW.id_usuario_modificacion
    );
END //

CREATE TRIGGER trg_rrhh_administrador_after_delete
AFTER DELETE ON rrhh_administrador
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'rrhh_administrador',
        'DELETE',
        CAST(OLD.id_usuario AS CHAR),
        JSON_OBJECT(
            'id_usuario', OLD.id_usuario,
            'correo_soporte', OLD.correo_soporte
        ),
        NULL,
        IFNULL(@id_usuario_actual, OLD.id_usuario_modificacion)
    );
END //

DELIMITER ;