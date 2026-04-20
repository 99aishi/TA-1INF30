DELIMITER $$

DROP PROCEDURE IF EXISTS pa_insertar_transaccion $$
CREATE PROCEDURE pa_insertar_transaccion(
    IN p_tipo_operacion VARCHAR(30),
    IN p_momento_operacion DATETIME,
    IN p_monto_transaccion DECIMAL(12,2),
    IN p_numero_operacion_bancaria VARCHAR(30),
    IN p_medio_pago VARCHAR(30),
    IN p_valor_tipo_cambio DECIMAL(10,4),
    IN p_id_cuenta_origen INT,
    IN p_id_cuenta_destino INT,
    IN p_id_moneda INT,
    OUT p_id_generado INT
)
BEGIN
    IF p_tipo_operacion IS NULL OR TRIM(p_tipo_operacion) = '' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El tipo de operación es obligatorio';
    END IF;

    IF p_monto_transaccion IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El monto de la transacción es obligatorio';
    END IF;

    INSERT INTO ope_transaccion(
        tipo_operacion,
        momento_operacion,
        monto_transaccion,
        numero_operacion_bancaria,
        medio_pago,
        valor_tipo_cambio,
        id_cuenta_origen,
        id_cuenta_destino,
        id_moneda
    )
    VALUES(
        TRIM(p_tipo_operacion),
        IFNULL(p_momento_operacion, NOW()),
        p_monto_transaccion,
        TRIM(p_numero_operacion_bancaria),
        TRIM(p_medio_pago),
        p_valor_tipo_cambio,
        p_id_cuenta_origen,
        p_id_cuenta_destino,
        p_id_moneda
    );
    SET p_id_generado = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS pa_modificar_transaccion $$
CREATE PROCEDURE pa_modificar_transaccion(
    IN p_id_transaccion INT,
    IN p_tipo_operacion VARCHAR(30),
    IN p_momento_operacion DATETIME,
    IN p_monto_transaccion DECIMAL(12,2),
    IN p_numero_operacion_bancaria VARCHAR(30),
    IN p_medio_pago VARCHAR(30),
    IN p_valor_tipo_cambio DECIMAL(10,4),
    IN p_id_cuenta_origen INT,
    IN p_id_cuenta_destino INT,
    IN p_id_moneda INT
)
BEGIN
    IF p_id_transaccion IS NULL OR p_id_transaccion <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de transacción inválido';
    END IF;

    IF p_tipo_operacion IS NULL OR TRIM(p_tipo_operacion) = '' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El tipo de operación es obligatorio';
    END IF;

    IF p_monto_transaccion IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El monto de la transacción es obligatorio';
    END IF;

    UPDATE ope_transaccion
       SET tipo_operacion = TRIM(p_tipo_operacion),
           -- Mantiene el valor actual si el parámetro viene NULL
           momento_operacion = IFNULL(p_momento_operacion, momento_operacion), 
           monto_transaccion = p_monto_transaccion,
           numero_operacion_bancaria = TRIM(p_numero_operacion_bancaria),
           medio_pago = TRIM(p_medio_pago),
           valor_tipo_cambio = p_valor_tipo_cambio,
           id_cuenta_origen = p_id_cuenta_origen,
           id_cuenta_destino = p_id_cuenta_destino,
           id_moneda = p_id_moneda
     WHERE id_transaccion = p_id_transaccion;
END$$

DROP PROCEDURE IF EXISTS pa_eliminar_transaccion $$
CREATE PROCEDURE pa_eliminar_transaccion(
    IN p_id_transaccion INT
)
BEGIN
    IF p_id_transaccion IS NULL OR p_id_transaccion <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de transacción inválido';
    END IF;
    -- TODO Revisar si se hace el borrado o no (ya sea físico o lógico)
    DELETE FROM ope_transaccion
     WHERE id_transaccion = p_id_transaccion;
END$$

DROP PROCEDURE IF EXISTS pa_buscar_transaccion_por_id $$
CREATE PROCEDURE pa_buscar_transaccion_por_id(
    IN p_id_transaccion INT
)
BEGIN
    SELECT 
        id_transaccion, 
        tipo_operacion, 
        momento_operacion, 
        monto_transaccion, 
        numero_operacion_bancaria, 
        medio_pago, 
        valor_tipo_cambio, 
        id_cuenta_origen, 
        id_cuenta_destino, 
        id_moneda
    FROM ope_transaccion
    WHERE id_transaccion = p_id_transaccion;
END$$

DROP PROCEDURE IF EXISTS pa_listar_transacciones $$
CREATE PROCEDURE pa_listar_transacciones()
BEGIN
    SELECT 
        id_transaccion, 
        tipo_operacion, 
        momento_operacion, 
        monto_transaccion, 
        numero_operacion_bancaria, 
        medio_pago, 
        valor_tipo_cambio, 
        id_cuenta_origen, 
        id_cuenta_destino, 
        id_moneda
    FROM ope_transaccion
    ORDER BY id_transaccion DESC;
END$$

DELIMITER ;