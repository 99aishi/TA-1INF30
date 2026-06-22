
DELIMITER $$

DROP PROCEDURE IF EXISTS pa_insertar_caja_chica $$
CREATE PROCEDURE pa_insertar_caja_chica(
    IN p_id_usuario_accion INT,
    IN p_id_fondo INT,
    IN p_monto_techo DECIMAL(12,2),
    IN p_id_cuenta_bancaria INT,
    IN p_id_moneda INT
)
BEGIN
    INSERT INTO tes_caja_chica (
        id_fondo,
        monto_techo,
        id_cuenta_bancaria,
        id_moneda,
        id_usuario_creacion,
        id_usuario_modificacion
    )
    VALUES (
        p_id_fondo,
        p_monto_techo,
        p_id_cuenta_bancaria,
        p_id_moneda,
        p_id_usuario_accion,
        p_id_usuario_accion
    );
END$$

DROP PROCEDURE IF EXISTS pa_modificar_caja_chica $$
CREATE PROCEDURE pa_modificar_caja_chica(
    IN p_id_usuario_accion INT,
    IN p_id_fondo INT,
    IN p_monto_techo DECIMAL(12,2),
    IN p_id_cuenta_bancaria INT,
    IN p_id_moneda INT
)
BEGIN
    IF p_id_fondo IS NULL OR p_id_fondo <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de caja chica no válido';
    END IF;

    UPDATE tes_caja_chica
    SET monto_techo = p_monto_techo,
        id_cuenta_bancaria = p_id_cuenta_bancaria,
        id_moneda = p_id_moneda,
        id_usuario_modificacion = p_id_usuario_accion
    WHERE id_fondo = p_id_fondo;
END$$


DROP PROCEDURE IF EXISTS pa_eliminar_caja_chica $$
CREATE PROCEDURE pa_eliminar_caja_chica(
    IN p_id_usuario_accion INT,
    IN p_id_fondo INT
)
BEGIN
    IF p_id_fondo IS NULL OR p_id_fondo <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de caja chica no válido';
    END IF;

    call pa_eliminar_fondo(p_id_usuario_accion, p_id_fondo);

END$$

DROP PROCEDURE IF EXISTS pa_buscar_por_id_caja_chica $$
CREATE PROCEDURE pa_buscar_por_id_caja_chica(
    IN p_id_fondo INT
)
BEGIN
    IF p_id_fondo IS NULL OR p_id_fondo <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de caja chica no válido';
    END IF;

    SELECT
        f.id_fondo,
        f.nombre_fondo,
        f.estado_fondo,
        c.monto_techo,
        c.id_cuenta_bancaria,
        c.id_moneda,
        m.id_moneda AS mon_id_moneda,
        m.codigo_iso AS mon_codigo_iso,
        m.simbolo AS mon_simbolo,
        m.nombre_moneda AS mon_nombre,
        m.descripcion AS mon_descripcion,
        m.activa AS mon_activa,
        cb.id_cuenta_bancaria AS cb_id_cuenta,
        cb.numero_cuenta AS cb_numero_cuenta,
        cb.nombre_banco AS cb_nombre_banco,
        cb.cci AS cb_cci,
        cb.activa AS cb_activa,
        cb.id_moneda AS cb_id_moneda,
        cbm.id_moneda AS cbm_id_moneda,
        cbm.codigo_iso AS cbm_codigo_iso,
        cbm.simbolo AS cbm_simbolo,
        cbm.nombre_moneda AS cbm_nombre,
        cbm.descripcion AS cbm_descripcion,
        cbm.activa AS cbm_activa
    FROM tes_fondo f
    JOIN tes_caja_chica c ON f.id_fondo = c.id_fondo
    LEFT JOIN tes_moneda m ON c.id_moneda = m.id_moneda
    LEFT JOIN tes_cuenta_bancaria cb ON c.id_cuenta_bancaria = cb.id_cuenta_bancaria
    LEFT JOIN tes_moneda cbm ON cb.id_moneda = cbm.id_moneda
    WHERE f.id_fondo = p_id_fondo;
END$$

DROP PROCEDURE IF EXISTS pa_listar_todas_cajas_chicas $$
CREATE PROCEDURE pa_listar_todas_cajas_chicas()
BEGIN
    SELECT
        f.id_fondo,
        f.nombre_fondo,
        f.estado_fondo,
        c.monto_techo,
        c.id_cuenta_bancaria,
        c.id_moneda,
        m.id_moneda AS mon_id_moneda,
        m.codigo_iso AS mon_codigo_iso,
        m.simbolo AS mon_simbolo,
        m.nombre_moneda AS mon_nombre,
        m.descripcion AS mon_descripcion,
        m.activa AS mon_activa,
        cb.id_cuenta_bancaria AS cb_id_cuenta,
        cb.numero_cuenta AS cb_numero_cuenta,
        cb.nombre_banco AS cb_nombre_banco,
        cb.cci AS cb_cci,
        cb.activa AS cb_activa,
        cb.id_moneda AS cb_id_moneda,
        cbm.id_moneda AS cbm_id_moneda,
        cbm.codigo_iso AS cbm_codigo_iso,
        cbm.simbolo AS cbm_simbolo,
        cbm.nombre_moneda AS cbm_nombre,
        cbm.descripcion AS cbm_descripcion,
        cbm.activa AS cbm_activa
    FROM tes_fondo f
    JOIN tes_caja_chica c ON f.id_fondo = c.id_fondo
    LEFT JOIN tes_moneda m ON c.id_moneda = m.id_moneda
    LEFT JOIN tes_cuenta_bancaria cb ON c.id_cuenta_bancaria = cb.id_cuenta_bancaria
    LEFT JOIN tes_moneda cbm ON cb.id_moneda = cbm.id_moneda
    ORDER BY f.estado_fondo DESC, f.id_fondo;
END$$

DROP PROCEDURE IF EXISTS pa_listar_cajas_chicas_activas $$
CREATE PROCEDURE pa_listar_cajas_chicas_activas()
BEGIN
    SELECT
        f.id_fondo,
        f.nombre_fondo,
        f.estado_fondo,
        c.monto_techo,
        c.id_cuenta_bancaria,
        c.id_moneda,
        m.id_moneda AS mon_id_moneda,
        m.codigo_iso AS mon_codigo_iso,
        m.simbolo AS mon_simbolo,
        m.nombre_moneda AS mon_nombre,
        m.descripcion AS mon_descripcion,
        m.activa AS mon_activa,
        cb.id_cuenta_bancaria AS cb_id_cuenta,
        cb.numero_cuenta AS cb_numero_cuenta,
        cb.nombre_banco AS cb_nombre_banco,
        cb.cci AS cb_cci,
        cb.activa AS cb_activa,
        cb.id_moneda AS cb_id_moneda,
        cbm.id_moneda AS cbm_id_moneda,
        cbm.codigo_iso AS cbm_codigo_iso,
        cbm.simbolo AS cbm_simbolo,
        cbm.nombre_moneda AS cbm_nombre,
        cbm.descripcion AS cbm_descripcion,
        cbm.activa AS cbm_activa
    FROM tes_fondo f
    JOIN tes_caja_chica c ON f.id_fondo = c.id_fondo
    LEFT JOIN tes_moneda m ON c.id_moneda = m.id_moneda
    LEFT JOIN tes_cuenta_bancaria cb ON c.id_cuenta_bancaria = cb.id_cuenta_bancaria
    LEFT JOIN tes_moneda cbm ON cb.id_moneda = cbm.id_moneda
    WHERE f.estado_fondo = 'ACTIVO'
    ORDER BY f.id_fondo;
END$$

DROP PROCEDURE IF EXISTS pa_listar_cajas_chicas_por_cuenta $$
CREATE PROCEDURE pa_listar_cajas_chicas_por_cuenta(
    IN p_id_cuenta_bancaria INT
)
BEGIN
    IF p_id_cuenta_bancaria IS NULL OR p_id_cuenta_bancaria <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de cuenta bancaria no válido';
    END IF;

    SELECT
        f.id_fondo,
        f.nombre_fondo,
        f.estado_fondo,
        c.monto_techo,
        c.id_cuenta_bancaria,
        c.id_moneda,
        m.id_moneda AS mon_id_moneda,
        m.codigo_iso AS mon_codigo_iso,
        m.simbolo AS mon_simbolo,
        m.nombre_moneda AS mon_nombre,
        m.descripcion AS mon_descripcion,
        m.activa AS mon_activa,
        cb.id_cuenta_bancaria AS cb_id_cuenta,
        cb.numero_cuenta AS cb_numero_cuenta,
        cb.nombre_banco AS cb_nombre_banco,
        cb.cci AS cb_cci,
        cb.activa AS cb_activa,
        cb.id_moneda AS cb_id_moneda,
        cbm.id_moneda AS cbm_id_moneda,
        cbm.codigo_iso AS cbm_codigo_iso,
        cbm.simbolo AS cbm_simbolo,
        cbm.nombre_moneda AS cbm_nombre,
        cbm.descripcion AS cbm_descripcion,
        cbm.activa AS cbm_activa
    FROM tes_fondo f
    JOIN tes_caja_chica c ON f.id_fondo = c.id_fondo
    LEFT JOIN tes_moneda m ON c.id_moneda = m.id_moneda
    LEFT JOIN tes_cuenta_bancaria cb ON c.id_cuenta_bancaria = cb.id_cuenta_bancaria
    LEFT JOIN tes_moneda cbm ON cb.id_moneda = cbm.id_moneda
    WHERE c.id_cuenta_bancaria = p_id_cuenta_bancaria
    ORDER BY f.id_fondo;
END$$

DELIMITER ;
