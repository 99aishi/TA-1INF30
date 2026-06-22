
DELIMITER //

-- ===============================================================================
-- 2. TABLA: tes_cuenta_bancaria
-- ===============================================================================
-- ===============================================================================

DROP TRIGGER IF EXISTS trg_tes_cuenta_bancaria_before_insert //
CREATE TRIGGER trg_tes_cuenta_bancaria_before_insert
BEFORE INSERT ON tes_cuenta_bancaria
FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
END //

DROP TRIGGER IF EXISTS trg_tes_cuenta_bancaria_before_update //
CREATE TRIGGER trg_tes_cuenta_bancaria_before_update
BEFORE UPDATE ON tes_cuenta_bancaria
FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
END //

DROP TRIGGER IF EXISTS trg_tes_cuenta_bancaria_after_insert //
CREATE TRIGGER trg_tes_cuenta_bancaria_after_insert
AFTER INSERT ON tes_cuenta_bancaria
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'tes_cuenta_bancaria',
        'INSERT',
        CAST(NEW.id_cuenta_bancaria AS CHAR),
        NULL,
        JSON_OBJECT(
            'id_cuenta_bancaria', NEW.id_cuenta_bancaria,
            'nombre_banco', NEW.nombre_banco,
            'numero_cuenta', NEW.numero_cuenta,
            'cci', NEW.cci,
            'id_moneda', NEW.id_moneda,
            'id_area', NEW.id_area,
            'id_usuario', NEW.id_usuario,
            'activa', NEW.activa
        ),
        NEW.id_usuario_creacion
    );
END //

DROP TRIGGER IF EXISTS trg_tes_cuenta_bancaria_after_update //
CREATE TRIGGER trg_tes_cuenta_bancaria_after_update
AFTER UPDATE ON tes_cuenta_bancaria
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'tes_cuenta_bancaria',
        'UPDATE',
        CAST(NEW.id_cuenta_bancaria AS CHAR),
        JSON_OBJECT(
            'id_cuenta_bancaria', OLD.id_cuenta_bancaria,
            'nombre_banco', OLD.nombre_banco,
            'numero_cuenta', OLD.numero_cuenta,
            'cci', OLD.cci,
            'id_moneda', OLD.id_moneda,
            'id_area', OLD.id_area,
            'id_usuario', OLD.id_usuario,
            'activa', OLD.activa
        ),
        JSON_OBJECT(
            'id_cuenta_bancaria', NEW.id_cuenta_bancaria,
            'nombre_banco', NEW.nombre_banco,
            'numero_cuenta', NEW.numero_cuenta,
            'cci', NEW.cci,
            'id_moneda', NEW.id_moneda,
            'id_area', NEW.id_area,
            'id_usuario', NEW.id_usuario,
            'activa', NEW.activa
        ),
        NEW.id_usuario_modificacion
    );
END //

DROP TRIGGER IF EXISTS trg_tes_cuenta_bancaria_after_delete //
CREATE TRIGGER trg_tes_cuenta_bancaria_after_delete
AFTER DELETE ON tes_cuenta_bancaria
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'tes_cuenta_bancaria',
        'DELETE',
        CAST(OLD.id_cuenta_bancaria AS CHAR),
        JSON_OBJECT(
            'id_cuenta_bancaria', OLD.id_cuenta_bancaria,
            'nombre_banco', OLD.nombre_banco,
            'numero_cuenta', OLD.numero_cuenta,
            'cci', OLD.cci,
            'id_moneda', OLD.id_moneda,
            'id_area', OLD.id_area,
            'id_usuario', OLD.id_usuario,
            'activa', OLD.activa
        ),
        NULL,
        OLD.id_usuario_modificacion
    );
END //

-- ===============================================================================