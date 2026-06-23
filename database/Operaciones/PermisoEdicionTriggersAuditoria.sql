
DELIMITER //

-- ===============================================================================
-- TABLA: ope_permiso_edicion
-- ===============================================================================

DROP TRIGGER IF EXISTS trg_ope_permiso_edicion_before_insert //
CREATE TRIGGER trg_ope_permiso_edicion_before_insert
BEFORE INSERT ON ope_permiso_edicion
FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
END //

DROP TRIGGER IF EXISTS trg_ope_permiso_edicion_before_update //
CREATE TRIGGER trg_ope_permiso_edicion_before_update
BEFORE UPDATE ON ope_permiso_edicion
FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
END //

DROP TRIGGER IF EXISTS trg_ope_permiso_edicion_after_insert //
CREATE TRIGGER trg_ope_permiso_edicion_after_insert
AFTER INSERT ON ope_permiso_edicion
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'ope_permiso_edicion',
        'INSERT',
        CAST(NEW.id_permiso AS CHAR),
        NULL,
        JSON_OBJECT(
            'id_permiso', NEW.id_permiso,
            'id_usuario_solicitante', NEW.id_usuario_solicitante,
            'id_usuario_autorizador', NEW.id_usuario_autorizador,
            'id_comprobante', NEW.id_comprobante,
            'estado', NEW.estado,
            'motivo_solicitud', NEW.motivo_solicitud,
            'motivo_autorizacion', NEW.motivo_autorizacion,
            'fecha_creacion', NEW.fecha_creacion,
            'fecha_expiracion', NEW.fecha_expiracion,
            'fecha_uso', NEW.fecha_uso
        ),
        NEW.id_usuario_creacion
    );
END //

DROP TRIGGER IF EXISTS trg_ope_permiso_edicion_after_update //
CREATE TRIGGER trg_ope_permiso_edicion_after_update
AFTER UPDATE ON ope_permiso_edicion
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'ope_permiso_edicion',
        'UPDATE',
        CAST(NEW.id_permiso AS CHAR),
        JSON_OBJECT(
            'id_permiso', OLD.id_permiso,
            'id_usuario_solicitante', OLD.id_usuario_solicitante,
            'id_usuario_autorizador', OLD.id_usuario_autorizador,
            'id_comprobante', OLD.id_comprobante,
            'estado', OLD.estado,
            'motivo_solicitud', OLD.motivo_solicitud,
            'motivo_autorizacion', OLD.motivo_autorizacion,
            'fecha_creacion', OLD.fecha_creacion,
            'fecha_expiracion', OLD.fecha_expiracion,
            'fecha_uso', OLD.fecha_uso
        ),
        JSON_OBJECT(
            'id_permiso', NEW.id_permiso,
            'id_usuario_solicitante', NEW.id_usuario_solicitante,
            'id_usuario_autorizador', NEW.id_usuario_autorizador,
            'id_comprobante', NEW.id_comprobante,
            'estado', NEW.estado,
            'motivo_solicitud', NEW.motivo_solicitud,
            'motivo_autorizacion', NEW.motivo_autorizacion,
            'fecha_creacion', NEW.fecha_creacion,
            'fecha_expiracion', NEW.fecha_expiracion,
            'fecha_uso', NEW.fecha_uso
        ),
        NEW.id_usuario_modificacion
    );
END //

DROP TRIGGER IF EXISTS trg_ope_permiso_edicion_after_delete //
CREATE TRIGGER trg_ope_permiso_edicion_after_delete
AFTER DELETE ON ope_permiso_edicion
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'ope_permiso_edicion',
        'DELETE',
        CAST(OLD.id_permiso AS CHAR),
        JSON_OBJECT(
            'id_permiso', OLD.id_permiso,
            'id_usuario_solicitante', OLD.id_usuario_solicitante,
            'id_usuario_autorizador', OLD.id_usuario_autorizador,
            'id_comprobante', OLD.id_comprobante,
            'estado', OLD.estado,
            'motivo_solicitud', OLD.motivo_solicitud,
            'motivo_autorizacion', OLD.motivo_autorizacion,
            'fecha_creacion', OLD.fecha_creacion,
            'fecha_expiracion', OLD.fecha_expiracion,
            'fecha_uso', OLD.fecha_uso
        ),
        NULL,
        OLD.id_usuario_modificacion
    );
END //

-- ===============================================================================
