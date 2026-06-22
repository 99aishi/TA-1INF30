
DELIMITER //

-- ===============================================================================
-- 3. TABLA: rrhh_area
-- ===============================================================================
-- ===============================================================================

DROP TRIGGER IF EXISTS trg_rrhh_area_before_insert //
CREATE TRIGGER trg_rrhh_area_before_insert
BEFORE INSERT ON rrhh_area
FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
END //


DROP TRIGGER IF EXISTS trg_rrhh_area_before_update //
CREATE TRIGGER trg_rrhh_area_before_update
BEFORE UPDATE ON rrhh_area
FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
END //

DROP TRIGGER IF EXISTS trg_rrhh_area_after_insert //
CREATE TRIGGER trg_rrhh_area_after_insert
AFTER INSERT ON rrhh_area
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'rrhh_area',
        'INSERT',
        CAST(NEW.id_area AS CHAR),
        NULL,
        JSON_OBJECT(
            'id_area', NEW.id_area,
            'nombre_area', NEW.nombre_area,
            'descripcion_area', NEW.descripcion_area,
            'id_jefe', NEW.id_jefe,
            'esta_activo', NEW.esta_activo
        ),
        NEW.id_usuario_creacion
    );
END //

DROP TRIGGER IF EXISTS trg_rrhh_area_after_update //
CREATE TRIGGER trg_rrhh_area_after_update
AFTER UPDATE ON rrhh_area
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'rrhh_area',
        'UPDATE',
        CAST(NEW.id_area AS CHAR),
        JSON_OBJECT(
            'id_area', OLD.id_area,
            'nombre_area', OLD.nombre_area,
            'descripcion_area', OLD.descripcion_area,
            'id_jefe', OLD.id_jefe,
            'esta_activo', OLD.esta_activo
        ),
        JSON_OBJECT(
            'id_area', NEW.id_area,
            'nombre_area', NEW.nombre_area,
            'descripcion_area', NEW.descripcion_area,
            'id_jefe', NEW.id_jefe,
            'esta_activo', NEW.esta_activo
        ),
        NEW.id_usuario_modificacion
    );
END //

DROP TRIGGER IF EXISTS trg_rrhh_area_after_delete //
CREATE TRIGGER trg_rrhh_area_after_delete
AFTER DELETE ON rrhh_area
FOR EACH ROW
BEGIN
    CALL pa_insertar_auditoria(
        'rrhh_area',
        'DELETE',
        CAST(OLD.id_area AS CHAR),
        JSON_OBJECT(
            'id_area', OLD.id_area,
            'nombre_area', OLD.nombre_area,
            'descripcion_area', OLD.descripcion_area,
            'id_jefe', OLD.id_jefe,
            'esta_activo', OLD.esta_activo
        ),
        NULL,
        OLD.id_usuario_modificacion
    );
END //

-- ===============================================================================