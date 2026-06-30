
DELIMITER $$

DROP PROCEDURE IF EXISTS pa_insertar_tipo_cambio $$
CREATE PROCEDURE pa_insertar_tipo_cambio(
    IN p_id_usuario_accion INT,
    IN p_id_moneda_origen INT,
    IN p_id_moneda_destino INT,
    IN p_valor_tipo_cambio DECIMAL(10,4),
    IN p_fecha_tipo_cambio DATE,
    OUT p_id_generado INT
)
BEGIN
    INSERT INTO tes_tipo_cambio(
        id_moneda_origen,
        id_moneda_destino,
        valor_tipo_cambio,
        fecha_tipo_cambio,
        id_usuario_creacion,
        id_usuario_modificacion
    ) VALUES(
        p_id_moneda_origen,
        p_id_moneda_destino,
        p_valor_tipo_cambio,
        p_fecha_tipo_cambio,
        p_id_usuario_accion,
        p_id_usuario_accion
    );
    SET p_id_generado = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS pa_modificar_tipo_cambio $$
CREATE PROCEDURE pa_modificar_tipo_cambio(
    IN p_id_usuario_accion INT,
    IN p_id_tipo_cambio INT,
    IN p_id_moneda_origen INT,
    IN p_id_moneda_destino INT,
    IN p_valor_tipo_cambio DECIMAL(10,4),
    IN p_fecha_tipo_cambio DATE
)
BEGIN
    IF p_id_tipo_cambio IS NULL OR p_id_tipo_cambio <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de tipo de cambio inválido';
    END IF;

    UPDATE tes_tipo_cambio
    SET
        id_moneda_origen = p_id_moneda_origen,
        id_moneda_destino = p_id_moneda_destino,
        valor_tipo_cambio = p_valor_tipo_cambio,
        fecha_tipo_cambio = p_fecha_tipo_cambio,
        id_usuario_modificacion = p_id_usuario_accion
    WHERE id_tipo_cambio = p_id_tipo_cambio;
END$$

DROP PROCEDURE IF EXISTS pa_eliminar_tipo_cambio $$
CREATE PROCEDURE pa_eliminar_tipo_cambio(
    IN p_id_usuario_accion INT,
    IN p_id_tipo_cambio INT
)
BEGIN
    IF p_id_tipo_cambio IS NULL OR p_id_tipo_cambio <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de tipo de cambio inválido';
    END IF;

    DELETE FROM tes_tipo_cambio
    WHERE id_tipo_cambio = p_id_tipo_cambio;
END$$

DROP PROCEDURE IF EXISTS pa_buscar_tipo_cambio_por_id $$
CREATE PROCEDURE pa_buscar_tipo_cambio_por_id(
    IN p_id_tipo_cambio INT
)
BEGIN
    SELECT
        tc.id_tipo_cambio,
        tc.id_moneda_origen,
        tc.id_moneda_destino,
        tc.valor_tipo_cambio,
        tc.fecha_tipo_cambio,
        mo.id_moneda AS origen_id_moneda,
        mo.codigo_iso AS origen_codigo_iso,
        mo.simbolo AS origen_simbolo,
        mo.nombre_moneda AS origen_nombre,
        mo.descripcion AS origen_descripcion,
        mo.activa AS origen_activa,
        md.id_moneda AS destino_id_moneda,
        md.codigo_iso AS destino_codigo_iso,
        md.simbolo AS destino_simbolo,
        md.nombre_moneda AS destino_nombre,
        md.descripcion AS destino_descripcion,
        md.activa AS destino_activa
    FROM tes_tipo_cambio tc
    LEFT JOIN tes_moneda mo ON tc.id_moneda_origen = mo.id_moneda
    LEFT JOIN tes_moneda md ON tc.id_moneda_destino = md.id_moneda
    WHERE tc.id_tipo_cambio = p_id_tipo_cambio;
END$$

DROP PROCEDURE IF EXISTS pa_listar_tipos_cambio $$
CREATE PROCEDURE pa_listar_tipos_cambio()
BEGIN
    SELECT
        tc.id_tipo_cambio,
        tc.id_moneda_origen,
        tc.id_moneda_destino,
        tc.valor_tipo_cambio,
        tc.fecha_tipo_cambio,
        mo.id_moneda AS origen_id_moneda,
        mo.codigo_iso AS origen_codigo_iso,
        mo.simbolo AS origen_simbolo,
        mo.nombre_moneda AS origen_nombre,
        mo.descripcion AS origen_descripcion,
        mo.activa AS origen_activa,
        md.id_moneda AS destino_id_moneda,
        md.codigo_iso AS destino_codigo_iso,
        md.simbolo AS destino_simbolo,
        md.nombre_moneda AS destino_nombre,
        md.descripcion AS destino_descripcion,
        md.activa AS destino_activa
    FROM tes_tipo_cambio tc
    LEFT JOIN tes_moneda mo ON tc.id_moneda_origen = mo.id_moneda
    LEFT JOIN tes_moneda md ON tc.id_moneda_destino = md.id_moneda
    ORDER BY tc.fecha_tipo_cambio DESC;
END$$

DROP PROCEDURE IF EXISTS pa_buscar_tipo_cambio_por_monedas_fecha $$
CREATE PROCEDURE pa_buscar_tipo_cambio_por_monedas_fecha(
    IN p_id_moneda_origen INT,
    IN p_id_moneda_destino INT,
    IN p_fecha DATE
)
BEGIN
    SELECT
        id_tipo_cambio,
        id_moneda_origen,
        id_moneda_destino,
        valor_tipo_cambio,
        fecha_tipo_cambio
    FROM tes_tipo_cambio
    WHERE id_moneda_origen = p_id_moneda_origen
      AND id_moneda_destino = p_id_moneda_destino
      AND DATE(fecha_tipo_cambio) <= p_fecha
    ORDER BY fecha_tipo_cambio DESC
    LIMIT 1;
END$$

DELIMITER ;
