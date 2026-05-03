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
        p_tipo_operacion,
        p_momento_operacion,
        p_monto_transaccion,
        p_numero_operacion_bancaria,
        p_medio_pago,
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

    UPDATE ope_transaccion
       SET 
            tipo_operacion = p_tipo_operacion,
            momento_operacion = p_momento_operacion
            monto_transaccion = p_monto_transaccion,
            numero_operacion_bancaria = p_numero_operacion_bancaria,
            medio_pago = p_medio_pago,
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
    UPDATE ope_transaccion
       SET estado_transaccion = 'Anulado'
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
        estado_transaccion,
        id_cuenta_origen, 
        id_cuenta_destino, 
        id_moneda
    FROM ope_transaccion
    WHERE id_transaccion = p_id_transaccion
    ORDER BY estado_transaccion DESC;
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
        estado_transaccion,
        id_cuenta_origen, 
        id_cuenta_destino, 
        id_moneda
    FROM ope_transaccion
    ORDER BY estado_transaccion, DESC;
END$$

DELIMITER ;