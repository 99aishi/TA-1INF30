elimiDELIMITER //

-- ===============================================================================
-- 5. TABLA: ope_transaccion
-- ===============================================================================
-- ===============================================================================

DROP TRIGGER IF EXISTS trg_ope_transaccion_before_insert //
CREATE TRIGGER trg_ope_transaccion_before_insert
BEFORE INSERT ON ope_transaccion
FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
END //

DROP TRIGGER IF EXISTS trg_ope_transaccion_before_update //
CREATE TRIGGER trg_ope_transaccion_before_update
BEFORE UPDATE ON ope_transaccion
FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
END //

DROP TRIGGER IF EXISTS trg_ope_transaccion_after_insert //
CREATE TRIGGER trg_ope_transaccion_after_insert
AFTER INSERT ON ope_transaccion
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'ope_transaccion',
        'INSERT',
        CAST(NEW.id_transaccion AS CHAR),
        NULL,
        JSON_OBJECT(
            'id_transaccion', NEW.id_transaccion,
            'tipo_operacion', NEW.tipo_operacion,
            'momento_operacion', NEW.momento_operacion,
            'monto_transaccion', NEW.monto_transaccion,
            'id_beneficiario', NEW.id_beneficiario,
            'numero_operacion_bancaria', NEW.numero_operacion_bancaria,
            'medio_pago', NEW.medio_pago,
            'id_tipo_cambio', NEW.id_tipo_cambio,
            'estado_transaccion', NEW.estado_transaccion,
            'id_cuenta_origen', NEW.id_cuenta_origen,
            'id_cuenta_destino', NEW.id_cuenta_destino,
            'id_moneda', NEW.id_moneda
        ),
        NEW.id_usuario_creacion
    );
END //

DROP TRIGGER IF EXISTS trg_ope_transaccion_after_update //
CREATE TRIGGER trg_ope_transaccion_after_update
AFTER UPDATE ON ope_transaccion
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'ope_transaccion',
        'UPDATE',
        CAST(NEW.id_transaccion AS CHAR),
        JSON_OBJECT(
            'id_transaccion', OLD.id_transaccion,
            'tipo_operacion', OLD.tipo_operacion,
            'momento_operacion', OLD.momento_operacion,
            'monto_transaccion', OLD.monto_transaccion,
            'id_beneficiario', OLD.id_beneficiario,
            'numero_operacion_bancaria', OLD.numero_operacion_bancaria,
            'medio_pago', OLD.medio_pago,
            'id_tipo_cambio', OLD.id_tipo_cambio,
            'estado_transaccion', OLD.estado_transaccion,
            'id_cuenta_origen', OLD.id_cuenta_origen,
            'id_cuenta_destino', OLD.id_cuenta_destino,
            'id_moneda', OLD.id_moneda
        ),
        JSON_OBJECT(
            'id_transaccion', NEW.id_transaccion,
            'tipo_operacion', NEW.tipo_operacion,
            'momento_operacion', NEW.momento_operacion,
            'monto_transaccion', NEW.monto_transaccion,
            'id_beneficiario', NEW.id_beneficiario,
            'numero_operacion_bancaria', NEW.numero_operacion_bancaria,
            'medio_pago', NEW.medio_pago,
            'id_tipo_cambio', NEW.id_tipo_cambio,
            'estado_transaccion', NEW.estado_transaccion,
            'id_cuenta_origen', NEW.id_cuenta_origen,
            'id_cuenta_destino', NEW.id_cuenta_destino,
            'id_moneda', NEW.id_moneda
        ),
        NEW.id_usuario_modificacion
    );
END //

DROP TRIGGER IF EXISTS trg_ope_transaccion_after_delete //
CREATE TRIGGER trg_ope_transaccion_after_delete
AFTER DELETE ON ope_transaccion
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'ope_transaccion',
        'DELETE',
        CAST(OLD.id_transaccion AS CHAR),
        JSON_OBJECT(
            'id_transaccion', OLD.id_transaccion,
            'tipo_operacion', OLD.tipo_operacion,
            'momento_operacion', OLD.momento_operacion,
            'monto_transaccion', OLD.monto_transaccion,
            'id_beneficiario', OLD.id_beneficiario,
            'numero_operacion_bancaria', OLD.numero_operacion_bancaria,
            'medio_pago', OLD.medio_pago,
            'id_tipo_cambio', OLD.id_tipo_cambio,
            'estado_transaccion', OLD.estado_transaccion,
            'id_cuenta_origen', OLD.id_cuenta_origen,
            'id_cuenta_destino', OLD.id_cuenta_destino,
            'id_moneda', OLD.id_moneda
        ),
        NULL,
        OLD.id_usuario_modificacion
    );
END //

DELIMITER ;