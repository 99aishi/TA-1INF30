
DELIMITER //

-- ===============================================================================
-- 4. TABLA: tes_caja_chica
-- ===============================================================================
-- ===============================================================================

DROP TRIGGER IF EXISTS trg_tes_caja_chica_before_insert //
CREATE TRIGGER trg_tes_caja_chica_before_insert
BEFORE INSERT ON tes_caja_chica
FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
END //

DROP TRIGGER IF EXISTS trg_tes_caja_chica_before_update //
CREATE TRIGGER trg_tes_caja_chica_before_update
BEFORE UPDATE ON tes_caja_chica
FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
END //

DROP TRIGGER IF EXISTS trg_tes_caja_chica_after_insert //
CREATE TRIGGER trg_tes_caja_chica_after_insert
AFTER INSERT ON tes_caja_chica
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'tes_caja_chica',
        'INSERT',
        CAST(NEW.id_fondo AS CHAR),
        NULL,
        JSON_OBJECT(
            'id_fondo', NEW.id_fondo,
            'monto_techo', NEW.monto_techo,
            'id_cuenta_bancaria', NEW.id_cuenta_bancaria,
            'id_moneda', NEW.id_moneda
        ),
        NEW.id_usuario_creacion
    );
END //

DROP TRIGGER IF EXISTS trg_tes_caja_chica_after_update //
CREATE TRIGGER trg_tes_caja_chica_after_update
AFTER UPDATE ON tes_caja_chica
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'tes_caja_chica',
        'UPDATE',
        CAST(NEW.id_fondo AS CHAR),
        JSON_OBJECT(
            'id_fondo', OLD.id_fondo,
            'monto_techo', OLD.monto_techo,
            'id_cuenta_bancaria', OLD.id_cuenta_bancaria,
            'id_moneda', OLD.id_moneda
        ),
        JSON_OBJECT(
            'id_fondo', NEW.id_fondo,
            'monto_techo', NEW.monto_techo,
            'id_cuenta_bancaria', NEW.id_cuenta_bancaria,
            'id_moneda', NEW.id_moneda
        ),
        NEW.id_usuario_modificacion
    );
END //

DROP TRIGGER IF EXISTS trg_tes_caja_chica_after_delete //
CREATE TRIGGER trg_tes_caja_chica_after_delete
AFTER DELETE ON tes_caja_chica
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'tes_caja_chica',
        'DELETE',
        CAST(OLD.id_fondo AS CHAR),
        JSON_OBJECT(
            'id_fondo', OLD.id_fondo,
            'monto_techo', OLD.monto_techo,
            'id_cuenta_bancaria', OLD.id_cuenta_bancaria,
            'id_moneda', OLD.id_moneda
        ),
        NULL,
        OLD.id_usuario_modificacion
    );
END //

DELIMITER ;
