
DELIMITER //

-- ===============================================================================
-- 1. TABLA: rrhh_rol
-- ===============================================================================
-- ===============================================================================

DROP TRIGGER IF EXISTS trg_rrhh_rol_before_insert //
CREATE TRIGGER trg_rrhh_rol_before_insert
BEFORE INSERT ON rrhh_rol
FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
END //

DROP TRIGGER IF EXISTS trg_rrhh_rol_before_update //
CREATE TRIGGER trg_rrhh_rol_before_update
BEFORE UPDATE ON rrhh_rol
FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
END //

DROP TRIGGER IF EXISTS trg_rrhh_rol_after_insert //
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

DROP TRIGGER IF EXISTS trg_rrhh_rol_after_update //
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

DROP TRIGGER IF EXISTS trg_rrhh_rol_after_delete //
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
        OLD.id_usuario_modificacion
    );
END //

-- ===============================================================================
