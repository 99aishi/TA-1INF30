DELIMITER //

-- ===============================================================================
-- 3. TABLA: tes_fondo
-- ===============================================================================
-- ===============================================================================

DROP TRIGGER IF EXISTS trg_tes_fondo_before_insert //
CREATE TRIGGER trg_tes_fondo_before_insert
BEFORE INSERT ON tes_fondo
FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
END //

DROP TRIGGER IF EXISTS trg_tes_fondo_before_update //
CREATE TRIGGER trg_tes_fondo_before_update
BEFORE UPDATE ON tes_fondo
FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
END //

DROP TRIGGER IF EXISTS trg_tes_fondo_after_insert //
CREATE TRIGGER trg_tes_fondo_after_insert
AFTER INSERT ON tes_fondo
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'tes_fondo',
        'INSERT',
        CAST(NEW.id_fondo AS CHAR),
        NULL,
        JSON_OBJECT(
            'id_fondo', NEW.id_fondo,
            'nombre_fondo', NEW.nombre_fondo,
            'estado_fondo', NEW.estado_fondo
        ),
        NEW.id_usuario_creacion
    );
END //

DROP TRIGGER IF EXISTS trg_tes_fondo_after_update //
CREATE TRIGGER trg_tes_fondo_after_update
AFTER UPDATE ON tes_fondo
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'tes_fondo',
        'UPDATE',
        CAST(NEW.id_fondo AS CHAR),
        JSON_OBJECT(
            'id_fondo', OLD.id_fondo,
            'nombre_fondo', OLD.nombre_fondo,
            'estado_fondo', OLD.estado_fondo
        ),
        JSON_OBJECT(
            'id_fondo', NEW.id_fondo,
            'nombre_fondo', NEW.nombre_fondo,
            'estado_fondo', NEW.estado_fondo
        ),
        NEW.id_usuario_modificacion
    );
END //

DROP TRIGGER IF EXISTS trg_tes_fondo_after_delete //
CREATE TRIGGER trg_tes_fondo_after_delete
AFTER DELETE ON tes_fondo
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'tes_fondo',
        'DELETE',
        CAST(OLD.id_fondo AS CHAR),
        JSON_OBJECT(
            'id_fondo', OLD.id_fondo,
            'nombre_fondo', OLD.nombre_fondo,
            'estado_fondo', OLD.estado_fondo
        ),
        NULL,
        OLD.id_usuario_modificacion
    );
END //

-- ===============================================================================