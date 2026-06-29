
DELIMITER //

-- ================================================================
-- Triggers para tes_tipo_cambio
-- TABLA: tes_tipo_cambio
-- ================================================================

DROP TRIGGER IF EXISTS trg_tes_tipo_cambio_before_insert //
CREATE TRIGGER trg_tes_tipo_cambio_before_insert
BEFORE INSERT ON tes_tipo_cambio
FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
END //

DROP TRIGGER IF EXISTS trg_tes_tipo_cambio_before_update //
CREATE TRIGGER trg_tes_tipo_cambio_before_update
BEFORE UPDATE ON tes_tipo_cambio
FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
END //

DROP TRIGGER IF EXISTS trg_tes_tipo_cambio_after_insert //
CREATE TRIGGER trg_tes_tipo_cambio_after_insert
AFTER INSERT ON tes_tipo_cambio
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'tes_tipo_cambio',
        'INSERT',
        CAST(NEW.id_tipo_cambio AS CHAR),
        NULL,
        JSON_OBJECT(
            'id_tipo_cambio', NEW.id_tipo_cambio,
            'id_moneda_origen', NEW.id_moneda_origen,
            'id_moneda_destino', NEW.id_moneda_destino,
            'valor_tipo_cambio', NEW.valor_tipo_cambio,
            'fecha_tipo_cambio', NEW.fecha_tipo_cambio
        ),
        NEW.id_usuario_creacion
    );
END //

DROP TRIGGER IF EXISTS trg_tes_tipo_cambio_after_update //
CREATE TRIGGER trg_tes_tipo_cambio_after_update
AFTER UPDATE ON tes_tipo_cambio
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'tes_tipo_cambio',
        'UPDATE',
        CAST(NEW.id_tipo_cambio AS CHAR),
        JSON_OBJECT(
            'id_moneda_origen', OLD.id_moneda_origen,
            'id_moneda_destino', OLD.id_moneda_destino,
            'valor_tipo_cambio', OLD.valor_tipo_cambio,
            'fecha_tipo_cambio', OLD.fecha_tipo_cambio
        ),
        JSON_OBJECT(
            'id_moneda_origen', NEW.id_moneda_origen,
            'id_moneda_destino', NEW.id_moneda_destino,
            'valor_tipo_cambio', NEW.valor_tipo_cambio,
            'fecha_tipo_cambio', NEW.fecha_tipo_cambio
        ),
        NEW.id_usuario_modificacion
    );
END //

DROP TRIGGER IF EXISTS trg_tes_tipo_cambio_after_delete //
CREATE TRIGGER trg_tes_tipo_cambio_after_delete
AFTER DELETE ON tes_tipo_cambio
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'tes_tipo_cambio',
        'DELETE',
        CAST(OLD.id_tipo_cambio AS CHAR),
        JSON_OBJECT(
            'id_tipo_cambio', OLD.id_tipo_cambio,
            'id_moneda_origen', OLD.id_moneda_origen,
            'id_moneda_destino', OLD.id_moneda_destino,
            'valor_tipo_cambio', OLD.valor_tipo_cambio,
            'fecha_tipo_cambio', OLD.fecha_tipo_cambio
        ),
        NULL,
        OLD.id_usuario_modificacion
    );
END //

DELIMITER ;
