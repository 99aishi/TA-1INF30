DELIMITER $$

DROP PROCEDURE IF EXISTS pa_insertar_rendicion $$
CREATE PROCEDURE pa_insertar_rendicion(
    IN p_fecha_presentacion DATE,
    IN p_fecha_aprobacion DATE,
    IN p_monto_total_declarado DECIMAL(12,2),
    IN p_monto_total_aprobado DECIMAL(12,2),
    IN p_monto_saldo_final DECIMAL(12,2),
    IN p_estado_rendicion VARCHAR(20),
    IN p_comentario VARCHAR(500),
    OUT p_id_generado INT
)
BEGIN
    IF p_estado_rendicion IS NULL OR TRIM(p_estado_rendicion) = '' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El estado de la rendición es obligatorio';
    END IF;

    INSERT INTO ope_rendicion(
        fecha_presentacion,
        fecha_aprobacion,
        monto_total_declarado,
        monto_total_aprobado,
        monto_saldo_final,
        estado_rendicion,
        comentario
    )
    VALUES(
        IFNULL(p_fecha_presentacion, CURDATE()),
        p_fecha_aprobacion,
        IFNULL(p_monto_total_declarado, 0.00),
        IFNULL(p_monto_total_aprobado, 0.00),
        IFNULL(p_monto_saldo_final, 0.00),
        TRIM(p_estado_rendicion),
        TRIM(p_comentario)
    );
    
    SET p_id_generado = LAST_INSERT_ID();
END$$


DROP PROCEDURE IF EXISTS pa_modificar_rendicion $$
CREATE PROCEDURE pa_modificar_rendicion(
    IN p_id_rendicion INT,
    IN p_fecha_presentacion DATE,
    IN p_fecha_aprobacion DATE,
    IN p_monto_total_declarado DECIMAL(12,2),
    IN p_monto_total_aprobado DECIMAL(12,2),
    IN p_monto_saldo_final DECIMAL(12,2),
    IN p_estado_rendicion VARCHAR(20),
    IN p_comentario VARCHAR(500)
)
BEGIN
    IF p_id_rendicion IS NULL OR p_id_rendicion <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de rendición inválido';
    END IF;

    IF p_estado_rendicion IS NULL OR TRIM(p_estado_rendicion) = '' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El estado de la rendición es obligatorio';
    END IF;

    UPDATE ope_rendicion
       SET fecha_presentacion = p_fecha_presentacion,
           fecha_aprobacion = p_fecha_aprobacion,
           monto_total_declarado = IFNULL(p_monto_total_declarado, 0.00),
           monto_total_aprobado = IFNULL(p_monto_total_aprobado, 0.00),
           monto_saldo_final = IFNULL(p_monto_saldo_final, 0.00),
           estado_rendicion = TRIM(p_estado_rendicion),
           comentario = TRIM(p_comentario)
     WHERE id_rendicion = p_id_rendicion;
END$$

DROP PROCEDURE IF EXISTS pa_eliminar_rendicion $$
CREATE PROCEDURE pa_eliminar_rendicion(
    IN p_id_rendicion INT
)
BEGIN
    IF p_id_rendicion IS NULL OR p_id_rendicion <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ID de rendición inválido';
    END IF;

    UPDATE ope_rendicion
       SET estado_rendicion = 'ANULADO'
     WHERE id_rendicion = p_id_rendicion;
END$$

DROP PROCEDURE IF EXISTS pa_buscar_rendicion_por_id $$
CREATE PROCEDURE pa_buscar_rendicion_por_id(
    IN p_id_rendicion INT
)
BEGIN
    SELECT 
        id_rendicion, 
        fecha_presentacion, 
        fecha_aprobacion, 
        monto_total_declarado, 
        monto_total_aprobado, 
        monto_saldo_final, 
        estado_rendicion, 
        comentario
    FROM ope_rendicion
    WHERE id_rendicion = p_id_rendicion;
END$$

DROP PROCEDURE IF EXISTS pa_listar_rendiciones $$
CREATE PROCEDURE pa_listar_rendiciones()
BEGIN
    SELECT 
        id_rendicion, 
        fecha_presentacion, 
        fecha_aprobacion, 
        monto_total_declarado, 
        monto_total_aprobado, 
        monto_saldo_final, 
        estado_rendicion, 
        comentario
    FROM ope_rendicion
    WHERE estado_rendicion != 'ANULADO'
    ORDER BY id_rendicion DESC;
END$$

DELIMITER ;