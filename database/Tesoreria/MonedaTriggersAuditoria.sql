DELIMITER //

-- ===============================================================================
-- 1. TABLA: tes_moneda
-- ===============================================================================
-- ===============================================================================

DROP TRIGGER IF EXISTS trg_tes_moneda_before_insert //
CREATE TRIGGER trg_tes_moneda_before_insert
BEFORE INSERT ON tes_moneda
FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
END //

DROP TRIGGER IF EXISTS trg_tes_moneda_before_update //
CREATE TRIGGER trg_tes_moneda_before_update
BEFORE UPDATE ON tes_moneda
FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
END //

DROP TRIGGER IF EXISTS trg_tes_moneda_after_insert //
CREATE TRIGGER trg_tes_moneda_after_insert
AFTER INSERT ON tes_moneda
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'tes_moneda',
        'INSERT',
        CAST(NEW.id_moneda AS CHAR),
        NULL,
        JSON_OBJECT(
            'id_moneda', NEW.id_moneda,
            'codigo_iso', NEW.codigo_iso,
            'simbolo', NEW.simbolo,
            'nombre_moneda', NEW.nombre_moneda,
            'descripcion', NEW.descripcion,
            'activa', NEW.activa
        ),
        NEW.id_usuario_creacion
    );
END //

DROP TRIGGER IF EXISTS trg_tes_moneda_after_update //
CREATE TRIGGER trg_tes_moneda_after_update
AFTER UPDATE ON tes_moneda
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'tes_moneda',
        'UPDATE',
        CAST(NEW.id_moneda AS CHAR),
        JSON_OBJECT(
            'id_moneda', OLD.id_moneda,
            'codigo_iso', OLD.codigo_iso,
            'simbolo', OLD.simbolo,
            'nombre_moneda', OLD.nombre_moneda,
            'descripcion', OLD.descripcion,
            'activa', OLD.activa
        ),
        JSON_OBJECT(
            'id_moneda', NEW.id_moneda,
            'codigo_iso', NEW.codigo_iso,
            'simbolo', NEW.simbolo,
            'nombre_moneda', NEW.nombre_moneda,
            'descripcion', NEW.descripcion,
            'activa', NEW.activa
        ),
        NEW.id_usuario_modificacion
    );
END //

DROP TRIGGER IF EXISTS trg_tes_moneda_after_delete //
CREATE TRIGGER trg_tes_moneda_after_delete
AFTER DELETE ON tes_moneda
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'tes_moneda',
        'DELETE',
        CAST(OLD.id_moneda AS CHAR),
        JSON_OBJECT(
            'id_moneda', OLD.id_moneda,
            'codigo_iso', OLD.codigo_iso,
            'simbolo', OLD.simbolo,
            'nombre_moneda', OLD.nombre_moneda,
            'descripcion', OLD.descripcion,
            'activa', OLD.activa
        ),
        NULL,
        OLD.id_usuario_modificacion
    );
END //

-- ===============================================================================