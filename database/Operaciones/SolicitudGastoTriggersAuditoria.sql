
DELIMITER //

-- ===============================================================================
-- 3. TABLA: ope_solicitud_gasto
-- ===============================================================================
-- ===============================================================================

DROP TRIGGER IF EXISTS trg_ope_solicitud_gasto_before_insert //
CREATE TRIGGER trg_ope_solicitud_gasto_before_insert
BEFORE INSERT ON ope_solicitud_gasto
FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
END //

DROP TRIGGER IF EXISTS trg_ope_solicitud_gasto_before_update //
CREATE TRIGGER trg_ope_solicitud_gasto_before_update
BEFORE UPDATE ON ope_solicitud_gasto
FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
END //

DROP TRIGGER IF EXISTS trg_ope_solicitud_gasto_after_insert //
CREATE TRIGGER trg_ope_solicitud_gasto_after_insert
AFTER INSERT ON ope_solicitud_gasto
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'ope_solicitud_gasto',
        'INSERT',
        CAST(NEW.id_solicitud_gasto AS CHAR),
        NULL,
        JSON_OBJECT(
            'id_solicitud_gasto', NEW.id_solicitud_gasto,
            'fecha_solicitud', NEW.fecha_solicitud,
            'monto_solicitado', NEW.monto_solicitado,
            'id_moneda_original', NEW.id_moneda_original,
            'tipo_cambio', NEW.tipo_cambio,
            'monto_convertido', NEW.monto_convertido,
            'motivo_solicitud', NEW.motivo_solicitud,
            'medio_desembolso', NEW.medio_desembolso,
            'estado_solicitud', NEW.estado_solicitud,
            'id_usuario_solicitante', NEW.id_usuario_solicitante,
            'id_usuario_destinatario', NEW.id_usuario_destinatario,
            'id_jefe_aprobador', NEW.id_jefe_aprobador,
            'id_tesorero_aprobador', NEW.id_tesorero_aprobador,
            'id_ciclo_caja', NEW.id_ciclo_caja
        ),
        NEW.id_usuario_creacion
    );
END //

DROP TRIGGER IF EXISTS trg_ope_solicitud_gasto_after_update //
CREATE TRIGGER trg_ope_solicitud_gasto_after_update
AFTER UPDATE ON ope_solicitud_gasto
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'ope_solicitud_gasto',
        'UPDATE',
        CAST(NEW.id_solicitud_gasto AS CHAR),
        JSON_OBJECT(
            'id_solicitud_gasto', OLD.id_solicitud_gasto,
            'fecha_solicitud', OLD.fecha_solicitud,
            'monto_solicitado', OLD.monto_solicitado,
            'id_moneda_original', OLD.id_moneda_original,
            'tipo_cambio', OLD.tipo_cambio,
            'monto_convertido', OLD.monto_convertido,
            'motivo_solicitud', OLD.motivo_solicitud,
            'medio_desembolso', OLD.medio_desembolso,
            'estado_solicitud', OLD.estado_solicitud,
            'id_usuario_solicitante', OLD.id_usuario_solicitante,
            'id_usuario_destinatario', OLD.id_usuario_destinatario,
            'id_jefe_aprobador', OLD.id_jefe_aprobador,
            'id_tesorero_aprobador', OLD.id_tesorero_aprobador,
            'id_ciclo_caja', OLD.id_ciclo_caja
        ),
        JSON_OBJECT(
            'id_solicitud_gasto', NEW.id_solicitud_gasto,
            'fecha_solicitud', NEW.fecha_solicitud,
            'monto_solicitado', NEW.monto_solicitado,
            'id_moneda_original', NEW.id_moneda_original,
            'tipo_cambio', NEW.tipo_cambio,
            'monto_convertido', NEW.monto_convertido,
            'motivo_solicitud', NEW.motivo_solicitud,
            'medio_desembolso', NEW.medio_desembolso,
            'estado_solicitud', NEW.estado_solicitud,
            'id_usuario_solicitante', NEW.id_usuario_solicitante,
            'id_usuario_destinatario', NEW.id_usuario_destinatario,
            'id_jefe_aprobador', NEW.id_jefe_aprobador,
            'id_tesorero_aprobador', NEW.id_tesorero_aprobador,
            'id_ciclo_caja', NEW.id_ciclo_caja
        ),
        NEW.id_usuario_modificacion
    );
END //

DROP TRIGGER IF EXISTS trg_ope_solicitud_gasto_after_delete //
CREATE TRIGGER trg_ope_solicitud_gasto_after_delete
AFTER DELETE ON ope_solicitud_gasto
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'ope_solicitud_gasto',
        'DELETE',
        CAST(OLD.id_solicitud_gasto AS CHAR),
        JSON_OBJECT(
            'id_solicitud_gasto', OLD.id_solicitud_gasto,
            'fecha_solicitud', OLD.fecha_solicitud,
            'monto_solicitado', OLD.monto_solicitado,
            'id_moneda_original', OLD.id_moneda_original,
            'tipo_cambio', OLD.tipo_cambio,
            'monto_convertido', OLD.monto_convertido,
            'motivo_solicitud', OLD.motivo_solicitud,
            'medio_desembolso', OLD.medio_desembolso,
            'estado_solicitud', OLD.estado_solicitud,
            'id_usuario_solicitante', OLD.id_usuario_solicitante,
            'id_usuario_destinatario', OLD.id_usuario_destinatario,
            'id_jefe_aprobador', OLD.id_jefe_aprobador,
            'id_tesorero_aprobador', OLD.id_tesorero_aprobador,
            'id_ciclo_caja', OLD.id_ciclo_caja
        ),
        NULL,
        OLD.id_usuario_modificacion
    );
END //

-- ===============================================================================