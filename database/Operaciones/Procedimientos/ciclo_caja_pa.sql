DELIMITER $$

DROP PROCEDURE IF EXISTS pa_insertar_ciclo_caja $$
CREATE PROCEDURE pa_insertar_ciclo_caja(
    IN p_numero_semana INT,
    IN p_fecha_apertura DATE,
    IN p_fecha_cierre DATE,
    IN p_monto_saldo_inicial DECIMAL(12,2),
    IN p_monto_total_gastado DECIMAL(12,2),
    IN p_estado_ciclo VARCHAR(20),
    IN p_id_caja_chica INT,
    IN p_id_rendicion INT,
    OUT p_id_generado INT
)
BEGIN
    IF p_id_caja_chica IS NULL OR p_id_caja_chica <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de ciclo de caja chica no válido';
    END IF;

    INSERT INTO ope_ciclo_caja(
        numero_semana,
        fecha_apertura,
        fecha_cierre,
        monto_saldo_inicial,
        monto_total_gastado,
        estado_ciclo,
        id_caja_chica,
        id_rendicion
    )
    VALUES(
        p_numero_semana,
        p_fecha_apertura,
        p_fecha_cierre,
        p_monto_saldo_inicial,
        p_monto_total_gastado,
        p_estado_ciclo,
        p_id_caja_chica,
        p_id_rendicion
    );
    
    SET p_id_generado = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS pa_modificar_ciclo_caja $$
CREATE PROCEDURE pa_modificar_ciclo_caja(
    IN p_id_ciclo_caja INT,
    IN p_numero_semana INT,
    IN p_fecha_apertura DATE,
    IN p_fecha_cierre DATE,
    IN p_monto_saldo_inicial DECIMAL(12,2),
    IN p_monto_total_gastado DECIMAL(12,2),
    IN p_estado_ciclo VARCHAR(20),
    IN p_id_caja_chica INT,
    IN p_id_rendicion INT
)
BEGIN
    IF p_id_ciclo_caja IS NULL OR p_id_ciclo_caja <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de ciclo de caja no válido';
    END IF;

    UPDATE ope_ciclo_caja
       SET numero_semana = p_numero_semana,
           fecha_apertura = p_fecha_apertura,
           fecha_cierre = p_fecha_cierre,
           monto_saldo_inicial = p_monto_saldo_inicial,
           monto_total_gastado = p_monto_total_gastado,
           estado_ciclo = p_estado_ciclo,
           id_caja_chica = p_id_caja_chica,
           id_rendicion = p_id_rendicion
     WHERE id_ciclo_caja = p_id_ciclo_caja;
END$$

DROP PROCEDURE IF EXISTS pa_eliminar_ciclo_caja $$
CREATE PROCEDURE pa_eliminar_ciclo_caja(
    IN p_id_ciclo_caja INT
)
BEGIN

    IF p_id_ciclo_caja IS NULL OR p_id_ciclo_caja <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de ciclo de caja no válido';
    END IF;

    UPDATE ope_ciclo_caja
       SET estado_ciclo = 'Cerrado'
     WHERE id_ciclo_caja = p_id_ciclo_caja;
END$$

DROP PROCEDURE IF EXISTS pa_buscar_ciclo_caja_por_id $$
CREATE PROCEDURE pa_buscar_ciclo_caja_por_id(
    IN p_id_ciclo_caja INT
)
BEGIN
    IF p_id_ciclo_caja IS NULL OR p_id_ciclo_caja <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de ciclo de caja no válido';
    END IF;

    SELECT 
        id_ciclo_caja, 
        numero_semana, 
        fecha_apertura, 
        fecha_cierre, 
        monto_saldo_inicial, 
        monto_total_gastado, 
        estado_ciclo, 
        id_caja_chica, 
        id_rendicion
    FROM ope_ciclo_caja
    WHERE id_ciclo_caja = p_id_ciclo_caja
    ORDER BY estado_ciclo DESC;
END$$

DROP PROCEDURE IF EXISTS pa_listar_ciclos_caja $$
CREATE PROCEDURE pa_listar_ciclos_caja()
BEGIN
    SELECT 
        id_ciclo_caja, 
        numero_semana, 
        fecha_apertura, 
        fecha_cierre, 
        monto_saldo_inicial, 
        monto_total_gastado, 
        estado_ciclo, 
        id_caja_chica, 
        id_rendicion
    FROM ope_ciclo_caja
    ORDER BY estado_ciclo DESC;
END$$

DELIMITER ;