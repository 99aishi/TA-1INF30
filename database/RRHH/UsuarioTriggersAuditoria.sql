DELIMITER //

-- ===============================================================================
-- 2. TABLA: rrhh_usuario
-- ===============================================================================
-- ===============================================================================

DROP TRIGGER IF EXISTS trg_rrhh_usuario_before_insert //
CREATE TRIGGER trg_rrhh_usuario_before_insert
BEFORE INSERT ON rrhh_usuario
FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
END //

DROP TRIGGER IF EXISTS trg_rrhh_usuario_before_update //
CREATE TRIGGER trg_rrhh_usuario_before_update
BEFORE UPDATE ON rrhh_usuario
FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
END //

DROP TRIGGER IF EXISTS trg_rrhh_usuario_after_insert //
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
        'correo', NEW.correo,
            'esta_activo', NEW.esta_activo
        ),
        NEW.id_usuario_creacion
    );
END //

DROP TRIGGER IF EXISTS trg_rrhh_usuario_after_update //
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
        'correo', OLD.correo,
            'esta_activo', OLD.esta_activo
        ),
        JSON_OBJECT(
            'id_usuario', NEW.id_usuario,
            'nombres', NEW.nombres,
            'apellido_paterno', NEW.apellido_paterno,
            'apellido_materno', NEW.apellido_materno,
            'password_hash', NEW.password_hash,
        'correo', NEW.correo,
            'esta_activo', NEW.esta_activo
        ),
        NEW.id_usuario_modificacion
    );
END //

DROP TRIGGER IF EXISTS trg_rrhh_usuario_after_delete //
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
        'correo', OLD.correo,
            'esta_activo', OLD.esta_activo
        ),
        NULL,
        OLD.id_usuario_modificacion
    );
END //

-- ===============================================================================