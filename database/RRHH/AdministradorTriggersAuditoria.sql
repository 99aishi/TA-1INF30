DELIMITER //

-- ===============================================================================
-- 5. TABLA: rrhh_administrador
-- ===============================================================================
-- ===============================================================================


DROP TRIGGER IF EXISTS trg_rrhh_administrador_before_insert //
CREATE TRIGGER trg_rrhh_administrador_before_insert
BEFORE INSERT ON rrhh_administrador
FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
END //

DROP TRIGGER IF EXISTS trg_rrhh_administrador_before_update //
CREATE TRIGGER trg_rrhh_administrador_before_update
BEFORE UPDATE ON rrhh_administrador
FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
END //

DROP TRIGGER IF EXISTS trg_rrhh_administrador_after_insert //
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
            'id_usuario', NEW.id_usuario
        ),
        NEW.id_usuario_creacion
    );
END //

DROP TRIGGER IF EXISTS trg_rrhh_administrador_after_update //
CREATE TRIGGER trg_rrhh_administrador_after_update
AFTER UPDATE ON rrhh_administrador
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'rrhh_administrador',
        'UPDATE',
        CAST(NEW.id_usuario AS CHAR),
        JSON_OBJECT(
            'id_usuario', OLD.id_usuario
        ),
        JSON_OBJECT(
            'id_usuario', NEW.id_usuario
        ),
        NEW.id_usuario_modificacion
    );
END //

DROP TRIGGER IF EXISTS trg_rrhh_administrador_after_delete //
CREATE TRIGGER trg_rrhh_administrador_after_delete
AFTER DELETE ON rrhh_administrador
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'rrhh_administrador',
        'DELETE',
        CAST(OLD.id_usuario AS CHAR),
        JSON_OBJECT(
            'id_usuario', OLD.id_usuario        ),
        NULL,
        OLD.id_usuario_modificacion
    );
END //

DELIMITER ;