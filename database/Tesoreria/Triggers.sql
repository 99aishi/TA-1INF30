DELIMITER //

-- ===============================================================================
-- 1. TABLA: tes_moneda
-- ===============================================================================

CREATE TRIGGER trg_tes_moneda_before_insert
BEFORE INSERT ON tes_moneda
FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
    SET NEW.id_usuario_creacion = COALESCE(NEW.id_usuario_creacion, @id_usuario_actual);
    SET NEW.id_usuario_modificacion = COALESCE(NEW.id_usuario_modificacion, @id_usuario_actual);
END //

CREATE TRIGGER trg_tes_moneda_before_update
BEFORE UPDATE ON tes_moneda
FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
    SET NEW.id_usuario_modificacion = COALESCE(NEW.id_usuario_modificacion, @id_usuario_actual);
END //

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
            'activa', NEW.activa
        ),
        NEW.id_usuario_creacion
    );
END //

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
            'activa', OLD.activa
        ),
        JSON_OBJECT(
            'id_moneda', NEW.id_moneda,
            'codigo_iso', NEW.codigo_iso,
            'simbolo', NEW.simbolo,
            'activa', NEW.activa
        ),
        NEW.id_usuario_modificacion
    );
END //

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
            'activa', OLD.activa
        ),
        NULL,
        IFNULL(@id_usuario_actual, OLD.id_usuario_modificacion)
    );
END //

-- ===============================================================================
-- 2. TABLA: tes_cuenta_bancaria
-- ===============================================================================

CREATE TRIGGER trg_tes_cuenta_bancaria_before_insert
BEFORE INSERT ON tes_cuenta_bancaria
FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
    SET NEW.id_usuario_creacion = COALESCE(NEW.id_usuario_creacion, @id_usuario_actual);
    SET NEW.id_usuario_modificacion = COALESCE(NEW.id_usuario_modificacion, @id_usuario_actual);
END //

CREATE TRIGGER trg_tes_cuenta_bancaria_before_update
BEFORE UPDATE ON tes_cuenta_bancaria
FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
    SET NEW.id_usuario_modificacion = COALESCE(NEW.id_usuario_modificacion, @id_usuario_actual);
END //

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
        IFNULL(@id_usuario_actual, OLD.id_usuario_modificacion)
    );
END //

-- ===============================================================================
-- 3. TABLA: tes_fondo
-- ===============================================================================

CREATE TRIGGER trg_tes_fondo_before_insert
BEFORE INSERT ON tes_fondo
FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
    SET NEW.id_usuario_creacion = COALESCE(NEW.id_usuario_creacion, @id_usuario_actual);
    SET NEW.id_usuario_modificacion = COALESCE(NEW.id_usuario_modificacion, @id_usuario_actual);
END //

CREATE TRIGGER trg_tes_fondo_before_update
BEFORE UPDATE ON tes_fondo
FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
    SET NEW.id_usuario_modificacion = COALESCE(NEW.id_usuario_modificacion, @id_usuario_actual);
END //

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
        IFNULL(@id_usuario_actual, OLD.id_usuario_modificacion)
    );
END //

-- ===============================================================================
-- 4. TABLA: tes_caja_chica
-- ===============================================================================

CREATE TRIGGER trg_tes_caja_chica_before_insert
BEFORE INSERT ON tes_caja_chica
FOR EACH ROW
BEGIN
    SET NEW.creado_at = NOW();
    SET NEW.actualizado_at = NOW();
    SET NEW.id_usuario_creacion = COALESCE(NEW.id_usuario_creacion, @id_usuario_actual);
    SET NEW.id_usuario_modificacion = COALESCE(NEW.id_usuario_modificacion, @id_usuario_actual);
END //

CREATE TRIGGER trg_tes_caja_chica_before_update
BEFORE UPDATE ON tes_caja_chica
FOR EACH ROW
BEGIN
    SET NEW.actualizado_at = NOW();
    SET NEW.id_usuario_modificacion = COALESCE(NEW.id_usuario_modificacion, @id_usuario_actual);
END //

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
            'id_area', NEW.id_area
        ),
        NEW.id_usuario_creacion
    );
END //

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
            'id_area', OLD.id_area
        ),
        JSON_OBJECT(
            'id_fondo', NEW.id_fondo,
            'monto_techo', NEW.monto_techo,
            'id_area', NEW.id_area
        ),
        NEW.id_usuario_modificacion
    );
END //

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
            'id_area', OLD.id_area
        ),
        NULL,
        IFNULL(@id_usuario_actual, OLD.id_usuario_modificacion)
    );
END //

DELIMITER ;