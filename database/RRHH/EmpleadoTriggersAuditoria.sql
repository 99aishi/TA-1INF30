
DELIMITER //

-- ===============================================================================
-- 4. TABLA: rrhh_empleado
-- ===============================================================================
-- ===============================================================================

DROP TRIGGER IF EXISTS trg_rrhh_empleado_before_insert //
CREATE TRIGGER trg_rrhh_empleado_before_insert
BEFORE INSERT ON rrhh_empleado
FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
END //

DROP TRIGGER IF EXISTS trg_rrhh_empleado_before_update //
CREATE TRIGGER trg_rrhh_empleado_before_update
BEFORE UPDATE ON rrhh_empleado
FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
END //

DROP TRIGGER IF EXISTS trg_rrhh_empleado_after_insert //
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
            'numero_celular', NEW.numero_celular,
            'rol_flujo', NEW.rol_flujo,
            'id_area', NEW.id_area,
            'id_rol', NEW.id_rol,
            'id_jefe_directo', NEW.id_jefe_directo
        ),
        NEW.id_usuario_creacion
    );
END //

DROP TRIGGER IF EXISTS trg_rrhh_empleado_after_update // 
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
            'numero_celular' , OLD.numero_celular,
            'rol_flujo', OLD.rol_flujo,
            'id_area', OLD.id_area,
            'id_rol', OLD.id_rol,
            'id_jefe_directo', OLD.id_jefe_directo
        ),
        JSON_OBJECT(
            'id_usuario', NEW.id_usuario,
            'numero_celular', NEW.numero_celular,
            'rol_flujo', NEW.rol_flujo,
            'id_area', NEW.id_area,
            'id_rol', NEW.id_rol,
            'id_jefe_directo', NEW.id_jefe_directo
        ),
        NEW.id_usuario_modificacion
    );
END //

DROP TRIGGER IF EXISTS trg_rrhh_empleado_after_delete //
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
            'numero_celular', OLD.numero_celular,
            'rol_flujo', OLD.rol_flujo,
            'id_area', OLD.id_area,
            'id_rol', OLD.id_rol,
            'id_jefe_directo', OLD.id_jefe_directo
        ),
        NULL,
        OLD.id_usuario_modificacion
    );
END //

-- ===============================================================================